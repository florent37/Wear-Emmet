package com.github.florent37.emmet.sample;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.github.florent37.EmmetWearableListenerService;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.WearProtocol;

public class WearService extends EmmetWearableListenerService implements WearProtocol {

    private final static String TAG = WearService.class.getCanonicalName();

    private Handler handler = new Handler(Looper.getMainLooper());

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
    public void sayGoodbye(int delay, String text, MyObject myObject) {
        show(delay + " " + text + " " + myObject.getName());
    }

}
