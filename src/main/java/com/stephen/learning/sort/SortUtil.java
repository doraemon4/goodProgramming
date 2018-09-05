package com.stephen.learning.sort;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: jack
 * @Date: 2018/9/5 17:28
 * @Description: 排序工具类
 */
public class SortUtil {
    /**
     * 实现Comparable
     */
    public static void sortCollection(){
        List<Teacher> list=initTeachers();
        Collections.sort(list);
        System.out.println(list);
    }

    /**
     * 没有实现Comparable,实现Comparator
     */
    public static void sortCollection1(){
        List<Student> list=initStudents();
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o2.getAge()-o1.getAge();
            }
        });
        System.out.println(list);
    }

    /**
     * 多个Comparator排序(以前的逻辑是层层嵌套，一级比较相等继续第二级）
     */
    public static void sortCollection2(){
        List<Student> list=initStudents();
        Comparator comparator=ComparableComparator.INSTANCE;
        //逆序
        comparator=ComparatorUtils.reversedComparator(comparator);

        Comparator comparator2=ComparableComparator.INSTANCE;
        List<Comparator> comparators=new ArrayList<>();
        comparators.add(new BeanComparator("age",comparator));
        comparators.add(new BeanComparator("score",comparator2));
        //创建一条比较器链
        ComparatorChain comparatorChain=new ComparatorChain(comparators);
        Collections.sort(list,comparatorChain);
        System.out.println(list);
    }

    /**
     * 使用jdk1.8 lamada表达式排序
     */
    public static void sortCollection3(){
        List<Student> list=initStudents();
        list=list.stream().sorted(Comparator.comparing(Student::getAge).reversed().
                thenComparing(Student::getScore)).collect(Collectors.toList());
        System.out.println(list);
    }



    private static List<Student> initStudents(){
        List<Student> list= Lists.newArrayList();
        list.add(Student.builder().name("jack").age(18).score(100).build());
        list.add(Student.builder().name("mary").age(20).score(99).build());
        list.add(Student.builder().name("peter").age(19).score(88).build());
        list.add(Student.builder().name("jenny").age(17).score(98).build());
        list.add(Student.builder().name("alice").age(18).score(78).build());
        return list;
    }

    private static List<Teacher> initTeachers(){
        List<Teacher> list=Lists.newArrayList();
        list.add(Teacher.builder().name("teacher").age(32).build());
        list.add(Teacher.builder().name("teacher2").age(40).build());
        list.add(Teacher.builder().name("teacher3").age(34).build());
        list.add(Teacher.builder().name("teacher4").age(56).build());
        list.add(Teacher.builder().name("teacher5").age(23).build());
        return list;
    }

    public static void main(String[] args) {
        SortUtil.sortCollection();
        SortUtil.sortCollection1();
        SortUtil.sortCollection2();
        SortUtil.sortCollection3();

    }
}