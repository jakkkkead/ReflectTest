package com.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器测试， 工具 ：quartz
 */
public class TimerTest {
    public static   int count = 0;
    public static void main(String[] args) {
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("bingo!");
//            }
//        },2000);

        class MyTimer extends TimerTask{
            @Override
            public void run() {
                count = (count+1)%2;
                System.out.println("bingo!");
                //在 定时器内又设置一个启动一个定时器
                // 2 秒，4秒执行一次
                new Timer().schedule(new MyTimer(),2000+2000*count);
            }
        }
        new Timer().schedule(new MyTimer(),2000);

        while (true){
            System.out.println(new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
