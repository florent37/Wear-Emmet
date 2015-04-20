package com.github.florent37.delorean.sample;

import java.io.Serializable;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class MyObject implements Serializable{
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
