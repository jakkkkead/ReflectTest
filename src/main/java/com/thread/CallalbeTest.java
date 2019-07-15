package com.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 使用Callable ，Future获取线程的返回值
 */
public class CallalbeTest {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<String> future = threadPool.submit(new Callable<String >() {
            public String call() throws Exception {
                Thread.sleep(2000);
                return "hello";
            }
        });
        System.out.println("等待结果");
        try {
            //get()方法会一直等待直到线程执行完毕
            System.out.println("结果为："+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("===========测试task=====");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        //哪个线程先执行完，就先获取返回值.适用于 线程返回结果互不相关的情况。
        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(fixedThreadPool);
        for (int i =0; i<10; i++){
            final int task = i;
            executorCompletionService.submit(new Callable() {
                public Object call() throws Exception {
                    Thread.sleep(new Random().nextInt(2000));
                    return task;
                }
            });
        }
        for (int j =0 ;j<10;j++){
            try {
                //哪个线程先执行完，就先获取返回值
                System.out.println(executorCompletionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
