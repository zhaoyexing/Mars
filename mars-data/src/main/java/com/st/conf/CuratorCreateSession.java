package com.st.conf;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/***
 * @ClassName CuratorCreateSession
 * @Decsription TODO
 * @Author zhaoyx
 * @Date 2019/11/12 0012 下午 4:58
 * @Version 1.0
 */
public class CuratorCreateSession {
    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.3.115:2181",5000,3000,retryPolicy);
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
