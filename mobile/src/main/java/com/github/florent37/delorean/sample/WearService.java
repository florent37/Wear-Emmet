package com.github.florent37.delorean.sample;

import android.os.Bundle;
import android.util.Log;

import com.github.florent37.DeLorean;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks {

    private final static String TAG = WearService.class.getCanonicalName();

    protected DeLorean deLorean = new DeLorean();

    @Override
    public void onCreate() {
        super.onCreate();
        deLorean.onCreate(this);

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deLorean.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        deLorean.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        deLorean.onDataChanged(dataEvents);
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
