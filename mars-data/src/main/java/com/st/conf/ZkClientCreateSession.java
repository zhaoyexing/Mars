package com.st.conf;

import org.I0Itec.zkclient.ZkClient;

/***
 * @ClassName ZkClientCreateSession
 * @Decsription TODO
 * @Author zhaoyx
 * @Date 2019/11/12 0012 下午 4:50
 * @Version 1.0
 */
public class ZkClientCreateSession {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.3.115:2181",5000);
        System.out.println("Zookeeper session established");
    }
}
