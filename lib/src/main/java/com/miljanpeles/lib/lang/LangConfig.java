package com.miljanpeles.lib.lang;

import android.content.Context;
import android.content.SharedPreferences;

class LangConfig {

    private final SharedPreferences sharedPreferences;

    private final String defaultLang;

    LangConfig(Context context, String defaultLang) {
        sharedPreferences = context.getSharedPreferences("langs", Context.MODE_PRIVATE);
        this.defaultLang = defaultLang;
    }

    void saveLangConfig(String lang) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PREF_LANG_CONFIG", lang);
        editor.apply();
    }

    String loadLangConfig() {
        return sharedPreferences.getString("PREF_LANG_CONFIG", defaultLang);
    }
}