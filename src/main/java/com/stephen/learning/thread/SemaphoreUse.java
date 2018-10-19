package com.stephen.learning.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: jack
 * @Date: 2018/10/19 10:18
 * @Description: 信号量的使用
 * semaphore.release()增加许可，可以超过开始的初始值
 */
public class SemaphoreUse {

    public void operate(Semaphore semaphore) {
        try {
            //获取许可
            semaphore.acquire();
            System.out.println("当前线程：" + Thread.currentThread().getName());
            System.out.println("start at " + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
            /**do something*/
            System.out.println("end at " + System.currentTimeMillis());
            //释放许可（semaphore.release(num)增加许可，可以超过开始的初始值）
            semaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class SemaphoreThread implements Runnable {
        private Semaphore semaphore;

        SemaphoreThread(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            operate(semaphore);
        }
    }

    /**
     * 同时只有一个线程在运行作用等同于synchronized
     */
    public void startOperate() {
        long waitTime=8*1000;
        Semaphore semaphore = new Semaphore(1);
        ExecutorService executorService=Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(new SemaphoreThread(semaphore));
        }
        try{
            //发出关闭线程池的通知
            executorService.shutdown();
            if(!executorService.awaitTermination(waitTime,TimeUnit.MILLISECONDS)){
                //如果超时，线程强制中断
                executorService.shutdownNow();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 同时有两个线程运行
     */
    public void startOperate2() {
        Semaphore semaphore = new Semaphore(2);
        ExecutorService executorService=Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(new SemaphoreThread(semaphore));
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        new SemaphoreUse().startOperate();
        new SemaphoreUse().startOperate2();

    }
}
