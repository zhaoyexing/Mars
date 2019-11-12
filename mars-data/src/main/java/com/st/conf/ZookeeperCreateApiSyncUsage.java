package com.st.conf;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/***
 * @ClassName ZookeeperCreateApiSyncUsage
 * @Decsription TODO
 * @Author zhaoyx
 * @Date 2019/11/12 0012 下午 2:45
 * @Version 1.0
 */
public class ZookeeperCreateApiSyncUsage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.3.115:2181", 5000, new ZookeeperCreateApiSyncUsage());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        System.out.println("Zookeeper session established");
        String path = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode :"+ path);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }
}
