package com.web.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ZooDistributeLock {
    private final static String ROOT_LOCK = "/locks";
    private ZooKeeper zooKeeper;
    //当前节点
    private String currentNode;
    private final static byte[] data={1,2};
    private CountDownLatch countDownLatch;
    public ZooDistributeLock (){
        try {
            zooKeeper = ZookeeperUtil.connect();
            //为false就不去注册当前的事件
            Stat stat = zooKeeper.exists(ROOT_LOCK, false);
            //判断当前根节点是否存在
            if (stat == null) {
                //创建持久化节点
                zooKeeper.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试获取锁
     * 1：在同一个根节点下创建有序临时节点
     * 2：获取根节点下的所有子节点，并监听
     * 3：对子节点排序，从小到大，将集合内的最小节点和当前创建的节点对比，取最小节点为锁
     * 4:如果当前节点不是最小，则监听当前节点上一个节点。一旦上一个节点被删除，则可以获取锁
     * @return
     */
    public boolean tryLock() {
        String split = "lock_";
        try {
            currentNode = zooKeeper.create(ROOT_LOCK+"/"+split,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("创建节点成功，path:"+currentNode+",开始竞争锁");
            //获取根节点下的所有子节点，并监控
            List<String> children = zooKeeper.getChildren(ROOT_LOCK,false);
            //对子节点排序，从小到大
            SortedSet<String> sortedSet = new TreeSet<>();
            for(String node : children){
                sortedSet.add(node);
            }
            //拿到最小的节点，与当前节点比较。取最小节点为锁
            if(currentNode.equals(sortedSet.first())){
                System.out.println("当前节点为第一个节点，获取锁成功");
                return true;
            }
            //获取当前节点之前的节点（比当前节点小的节点，列表已经有序）
            SortedSet<String> headSet = sortedSet.headSet(currentNode);
            if (!CollectionUtils.isEmpty(headSet)){
                // 获取比自己小的节点(紧邻的上一个节点）
                String preNode = headSet.last();
                CountDownLatch latch = new CountDownLatch(1);
                //获取锁失败，开始等待，监控上一个节点
                zooKeeper.exists(preNode,new LockWatch(latch));
                latch.await();
            }
            System.out.println("当前节点等待，获取锁成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void unlock(){
        System.out.println("开始释放锁，path:"+currentNode);
        try {
            //删除节点，-1表示强制删除
            zooKeeper.delete(currentNode,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        final CountDownLatch latch=new CountDownLatch(50);
        AtomicInteger count = new AtomicInteger(0);
        for(int i=0;i<50;i++){
            new Thread(()->{
                ZooDistributeLock lock=null;
                try {
                    lock=new ZooDistributeLock();
                    latch.countDown();
                    //等10个线程都启动后，再开始争夺分布式锁
                    latch.await();
                    if (lock.tryLock()){
                        System.out.println("库存加1："+count.incrementAndGet()+",lock:"+lock.currentNode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(lock!=null){
                        lock.unlock();
                    }
                }
            }).start();
        }
    }
}
