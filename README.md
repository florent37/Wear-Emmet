Emmet
=======

[![Build Status](https://travis-ci.org/florent37/Wear-Emmet.svg)](https://travis-ci.org/florent37/Wear-Emmet)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Emmet-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1741)
[![Join the chat at https://gitter.im/florent37/Emmet](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/florent37/Wear-Emmet?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


![Alt wearprotocol](https://raw.githubusercontent.com/florent37/Emmet/master/mobile/src/main/res/drawable/emmet_small.png)

Emmet is an protocol based data-transfer for Android Wear

#Sample

Take a look to a fully sample app using Emmet and [DaVinci](https://github.com/florent37/DaVinci) : [https://github.com/florent37/Potier](https://github.com/florent37/Potier)

#Usage

1/ Declare your protocols into interfaces
```java
public interface WearProtocol{
    public void sendMyObject(MyObject object);
}
```

2/ Use Emmet to send data to the Wear
```java
WearProtocol wearProtocol = Emmet.createSender(WearProtocol.class);
wearProtocol.sendMyObject(myObject);
```

3/ Receive the data in Wear
```
Emmet.registerReceiver(WearProtocol.class, new WearProtocol() {
    @Override
    public void sendMyObject(MyObject object) {
        /* Do whatever you want with the object */
    }
});
```

Please take a look to the [wiki](https://github.com/florent37/Wear-Emmet/wikir)

#Import

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
    compile ("com.github.florent37:emmet:1.0.3@aar"){
        transitive=true
    }
}
```

![Alt wearprotocol](https://raw.githubusercontent.com/florent37/Emmet/master/mobile/src/main/res/drawable/module_protocol_small.png)


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
