package com.stephen.learning.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: jack
 * @Date: 2018/10/22 11:13
 * @Description: CountDownLatch实现：某一线程在开始运行前等待n个线程执行完毕
 */
public class CountDownLatchUse {
    private static CountDownLatch countDownLatch=new CountDownLatch(2);

    private static class MyThread extends Thread{
        @Override
        public void run() {
            try {
                System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                Thread.sleep(3000);
                System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            countDownLatch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
