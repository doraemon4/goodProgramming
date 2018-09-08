package com.stephen.learning.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @Auther: jack
 * @Date: 2018/7/23 19:50
 * @Description:
 *            Fork/Join框架是Java 7提供的一个用于并行执行任务的框架，
 *            是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架
 */
public class ForkJoinCalculator extends RecursiveTask<Long> {
    private long start;
    private long end;
    private static final long THRESHOLD=10000;

    public ForkJoinCalculator(long start,long end){
        this.start=start;
        this.end=end;
    }

    @Override
    protected Long compute() {
        if(end-start<THRESHOLD){
            long sum=0;
            for(long i=start;i<=end;i++){
                sum+=i;
            }
            return sum;
        }
        else{
            //大于临界值拆分任务
            long mid=(start+end)/2;

            ForkJoinCalculator forkJoinCalculator=new ForkJoinCalculator(start,mid);
            forkJoinCalculator.fork();
            ForkJoinCalculator forkJoinCalculator2=new ForkJoinCalculator(mid+1,end);
            forkJoinCalculator2.fork();

            return forkJoinCalculator.join()+forkJoinCalculator2.join();
        }

    }

    public static void main(String[] args) {
        //jdk7的fork,join使用
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> forkJoinTask = forkJoinPool.submit(new ForkJoinCalculator(1, 10000000L));
        try{
            System.out.println(forkJoinTask.get());
        }catch (Exception e){
            e.printStackTrace();
        }
        Long sum=forkJoinPool.invoke(new ForkJoinCalculator(1, 10000000L));
        System.out.println(sum);
        long end=System.currentTimeMillis();
        System.out.println("总共耗时为："+(end-start));

        //jdk8的并行流
        start = System.currentTimeMillis();
        sum=LongStream.rangeClosed(1,10000000L).parallel().reduce(0,Long::sum);
        System.out.println(sum);
        end=System.currentTimeMillis();
        System.out.println("总共耗时为："+(end-start));
    }
}