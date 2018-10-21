package com.stephen.learning.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: jack
 * @Date: 2018/10/20 20:13
 * @Description: 显示锁提供的等待通知(替代了对象锁的wait和notify)
 * 1.一个lock对象可以通过多次调用 lock.newCondition() 获取多个Condition对象，也就是说，
 * 在一个lock对象上，可以有多个等待队列;而Object的等待通知在一个Object上，只能有一个等待队列;
 * 2.Condition的本质就是等待队列和同步队列的交互
 */
public class ConditionUse {
    private static volatile boolean flag = false;
    private static Lock lock = new ReentrantLock();
    private static Condition condition=lock.newCondition();

    static class Producer implements Runnable{

        @Override
        public void run() {
            int count = 10;
            while (count>0){
                try {
                    lock.lock();
                    count--;
                    System.out.println("Producer");
                    if (flag==false){
                        flag=true;
                        condition.signal();
                        condition.await();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class Consumer implements Runnable{

        @Override
        public void run() {
            int count = 10;
            while (count>0){
                try {
                    lock.lock();
                    count--;
                    System.out.println("Consumer");
                    if(flag==true){
                        flag=false;
                        condition.signal();
                        condition.await();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ConditionUse.Producer()).start();
        new Thread(new ConditionUse.Consumer()).start();
    }
}
