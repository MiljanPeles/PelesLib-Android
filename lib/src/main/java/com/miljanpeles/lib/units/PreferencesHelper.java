package com.miljanpeles.lib.units;

import android.content.SharedPreferences;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miljanpeles.lib.json.GsonHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Klasa za skladistenje podataka u shared preference
 * Sadrzi dodatne metode koje klasicni sharedpreference nema
 */
public class PreferencesHelper {

    private static final String DEFAULT_DELIMITER = ",";

    private PreferencesHelper() {}

    /**
     * Konvertuje i upisuje double vrednost kao long
     * @param editor shared preference editor
     * @param key - kljuc
     * @param value - vrednost
     * @return vraca prosledjen editor objekat
     */
    @NonNull
    public static SharedPreferences.Editor putDouble(@NonNull SharedPreferences.Editor editor, @NonNull String key, double value) {
        editor.putLong(key, Double.doubleToLongBits(value));
        return editor;
    }

    /**
     * Uzimanje double vrednosti
     * @param prefs - objekat sharedpreference
     * @param key - kljuc
     * @param defaultValue - default vrednost
     * @return Vraca double vrednost sacuvanu kao long
     */
    public static double getDouble(@NonNull SharedPreferences prefs, @NonNull String key, double defaultValue) {
        long bits = prefs.getLong(key, Double.doubleToLongBits(defaultValue));
        return Double.longBitsToDouble(bits);
    }


    /**
     * Upisuje Date kao long
     * @param editor shared preference editor
     * @param key kljuc
     * @param value vrednost
     * @return vraca prosledjen editor objekat
     */
    @NonNull
    public static SharedPreferences.Editor putDate(@NonNull SharedPreferences.Editor editor, @NonNull String key, @Nullable Date value) {
        editor.putLong(key, value == null ? Long.MIN_VALUE : value.getTime());
        return editor;
    }

    /**
     * Vraca date, Long.MIN_VALUE se koristi ako je date null
     * @param prefs - objekat sharedpreference
     * @param key - kljuc
     * @return Date objekat
     */
    @Nullable
    public static Date getDate(@NonNull SharedPreferences prefs, @NonNull String key) {
        long time = prefs.getLong(key, Long.MIN_VALUE);
        return time == Long.MIN_VALUE ? null : new Date(time);
    }


    /**
     * Upisuje array stringova
     * @param editor shared preference editor
     * @param key kljuc
     * @param values vrednost
     * @return vraca prosledjen editor objekat
     */
    @NonNull
    public static SharedPreferences.Editor putStringArray(@NonNull SharedPreferences.Editor editor, @NonNull String key, @Nullable String[] values) {
        editor.putString(key, concat(values));
        return editor;
    }

    /**
     * Vraca string array
     * @param prefs objekat sharedpreference
     * @param key kljuc
     * @return String array
     */
    @Nullable
    public static String[] getStringArray(@NonNull SharedPreferences prefs, @NonNull String key) {
        return split(prefs.getString(key, null));
    }

    /**
     * Upisuje serializable objekat kao BASE_64 enkodiran string
     * @param editor shared preference editor
     * @param key kljuc
     * @param obj objekat
     * @return vraca prosledjen editor objekat
     */
    @NonNull
    public static SharedPreferences.Editor putSerializable(@NonNull SharedPreferences.Editor editor, @NonNull String key, @Nullable Serializable obj) {
        editor.putString(key, serialize(obj));
        return editor;
    }

    /**
     * Vraca serializable objekat sacuvan kao BASE_64 enkodiran string
     * @param prefs objekat sharedpreference
     * @param key kljuc
     * @return Serializable objekat
     */
    @Nullable
    public static Serializable getSerializable(@NonNull SharedPreferences prefs, @NonNull String key) {
        return deserialize(prefs.getString(key, null));
    }


    /**
     * Upisuje objekat kao json string, potrebna Gson biblioteka
     * @param editor editor
     * @param key kljuc
     * @param obj objekat
     * @return vraca prosledjen editor objekat
     */
    @NonNull
    public static SharedPreferences.Editor putObj(@NonNull SharedPreferences.Editor editor, @NonNull String key, @Nullable Object obj) {
        editor.putString(key, GsonHelper.toJson(obj));
        return editor;
    }


    /**
     * Vraca objekat sacuvan kao json string
     * @param prefs preference
     * @param key kljuc
     * @param clazz klasa
     * @param <T> generic
     * @return objekat
     */
    @Nullable
    public static <T> T getJson(@NonNull SharedPreferences prefs, @NonNull String key, @NonNull Class<T> clazz) {
        return getJson(prefs, key, (Type) clazz);
    }


    /**
     * Vraca objekat sacuvan kao json string
     * @param prefs preference
     * @param key kljuc
     * @param type tip, primer type: Type listType = new TypeToken<MojObjekat>(){}.getType();
     * @param <T> generic
     * @return objekat
     */
    @Nullable
    public static <T> T getJson(@NonNull SharedPreferences prefs, @NonNull String key, @NonNull Type type) {
        return GsonHelper.fromJson(prefs.getString(key, null), type);
    }


    /* Helper methods */

    @Nullable
    private static String concat(@Nullable String[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        final StringBuilder str = new StringBuilder();
        for (String val : values) {
            str.append(val).append(DEFAULT_DELIMITER);
        }
        str.delete(str.length() - DEFAULT_DELIMITER.length(), str.length());
        return str.toString();
    }

    @Nullable
    private static String[] split(@Nullable String value) {
        return value == null ? null : value.split(DEFAULT_DELIMITER);
    }

    @Nullable
    private static String serialize(@Nullable Serializable obj) {
        if (obj == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            out.close();
            return Base64.encodeToString(byteOut.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static Serializable deserialize(@Nullable String serialized) {
        if (serialized == null) {
            return null;
        }
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(
                    Base64.decode(serialized, Base64.DEFAULT));
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Serializable obj = (Serializable) in.readObject();
            in.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
