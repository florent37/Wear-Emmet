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
import com.github.florent37.protocol.MySecondObject;
import com.github.florent37.protocol.SmartphoneProtocol;
import com.github.florent37.protocol.WearProtocol;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, WearProtocol {

    private SmartphoneProtocol smartphoneProtocol;

    private Button buttonHello;
    private Button buttonGoodbye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Emmet.onCreate(this); //<---------- IMPORTANT

        //create a sender
        smartphoneProtocol = Emmet.createSender(SmartphoneProtocol.class);

        //register this activity as WearProtocolReceiver
        Emmet.registerReceiver(WearProtocol.class, this);

        buttonHello = (Button) findViewById(R.id.buttonHello);
        buttonGoodbye = (Button) findViewById(R.id.buttonGoodbye);

        buttonHello.setOnClickListener(this);
        buttonGoodbye.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Emmet.onDestroy(); //<---------- IMPORTANT
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonHello:
                smartphoneProtocol.sayHello();
                break;
            case R.id.buttonGoodbye:

                MyObject myObject = new MyObject("Emmet");
                myObject.setMySecondObject(new MySecondObject("0",0,0));
                myObject.setList(Arrays.asList(
                        new MySecondObject("1",1,1.0f),
                        new MySecondObject("2",2,3.0f),
                        new MySecondObject("3",3,3.0f)
                ));

                smartphoneProtocol.sayGoodbye(3, "bye", Arrays.asList(
                        myObject,
                        new MyObject("DeLorean")
                ));
                break;
        }
    }

    @Override
    public void sayReceived(List<MyObject> list) {
        Baguette.makeText(this, list.toString(), Baguette.LENGTH_SHORT).show();
    }
}
