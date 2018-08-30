package com.stephen.learning.enumeration;

import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;

/**
 * @Auther: jack
 * @Date: 2018/8/27 23:57
 * @Description: 枚举类的一些操作
 */
public class EnumUtil {
    /**
     * 自定义实现通过name获取实例
     * @param name
     * @param tClass
     * @param <T>
     * @return
     */
    public static<T extends MonthConstant.Indonesian> T getByIndonesianMonth(String name,Class<T> tClass){
        for(T t:tClass.getEnumConstants()){
            if(t.name().equals(name)){
                return t;
            }
        }
        return null;
    }

    public static<T extends MonthConstant.English> T getByEnglishMonth(String name,Class<T> tClass){
        for(T t:tClass.getEnumConstants()){
            if(t.name().equals(name)){
                return t;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //使用apache的工具包
        boolean flag=Arrays.asList(MonthConstant.Indonesian.values()).
                contains(EnumUtils.getEnum(MonthConstant.Indonesian.class,"Agustus"));
        System.out.println(flag);

        flag=Arrays.asList(MonthConstant.Indonesian.values()).
                contains(EnumUtil.getByIndonesianMonth("Agustus",MonthConstant.Indonesian.class));

        System.out.println(flag);
    }
}
