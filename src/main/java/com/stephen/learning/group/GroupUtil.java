package com.stephen.learning.group;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: jack
 * @Date: 2018/11/14 10:39
 * @Description: 分组，求和
 */
public class GroupUtil {
    @Data
    @AllArgsConstructor
    public static class Student{
        private String name;
        private String sex;
        private double score;
    }

    /**
     * 根据年龄分组
     * @return
     */
    public static Map<String,List<Student>> groupByAge(){
        List<Student> students=initStudents();
        Map<String,List<Student>> map=students.stream().collect(Collectors.groupingBy(Student::getSex));
        return map;
    }

    /**
     * 计算总分数
     * @return
     */
    public static double getTotalScore(){
        List<Student> students=initStudents();
        return students.stream().mapToDouble(Student::getScore).sum();
    }


    private static List<Student> initStudents(){
        List<Student> list= Lists.newArrayList();
        list.add(new Student("jack","male",98));
        list.add(new Student("peter","male",68));
        list.add(new Student("jenny","female",98));
        list.add(new Student("alice","female",70));
        list.add(new Student("lily","female",100));
        return list;
    }

    public static void main(String[] args) {
        System.out.println(GroupUtil.groupByAge());
        System.out.println(GroupUtil.getTotalScore());
    }
}
