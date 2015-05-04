Emmet
=======

[![Build Status](https://travis-ci.org/florent37/Emmet.svg)](https://travis-ci.org/florent37/Emmet)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Emmet-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1741)
[![Join the chat at https://gitter.im/florent37/Emmet](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/florent37/Emmet?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


![Alt wearprotocol](https://raw.githubusercontent.com/florent37/Emmet/master/mobile/src/main/res/drawable/emmet_small.png)

Emmet is an protocol based data-transfer for Android Wear

Sample
--------

Take a look to a fully sample app using Emmet and [DaVinci](https://github.com/florent37/DaVinci) : [https://github.com/florent37/Potier](https://github.com/florent37/Potier)


Protocol-based Exchange
--------

Emmet is based on data exchanges by protocol.
You have to create your protocol, wich will be used by your wear and smartphone module.

Create a new library module, for example **wearprotocol**

Import **wearprotocol** into your **wear & smartphone modules**

```java
dependencies {
    compile project(':wearprotocol')
}
```

In the module wearprotocol import **emmet**

[![Download](https://api.bintray.com/packages/florent37/maven/Emmet/images/download.svg)](https://bintray.com/florent37/maven/Emmet/_latestVersion)
```java
dependencies {
    compile ("com.github.florent37:emmet:1.0.1@aar"){
        transitive=true
    }
}
```

Declare your protocols into interfaces
```java
//messages to send at SmartWatch
public interface WearProtocol{
    public void sayReceived(String text);
}
```

```java
//messages to send at SmartPhone
public interface SmartphoneProtocol{
     public void sayHello();

    public void sayGoodbye(int delay, String text, MyObject myObject);
}
```

And add your shared models into this module
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

![Alt wearprotocol](https://raw.githubusercontent.com/florent37/Emmet/master/mobile/src/main/res/drawable/module_protocol_small.png)


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

Send datas
--------

To send datas, just create a Sender

**I will only show how to send datas to From Wear -> Smartphone, it's the same process for Smartphone -> Wear**

```java
SmartphoneProtocol smartphoneProtocol = emmet.createSender(SmartphoneProtocol.class);
```

And simply call method on the implemented protocol, the message will be send to Smartphone
```java
smartphoneProtocol.sayHello();
```
Can be used with parameters
```java
smartphoneProtocol.sayGoodbye(3,"bye", new MyObject("DeLorean"));
```

Receive datas
--------

To receive datas, simply register a Receiver
*(can be used from Wear or Smartphone)*

**Simple note, Smartphone Service created only with the reception of a message coming from the SmartWatch**

```java
emmet.registerReceiver(SmartphoneProtocol.class,new SmartphoneProtocol() {
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
public class **** implements SmartphoneProtocol {

    @Override
    public void onCreate() {
        super.onCreate();
        getEmmet().registerReceiver(SmartphoneProtocol.class,this);
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
