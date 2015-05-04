package com.github.florent37.emmet;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

/**
 * Created by florentchampigny on 20/04/15.
 */
public class SerialisationUtils {

    public static byte[] serialize(Object obj) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            Log.e("SerialisationUtils", "error during serialize " + obj.getClass().getSimpleName(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Log.e("SerialisationUtils", "error during serialize " + obj.getClass().getSimpleName(), ex);

            }
            try {
                bos.close();
            } catch (IOException ex) {
                Log.e("SerialisationUtils", "error during serialize " + obj.getClass().getSimpleName(), ex);
            }
        }

        return null;

    }

    public static Object deserialize(Class className, byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } catch (Exception e) {
                Log.e("SerialisationUtils", "error during deserialize " + className.getSimpleName(), e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                Log.e("SerialisationUtils", "error during deserialize " + className.getSimpleName(), ex);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Log.e("SerialisationUtils", "error during deserialize " + className.getSimpleName(), ex);
            }
        }

        return null;
    }

}
