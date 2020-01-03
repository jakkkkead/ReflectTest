package com.web.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class LockWatch implements Watcher {
    private CountDownLatch countDownLatch;
    public LockWatch(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        //如果监听到节点被删除，则可以释放锁
        if (watchedEvent.getType()==Event.EventType.NodeDeleted){
            countDownLatch.countDown();
        }
    }
}
