package com.st.task;

import com.st.mapper.MarsMemberInfoMapper;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientUtils;
import com.st.utils.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 2016/11/21.
 */
public class SyncMatchOrderDataTask extends RecursiveTask<List<Map<String,Object>>> {

    private static Logger logger = LoggerFactory.getLogger(SyncMatchOrderDataTask.class);

    private static final int threshold = 50;//阈值
    private int start;
    private int end;

    //订单数据
    private List<Map<String,Object>> orderList;

    public SyncMatchOrderDataTask(List<Map<String,Object>> orderList, int start, int end){
        super();
        this.orderList = orderList;
        this.start = start;
        this.end = end;
    }



    @Override
    protected List<Map<String,Object>> compute() {
        //接收推送成功后的list
        List<Map<String,Object>> successPushList = new ArrayList<>();
        if((end - start) <= threshold){
            //调用同步会员数据
           List successList =  syncOrderData(orderList.subList(start,end));
            //推送成功后的list
            successPushList.addAll(successList);

        }else {
            int middle = (start + end) / 2;
            SyncMatchOrderDataTask leftTask = new SyncMatchOrderDataTask(orderList,start,middle);
            SyncMatchOrderDataTask rightTask = new SyncMatchOrderDataTask(orderList,middle,end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();
            //等待子任务完成，并得到结果
            List leftResult = leftTask.join();
            List rightResult = rightTask.join();
            //合并子任务
            successPushList.addAll(leftResult);
            successPushList.addAll(rightResult);
        }
        return successPushList;
    }

    public  List<Map<String,Object>> syncOrderData(List<Map<String,Object>> matchOrderDataList){
        int pushSuCount = 0;
        List<Map<String,Object>> successPushList = new ArrayList<>();
        //如果列表为空直接返回
        if(StringUtils.isNull(matchOrderDataList)){
            return successPushList;
        }
        for (Map<String,Object> orderMap:matchOrderDataList){
            //检查参数，如果open_id为空则跳出不推这条数据
            if(StringUtils.isNull(orderMap.get("open_id"))||StringUtils.isNull(orderMap.get("bar_code"))){
                continue;
            }
            //转换actionTime
            //  String actionTimeStr = ;
            Date actionTimeDate = (Date) orderMap.get("bill_payment_time");
            Map<String,Object> orderLogMap = new HashMap<>();
            orderLogMap.put("appkey",SendDataUtil.LOG_APPKEY);
            //barcode
            orderLogMap.put("itemid",orderMap.get("bar_code"));
            //订单
            orderLogMap.put("itemtype",5);
            //购买
            orderLogMap.put("actionid",1);
            //会员openid
            orderLogMap.put("uid", orderMap.get("open_id"));
            //客户会员
            orderLogMap.put("utype",4);
            orderLogMap.put("clientip","127.0.0.1");
            //支付时间
            orderLogMap.put("actiontime",actionTimeDate.getTime()/1000);
            String orderJsonStr =  StringUtils.mapToJsonStr(orderLogMap);
            logger.info("推送订单数据json========>>>>>>"+orderJsonStr);
            boolean sendFlag  =  SendDataUtil.sendData(orderJsonStr);
            if(sendFlag){
                pushSuCount++;
                successPushList.add(orderMap);
            }

        }
        logger.info("当前线程："+Thread.currentThread().getName()+"====>>>推送订单数据："+matchOrderDataList.size()+"条，成功推送："+pushSuCount+"条");
        return  successPushList;
    };


}
