package com.stephen.learning.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: jack
 * @Date: 2018/10/30 22:14
 * @Description:
 */
public class FutureTaskService {
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

        List<FutureTask<String>> taskList = new ArrayList<>();
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < list.size(); i++) {
            // 传入Callable对象创建FutureTask对象
            FutureTask<String> futureTask = new FutureTask<>(new Task(list.get(i)));
            taskList.add(futureTask);
            // 提交给线程池执行任务，executorService.invokeAll(taskList)一次性提交所有任务;
            executorService.submit(futureTask);
        }
        try {
            for (FutureTask<String> futureTask : taskList) {
                //FutureTask的get方法会自动阻塞,直到获取计算结果为止
                String result = futureTask.get();
                System.out.println("处理的返回值：" + result);
            }
        } catch (InterruptedException e) {
                e.printStackTrace();
        } catch (ExecutionException e) {
                e.printStackTrace();
        }finally {
            System.out.println("操作完毕");
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        new FutureTaskService().sellTicket();
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
