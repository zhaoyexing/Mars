package com.st.utils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by zhaoyx on 2016/8/1.
 */
public class HttpClientUtils  extends BaseAPI {
    private static HttpPost httppost = new HttpPost(BASE_URI_PRODUCT);
    //设置超时重试
    private static CloseableHttpClient httpclient = HttpClientBuilder.create().setRetryHandler(new DefaultHttpRequestRetryHandler()).build();

    public static HttpPost getHttpPost(){
        return httppost;
    }
    public static CloseableHttpClient getHttpclient(){
        return httpclient;
    }
}
