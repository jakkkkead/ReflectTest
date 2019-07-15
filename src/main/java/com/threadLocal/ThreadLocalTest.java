package com.threadLocal;

import sun.java2d.loops.GraphicsPrimitive;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 测试线程范围内的共享变量：每个线程有独立的变量，但是在一个线程内的多个模块，
 * 共享同一个变量
 */
public class ThreadLocalTest {
    private static Map<Thread,Integer> threadMap = new HashMap<Thread, Integer>();
    public static void main(String[] args) {
        for (int i = 0;i<2 ; i++){
            final int data = new Random().nextInt();
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("data :"+data+"--ThreadName:"+Thread.currentThread().getName());
                    threadMap.put(Thread.currentThread(),data);
                    //每个线程 不同模块间共享同一份变量
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
   static class A{
        public void get(){
            System.out.println("A中的data："+"当前thread"+Thread.currentThread().getName()+threadMap.get(Thread.currentThread()));
        }
    }
    static class B{
        public void get(){
            System.out.println("B中的data："+"当前thread"+Thread.currentThread().getName()+threadMap.get(Thread.currentThread()));
        }

    }
}
