package com.miljanpeles.lib.lang;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Klasa koja upravlja jezikom aplikacije. Pre koriscenja potrebno je u odgovarajuce values foldere smestiti odgovarajuce strings.xml fajlove*
 * Koristi se tako sto se jezik postavlja metodom changeAppLanguage kojoj se prosledjuje oznaka jezika koja se poredi iz resource (sr,en...)
 * Nakon koriscenja metode changeAppLanguage resetuje se activity aplikacije
 * setAndLoadLanguage se koristi za ucitavanje jezika, koristi se u activity pre setContentView
 */
public class LangManager {

    /**
     * Metoda koja se koristi unutar aktivnosti posle super() i pre setContentView()
     * @param context - context
     * @param defaultLang - Default jezik koji ce se ucitati iz preferenca ako nije sacuvan ni jedan jezik, ako se posalje null jezik je en
     */
    public static void setAndLoadLanguage(Activity context, String defaultLang) {
        if(defaultLang == null) defaultLang = "en";
        LangConfig langConfig;
        langConfig = new LangConfig(context, defaultLang);
        String savedLang = langConfig.loadLangConfig();
        setLocale(savedLang, context);
    }

    /**
     * Menja jezik aplikacije. Upisuje novi jezik u preference i ucitava ga sledeci put kad se koristi setAndLoadLanguage
     * @param activity - context
     * @param lang - Jezik koji se postavlja u aplikaciji. Format en, sr, ro...
     */
    public static void changeAppLanguage(Activity activity, String lang) {
        LangConfig langConfig = new LangConfig(activity, null);
        langConfig.saveLangConfig(lang);
        setLocale(lang, activity);
        resetActivity(activity);
    }

    private static void resetActivity(Activity a) {
        if(a != null ) {
            a.finish();
            a.startActivity(a.getIntent());
        }
    }

    private static void setLocale(String lang, Activity activity){
        Locale locale;
        Resources activityRes = activity.getResources();
        Configuration activityConf = activityRes.getConfiguration();

        String[] oblasti = lang.split("-");
        if(oblasti.length > 1) locale = new Locale(oblasti[0], oblasti[1]);
        else locale = new Locale(oblasti[0]);

        activityConf.setLocale(locale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = activity.getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(locale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());

        /*Locale.setDefault(locale);
        Configuration config = activity.getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());*/
    }

}
