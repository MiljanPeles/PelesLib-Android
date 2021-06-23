package com.miljanpeles.lib.json;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonHelper {

    private static Boolean hasGson;
    private static Object gson;

    /**
     * Da li projekat na kojem radis ima implementiran gson lib?
     * @return true ako ima, false ako nema
     */
    public static boolean hasGson() {
        if (hasGson == null) {
            try {
                Class.forName("com.google.gson.Gson");
                hasGson = true;
            } catch (Exception e) {
                hasGson = false;
            }
        }
        return hasGson;
    }

    /**
     * Singlton gson
     * @return gson
     */
    public static Gson get() {
        if (gson == null) {
            gson = new Gson();
        }
        return (Gson) gson;
    }

    /**
     * Pretvara objekat u json
     * @param obj objekat
     * @return json string
     */
    public static String toJson(Object obj) {
        try {
            return obj == null ? null : get().toJson(obj);
        } catch (Exception e) {
            Log.e("GsonHelper", "Cannot convert object to JSON", e);
            return null;
        }
    }

    /**
     * Iz jsona u objekat
     * @param str json
     * @param clazz klasa
     * @param <T> generik
     * @return objekat
     */
    public static <T> T fromJson(String str, Class<T> clazz) {
        return fromJson(str, (Type) clazz);
    }

    /**
     * Iz jsona u objekat
     * @param str json
     * @param type tip
     * @param <T> generic
     * @return objekat
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String str, Type type) {
        try {
            return str == null ? null : (T) get().fromJson(str, type);
        } catch (Exception e) {
            Log.e("GsonHelper", "Cannot parse JSON to object", e);
            return null;
        }
    }
}