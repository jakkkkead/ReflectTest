package com.thread;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadTest {
    public static ExecutorService service = Executors.newFixedThreadPool(50);
    public static ExecutorService mainservice = Executors.newFixedThreadPool(40);
    public  static AtomicInteger count = new AtomicInteger(0);
    public static void main(String[] args) {
      for (int i =0 ; i< 50 ; i++){
          mainservice.execute(new Runnable() {
             @Override
             public void run() {
                 test();
             }
         });
      }
    }
    public static void test(){
        System.out.println("id:"+Thread.currentThread().getId());
        CountDownLatch num1 = new CountDownLatch(2);
        CountDownLatch cpNum = new CountDownLatch(2);
        Future<String> data = sendByFuture(service,num1);
        Future<Map<String,String>> cp = sendCpData(service,num1,cpNum);
        try {
//            cpNum.await();
//            System.out.println("同期锁在主线程解锁");
//            num1.await();
            String s = data.get();
            Map<String,String> map =cp.get();
            System.out.println("s:"+s+",map:"+map+",count:"+count.addAndGet(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static String getTotal (){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }
    public static Future sendByFuture(ExecutorService service,CountDownLatch num){
        try{
            Future future = service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return getTotal();
                }
            });
            return future;
        }finally {
            num.countDown();
            System.out.println("lantch:"+num.getCount());
        }
    }
    public static Future<Map<String,String>> sendCpData(ExecutorService service , CountDownLatch num,CountDownLatch cpNum){
         try{
             Future future = service.submit(new Callable<Map<String,String>>() {
                 @Override
                 public Map<String, String> call() throws Exception {
                     return getCpData(service,cpNum);
                 }
             });
             return future;
         }finally {
             num.countDown();
             System.out.println("lantch:"+num.getCount());
         }
    }
    public static Map<String,String> getCpData(ExecutorService service , CountDownLatch cpNum){
        Map<String,Future<String>> map = new HashMap<>();
        for (int i = 0; i<2;i++){
            Future future = sendByFuture(service,cpNum);
            map.put("i"+i , future);
        }
        try {
//            System.out.println("同期锁子线程开始等待");
//            cpNum.await();
//            System.out.println("同期锁子线程解锁完毕");
            return getCpFutureData(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Map<String,String> getCpFutureData(Map<String,Future<String>> map){
        Map<String,String> res = new HashMap<>();
        if (!CollectionUtils.isEmpty(map)){
            for (String key : map.keySet()){
                try {
                   String result = map.get(key).get();
                   res.put(key,result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
