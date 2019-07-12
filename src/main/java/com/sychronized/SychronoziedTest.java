package com.sychronized;

public class SychronoziedTest {
    public static void main(String[] args) {
        SychronoziedTest sychronoziedTest = new SychronoziedTest();
        // 静态方法 内不能直接 创建 内部类
//        OutPut outPut = new OutPut();
        sychronoziedTest.init();
    }
    public void  init(){
        final OutPut outPut = new OutPut();
        //两个线程访问同一方法
        while (true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                public void run() {
                    outPut.outPut1("xiaoming");
                }
            }).start();
            new Thread(new Runnable() {
                public void run() {
                    outPut.outPut1("tinaioojwe");
                }
            }).start();
        }
    }


   static class OutPut{

        /**
         * 循环输出name的每个字符
         * @param name
         */
        public void outPut(String name){
            for (int i= 0; i< name.length(); i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }
       // 锁对象是 OutPut 的实例对象 ，相当于this
        public synchronized void outPut1(String name){
            for (int i= 0; i< name.length(); i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }

        public  void outPut2(String name){
            // 对代码片段进行加锁， 锁对象可以是任意对象，只要保证多个线程使用同一个锁对象
            synchronized (this){
                for (int i= 0; i< name.length(); i++){
                    System.out.print(name.charAt(i));
                }
            }

            System.out.println();
        }

       /**
        * 内部类不可以有 静态 方法，所以把 类变成 静态内部类
        * @param name
        */
        //锁对象是 OutPut.class
        public static synchronized void outPut3(String name){
                for (int i= 0; i< name.length(); i++){
                    System.out.print(name.charAt(i));
                }
            System.out.println();
        }
    }
}
