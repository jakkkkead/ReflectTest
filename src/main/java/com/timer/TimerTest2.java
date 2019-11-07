package com.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest2 {
    public static void main(String args[]){
        final int i = 5;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                System.out.println(i);
                timer.cancel();

            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
