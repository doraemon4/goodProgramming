package com.stephen.learning.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: jack
 * @Date: 2018/10/12 14:33
 * @Description: 多线程之间实现可见性，原子性，有序性
 */
public class MutiThreadSafe {

    private static volatile int count = 0;
    private static AtomicInteger atomicCount = new AtomicInteger(0);
    private static int synchronizedCount = 0;

    private static void volatileCount() {
        for (int i = 0; i < 10; i++) {
            Executors.newFixedThreadPool(3).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 虽然使用volatile关键字修饰int变量，但是对于多线程的环境下，也很难保证没问题，所以一般用来修饰标志位
                    System.out.println("volatile count: " + (++count));
                }
            });
        }
    }

    private static void atomicCount() {
        for (int i = 0; i < 10; i++) {
            Executors.newFixedThreadPool(3).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 通过使用Atomic包中的原子类保证数据操作是原子的（数据没有重复，表示是原子操作），但是不能保障有序性
                    System.out.println("atomic count: " + atomicCount.incrementAndGet());
                }
            });
        }
    }

    private static void synchronizedCount() {
        for (int i = 0; i < 10; i++) {
            Executors.newFixedThreadPool(3).execute(new Runnable() {
                @Override
                public void run() {
                    // 通过synchronized关键字来保证线程之间的有序性
                    synchronized (MutiThreadSafe.class) {
                        System.out.println("synchronized count: " + (++synchronizedCount));
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        volatileCount();
        atomicCount();
        synchronizedCount();
    }
}
