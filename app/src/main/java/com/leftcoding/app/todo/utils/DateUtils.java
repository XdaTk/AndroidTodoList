package com.leftcoding.app.todo.utils;

import java.text.SimpleDateFormat;

public class DateUtils {
    public static String getDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public static String getTime(long time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }
    public static String getFullDate(long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return fullDateFormat.format(date);
    }
}
