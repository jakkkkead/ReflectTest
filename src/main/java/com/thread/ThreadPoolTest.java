package com.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池测试
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        //创建固定个数的线程池
      //  ExecutorService fixPool = Executors.newFixedThreadPool(3);
        //如果线程不够，会自动创建线程；如果没有任务，会回收线程
        ExecutorService cachePool = Executors.newCachedThreadPool();
        //只创建一个线程，如果线程死亡，会创建一个线程代替它执行任务
        ExecutorService singlelPool = Executors.newSingleThreadExecutor();
        for (int i = 0; i< 10; i++){
            final int task = i;
            singlelPool.execute(new Runnable() {
                public void run() {
                    for (int j = 0; j<10;j++){
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("j:"+j+"loop of task:"+task);
                    }
                }
            });
        }
        System.out.println("10 个任务以提交");
        //当线程池中没有任务后，关闭线程池
        //fixPool.shutdown();
        //执行到这一步，立即关闭线程池
        //fixPool.shutdownNow();
    }
}
