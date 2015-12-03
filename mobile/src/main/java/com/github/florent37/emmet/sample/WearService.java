package com.github.florent37.emmet.sample;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.github.florent37.emmet.Emmet;
import com.github.florent37.emmet.EmmetWearableListenerService;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.SmartphoneProtocol;
import com.github.florent37.protocol.WearProtocol;

import java.util.List;

public class WearService extends EmmetWearableListenerService implements SmartphoneProtocol {

    //private final static String TAG = "WearService";

    private Handler handler = new Handler(Looper.getMainLooper());

    private WearProtocol sender;

    @Override
    public void onCreate() {
        super.onCreate();
        getEmmet().registerReceiver(SmartphoneProtocol.class, this);
        sender = getEmmet().createSender(WearProtocol.class);
    }

    private void show(final String text){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WearService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void sayHello() {
        show("hello");
    }

    @Override
    public void sayGoodbye(int delay, String text, List<MyObject> myObject) {
        show(delay + " " + text + " " + myObject.toString());


        //send a message to wear
        sender.sayReceived(myObject);
    }

}
