package com.miljanpeles.lib.time;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private SimpleDateFormat simpleDateFormat;
    private final Locale locale = Locale.getDefault();
    private Date date;

    public DateHelper(@NonNull Date date) {
        this.date = date;
    }

    /**
     *
     * @return Vraca String dana (npr. 13)
     */
    public String getDay() {
        simpleDateFormat = new SimpleDateFormat("dd", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca int dana (npr. 13)
     */
    public int getIntDay() {
        simpleDateFormat = new SimpleDateFormat("dd", locale);
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    /**
     *
     * @return Vraca String meseca (npr. April)
     */
    public String getMonthLongName() {
        simpleDateFormat = new SimpleDateFormat("MMMM", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca sate:minute  (npr. 13:21)
     */
    public String getHour() {
        simpleDateFormat = new SimpleDateFormat("HH:mm", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca sate:minute AM/PM dana (npr. 1:30 AM)
     */
    public String getHourWithAMPM() {
        simpleDateFormat = new SimpleDateFormat("h:mm a", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca sate:minute u 12hour formatu (npr. 1:30)
     */
    public String getHourIn12HourFormat() {
        simpleDateFormat = new SimpleDateFormat("h:mm", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca AM ili PM
     */
    public String getDateAMPM() {
        simpleDateFormat = new SimpleDateFormat("a", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca kratak String meseca (npr. Apr)
     */
    public String getMonthShortName() {
        simpleDateFormat = new SimpleDateFormat("MMM", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca String dana (npr. Wednesday)
     */
    public String getDayOfTheWeek() {
        simpleDateFormat = new SimpleDateFormat("EEEE", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return Vraca kratki String dana (npr. Wed)
     */
    public String getDayOfWeekShortName() {
        simpleDateFormat = new SimpleDateFormat("EEE", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return String meseca (npr. 04)
     */
    public String getMonth() {
        simpleDateFormat = new SimpleDateFormat("MM", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return int meseca (npr. 04)
     */
    public int getIntMonth() {
        simpleDateFormat = new SimpleDateFormat("MM", locale);
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    /**
     *
     * @return string godine (npr. 1993)
     */
    public String getYear() {
        simpleDateFormat = new SimpleDateFormat("yyyy", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return int godine (npr. 1993)
     */
    public int getIntYear() {
        simpleDateFormat = new SimpleDateFormat("yyyy", locale);
        return Integer.parseInt(simpleDateFormat.format(date));
    }

    /**
     *
     * @return string sata (npr. 19)
     */
    public String getHourOnly() {
        simpleDateFormat = new SimpleDateFormat("HH", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return string minuta (npr. 54)
     */
    public String getMinuteOnly() {
        simpleDateFormat = new SimpleDateFormat("mm", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return string sekunde (npr. 23)
     */
    public String getSeconds() {
        simpleDateFormat = new SimpleDateFormat("ss", locale);
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @return int sekunde (npr. 23)
     */
    public int getIntSeconds() {
        simpleDateFormat = new SimpleDateFormat("ss", locale);
        return Integer.parseInt(simpleDateFormat.format(date));
    }


}
