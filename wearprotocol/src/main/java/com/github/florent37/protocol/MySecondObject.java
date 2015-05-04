package com.github.florent37.protocol;

import java.io.Serializable;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class MySecondObject implements Serializable {
    private String name;
    private int value;
    private float percent;

    public MySecondObject(String name, int value, float percent) {
        this.name = name;
        this.value = value;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public float getPercent() {
        return percent;
    }
}
