package com.miljanpeles.lib.json;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class JsonLoader {

    /**
     * Ucitava json iz fajla sa putanje iz asseta, vraca ceo json string
     * @param context - kontekst poziva
     * @param putanja - putanja ka fajlu
     * @return - String json
     */
	public static String loadJSONFromAsset(Context context, String putanja) {
	    String json = null;
	    try {
	        InputStream is = context.getAssets().open( putanja );
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        json = new String(buffer, "UTF-8");
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;
	}
}
