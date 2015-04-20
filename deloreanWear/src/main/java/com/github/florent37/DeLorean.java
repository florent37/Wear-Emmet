package com.github.florent37;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class DeLorean implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener, DataApi.DataListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = DeLorean.class.getSimpleName();
    private final String PATH = "/DeLoran/";
    protected GoogleApiClient mApiClient;

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

    private ArrayList<PutDataMapRequest> waitingItems = new ArrayList<>();

    public void onCreate(Context context) {
        mApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    public void onDestroy() {
        if (mApiClient != null)
            mApiClient.disconnect();
    }

    public <T> T create(Class<T> protocol) {
        return (T) Proxy.newProxyInstance(protocol.getClassLoader(), new Class<?>[]{protocol},
                new DeLoreanInvocationHandler());
    }

    public <T> void implement(Class<T> protocolClass, T protocol) {
        if (protocol != null) {
            interfaces.add(protocol);
        }
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

        try {
            Method method = getMethodWithName(object, methodName);
            if (method != null){
                ArrayList<DataMap> methodDataMapList = methodDataMap.getDataMapArrayList(METHOD_PARAMS);
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
                            Object deserialized = SerialisationUtils.deserialize(paramClass, map.getString(PARAM_VALUE));
                            params[argumentPos] = deserialized;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "callMethodOnObject error", e);
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

    private Method getMethodWithName(final Object object, final String name) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }

    private MethodDecoded decodeMethodDataMap(DataMap methodDataMap) throws Throwable {
        MethodDecoded methodDecoded = new MethodDecoded();
        methodDecoded.name = methodDataMap.getString(METHOD_NAME);

        ArrayList<DataMap> methodDataMapList = methodDataMap.getDataMapArrayList(METHOD_PARAMS);
        int nbArgs = methodDataMapList.size();

        methodDecoded.params = new Object[nbArgs];

        for (int argumentPos = 0; argumentPos < nbArgs; ++argumentPos) {
            DataMap map = methodDataMapList.get(argumentPos);

            String type = map.getString(PARAM_TYPE);
            switch (type) {
                case (TYPE_INT):
                    methodDecoded.params[argumentPos] = map.getInt(PARAM_VALUE);
                    break;
                case (TYPE_FLOAT):
                    methodDecoded.params[argumentPos] = map.getFloat(PARAM_VALUE);
                    break;
                case (TYPE_DOUBLE):
                    methodDecoded.params[argumentPos] = map.getDouble(PARAM_VALUE);
                    break;
                case (TYPE_LONG):
                    methodDecoded.params[argumentPos] = map.getLong(PARAM_VALUE);
                    break;
                case (TYPE_STRING):
                    methodDecoded.params[argumentPos] = map.getString(PARAM_VALUE);
                    break;
                default: {
                    String value = map.getString(PARAM_VALUE);
                    Object deserialized = SerialisationUtils.deserialize(Class.forName(type), value);
                    methodDecoded.params[argumentPos] = deserialized;
                }
            }
        }

        return methodDecoded;
    }


    private class MethodDecoded {
        public String name;
        public Object[] params;
    }

    //@Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().startsWith(PATH)) {
            String messageName = new String(messageEvent.getData());
            callMethodOnInterfaces(messageName);
        }
    }

    //@Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            String path = dataEvent.getDataItem().getUri().getPath();
            Log.d(TAG, "onDataChanged(" + path + ")");
            if (path.startsWith(PATH)) { //if it's a DeLorean path
                Log.d(TAG, "delorean-onDataChanged " + path);

                try {

                    DataMap methodDataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                    MethodDecoded methodDecoded = decodeMethodDataMap(methodDataMap);
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

                        if (argument instanceof Serializable) {
                            map.putString(PARAM_TYPE, argument.getClass().getName());
                            String encoded = SerialisationUtils.serialize(argument);
                            map.putString(PARAM_VALUE, encoded);
                        }

                    }

                    methodDataMapList.add(map);
                }

                datamap.putDataMapArrayList(METHOD_PARAMS, methodDataMapList);
            }

            Log.d(TAG, datamap.toString());

            if (mApiClient != null) {
                if (mApiClient.isConnected()) {
                    sendDataMapRequest(putDataMapRequest);
                } else {
                    waitingItems.add(putDataMapRequest);
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
                            Log.d(TAG, "sent");
                        } else {
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

    protected void sendMessage(final String path, final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, message.getBytes()).await();

                }
            }
        }).start();
    }


    //endregion

    //region dataapi

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mApiClient, this);
        Wearable.DataApi.addListener(mApiClient, this);

        if (waitingItems != null && !waitingItems.isEmpty())
            sendDataMapRequests(waitingItems);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //endregion
}