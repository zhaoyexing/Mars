package com.st.conf;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/***
 * @ClassName CuratorFluentCreateSession
 * @Decsription TODO
 * @Author zhaoyx
 * @Date 2019/11/12 0012 下午 5:06
 * @Version 1.0
 */
public class CuratorFluentCreateSession {
    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.3.115:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
//
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
//        CuratorFramework client = CuratorFrameworkFactory.builder()
//                .connectString("192.168.3.115:2181")
//                .sessionTimeoutMs(5000)
//                .retryPolicy(retryPolicy)
//                .namespace("/base")
//                .build();
//        client.start();
//        Thread.sleep(Integer.MAX_VALUE);
    }
}

