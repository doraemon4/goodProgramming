package com.stephen.learning.thread;

import java.util.concurrent.Phaser;

/**
 * @author: jack
 * @Date: 2019/6/20 16:21
 * @Description: 阶段器使用
 */
public class PhaserUse {
    static class MyPhaser extends Phaser {
        private int phaseToTerminate = 2;

        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            System.out.println("*第" + phase + "阶段完成*");

            //到达结束阶段,或者还没到结束阶段但是party为0,都返回true,结束phaser
            return phase == phaseToTerminate || registeredParties == 0;
        }
    }

    static class Swimmer implements Runnable{

        private Phaser phaser;

        public Swimmer(Phaser phaser) {

            this.phaser = phaser;

        }

        @Override

        public void run() {
            //从这里到第一个phaser.arriveAndAwaitAdvance()是第一阶段做的事
            System.out.println("游泳选手-"+Thread.currentThread().getName()+":已到达赛场");
            phaser.arriveAndAwaitAdvance();

            //从这里到第二个phaser.arriveAndAwaitAdvance()是第二阶段做的事
            System.out.println("游泳选手-"+Thread.currentThread().getName()+":已准备好");
            phaser.arriveAndAwaitAdvance();

            //从这里到第三个phaser.arriveAndAwaitAdvance()是第三阶段做的事
            System.out.println("游泳选手-"+Thread.currentThread().getName()+":完成比赛");
            phaser.arriveAndAwaitAdvance();

        }

    }

    public static void main(String[] args) {
        int swimmerNum = 6;
        MyPhaser phaser = new MyPhaser();

        //注册主线程,用于控制phaser何时开始第二阶段
        phaser.register();

        for(int i=0; i<swimmerNum; i++) {
            phaser.register();
            new Thread(new Swimmer(phaser),"swimmer"+i).start();
        }

        //主线程到达第一阶段并且不参与后续阶段.其它线程从此时可以进入后面的阶段.
        phaser.arriveAndDeregister();

        //加while是为了防止其它线程没结束就打印了"比赛结束”
        while (!phaser.isTerminated()) {

        }
        System.out.println("===== 比赛结束 =====");

    }
}
