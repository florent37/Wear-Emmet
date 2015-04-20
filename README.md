DeLorean
=======

[![Build Status](https://travis-ci.org/florent37/DeLorean.svg)](https://travis-ci.org/florent37/DeLorean)

WORK IN PROGRESS

Download
--------

In your root build.gradle add
```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/florent37/maven"
    }
}
```


Usage
--------

To send datas from Wear to Smartphone

Create a protocol
```java
public interface WearProtocol{
    public void sayHello();

    public void sayGoodbye(int delay, String text, MyObject myObject);
}
```

Copy it in your wear module, and create a Sender
```java
public class MainActivity extends Activity {

    private DeLorean deLorean =  new DeLorean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deLorean.onCreate(this);

        WearProtocol wearProtocol = deLorean.createSender(WearProtocol.class);
        wearProtocol.sayHello();
        wearProtocol.sayGoodbye(3,"bye", new MyObject("DeLorean"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deLorean.onDestroy();
    }
}
```

Copy your protocol in your smartphone module.
To receive datas on your Smartphone Service, register a Receiver

```java
public class WearService extends WearableListenerService {

    private final static String TAG = WearService.class.getCanonicalName();

    protected DeLorean deLorean = new DeLorean();

    @Override
    public void onCreate() {
        super.onCreate();
        deLorean.onCreate(this);

        deLorean.registerReceiver(WearProtocol.class,new WearProtocol() {
            @Override
            public void sayHello() {
                Log.d(TAG,"sayHello");
            }

            @Override
            public void sayGoodbye(int delay, String text, MyObject myObject) {
                Log.d(TAG,"sayGoodbye "+delay+" "+text+" "+myObject.getName());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deLorean.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        deLorean.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        deLorean.onDataChanged(dataEvents);
    }
}
```

TODO
--------

Community
--------

Looking for contributors, feel free to fork !

Wear
--------

If you want to learn wear development : [http://tutos-android-france.com/developper-une-application-pour-les-montres-android-wear/][tuto_wear].

Credits
-------

Author: Florent Champigny

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/profile/view?id=297860624">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


Pictures by Logan Bourgouin

<a href="https://plus.google.com/+LoganBOURGOIN">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>

License
--------

    Copyright 2015 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[snap]: https://oss.sonatype.org/content/repositories/snapshots/
[tuto_wear]: http://tutos-android-france.com/developper-une-application-pour-les-montres-android-wear/