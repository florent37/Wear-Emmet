package com.github.florent37.emmet.sample;

import android.app.Activity;
import android.os.Bundle;

import com.github.florent37.Emmet;

public class MainActivity extends Activity {

    private Emmet emmet = new Emmet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emmet.onCreate(this);

        WearProtocol wearProtocol = emmet.createSender(WearProtocol.class);
        wearProtocol.sayHello();
        wearProtocol.sayGoodbye(3, "bye", new MyObject("DeLorean"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emmet.onDestroy();
    }

}
