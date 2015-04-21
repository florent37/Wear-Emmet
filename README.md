Emmet
=======

[![Build Status](https://travis-ci.org/florent37/Emmet.svg)](https://travis-ci.org/florent37/Emmet)

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

Protocol-based Exchange
--------

Emmet is based on data exchanges by protocol.
You have to create your protocol, wich will be used by your wear and smartphone module.

Create a new library module, for exmaple **wearprotocol**
In the module wearprotocol, import **emmet**

```java
import ("com.github.florent37:emmet:1.0.0@aar"){
    transitive=true
}
```

Declare your protocol
```java
public interface WearProtocol{
    public void sayHello();

    public void sayGoodbye(int delay, String text, MyObject myObject);
}
```

And add your shared models into it
```java
public class MyObject{
    private String name;

    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Setup
--------

**Wear - Activity**

To use Emmet, you have to create an new instance for each activity
and attach it to his life-cycle

```java
public class MainActivity extends Activity {

    private Emmet emmet = new Emmet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emmet.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emmet.onDestroy();
    }
}
```

**Smartphone - Service**

Extend EmmetWearableListenerService

```java

public class WearService extends EmmetWearableListenerService implements WearProtocol {

    @Override
    public void onCreate() {
        super.onCreate();

        Emmet emmet = getEmmet();
    }

}
```

Or Create an ew instance in the WearableListenerService
and attach it to his life-cycle,
don't foget to dispatch onMessageReceived(x) and onDataChanged(x)

```java
public class WearService extends WearableListenerService {

    private Emmet emmet = new Emmet();

    @Override
    public void onCreate() {
        super.onCreate();
        emmet.onCreate(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        emmet.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        //on WearableListenerService you have to dispatch onMessageReceived(x)
        emmet.onMessageReceived(messageEvent);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        //on WearableListenerService you have to dispatch onDataChanged(x)
        emmet.onDataChanged(dataEvents);
    }
}
```

Send datas
--------

To send datas, just create a Sender

```java
WearProtocol wearProtocol = emmet.createSender(WearProtocol.class);
```

And simply call method on the implemented protocol
```java
wearProtocol.sayHello();
```
Can be used with parameters
```java
wearProtocol.sayGoodbye(3,"bye", new MyObject("DeLorean"));
```

Receive datas
--------

To receive datas, simply register a Receiver

```java
getEmmet().registerReceiver(WearProtocol.class,new WearProtocol() {
    @Override
    public void sayHello() {
        Log.d(TAG,"sayHello");
    }

    @Override
    public void sayGoodbye(int delay, String text, MyObject myObject) {
        Log.d(TAG,"sayGoodbye "+delay+" "+text+" "+myObject.getName());
    }
});
```

Or directly implement it
```java
public class WearService extends EmmetWearableListenerService implements WearProtocol {

    @Override
    public void onCreate() {
        super.onCreate();
        getEmmet().registerReceiver(WearProtocol.class,this);
    }

    @Override
    public void sayHello() {
        Log.d(TAG,"sayHello");
    }

    @Override
    public void sayGoodbye(int delay, String text, MyObject myObject) {
        Log.d(TAG,"sayGoodbye "+delay+" "+text+" "+myObject.getName());
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

Dependencies
--------

- GSON from Google : [https://github.com/google/gson][gson]

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
[gson]: https://github.com/google/gson