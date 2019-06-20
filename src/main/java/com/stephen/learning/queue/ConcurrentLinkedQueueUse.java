package com.stephen.learning.queue;

import java.util.concurrent.*;

/**
 * @author: jack
 * @Date: 2019/6/19 17:00
 * @Description: 模拟停车场停车
 */
public class ConcurrentLinkedQueueUse {
    private int space = 10;
    private int cars = 1000;
    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
    private CountDownLatch countDownLatch = new CountDownLatch(space);

    public void park() throws InterruptedException {
        for (int i = 0; i < cars; i++) {
            queue.offer("the number of car:" + i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(space);
        for (int i = 0; i < space; i++) {
            executorService.submit(new ParkingLot(String.valueOf(i), queue, countDownLatch));
        }
        countDownLatch.await();
        System.out.println("------------所有的车都离开了停车场------------");
        executorService.shutdown();
    }

    public static class ParkingLot implements Callable {
        private String code;
        private ConcurrentLinkedQueue<String> queue;
        private CountDownLatch count;

        public ParkingLot(String code, ConcurrentLinkedQueue<String> queue, CountDownLatch count) {
            this.code = code;
            this.queue = queue;
            this.count = count;
        }

        @Override
        public Object call() throws Exception {
            while (!queue.isEmpty()) {
                String car = queue.poll();
                System.out.println(car + " 离开了车位:" + code);

            }
            count.countDown();
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ConcurrentLinkedQueueUse().park();
    }
}
