package com.st.service.impl;

import com.st.mapper.MarsMemberInfoMapper;
import com.st.service.PushMemberService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientUtils;
import com.st.utils.StringUtils;
import com.st.vo.ReturnData;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2016/11/17.
 */
@Service
public class PushMemberServiceImpl implements PushMemberService{

    //请求url
    private static String LOG_URL = "";
    // 行为接口的固定配置参数appid,passwd,method以及appey
    private static final  String LOG_APPID = "35";
    private static final  String LOG_PASSWD = "8344202725";
    private static final  String LOG_METHOD = "pushLogToServer";
    private static final  Long LOG_APPKEY = 172205760L;

    private static Logger logger = LoggerFactory.getLogger(PushMemberServiceImpl.class);
    @Autowired
    private MarsMemberInfoMapper marsMemberInfoMapper;

    @Override
    public void saveMemberInfo(Map<String, Object> paramMap) {

        try{

            marsMemberInfoMapper.saveMemberInfo(paramMap);
            //测试调用
        }catch (Exception e){
            e.printStackTrace();
            logger.error("会员插入失败，会员Id为"+paramMap.get("id"));
        }


    }


}
