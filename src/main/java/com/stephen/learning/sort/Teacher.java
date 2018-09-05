package com.stephen.learning.sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Auther: jack
 * @Date: 2018/9/5 17:33
 * @Description: 实现Comparable
 */
@AllArgsConstructor
@Builder
@Data
public class Teacher implements Comparable<Teacher> {
    private String name;
    private int age;

    @Override
    public int compareTo(Teacher o) {
        return this.age-o.age;
    }
}
