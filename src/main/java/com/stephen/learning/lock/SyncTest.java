package com.stephen.learning.lock;

/**
 * @Auther: jack
 * @Date: 2018/9/29 18:30
 * @Description: synchronized的使用
 */
public class SyncTest {
    public static final Object obj = new Object();

    static class Producer implements Runnable{

        @Override
        public void run() {
            int count=10;
            while (count>0){
                synchronized (SyncTest.obj) {
                    count--;
                    System.out.println("Producer");
                    SyncTest.obj.notifyAll();
                    try{
                        SyncTest.obj.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    static class Consumer implements Runnable{

        @Override
        public void run() {
            int count=10;
            while (count>0){
                synchronized (SyncTest.obj) {
                    count--;
                    System.out.println("Consumer");
                    SyncTest.obj.notifyAll();
                    try{
                        SyncTest.obj.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }
}
