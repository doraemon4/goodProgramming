package com.stephen.learning.sort;

import lombok.Builder;
import lombok.Data;

/**
 * @Auther: jack
 * @Date: 2018/9/5 17:22
 * @Description:不实现Comparable
 */
@Data
@Builder
public class Student {
    private String name;
    private int age;
    private int score;
}
