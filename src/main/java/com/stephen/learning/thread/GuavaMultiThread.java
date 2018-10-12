package com.stephen.learning.thread;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: jack
 * @Date: 2018/9/19 17:57
 * @Description:  模拟
 */
@Slf4j
public class GuavaMultiThread {

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

        List<ListenableFuture<String>> futures = Lists.newArrayList();
        //定义线程数
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);
        for (int i = 0; i < list.size(); i++) {
            futures.add(executorService.submit(new Task(list.get(i))));
        }

        final ListenableFuture<List<String>> resultsFuture = Futures.successfulAsList(futures);
        try {//所有都执行完毕
            List<String> result=resultsFuture.get();
            System.out.println("处理的返回值："+result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("操作完毕");
            pool.shutdown();
        }
    }

    public static void main(String[] args) {
        new GuavaMultiThread().sellTicket();
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
            System.out.println("窗口："+Thread.currentThread().getName()+",已卖" + ticket);
            return ticket;
        }
    }
}
