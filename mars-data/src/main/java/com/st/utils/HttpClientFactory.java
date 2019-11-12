package com.st.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * httpclient连接池
 * Created by zhaoyx on 2016/9/13.
 */
public class HttpClientFactory {
    /**
     * 通过PoolingHttpClientConnectionManager管理连接，创建HttpClient对象
     *
     * @param maxTotal    最大连接数
     * @param maxPerRoute 每个路由基础的连接
     * @return HttpClient实例对象
     */
    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute) {
        // 创建PoolingHttpClientConnectionManager来管理连接
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 设定最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
        // 设定每个路由的基础连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
        // 创建并返回HttpClient对象
        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
    }

    /**
     *  http get方法
     * @param urlWithParams
     * @return String
     * @throws Exception
     */
    public static String requestGet(String urlWithParams) throws Exception {
        HttpGet httpget = null;
        CloseableHttpResponse response = null;
        String jsonStr = null;
        try {
            CloseableHttpClient httpclient = HttpClientFactory.createHttpClient(200,20);
            System.out.println("请求URL:"+urlWithParams);
            httpget = new HttpGet(urlWithParams);
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            jsonStr = EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            httpget.releaseConnection();
            response.close();
        }
       return jsonStr;
    }

    /**
     * post请求方法
     * @param url
     * @param params
     * @return
     */
    public static String requestPost (String url, Map<String,String> params){
        CloseableHttpClient httpclient = HttpClientFactory.createHttpClient(200,20);
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        String content = null;
        try {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //创建参数队列
            for(Map.Entry<String,String> entry : params.entrySet()){
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            post.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

//    public static void main(String[] args) {
//        HttpClientFactory http = new HttpClientFactory();
//        String url = "http://api.kmp.social-touch.com/project/header";
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("token","to_bee7454eb407b5ca3fbfbd26a5768b99");
//        map.put("third_uname","zhoujun1@social-touch.com");
//        map.put("pro_id","110");
//        http.requestPost(url,map);
//    }
}
