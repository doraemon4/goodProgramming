package com.stephen.learning.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: jack
 * @Date: 2018/10/31 22:14
 * @Description: jdk实现的CompletionService(先执行完的先输出结果）
 */
public class CompleteService {
    /**
     * 生成车票
     *
     * @return
     */
    public List<String> createTickets() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("车票" + i);
        }
        return list;
    }

    public void sellTicket() {
        //获取车票
        List<String> list = createTickets();

        //定义线程数
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CompletionService<String> completionService= new ExecutorCompletionService<String>(pool);
        for (int i = 0; i < list.size(); i++) {
            completionService.submit(new CompleteService.Task(list.get(i)));
        }

        try {
            //所有都执行完毕
            for(int i=0;i<list.size();i++){
                String result=completionService.take().get();
                System.out.println("处理的返回值：" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("操作完毕");
            pool.shutdown();
        }
    }

    public static void main(String[] args) {
        new CompleteService().sellTicket();
    }

    class Task implements Callable<String> {
        private String ticket;

        /**
         * 构造方法，用于参数传递
         *
         * @param ticket
         */
        public Task(String ticket) {
            this.ticket = ticket;
        }

        @Override
        public String call() throws Exception {
            System.out.println("窗口：" + Thread.currentThread().getName() + ",已卖" + ticket);
            return ticket;
        }
    }
}
