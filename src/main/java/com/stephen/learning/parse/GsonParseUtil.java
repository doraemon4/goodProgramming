package com.stephen.learning.parse;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Auther: jack
 * @Date: 2018/9/27 21:50
 * @Description: 使用gson与json之间转换
 */
public class GsonParseUtil {
    public  static <T> String obj2Json(T t){
        Gson gson = new Gson();
        //把对象转为JSON格式的字符串
        String objectStr = gson.toJson(t);
        return objectStr;
    }

    public static <T> String objs2Json(List<T> t){
        return obj2Json(t);
    }

    public static <T> List json2Obj(String jsonStr,Class T){
        Gson gson = new Gson();
        //把JSON格式的字符串转为List
        return gson.fromJson(jsonStr, new TypeToken<List<T>>(){}.getType());
    }

    @Data
    @Builder
    @ToString
    static class Person {
        private String name;
        private String gender;
        private int age;
    }

    public static void main(String[] args) {
        Person person=Person.builder().name("jack").gender("male").age(18).build();
        System.out.println(GsonParseUtil.obj2Json(person));

        System.out.println("--------------------------------华丽的分割线--------------------------");
        List<Person> persons= Lists.newArrayList();
        person=Person.builder().name("peter").gender("male").age(18).build();
        persons.add(person);
        person=Person.builder().name("alice").gender("female").age(18).build();
        persons.add(person);
        person=Person.builder().name("smith").gender("male").age(26).build();
        persons.add(person);
        person=Person.builder().name("jenny").gender("female").age(20).build();
        persons.add(person);
        System.out.println(GsonParseUtil.objs2Json(persons));

        System.out.println("--------------------------------华丽的分割线--------------------------");
        String jsonStr="[{\"name\":\"peter\",\"gender\":\"male\",\"age\":18},{\"name\":\"alice\",\"gender\":\"female\",\"age\":18},{\"name\":\"smith\",\"gender\":\"male\",\"age\":26},{\"name\":\"jenny\",\"gender\":\"female\",\"age\":20}]";
        System.out.println(GsonParseUtil.json2Obj(jsonStr,Person.class));
    }
}
