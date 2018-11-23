package com.xs.lightpuzzle.data.serializer;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by xs on 2018/11/5.
 */

public class Serializer {
    private final Gson gson = new Gson();

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    public String serialize(Object object, Class clazz) {
        return gson.toJson(object, clazz);
    }

    /**
     * Serialize an object to Json.
     *
     * @param object to serialize.
     */
    public String serialize(Object object, Type typeOfT) {
        return gson.toJson(object, typeOfT);
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    public <T> T deserialize(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param string A json string to deserialize.
     */
    public <T> T deserialize(String string, Type typeOfT) {
        return gson.fromJson(string, typeOfT);
    }
}
