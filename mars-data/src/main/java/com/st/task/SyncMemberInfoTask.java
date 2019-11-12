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
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 2016/11/21.
 */
public class SyncMemberInfoTask extends RecursiveTask<List<Map<String,Object>>> {

    private static Logger logger = LoggerFactory.getLogger(SyncMemberInfoTask.class);

    private static final int threshold = 50;//阈值
    private int start;
    private int end;

    //天猫数据
    private List<Map<String,Object>> memberList;

    public SyncMemberInfoTask(List<Map<String,Object>> memberList,int start, int end){
        super();
        this.memberList = memberList;
        this.start = start;
        this.end = end;
    }


    @Override
    protected List<Map<String,Object>> compute() {
        //接收推送成功后的list
        List<Map<String,Object>> successPushList = new ArrayList<>();
        if((end - start) <= threshold){
            //调用同步会员数据
           List successList =  syncMemberData(memberList.subList(start,end));
            //推送成功后的list
            successPushList.addAll(successList);

        }else {
            int middle = (start + end) / 2;
            SyncMemberInfoTask leftTask = new SyncMemberInfoTask(memberList,start,middle);
            SyncMemberInfoTask rightTask = new SyncMemberInfoTask(memberList,middle,end);
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

    //同步会员数据
    public List<Map<String,Object>> syncMemberData(List<Map<String,Object>> memberDataList){
        int pushSuCount = 0;
        List<Map<String,Object>> successPushList = new ArrayList<>();
        //如果列表为空直接返回
        if(StringUtils.isNull(memberDataList)){
            return successPushList;
        }
        for (Map<String,Object> memberMap:memberDataList){
            //检查参数，如果open_id为空则跳出不推这条数据
            if(StringUtils.isNull(memberMap.get("open_id"))){
                continue;
            }
            String sexStr = memberMap.get("sex")==null?"0":(String) memberMap.get("sex");

            memberMap.put("sex",sexStr);
            long actionTime = 0;
            //处理actiontime
            Date recordCreateTime = (Date) memberMap.get("record_create_time");
           actionTime = recordCreateTime.getTime()/1000;

            Map<String,Object> memberLogMap = new HashMap<>();
            memberLogMap.put("appkey",SendDataUtil.LOG_APPKEY);
            memberLogMap.put("itemid","all");
            //itemtype页面
            memberLogMap.put("itemtype",3);
            //actionid为5 用户注册
            memberLogMap.put("actionid",5);
            memberLogMap.put("uid", memberMap.get("open_id"));
            //utype 微信用户
            memberLogMap.put("utype",1);
            memberLogMap.put("clientip","127.0.0.1");
            //TODO 修改actiontime
            memberLogMap.put("actiontime",actionTime);
            memberLogMap.put("expand_info",getExpandInfoList(memberMap));
            String memJsonStr =    StringUtils.mapToJsonStr(memberLogMap);
            logger.info("推送会员数据json========>>>>>>"+memJsonStr);
            boolean sendFlag  =  SendDataUtil.sendData(memJsonStr);
            if(sendFlag){
                pushSuCount++;//计数加1
                successPushList.add(memberMap);//添加到成功列表
            }
        }
        logger.info("当前线程："+Thread.currentThread().getName()+"====>>>推送会员数据："+memberDataList.size()+"条，成功推送："+pushSuCount+"条");
        return successPushList;
    }
    /**
     * 会员拓展信息列表
     * @param memberMap
     * @return
     */
    public List<Map<String,Object>> getExpandInfoList(Map<String,Object> memberMap){


        List<Map<String,Object>> expandInfoList = new ArrayList<>();
        //会员省份拓展信息
        Map<String,Object> expandProvinceMap = new HashMap<>();
        expandProvinceMap.put("expkey",0);
        expandProvinceMap.put("expvalue",memberMap.get("province"));
        expandProvinceMap.put("exptype",0);

        //会员性别拓展信息
        Map<String,Object> expandSexMap = new HashMap<>();
        expandSexMap.put("expkey",2);
        expandSexMap.put("expvalue",memberMap.get("sex"));
        expandSexMap.put("exptype",0);

        //会员性别拓展信息
        Map<String,Object> expandMobileMap = new HashMap<>();
        expandMobileMap.put("expkey",6);
        expandMobileMap.put("expvalue",memberMap.get("member_mobile"));
        expandMobileMap.put("exptype",0);

        expandInfoList.add(expandProvinceMap);
        expandInfoList.add(expandSexMap);
        expandInfoList.add(expandMobileMap);
        return expandInfoList;
    }

}
