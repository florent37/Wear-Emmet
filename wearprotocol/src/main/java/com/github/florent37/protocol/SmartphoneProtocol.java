package com.github.florent37.protocol;

import java.util.List;

/**
 * Created by florentchampigny on 20/04/15.
 */
public interface SmartphoneProtocol {
    public void sayHello();

    public void sayGoodbye(int delay, String text, List<MyObject> myObject);
}

