package com.stephen.learning.lock;

import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * @Auther: jack
 * @Date: 2018/10/21 21:21
 * @Description: 实现两个线程之间交换数据
 */
public class ExchangerUse {
    private static Exchanger<String> exchanger = new Exchanger<>();
    private static Random random = new Random();
    public final static String[] food = {
            "打边炉", "奶味芦笋汤", "糟片鸭", "烤花揽桂鱼", "苦中作乐", "七星丸", "鸭黄豆腐", "贝丝扒菜胆", "脆炒南瓜丝", "龙凤双腿",
    };

    static class Producer implements Runnable {

        @Override
        public void run() {
            int count = 10;
            try {
                while (count > 0) {
                    count--;
                    String foodName = food[random.nextInt(food.length)];
                    String result = exchanger.exchange(foodName);
                    System.out.println("Producer has created food:" + foodName);
                    System.out.println("Consumer says:" + result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            int count = 10;
            try {
                String words="i am going to eat!";
                while (count > 0) {
                    count--;
                    String name = exchanger.exchange(words);
                    System.out.println("Consumer has receieved the food:" + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ExchangerUse.Producer()).start();
        new Thread(new ExchangerUse.Consumer()).start();
    }
}
