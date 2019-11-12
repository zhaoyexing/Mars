package com.st.conf;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/***
 * @ClassName ZookeeperConstructorUsage
 * @Decsription TODO
 * @Author zhaoyx
 * @Date 2019/11/12 0012 上午 9:59
 * @Version 1.0
 */
public class ZookeeperConstructorUsage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.3.115:2181",5000,new ZookeeperConstructorUsage());
        System.out.println(zooKeeper.getState());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper session established");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:"+watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()){
            countDownLatch.countDown();
        }
    }
}
