package com.st.task;

import com.st.utils.HttpClientUtils;
import com.st.vo.ReturnData;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */

public class SendDataUtil {
    public static Logger logger = LoggerFactory.getLogger(SendDataUtil.class);
    //请求url
    public static final String  LOG_URL = "http://api.data.social-touch.com:8091/cdapi/req";

   // public static final String  LOG_URL = "http://211.151.58.219:10091/cdapi/req";
    // 行为接口的固定配置参数appid,passwd,method以及appey
    public static final  String LOG_APPID = "35";
    public static final  String LOG_PASSWD = "8344202725";
    public static final  String LOG_METHOD = "pushLogToServer";
    public static final  Long LOG_APPKEY = 172205760L;

    /**
     * httpclient发送数据
     *
     * @param menuJsonStr
     * @throws IOException
     */
    public static boolean sendData(String menuJsonStr) {
        HttpPost httppost = new HttpPost(LOG_URL);
        try {
            //设置请求参数
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appid", LOG_APPID));
            params.add(new BasicNameValuePair("passwd", LOG_PASSWD));
            params.add(new BasicNameValuePair("method", LOG_METHOD));
            params.add(new BasicNameValuePair("log", menuJsonStr));

            CloseableHttpClient httpclient = HttpClientUtils.getHttpclient();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();
            //设置请求和传输超时时间
            httppost.setConfig(requestConfig);
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity, "UTF-8");
    //        System.out.println(jsonStr);
            logger.info("发送数据========>>"+jsonStr);
            ReturnData returnData = (ReturnData) JSONObject.toBean(JSONObject.fromObject(jsonStr), ReturnData.class);
            if(!returnData.getDatas().isResult()){
                logger.error("推送失败，失败信息========>>"+jsonStr);
            }
            return returnData.getDatas().isResult();
        } catch (Exception e) {
            logger.error("[sendData] error"+e);
            return false;
        } finally {
            httppost.releaseConnection();
        }

    }
}
