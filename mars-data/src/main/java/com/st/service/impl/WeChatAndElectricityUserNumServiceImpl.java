package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsOrderDsMapper;
import com.st.model.OpResult;
import com.st.service.WeChatAndElectricityUserNumService;
import com.st.utils.HttpClientFactory;
import com.st.utils.MapUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by admin on 2017/2/10.
 */
@Service
public class WeChatAndElectricityUserNumServiceImpl implements WeChatAndElectricityUserNumService {

    @Value("${user.draw.url}")
    private String urlValue;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;

    private Map<String,Integer> getCount(){
        Map<String, Integer> map = null;
        try {
            //取当前日期，也就是结束时间
            String endTime = String.valueOf(new Date().getTime() / 1000);
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime = String.valueOf(jsonObject.get("datas"));
            //获取生命下的粉丝分布
            String jsonProperty = HttpClientFactory.requestGet(urlValue + "getFansNumDistOfProperty&propertyName=life_cycle&startTime=" + startTime + "&endTime=" + endTime);
            JSONObject jsonObjectProperty = JSONObject.fromObject(jsonProperty);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            JSONObject jsonObjectClass = JSONObject.fromObject(tagStr);
            for (Iterator iter = jsonObjectClass.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                JSONObject jsonObjectTag = JSONObject.fromObject(jsonObjectClass.get(key));
                Gson gson = new Gson();
                map = gson.fromJson(String.valueOf(jsonObjectTag), new TypeToken<Map<String, Integer>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取微信用户数量
     * @return
     */
    @Override
    public OpResult getWeChatUserNum() {
        try {
            int wxCount = getCount().entrySet().stream().filter(ele -> !"电商购买用户".equals(ele.getKey())).map(values -> values.getValue()).mapToInt(value -> (Integer) value).sum();
            return OpResult.createSucResult(wxCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WX_USER_NUMBER_EXCEPTION);
    }

    /**
     * 获取电商用户数量
     * @return
     */
    @Override
    public OpResult getElectricityUserNum() {
        try {
            int eleCount = getCount().entrySet().stream().filter(ele -> "电商购买用户".equals(ele.getKey())).map(values -> values.getValue()).mapToInt(value -> (Integer) value).sum();
            return OpResult.createSucResult(eleCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.ELE_USER_NUMBER_EXCEPTION);
    }

    /**
     * 获取用户总数
     * @return
     */
    @Override
    public OpResult getUserCount() {
        try {
            int wxCount = getCount().entrySet().stream().filter(ele -> !"电商购买用户".equals(ele.getKey())).map(values -> values.getValue()).mapToInt(value -> (Integer) value).sum();
            int eleCount = getCount().entrySet().stream().filter(ele -> "电商购买用户".equals(ele.getKey())).map(values -> values.getValue()).mapToInt(value -> (Integer) value).sum();
            int disCount = marsOrderDsMapper.selectDistinctData();
            int userTotalCount = wxCount+eleCount-disCount;
            return OpResult.createSucResult(userTotalCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }
}
