package com.github.florent37.emmet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class SerialisationUtilsGSON {
    public static Gson gson = new Gson();

    public static String serialize(Object obj){
        return gson.toJson(obj);
    }
    public static Object deserialize(Class className, String string) {
        return gson.fromJson(string, className);
    }
    public static Object deserialize(Type type, String string) {
        return gson.fromJson(string,TypeToken.get(type).getType());
    }
}
