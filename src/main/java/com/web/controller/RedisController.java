package com.web.controller;

import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
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
    @Autowired
    private RedissonClient redissonClient;

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

    /**
     * redisson客户端已经对分布式锁进行了封装
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/redisson")
    public String testRedissonClient() throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        //为锁设置存活时间，超时则锁消失
        //lock.lock(5,TimeUnit.SECONDS);
        //尝试获取锁，等待5秒，可同时为锁加存活是假（leaseTime)
        //boolean flag = lock.tryLock(5,TimeUnit.SECONDS);
        //获取锁，没获取到则一直等待
        lock.lock();
        try{
            //处理逻辑
            int i = (int)redisTemplate.opsForValue().get(goodsKey);
            if (i<=0){
                System.out.println("库存不足！");
                return "库存不足";
            }
            i = i - 1;
            redisTemplate.opsForValue().set(goodsKey,i);
            System.out.println("扣减成功，i = "+i);
            return "操作成功，i="+i;
        }finally {
            lock.unlock();
        }
    }
    @GetMapping("/redis")
    public void testTemplate(){
        //redisTemplate.opsForHash()
        //添加zset集合，key，value，score
        redisTemplate.opsForZSet().add("article","001",1);
        redisTemplate.opsForZSet().add("article","002",2);
        redisTemplate.opsForZSet().add("article","003",4);
        RedisOperations operations = redisTemplate.opsForZSet().getOperations();
        System.out.println(operations);
    }
    @GetMapping("/addScore")
    public void addScore(){
        //增加分数
        redisTemplate.opsForZSet().incrementScore("article","002",8);

    }
    @GetMapping("/zrange")
    public void testGetZset(){
        //获取key的集合，索引从0---》1，并默认按分数从小到大排序
        Set article = redisTemplate.opsForZSet().range("article", 0, 1);
        Set re = redisTemplate.opsForZSet().reverseRange("article", 0, -1);
        System.out.println("从小到大"+article);
        ////获取key的全部集合，并默认按分数从小到大排序
        Set allSet = redisTemplate.opsForZSet().range("article", 0, -1);

        System.out.println("从小到大"+allSet);
        System.out.println("从大到小"+re);
        //返回002在article集合中的索引，从小到大排
        Long rank = redisTemplate.opsForZSet().rank("article", "002");
        System.out.println("002的排名(从小到大)："+rank);
        //获取所有带分数的article集合，从小到大排
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores = redisTemplate.opsForZSet().rangeWithScores("article", 0, -1);
        printSet(rangeWithScores);
        //返回指定范围的集合，从小到大
        Set rangeByScore = redisTemplate.opsForZSet().rangeByScore("article", 0, 3);
        System.out.println("指定0--3分数范围内的集合："+rangeByScore);
        //返回指定范围的集合,包括分数，从小到大
        printSet(redisTemplate.opsForZSet().rangeByScoreWithScores("article", 0, 3));
    }
    public void printSet(Set<ZSetOperations.TypedTuple<Object>> rangeWithScores){
        if (!CollectionUtils.isEmpty(rangeWithScores)){
            Iterator<ZSetOperations.TypedTuple<Object>> iterable = rangeWithScores.iterator();
            while (iterable.hasNext()){
                ZSetOperations.TypedTuple<Object> bean = iterable.next();
                System.out.println("value:"+bean.getValue()+",score:"+bean.getScore());
            }
        }
    }
    @GetMapping("/pipleSet")
    public void testPiple(){
        //管道操作，批量请求，等待所有命令执行完毕后，一次性返回结果
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i=0;i<10000;i++){

                    redisConnection.set(("piple"+i).getBytes(),
                            //这里一定要保证序列化方式要和redisTemplate的序列化方式一致
                            //否则获取值是会出现序列化不一致导致解析失败
                            redisTemplate.getValueSerializer().serialize(i+"chandao"));
                }
                return null;
            }
        });
    }
    @GetMapping("/getPiple")
    public void testGetPiple(){
        List<String> list = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    redisConnection.get(("piple"+i).getBytes());
                }
                return null;
            }
        });
        if (!CollectionUtils.isEmpty(list)) {
            System.out.println(list);
        }
    }
    @GetMapping("/delPiple")
    public void testdelPiple(){
       redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    redisConnection.del(("piple"+i).getBytes());
                }
                return null;
            }
        });
    }
    @GetMapping("/get/{key}")
    public String getSetValueByKey(@PathVariable(value = "key") String key){
        String value = (String)redisTemplate.opsForValue().get(key);
        return value;
    }



}
