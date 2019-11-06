package com.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁测试
 * 以扣减库存量为例子
 */
@RestController
@RequestMapping("redis")
public class RedisController {
    private final static String lockKey = "lockKey";
    private final static String goodsKey = "goodsKey";

    /**
     * 初始化库存量
     */
    @PostConstruct
    public void init(){
        redisTemplate.opsForValue().set(goodsKey,50);
    }
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 为什么需要分布式锁，如果应用部署在多台服务器上，那么即使在程序内使用普通锁如：synchronized,只能控制
     * 本台服务器上的线程，而无法影响到其他服务器的线程执行顺序。
     * 使用redis做分布式锁：redis是单线程的
     *用redis做分布式锁存在的问题：
     *  如果redis是集群式的主从架构，可能存在一种状况，在一个线程获取到锁时（在Master上），master还未来的及复制
     *  到从服务器，此时master挂了，从服务其自动切换成主服务器。其他线程获取同一把锁成功，出现问题
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/buy")
    public String testBuyKey() throws InterruptedException {
        //使用uuid做锁的值，是为关闭锁时，避免因主代码执行时间太长，导致锁失效，此时其他线程占有锁。而当前线程最终释放的锁是其他线程持有的锁
        //用uuid判断是否是当前线程持有的锁
        String lockValue = UUID.randomUUID().toString();
        try {
            //该语句是原子操作，如果redis中不存在lockKey，则设置，否则不做任何操作。为key添加存活时间，防止程序死亡，导致死锁
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(lockKey,lockValue,3, TimeUnit.SECONDS);
            if (!flag){
                return "系统繁忙，请稍后再试！";
            }else {
                //开启一个后台线程，主要是为了保证，即使之后的主代码逻辑执行时间超过锁的存活时间，该线程可以延长锁的时间
                // ，保证主代码可以在锁存活时间范围内执行完毕
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                //判断锁释是否还存在，如果存在则延长锁的时间
                                if (redisTemplate.opsForValue().get(lockKey)!=null){
                                    long expireTime = redisTemplate.getExpire(lockKey,TimeUnit.SECONDS);
                                    redisTemplate.expire(lockKey,expireTime+1,TimeUnit.SECONDS);
                                }else {
                                    System.out.println("取消定时器");
                                    timer.cancel();
                                }

                            }
                        };
                        //延迟1/3 的锁时间开始执行
                        timer.schedule(timerTask,1000,1000);
                    }
                }).start();
            }
            int i = (int)redisTemplate.opsForValue().get(goodsKey);
            if (i<=0){
                System.out.println("库存不足！");
                return "库存不足";
            }
            Thread.sleep(3000);
            i = i - 1;
            redisTemplate.opsForValue().set(goodsKey,i);
            System.out.println("扣减成功，i = "+i);
            return "操作成功，i="+i;
        }finally {
            if (lockValue.equals(redisTemplate.opsForValue().get(lockKey))){
                System.out.println("删除锁");
                redisTemplate.delete(lockKey);
            }
        }
    }
}
