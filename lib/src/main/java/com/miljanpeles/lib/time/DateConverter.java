package com.miljanpeles.lib.time;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private static final String TAG = "DateConverter";

    /**
     * Konvertuje datum iz Stringa u Date
     *
     * @param date datum u stringu
     * @param datePattern pattern
     * @return formatiran datum
     */
    public static Date stringToDate(String date, DatePatterns datePattern) {

        DateFormat format = new SimpleDateFormat(datePattern.getPattern(), Locale.ENGLISH);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Proveri da li je dobar pattern!");
        }
        return null;
    }

    /**
     * Konvertuje datum iz stringa u Date pritom da moze da mu se da vise patterna i on pronalazi pravi medju njima i konvertuje
     *
     * @param date datum u stringu
     * @param datePatterns patterni
     * @return datum u Date formatu ili null ako ne odgovara ni jedan format
     */
    public static Date stringToDate(String date, DatePatterns... datePatterns) {

        for (DatePatterns datePattern : datePatterns) {
            DateFormat format = new SimpleDateFormat(datePattern.getPattern(), Locale.ENGLISH);
            try {
                return format.parse(date);
            } catch (ParseException e) {
                Log.e(TAG, "Failed, trying next pattern");
            }
        }
        return null;
    }

    /**
     * Konvertuje datum iz Date u Calendar
     *
     * @param date - Date
     * @return - vraca objekat tipa Calendar
     */
    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
