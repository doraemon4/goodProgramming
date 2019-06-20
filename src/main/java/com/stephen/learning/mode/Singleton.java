package com.stephen.learning.mode;

/**
 * @author: jack
 * @Date: 2019/6/5 23:03
 * @Description:
 */
public class Singleton {
    private static volatile Singleton instance = null;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10 ; i++) {
            System.out.println(Singleton.getInstance());
        }
    }
}
