package com.stephen.learning.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: jack
 * @Date: 2018/9/29 20:16
 * @Description: lock的使用
 */
public class LockUse {
    private static Lock lock = new ReentrantLock();

    static class Producer implements Runnable {
        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                try {
                    lock.lock();
                    count--;
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Producer");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    try {
                        Thread.sleep(90L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                try {
                    lock.lock();
                    count--;
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Consumer");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    try {
                        Thread.sleep(90L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new LockUse.Producer()).start();
        new Thread(new LockUse.Consumer()).start();
    }
}
