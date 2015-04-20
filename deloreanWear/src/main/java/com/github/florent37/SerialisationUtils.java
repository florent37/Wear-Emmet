package com.github.florent37;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class SerialisationUtils {
    public static Gson gson = new Gson();

    public static String serialize(Object obj){
        return gson.toJson(obj);
    }
    public static Object deserialize(Class className, String string) {
        return gson.fromJson(string,className);
    }
}
