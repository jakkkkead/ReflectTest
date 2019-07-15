package com.threadLocal;

/**
 * 多个线程共享同一个变量
 * 面试题：2个线程对 j 加1，2个线程对j 减一。循环2次
 */
public class MultiThreadData {
    //共享变量，全局变量。也可以将共享变量封装成一个对象，然后在同一个对象内操作（加锁）该变量
    private int j;
    public static void main(String[] args) {
        MultiThreadData multiThreadData = new MultiThreadData();
        //内部类依赖外部类的实例
        Inc inc = multiThreadData.new Inc();
        Dec dec = multiThreadData.new Dec();
        for (int i = 0 ; i<2;i++){
            new Thread(inc).start();
            new Thread(dec).start();
        }
    }

    /**
     * 直接在方法上加锁
     */
    public synchronized void increatMent(){
        j++;
        System.out.println("j加一后:"+j);
    }
    public synchronized void decMent(){
        j--;
        System.out.println("j减一后:"+j);
    }
    class Inc implements Runnable{
        public Inc(){}
        public void increat(){
            increatMent();
        }
        public void run() {
            increat();
        }
    }
    class Dec implements Runnable{
        public Dec(){}
        public void decreat(){
            decMent();
        }
        public void run() {
            decreat();
        }
    }
}
