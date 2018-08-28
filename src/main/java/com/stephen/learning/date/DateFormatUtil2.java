package com.stephen.learning.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: jack
 * @Date: 2018/8/28 23:39
 * @Description: 使用simpleDateFormat线程安全
 */
public class DateFormatUtil2 {
    private static String PATTERN="yyyy-MM-dd HH:mm:ss";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(PATTERN);
        }
    };

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }

    public static void main(String[] args) {
        System.out.println(DateFormatUtil2.format(new Date()));
    }
}
