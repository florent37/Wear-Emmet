package com.github.florent37.emmet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class Emmet implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<NodeApi.GetConnectedNodesResult> {

    private static final String TAG = "Emmet";
    private final String PATH = "/Emmet/";
    protected GoogleApiClient mApiClient;

    private boolean ENABLE_LOG = false;

    private static final String METHOD_NAME = "name";
    private static final String METHOD_PARAMS = "params";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_VALUE = "value";

    private static final String TYPE_INT = "int";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_LONG = "long";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_SERIALIZED = "serialized";

    private ArrayList<Object> interfaces = new ArrayList<>();

    private ArrayList<PutDataMapRequest> mWaitingDataMapItems = new ArrayList<>();

    private ArrayList<Pair<String, String>> mWaitingMessageItems = new ArrayList<>();

    private ConnectionListener mConnectionListener;

    private NodeListener mNodeListener;
    private boolean mNodeListenerRegistered;

    private static Emmet INSTANCE;

    private Emmet() {
    }

    public static Emmet getDefault() {
        if (INSTANCE == null)
            INSTANCE = new Emmet();
        return INSTANCE;
    }

    private static Emmet onCreate(Context context, ConnectionListener connectionListener) {
        Emmet emmet = getDefault();
        emmet.create(context, connectionListener);
        return emmet;
    }

    public static Emmet onCreate(Context context) {
        Emmet emmet = getDefault();
        emmet.create(context, null);
        return emmet;
    }

    private void create(Context context, ConnectionListener connectionListener) {
        mConnectionListener = connectionListener;

        mApiClient = new GoogleApiClient.Builder(context.getApplicationContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Wearable.MessageApi.addListener(mApiClient, new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                messageReceived(messageEvent);
            }
        });
        Wearable.DataApi.addListener(mApiClient, new DataApi.DataListener() {
            @Override
            public void onDataChanged(DataEventBuffer dataEventBuffer) {
                dataChanged(dataEventBuffer);
            }
        });
        mApiClient.connect();
    }

    public static Emmet onDestroy() {
        Emmet emmet = getDefault();
        emmet.destroy();
        return emmet;
    }

    private void destroy() {
        if (mApiClient != null) {
            mApiClient.unregisterConnectionCallbacks(this);
            mApiClient.unregisterConnectionFailedListener(this);
            unregisterNodeListener();
            mApiClient.disconnect();
            mApiClient = null;

            interfaces.clear();
        }
    }

    public static <T> T createSender(Class<T> protocol) {
        return getDefault().create(protocol);
    }

    private <T> T create(Class<T> protocol) {
        return (T) Proxy.newProxyInstance(protocol.getClassLoader(), new Class<?>[]{protocol},
                new DeLoreanInvocationHandler());
    }

    private  <T> void register(Class<T> protocolClass, T protocol) {
        if (protocol != null) {
            interfaces.add(protocol);
        }
    }

    public  <T>  void unregister(T protocol){
        if(protocol != null){
            interfaces.remove(protocol);
        }
    }

    public static <T> void registerReceiver(Class<T> protocolClass, T protocol) {
        if (protocol != null) {
            getDefault().register(protocolClass,protocol);
        }
    }

    public void setLogEnabled(boolean enabled) {
        this.ENABLE_LOG = enabled;
    }

    public boolean isLogEnabled() {
        return this.ENABLE_LOG;
    }

    //region reciever

    private void callMethodOnInterfaces(final String name) {
        for (Object theInterface : interfaces) {
            callMethodOnObject(theInterface, name);
        }
    }

    private void callMethodOnObject(final Object object, final String name) {
        try {
            Method method = object.getClass().getMethod(name);
            if (method != null)
                method.invoke(object);
        } catch (Exception e) {
            Log.e(TAG, "callMethodOnObject error", e);
        }
    }

    private void callMethodOnInterfaces(final DataMap methodDataMap) {
        for (Object theInterface : interfaces) {
            callMethodOnObject(theInterface, methodDataMap);
        }
    }

    private void callMethodOnObject(final Object object, final DataMap methodDataMap) {

        String methodName = methodDataMap.getString(METHOD_NAME);
        ArrayList<DataMap> methodDataMapList = methodDataMap.getDataMapArrayList(METHOD_PARAMS);

        List<Method> methodsList = getMethodWithName(object, methodName, methodDataMapList.size());
        for (Method method : methodsList) {
            try {
                if (method != null) {
                    int nbArgs = methodDataMapList.size();

                    Object[] params = new Object[nbArgs];

                    for (int argumentPos = 0; argumentPos < nbArgs; ++argumentPos) {
                        Class paramClass = method.getParameterTypes()[argumentPos];

                        DataMap map = methodDataMapList.get(argumentPos);

                        String type = map.getString(PARAM_TYPE);
                        switch (type) {
                            case (TYPE_INT):
                                params[argumentPos] = map.getInt(PARAM_VALUE);
                                break;
                            case (TYPE_FLOAT):
                                params[argumentPos] = map.getFloat(PARAM_VALUE);
                                break;
                            case (TYPE_DOUBLE):
                                params[argumentPos] = map.getDouble(PARAM_VALUE);
                                break;
                            case (TYPE_LONG):
                                params[argumentPos] = map.getLong(PARAM_VALUE);
                                break;
                            case (TYPE_STRING):
                                params[argumentPos] = map.getString(PARAM_VALUE);
                                break;
                            default: {
                                Type t = method.getGenericParameterTypes()[argumentPos];
                                Object deserialized = SerialisationUtilsGSON.deserialize(t, map.getString(PARAM_VALUE));
                                params[argumentPos] = deserialized;
                            }
                        }
                    }

                    //found the method
                    method.invoke(object, params);

                    //if call success, return / don't call other methods
                    return;
                }

            } catch (Exception e) {
                Log.e(TAG, "callMethodOnObject error", e);
            }
        }
    }

    private Class[] getParametersType(final Object[] params) {
        int count = params.length;
        Class[] types = new Class[count];
        for (int paramPos = 0; paramPos < count; ++paramPos) {
            types[paramPos] = params[paramPos].getClass();
        }
        return types;
    }

    private List<Method> getMethodWithName(final Object object, final String name, int parametersCount) {
        List<Method> methodsList = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.getName().equals(name)
                    && method.getParameterTypes().length == parametersCount)
                methodsList.add(method);
        }
        return methodsList;
    }

    public static Emmet onMessageReceived(MessageEvent messageEvent) {
        Emmet emmet = getDefault();
        emmet.messageReceived(messageEvent);
        return emmet;
    }

    //@Override
    public void messageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().startsWith(PATH)) {
            String messageName = new String(messageEvent.getData());
            callMethodOnInterfaces(messageName);
        }
    }

    public static Emmet onDataChanged(DataEventBuffer dataEvents) {
        Emmet emmet = getDefault();
        emmet.dataChanged(dataEvents);
        return emmet;
    }

    //@Override
    public void dataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            String path = dataEvent.getDataItem().getUri().getPath();
            if (ENABLE_LOG)
                Log.d(TAG, "onDataChanged(" + path + ")");
            if (path.startsWith(PATH)) { //if it's an Emmet path
                if (ENABLE_LOG)
                    Log.d(TAG, "emmet-onDataChanged " + path);

                try {

                    DataMap methodDataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                    callMethodOnInterfaces(methodDataMap);

                } catch (Throwable t) {
                    Log.e(TAG, "error decoding datamap", t);
                }
            }
        }
    }

//endregion

//region sender

    private class DeLoreanInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            String methodName = method.getName();
            if (method.getParameterTypes().length == 0) {
                sendMessage(PATH + methodName, methodName);
            } else {
                sendDataMap(PATH + methodName, proxy, method, args);
            }

            return null;
        }

        private void sendDataMap(String path, Object proxy, Method method, Object[] args) throws Throwable {
            final PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);

            DataMap datamap = putDataMapRequest.getDataMap();
            datamap.putString("timestamp", new Date().toString());
            datamap.putString(METHOD_NAME, method.getName());

            {
                ArrayList<DataMap> methodDataMapList = new ArrayList<>();

                int nbArgs = args.length;
                for (int argumentPos = 0; argumentPos < nbArgs; ++argumentPos) {
                    DataMap map = new DataMap();

                    Object argument = args[argumentPos];

                    if (argument instanceof Integer) {
                        map.putString(PARAM_TYPE, TYPE_INT);
                        map.putInt(PARAM_VALUE, (Integer) argument);
                    } else if (argument instanceof Float) {
                        map.putString(PARAM_TYPE, TYPE_FLOAT);
                        map.putFloat(PARAM_VALUE, (Float) argument);
                    } else if (argument instanceof Double) {
                        map.putString(PARAM_TYPE, TYPE_DOUBLE);
                        map.putDouble(PARAM_VALUE, (Double) argument);
                    } else if (argument instanceof Long) {
                        map.putString(PARAM_TYPE, TYPE_LONG);
                        map.putLong(PARAM_VALUE, (Long) argument);
                    } else if (argument instanceof String) {
                        map.putString(PARAM_TYPE, TYPE_STRING);
                        map.putString(PARAM_VALUE, (String) argument);
                    } else {

                        map.putString(PARAM_TYPE, argument.getClass().getName());
                        String encoded = SerialisationUtilsGSON.serialize(argument);
                        map.putString(PARAM_VALUE, encoded);

                    }

                    methodDataMapList.add(map);
                }

                datamap.putDataMapArrayList(METHOD_PARAMS, methodDataMapList);
            }

            if (ENABLE_LOG)
                Log.d(TAG, datamap.toString());

            if (mApiClient != null) {
                if (mApiClient.isConnected()) {
                    sendDataMapRequest(putDataMapRequest);
                } else {
                    mWaitingDataMapItems.add(putDataMapRequest);
                    mApiClient.connect();
                }
            }
        }

    }

    private void sendDataMapRequest(PutDataMapRequest request) {
        Wearable.DataApi.putDataItem(mApiClient, request.asPutDataRequest()).setResultCallback(
                new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (dataItemResult.getStatus().isSuccess()) {
                            if (ENABLE_LOG)
                                Log.d(TAG, "sent");
                        } else {
                            if (ENABLE_LOG)
                                Log.d(TAG, "send error");
                        }
                    }
                }
        );
    }

    private void sendDataMapRequests(List<PutDataMapRequest> requests) {
        for (PutDataMapRequest request : requests)
            sendDataMapRequest(request);
    }

    private void sendMessages(List<Pair<String, String>> requests) {
        for (Pair<String, String> pair : requests)
            sendMessage(pair.first, pair.second);
    }

    protected void sendMessage(final String path, final String message) {
        if (mApiClient != null) {
            if (mApiClient.isConnected()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final NodeApi.GetConnectedNodesResult nodes =
                                Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                    mApiClient, node.getId(), path, message.getBytes()).await();

                        }
                    }
                }).start();
            } else {
                mWaitingMessageItems.add(new Pair<>(path, message));
                mApiClient.connect();
            }
        }
    }

    //endregion

    //region dataapi

    public void setConnectionListener(ConnectionListener connectionListener) {
        mConnectionListener = connectionListener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ENABLE_LOG)
            Log.d(TAG, "onConnected");

        if (mWaitingDataMapItems != null && !mWaitingDataMapItems.isEmpty()) {
            sendDataMapRequests(mWaitingDataMapItems);
            mWaitingDataMapItems.clear();
        }

        if (mWaitingMessageItems != null && !mWaitingMessageItems.isEmpty()) {
            sendMessages(mWaitingMessageItems);
            mWaitingMessageItems.clear();
        }

        if (mConnectionListener != null) {
            mConnectionListener.onConnected(bundle);
        }

        registerNodeListener();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (ENABLE_LOG)
            Log.d(TAG, "onConnectionSuspended");
        unregisterNodeListener();
        if (mConnectionListener != null) {
            mConnectionListener.onConnectionSuspended(i);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (ENABLE_LOG)
            Log.d(TAG, "onConnectionFailed");
        if (mConnectionListener != null) {
            mConnectionListener.onConnectionFailed(connectionResult);
        }
    }

    //endregion

    //region nodeapi

    public void setNodeListener(NodeListener nodeListener) {
        unregisterNodeListener();
        mNodeListener = nodeListener;
        if (mApiClient != null && mApiClient.isConnected()) {
            registerNodeListener();
        }
    }

    private void registerNodeListener() {
        if (mNodeListener != null) {
            // request list of already connected nodes
            Wearable.NodeApi.getConnectedNodes(mApiClient).setResultCallback(this);
        }
    }

    @Override
    public void onResult(NodeApi.GetConnectedNodesResult result) {
        if (mNodeListener != null) {
            mNodeListener.onConnectedPeersListReceived(result.getNodes());
            Wearable.NodeApi.addListener(mApiClient, mNodeListener);
            mNodeListenerRegistered = true;
        }
    }

    private void unregisterNodeListener() {
        if (mNodeListenerRegistered && mNodeListener != null) {
            Wearable.NodeApi.removeListener(mApiClient, mNodeListener);
            mNodeListenerRegistered = false;
        }
    }

    //endregion

    public interface ConnectionListener {
        void onConnected(Bundle connectionHint);

        void onConnectionSuspended(int cause);

        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public static abstract class ConnectionListenerAdapter implements ConnectionListener {
        @Override
        public void onConnected(Bundle connectionHint) {
            // do nothing
        }

        @Override
        public void onConnectionSuspended(int cause) {
            // do nothing
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            // do nothing
        }
    }

    public interface NodeListener extends NodeApi.NodeListener {
        void onConnectedPeersListReceived(List<Node> nodes);
    }

    public static abstract class NodeListenerAdapter implements NodeListener {
        @Override
        public void onConnectedPeersListReceived(List<Node> nodes) {
            // do nothing
        }

        @Override
        public void onPeerConnected(Node node) {
            // do nothing
        }

        @Override
        public void onPeerDisconnected(Node node) {
            // do nothing
        }
    }

}