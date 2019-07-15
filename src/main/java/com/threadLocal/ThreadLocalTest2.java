package com.threadLocal;

import java.util.Random;

/**
 * 使用ThreadLocal 来保存每个线程的变量.当线程死亡时，线程锁绑定的变量会自动被jvm垃圾回收
 */
public class ThreadLocalTest2 {
    public static void main(String[] args) {
        for (int i = 0;i<2 ; i++){
            final int data = new Random().nextInt();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("data :"+data+"--ThreadName:"+Thread.currentThread().getName());
                        System.out.println("ThreadName:"+Thread.currentThread().getName()+",运行状态："+Thread.currentThread().getState());
                        //不需要用 threadLocal set，因为MyThreadData的实例本来就是从theadLocal中取出，对实例设置值会自动生效
                        MyThreadData.getThreadInstance().setAge(data);
                        MyThreadData.getThreadInstance().setName(data+"name");
                        //每个线程 不同模块间共享同一份变量
                        new A().get();
                        new B().get();
                    }finally {
                        System.out.println("ThreadName:"+Thread.currentThread().getName()+",运行状态："+Thread.currentThread().getState());
                    }

                }
            }).start();
        }
    }

    static class A{
        public void get(){
            MyThreadData myThreadData = MyThreadData.getThreadInstance();
            System.out.println("A中的data："+"当前thread"+Thread.currentThread().getName()+",name"+myThreadData.getName()+",age:"+myThreadData.getAge());
        }
    }
    static class B{
        public void get(){
            MyThreadData myThreadData = MyThreadData.getThreadInstance();
            System.out.println("B中的data："+"当前thread"+Thread.currentThread().getName()+",name"+myThreadData.getName()+",age:"+myThreadData.getAge());
        }

    }
}

/**
 * 用于统一封装 threadLocal,以及变量。
 * 每个对象的实例和线程相关
 *
 */
class MyThreadData{
    private int age;
    private String name;
    private MyThreadData(){}
    /**
     * 与单例模式相似，不过返回的变量是与每个线程相关的，所以不需要用锁
     * @return
     */
    public static /*synchronized*/ MyThreadData getThreadInstance(){
        //获取线程相关的 对象，本来就不相关，所以无需加锁
        MyThreadData myThreadData = threadDataThreadLocal.get();
        if(myThreadData == null){
            myThreadData = new MyThreadData();
            //一定要set进去
            threadDataThreadLocal.set(myThreadData);
        }
        return myThreadData;
    }
    //ThreadLocal 只能保存一个变量，如果有100 个变量，可以将100个变量整合成一个对象，一起存储
    private static ThreadLocal<MyThreadData> threadDataThreadLocal = new ThreadLocal<MyThreadData>();

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
