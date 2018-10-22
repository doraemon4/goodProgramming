package com.stephen.learning.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: jack
 * @Date: 2018/10/22 15:37
 * @Description: CountDownLatch实现：实现多个线程开始执行任务的最大并行性
 */
public class CountDownLatchUse2 {
    private static CountDownLatch countDownLatch =new CountDownLatch(1);
    private static class MyThead extends Thread{
        @Override
        public void run() {
            try{
                System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                countDownLatch.await();
                System.out.println("子线程" + Thread.currentThread().getName() + "继续执行");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MyThead().start();
        new MyThead().start();
        try{
            Thread.sleep(3000);
            countDownLatch.countDown();
            System.out.println("主线程继续执行");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
