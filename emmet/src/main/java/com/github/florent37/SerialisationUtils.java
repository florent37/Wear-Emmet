package com.github.florent37;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class SerialisationUtils {
    public static Gson gson = new Gson();

    public static String serialize(Object obj){
        return gson.toJson(obj);
    }
    public static Object deserialize(Class className, String string) {
        return gson.fromJson(string, className);
    }
    public static Object deserialize(Type type, String string) {
        return gson.fromJson(string,type);
    }
}
