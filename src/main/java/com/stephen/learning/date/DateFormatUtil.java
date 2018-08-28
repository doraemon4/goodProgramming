package com.stephen.learning.date;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.stephen.learning.enumeration.MonthConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Auther: jack
 * @Date: 2018/8/28 10:20
 * @Description: 时间处理
 */
public class DateFormatUtil {
    private static String PATTERN="yyyy-MM-dd HH:mm";
    private static String PATTERN2="yyyy-MM-dd HH:mm:ss";

    /**
     * 使用jdk自带的DateTimeFormatter线程安全类格式化日期
     * @param date
     * @return
     */
    public static String getFormatDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN2);
        return formatter.format(localDateTime);
    }

    /**
     * 时间转换
     * @param callTime 原始时间格式：2 Agustus 11.39
     * @param timestamp 时间戳
     * @return
     */
    public static String getTime(String callTime,String timestamp){
        //使用guava字符串分割
        List<String> data=Splitter.on(CharMatcher.is(' ').or(CharMatcher.is('.')))
                .splitToList(callTime);

        /*List<String> data2=Splitter.on(new CharMatcher() {
            @Override
            public boolean matches(char c) {
                return c==' '||c=='.';
            }
        }).splitToList(callTime);*/
        Date date=new Date(Long.valueOf(timestamp));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=MonthConstant.Indonesian.valueOf(data.get(1)).getMonth()-1;
        int day=Integer.valueOf(data.get(0));
        int hour=Integer.valueOf(data.get(2));
        int minute=Integer.valueOf(data.get(3));
        calendar.set(year,month,day,hour,minute);
        //使用apache时间格式化工具
        return DateFormatUtils.format(calendar.getTime(),PATTERN);
    }

    /**
     *
     * @param callTime 原始时间格式：August 2, 11:39
     * @param timestamp
     * @return
     */
    public static String getTime2(String callTime,String timestamp){
        List<String> data=Splitter.on(CharMatcher.is(' ').or(CharMatcher.is(',')).or(CharMatcher.is(':'))).
                //去掉空字符串
                omitEmptyStrings().
                splitToList(callTime);
        Date date=new Date(Long.valueOf(timestamp));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=MonthConstant.English.valueOf(data.get(0)).getMonth()-1;
        int day=Integer.valueOf(data.get(1));
        int hour=Integer.valueOf(data.get(2));
        int minute=Integer.valueOf(data.get(3));
        calendar.set(year,month,day,hour,minute);
        //使用apache时间格式化工具
        return DateFormatUtils.format(calendar.getTime(),PATTERN);
    }

    /**
     *
     * @param callTime 原始时间格式：8月2号 11:39
     * @param timestamp
     * @return
     */
    public static String getTime3(String callTime,String timestamp){
        List<String> data=Arrays.asList(StringUtils.split(callTime,"月号 :"));
        Date date=new Date(Long.valueOf(timestamp));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=Integer.valueOf(data.get(0))-1;
        int day=Integer.valueOf(data.get(1));
        int hour=Integer.valueOf(data.get(2));
        int minute=Integer.valueOf(data.get(3));
        calendar.set(year,month,day,hour,minute);
        //使用apache时间格式化工具
        return DateFormatUtils.format(calendar.getTime(),PATTERN);
    }


    public static void main(String[] args) {
        String time=DateFormatUtil.getTime("2 Agustus 11.39","1535096035916");
        System.out.println(time);
        time=DateFormatUtil.getTime2("August 2, 11:39","1535096035916");
        System.out.println(time);
        time=DateFormatUtil.getTime3("8月2号 11:39","1535096035916");
        System.out.println(time);
        time=DateFormatUtil.getFormatDateTime(new Date(Long.valueOf("1535096035916")));
        System.out.println(time);

        Date date=new Date(Long.valueOf("1535342773638"));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(DateFormatUtils.format(calendar.getTime(),PATTERN));
        calendar.add(Calendar.DATE,-1);
        calendar.add(Calendar.MINUTE,-120);
        System.out.println(DateFormatUtils.format(calendar.getTime(),PATTERN));
        System.out.println(-Integer.valueOf(20));

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh:mm a");
        String nowStr = now.format(format);
        System.out.println(nowStr);

    }
}
