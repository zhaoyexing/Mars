package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.model.OpResult;
import com.st.service.UserCountService;
import com.st.utils.HttpClientFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


/**
 * 用户接口
 * Created by zhaoyx on 2016/9/12.
 */
@Service
public class UserCountServiceImpl implements UserCountService {

    @Value("${user.draw.url}")
    private String urlValue;
    /**
     * 获取用户总数
     * 取最新离线时间和当前时间的时间间隔，再进行查询。
     *
     * @return
     */
    @Override
    public OpResult<String> getUserCount() {
        String endTime = String.valueOf(new Date().getTime()/1000);
        String count = "0";
        try {
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue+"getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject= JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime =  String.valueOf(jsonObject.get("datas"));
            //统计粉丝数量
            String statisJson = HttpClientFactory.requestGet(urlValue+"searchAppkeyStatis&startTime="+startTime+"&endTime="+endTime);
            JSONObject jsonObject1= JSONObject.fromObject(statisJson);
            if(!Objects.equals(jsonObject1.get("datas").toString(),"{}")){
                //取得当前粉丝人数
                count = String.valueOf(JSONObject.fromObject(JSONObject.fromObject(jsonObject1.get("datas")).get(startTime)).get("dmp_total_fans"));
            }
            return OpResult.createSucResult(count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

}
