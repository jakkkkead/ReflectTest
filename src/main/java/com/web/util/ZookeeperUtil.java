package com.web.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperUtil {
    private final static String HOST = "192.168.88.128:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static ZooKeeper zooKeeper;
    private static CountDownLatch connectedSignal = new CountDownLatch(1);

    /**
     * 创建zookeeper连接
     * @return
     */
    public static ZooKeeper connect(){
        if (zooKeeper != null){
            return zooKeeper;
        }
        try{
            zooKeeper = new ZooKeeper(HOST, SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
            connectedSignal.await();
            return zooKeeper;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static void close(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建zookeeper持久化节点
     * @param path
     * @param data
     */
    public static void createNode(String path,byte[] data ){
        try {
            //创建zookeeper持久化节点，
            zooKeeper.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stat existNode(String path){
        try {
           return zooKeeper.exists(path,true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取子节点
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static List<String> getChildren(String path) throws KeeperException, InterruptedException {
        if (existNode(path) != null){
            List<String> children = zooKeeper.getChildren(path, null);
            return children;
        }else {
            return null;
        }
    }
    public static void update(String path,byte[] data) throws KeeperException, InterruptedException {
        zooKeeper.setData(path,data,existNode(path).getVersion());
    }
    public static void delete(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path,existNode(path).getVersion());
    }



    public static void main(String args[]) throws KeeperException, InterruptedException {
        ZooKeeper zooKeeper = ZookeeperUtil.connect();
//        byte[] data = "I am a people".getBytes();
        String path = "/people";
        //对于已经存在的节点，不能重复创建，否则报错
//        createNode(path,data);
//        Stat stat = existNode(path);
//        if (stat != null){
//            try {
//                byte[] datas = zooKeeper.getData(path, false, null);
//                String res = new String(datas,"UTF-8");
//                System.out.println(path+"的data:"+res);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
        delete("/locks");
        ZookeeperUtil.close();
    }
}
