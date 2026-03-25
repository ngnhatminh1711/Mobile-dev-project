package com.lttbdd.hrmsystem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String today() {
        return fmt(Constants.DATE_FORMAT).format(new Date());
    }

    public static String currentMonth() {
        return fmt(Constants.MONTH_FORMAT).format(new Date());
    }

    public static String currentYear() {
        return fmt("yyyy").format(new Date());
    }

    public static String currentTime() {
        return fmt(Constants.TIME_FORMAT).format(new Date());
    }

    public static String toDisplay(String dbDate) {
        try {
            return fmt(Constants.DISPLAY_DATE).format(fmt(Constants.DATE_FORMAT).parse(dbDate));
        } catch (Exception e) {
            return dbDate;
        }
    }

    public static String monthToDisplay(String dbMonth) {
        try {
            return fmt(Constants.DISPLAY_MONTH).format(fmt(Constants.MONTH_FORMAT).parse(dbMonth));
        } catch (Exception e) {
            return dbMonth;
        }
    }

    public static String getWeekStart(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fmt(Constants.DATE_FORMAT).parse(date));
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return fmt(Constants.DATE_FORMAT).format(cal.getTime());
        } catch (ParseException e) {
            return date;
        }
    }

    public static String getWeekEnd(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fmt(Constants.DATE_FORMAT).parse(date));
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            return fmt(Constants.DATE_FORMAT).format(cal.getTime());
        } catch (ParseException e) {
            return date;
        }
    }

    public static String millisToDate(long millis) {
        return fmt(Constants.DATE_FORMAT).format(new Date(millis));
    }

    private static SimpleDateFormat fmt(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }
}
