package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsUserGroupMapper;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.LifeCycleService;
import com.st.utils.CombineSendJson;
import com.st.utils.HttpClientFactory;
import com.st.utils.MapUtils;
import com.st.utils.OkHttpUtils;
import com.st.vo.UserDatas;
import com.st.vo.UserDetail;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 生命周期
 * Created by zhaoyx on 2016/9/12.
 */
@Service
public class LifeCycleServiceImpl implements LifeCycleService {

    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${user.draw.short_url}")
    private String shortUrl;
    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;

    /**
     * 获取生命周期标签和人数
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getLifeCycleAttribute() {
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
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
                map = MapUtils.toMap(String.valueOf(jsonObjectTag));
            }
            //将数据加入list中
            MapUtils.getaVoid(map, list);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.LIFECYCLE_EXCEPTION);
    }

    /**
     * 获取用户列表
     *
     * @param page
     * @param pageSize
     * @param propertyValue
     * @return
     */
    @Override
    public OpResult<Map<String, Object>> getUserList(int page, int pageSize, String propertyValue, String groupId) {
        Map<String, Class> map = new HashMap<>();
        Map<String, Object> maptemp = new HashMap<>();
        UserDatas userDatas;
        try {
            if (null == groupId) {
                if ("购买用户".equals(propertyValue)){
                    propertyValue = "电商购买用户";
                }
                //取出用户列表
                String jsonProperty = HttpClientFactory.requestGet(urlValue + "getFansListByFanPropertyAndTagCondi&page=" + page + "&pageSize=" + pageSize + "&sortFlag=true&sortType=desc&sortPropertyName=loyalty_val&propertyName=life_cycle&propertyValue=" + propertyValue);
                JSONObject jsonObjectProperty = JSONObject.fromObject(jsonProperty);
                map.put("detail", UserDetail.class);
                //将用户列表转换为对象
                userDatas = (UserDatas) JSONObject.toBean(JSONObject.fromObject(jsonObjectProperty.get("datas")), UserDatas.class, map);
                System.out.println("==============================================================");
                //取总人数
                maptemp.put("total", userDatas.getTotalRecordSize());
                //取用户数据集合
                maptemp.put("userlist", userDatas.getDetail());
            } else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), null);
                Map<String, String> data = new HashMap<>();
                data.put("appid", "35");
                data.put("passwd", "8344202725");
                data.put("appkey", "172205760");
                data.put("method", "getListByCollectionQuery");
                data.put("page", String.valueOf(page));
                data.put("pageSize", String.valueOf(pageSize));
                data.put("sortFlag", "true");
                data.put("sortType", "desc");
                data.put("sortPropertyName", "loyalty_val");
                data.put("tagCondition", mapes.get("tagCondition"));
                if (!"".equals(mapes.get("propertyCondition"))) {
                    data.put("propertyCondition", mapes.get("propertyCondition"));
                }
                String returnJson = OkHttpUtils.post(shortUrl,data);
                //String returnJsons = OkHttpUtils.get(urlValue+"getListByCollectionQuery&page="+page+"&pageSize="+pageSize+"&sortFlag=true&sortType=desc&sortPropertyName=loyalty_val&tagCondition=" + json);
                JSONObject jsonObject = JSONObject.fromObject(returnJson);
               if (!"执行失败".equals(String.valueOf(jsonObject.get("datas")))){
                   map.put("detail", UserDetail.class);
                   userDatas = (UserDatas) JSONObject.toBean(JSONObject.fromObject(jsonObject.get("datas")), UserDatas.class, map);
                   //JSONObject datas = JSONObject.fromObject(jsonObject);
                   //String detail = String.valueOf(datas.get("detail"));
                   //Map<String, String> tagMap1 = new Gson().fromJson(detail, new TypeToken<Map<String, String>>() {}.getType());
                   //Map<String, String> tagMap2 = new Gson().fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
                   //String count = tagMap1.get("totalRecordSize");
                   maptemp.put("total",userDatas.getTotalRecordSize());
                   maptemp.put("userlist",userDatas.getDetail());
               }
            }

            return OpResult.createSucResult(maptemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_LIST_EXCEPTION);
    }

    /**
     * 获取指定生命周期下的人数统计
     *
     * @param propertyValue 属性名(生命周期下的)
     * @return
     */
    @Override
    public OpResult<Integer> getListCycleCountByProperty(String propertyValue) {
        Map<String, Object> map = null;
        try {
            String endTime = String.valueOf(new Date().getTime() / 1000);
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime = String.valueOf(jsonObject.get("datas"));
            String jsonProperty = HttpClientFactory.requestGet(urlValue + "getFansNumDistOfProperty&propertyName=life_cycle&propertyValue=" + propertyValue + "&startTime=" + startTime + "&endTime=" + endTime);
            JSONObject jsonObjectProperty = JSONObject.fromObject(jsonProperty);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            JSONObject jsonObjectClass = JSONObject.fromObject(tagStr);
            for (Iterator iter = jsonObjectClass.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                JSONObject jsonObjectTag = JSONObject.fromObject(jsonObjectClass.get(key));
                map = MapUtils.toMap(String.valueOf(jsonObjectTag));
            }
            Integer val = Integer.parseInt(String.valueOf(map.get(propertyValue)));
            return OpResult.createSucResult(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.LIFECYCLE_EXCEPTION);
    }


}
