package com.github.florent37.delorean.sample;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;

import com.github.florent37.DeLorean;
import com.github.florent37.delorean.sample.MyObject;
import com.github.florent37.delorean.sample.R;
import com.github.florent37.delorean.sample.WearProtocol;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private final static String TAG = MainActivity.class.getCanonicalName();
    private DeLorean deLorean =  new DeLorean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deLorean.onCreate(this);

        WearProtocol wearProtocol = deLorean.registerSender(WearProtocol.class);
        wearProtocol.sayHello();
        wearProtocol.sayGoodbye(3,"bye", new MyObject("DeLorean"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deLorean.onDestroy();
    }

}
