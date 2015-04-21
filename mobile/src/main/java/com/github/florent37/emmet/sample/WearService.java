package com.github.florent37.emmet.sample;

import android.widget.Toast;

import com.github.florent37.EmmetWearableListenerService;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.WearProtocol;

public class WearService extends EmmetWearableListenerService implements WearProtocol {

    private final static String TAG = WearService.class.getCanonicalName();

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        deLorean.registerReceiver(WearProtocol.class,new WearProtocol() {
            @Override
            public void sayHello() {
                Log.d(TAG,"sayH ello");
            }

            @Override
            public void sayGoodbye(int delay, String text, MyObject myObject) {
                Log.d(TAG,"sayGoodbye "+delay+" "+text+" "+myObject.getName());
            }
        });
        */

        getEmmet().registerReceiver(WearProtocol.class, this);
    }

    @Override
    public void sayHello() {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sayGoodbye(int delay, String text, MyObject myObject) {
        Toast.makeText(this, delay + " " + text + " " + myObject.getName(), Toast.LENGTH_SHORT).show();
    }

}
