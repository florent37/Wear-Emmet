package com.github.florent37.emmet.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.florent37.Emmet;
import com.github.florent37.protocol.MyObject;
import com.github.florent37.protocol.WearProtocol;

public class MainActivity extends Activity implements View.OnClickListener {

    private Emmet emmet = new Emmet();
    private WearProtocol wearProtocol;

    private Button buttonHello;
    private Button buttonGoodbye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emmet.onCreate(this);
        wearProtocol = emmet.createSender(WearProtocol.class);

        buttonHello = (Button) findViewById(R.id.buttonHello);
        buttonGoodbye = (Button) findViewById(R.id.buttonGoodbye);

        buttonHello.setOnClickListener(this);
        buttonGoodbye.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emmet.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonHello:
                wearProtocol.sayHello();
                break;
            case R.id.buttonGoodbye:
                wearProtocol.sayGoodbye(3, "bye", new MyObject("DeLorean"));
                break;
        }
    }
}
