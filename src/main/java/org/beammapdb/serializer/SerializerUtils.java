package org.beammapdb.serializer;

import org.beammapdb.Serializer;

import java.util.HashMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by jan on 2/28/16.
 */
public final class SerializerUtils {

    private static Map<Class, Serializer> SERIALIZER_FOR_CLASS = new HashMap();

    static {
            SERIALIZER_FOR_CLASS.put(char.class, Serializer.CHAR);
            SERIALIZER_FOR_CLASS.put(Character.class, Serializer.CHAR);
            SERIALIZER_FOR_CLASS.put(String.class, Serializer.STRING);
            SERIALIZER_FOR_CLASS.put(long.class, Serializer.LONG);
            SERIALIZER_FOR_CLASS.put(Long.class, Serializer.LONG);
            SERIALIZER_FOR_CLASS.put(int.class, Serializer.INTEGER);
            SERIALIZER_FOR_CLASS.put(Integer.class, Serializer.INTEGER);
            SERIALIZER_FOR_CLASS.put(boolean.class, Serializer.BOOLEAN);
            SERIALIZER_FOR_CLASS.put(Boolean.class, Serializer.BOOLEAN);
            SERIALIZER_FOR_CLASS.put(byte[].class, Serializer.BYTE_ARRAY);
            SERIALIZER_FOR_CLASS.put(char[].class, Serializer.CHAR_ARRAY);
            SERIALIZER_FOR_CLASS.put(int[].class, Serializer.INT_ARRAY);
            SERIALIZER_FOR_CLASS.put(long[].class, Serializer.LONG_ARRAY);
            SERIALIZER_FOR_CLASS.put(double[].class, Serializer.DOUBLE_ARRAY);
            SERIALIZER_FOR_CLASS.put(UUID.class, Serializer.UUID);
            SERIALIZER_FOR_CLASS.put(byte.class, Serializer.BYTE);
            SERIALIZER_FOR_CLASS.put(Byte.class, Serializer.BYTE);
            SERIALIZER_FOR_CLASS.put(float.class, Serializer.FLOAT);
            SERIALIZER_FOR_CLASS.put(Float.class, Serializer.FLOAT);
            SERIALIZER_FOR_CLASS.put(double.class, Serializer.DOUBLE);
            SERIALIZER_FOR_CLASS.put(Double.class, Serializer.DOUBLE);
            SERIALIZER_FOR_CLASS.put(short.class, Serializer.SHORT);
            SERIALIZER_FOR_CLASS.put(Short.class, Serializer.SHORT);
            SERIALIZER_FOR_CLASS.put(short[].class, Serializer.SHORT_ARRAY);
            SERIALIZER_FOR_CLASS.put(float[].class, Serializer.FLOAT_ARRAY);
            SERIALIZER_FOR_CLASS.put(BigDecimal.class, Serializer.BIG_DECIMAL);
            SERIALIZER_FOR_CLASS.put(BigInteger.class, Serializer.BIG_INTEGER);
            SERIALIZER_FOR_CLASS.put(Class.class, Serializer.CLASS);
            SERIALIZER_FOR_CLASS.put(Date.class, Serializer.DATE);

    }


    public static <R> Serializer<R> serializerForClass(Class<R> clazz){
        return SERIALIZER_FOR_CLASS.get(clazz);
    }

    public static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }



}
