package com.github.florent37.emmet;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by florentchampigny on 21/04/15.
 */
public class EmmetWearableListenerService extends WearableListenerService {

    @Override
    public void onCreate() {
        super.onCreate();

        Emmet.onCreate(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Emmet.onDestroy(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        //on WearableListenerService you have to dispatch onMessageReceived(x)
        Emmet.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        //on WearableListenerService you have to dispatch onDataChanged(x)
        Emmet.onDataChanged(dataEvents);
    }
}
