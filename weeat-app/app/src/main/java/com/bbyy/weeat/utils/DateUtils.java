package com.bbyy.weeat.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class DateUtils {
    public static int getDurationDays(long ddl) {
        long differenceInDays = (ddl - getToday()) / (24 * 60 * 60 * 1000);
        return (int)differenceInDays;
    }

    private static void initCalendar(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static long getStartOfDay(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        initCalendar(calendar);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDay(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        initCalendar(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    /*
    * 本日起点
    * */
    public static long getToday() {
        Calendar calendar = Calendar.getInstance();
        initCalendar(calendar);
        return calendar.getTimeInMillis();
    }

    public static String getDateString(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    //要求ddl设置至少间隔一天
    public static boolean isAtLeastOneDayApart(long timestamp){
        return getDurationDays(timestamp) >= 1;
    }

    /*
     * 本周起点
     * */
    public static long getFirstDayOfThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        initCalendar(calendar);
        return calendar.getTimeInMillis();
    }

    /*
     * 本月起点
     * */
    public static long getFirstDayOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        initCalendar(calendar);
        return calendar.getTimeInMillis();
    }

    /*
     * 本年起点
     * */
    public static long getFirstDayOfThisYear() {
        Calendar calendar = Calendar.getInstance();
        initCalendar(calendar);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static String getRemainDayString(long remainDays){
        if(remainDays<=1)
            return remainDays+" day";
        else
            return remainDays+" days";
    }

    public static String getTabDateText(int tabPosition){
        Calendar calendar = Calendar.getInstance();
        int position = tabPosition+1;
        String pattern="MMM dd, EEE";
        calendar.set(Calendar.DAY_OF_WEEK, position);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static long getTabTimestamp(int tabPosition){
        Calendar calendar = Calendar.getInstance();
        int position = tabPosition+1;
        calendar.set(Calendar.DAY_OF_WEEK, position);
        return calendar.getTimeInMillis();
    }

    public static String getTimeString(long timestamp){
        String pattern="HH:MM";
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(calendar.getTime());
    }
}
