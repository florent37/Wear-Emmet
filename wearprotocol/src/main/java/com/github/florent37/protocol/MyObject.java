package com.github.florent37.protocol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class MyObject implements Serializable{
    private String name;

    private MySecondObject mySecondObject;
    private List<MySecondObject> list;

    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MySecondObject getMySecondObject() {
        return mySecondObject;
    }

    public void setMySecondObject(MySecondObject mySecondObject) {
        this.mySecondObject = mySecondObject;
    }

    public List<MySecondObject> getList() {
        return list;
    }

    public void setList(List<MySecondObject> list) {
        this.list = list;
    }
}
