package com.stephen.learning.lock;

/**
 * @Auther: jack
 * @Date: 2018/9/29 19:52
 * @Description:
 */
public class AsyncUse {

    static class Producer implements Runnable {

        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                count--;
                System.out.println("Producer");
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            int count = 10;
            while (count > 0) {
                count--;
                System.out.println("Consumer");
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new AsyncUse.Producer()).start();
        new Thread(new AsyncUse.Consumer()).start();
    }
}
