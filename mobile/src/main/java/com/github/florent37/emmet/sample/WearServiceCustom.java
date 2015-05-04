package com.github.florent37.emmet.sample;

import android.widget.Toast;

import com.github.florent37.emmet.Emmet;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.SmartphoneProtocol;
import com.github.florent37.protocol.WearProtocol;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class WearServiceCustom extends WearableListenerService implements SmartphoneProtocol {

    private final static String TAG = WearService.class.getCanonicalName();

    private Emmet emmet = new Emmet();

    private WearProtocol sender;

    @Override
    public void onCreate() {
        super.onCreate();
        emmet.onCreate(this);

        emmet.registerReceiver(SmartphoneProtocol.class, this);
        sender = emmet.createSender(WearProtocol.class);
    }

    @Override
    public void sayHello() {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sayGoodbye(int delay, String text, List<MyObject> myObject) {
        Toast.makeText(this, delay + " " + text + " " + myObject.toString(), Toast.LENGTH_SHORT).show();

        //send a message to wear
        sender.sayReceived(myObject);
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
