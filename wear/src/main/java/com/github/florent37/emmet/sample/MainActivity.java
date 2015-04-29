package com.github.florent37.emmet.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.github.florent37.baguette.Baguette;
import com.github.florent37.emmet.Emmet;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.SmartphoneProtocol;
import com.github.florent37.protocol.WearProtocol;

import java.util.Arrays;

public class MainActivity extends Activity implements View.OnClickListener, WearProtocol {

    private Emmet emmet = new Emmet();
    private SmartphoneProtocol wearProtocol;

    private Button buttonHello;
    private Button buttonGoodbye;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emmet.onCreate(this); //<---------- IMPORTANT

        //create a sender
        wearProtocol = emmet.createSender(SmartphoneProtocol.class);

        //register this activity as WearProtocolReceiver
        emmet.registerReceiver(WearProtocol.class,this);

        buttonHello = (Button) findViewById(R.id.buttonHello);
        buttonGoodbye = (Button) findViewById(R.id.buttonGoodbye);

        buttonHello.setOnClickListener(this);
        buttonGoodbye.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emmet.onDestroy(); //<---------- IMPORTANT
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonHello:
                wearProtocol.sayHello();
                break;
            case R.id.buttonGoodbye:

                wearProtocol.sayGoodbye(3, "bye", Arrays.asList(new MyObject("DeLorean"),new MyObject("Emmet")));
                break;
        }
    }

    @Override
    public void sayReceived(String text) {
        Baguette.makeText(this, text, Baguette.LENGTH_SHORT).show();
    }
}
