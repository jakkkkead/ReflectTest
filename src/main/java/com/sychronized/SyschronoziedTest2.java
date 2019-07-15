package com.sychronized;

/**
 *  面试题：
 *    子线程循环10次，主线程 循环 100次；接着又回到子线程循环10，主线程100次。如此循环 50次
 */
public class SyschronoziedTest2 {
    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i<= 50; i++){
                    bussiness.stub(i);
                }

            }
        }).start();
        for (int i = 1;i <= 50; i++){
           bussiness.mainB(i);
        }
    }

}

/**
 * 将 子线程和主线程需要执行的业务代码 放在同一个类中（面向对象思想），方便控制同步
 */
class Bussiness{
    //第一次 需要子线程先执行，所以默认值为false
    private boolean flag = false;
    public synchronized void stub(int i){
        while (flag){
            try {
                //等待，直到 被唤醒和 flag 为false . wait 会释放锁。
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int k =1 ; k <= 10 ; k++){
            System.out.println("子线程："+k+",loop of total:"+i);
        }
        //修改标志位
        flag = true;
        //唤醒其他在等待该锁对象的线程
        this.notify();
    }
    public synchronized void mainB(int i){
        while (!flag){// 之所以不用 if，while 可以防止伪唤起。因为线程执行是随机的，可能此时就轮到这个线程了，但是 flag 标志位任然没有改变，任然需要等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int k =1 ; k <= 100 ; k++){
            System.out.println("main线程："+k+",loop of total:"+i);
        }
        //修改标志位
        flag = false;
        this.notify();
    }

}
