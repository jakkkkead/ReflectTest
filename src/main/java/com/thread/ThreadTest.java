package com.thread;

/**
 * 多线程
 */
public class ThreadTest {
    public static void main(String[] args) {
        final Thread thread = new Thread(){
            //相当与创建了 Thread 的子类，重写父类的run方法
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
                        System.out.println("重写run方法,"+Thread.currentThread().getName());
                        System.out.println("重写run方法，通过this获取当前线程："+this.getName());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        System.out.println("============通过传入runable创建线程");
        //这种方式更体现 面向对象思想，Thread 对象， 线程中运行的代码放入一个 Runnable中
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
                        //如果使用this 获取该线程信息，实际 this的宿主是 Runnable
                        System.out.println("创建runnable对象,"+Thread.currentThread().getName());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        // new Thread (runnable ){ run } ,由于子类覆盖了父类的run方法，所以只回执行 子类的run方法
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
                        //如果使用this 获取该线程信息，实际 this的宿主是 Runnable
                        System.out.println("00001创建runnable对象,"+Thread.currentThread().getName());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(1000);
                        //如果使用this 获取该线程信息，实际 this的宿主是 Runnable
                        System.out.println("00002创建runnable对象,"+Thread.currentThread().getName());
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread2.start();

    }
}
