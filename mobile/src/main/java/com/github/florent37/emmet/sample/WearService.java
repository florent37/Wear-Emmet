package com.github.florent37.emmet.sample;

import android.util.Log;

import com.github.florent37.Emmet;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearService extends WearableListenerService implements WearProtocol {

    private final static String TAG = WearService.class.getCanonicalName();

    private Emmet emmet = new Emmet();

    @Override
    public void onCreate() {
        super.onCreate();
        emmet.onCreate(this);

        /*
        deLorean.registerReceiver(WearProtocol.class,new WearProtocol() {
            @Override
            public void sayHello() {
                Log.d(TAG,"sayHello");
            }

            @Override
            public void sayGoodbye(int delay, String text, MyObject myObject) {
                Log.d(TAG,"sayGoodbye "+delay+" "+text+" "+myObject.getName());
            }
        });
        */

        emmet.registerReceiver(WearProtocol.class, this);
    }

    @Override
    public void sayHello() {
        Log.d(TAG, "sayHello");
    }

    @Override
    public void sayGoodbye(int delay, String text, MyObject myObject) {
        Log.d(TAG, "sayGoodbye " + delay + " " + text + " " + myObject.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        emmet.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        //on WearableListenerService you have to dispatch onMessageReceived(x)
        emmet.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        //on WearableListenerService you have to dispatch onDataChanged(x)
        emmet.onDataChanged(dataEvents);
    }


}
