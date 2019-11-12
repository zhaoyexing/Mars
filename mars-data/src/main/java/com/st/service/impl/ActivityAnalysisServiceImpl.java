package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.fabric.xmlrpc.base.Data;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsActivityCampaginMapper;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.mapper.MarsUserGroupMapper;
import com.st.model.MarsTagsTmp;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.ActivityAnalysisService;
import com.st.utils.CombineSendJson;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.OkHttpUtils;
import com.st.vo.UtilNameVO;
import com.st.vo.UtilVo;
import net.sf.ehcache.search.aggregator.Count;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  活动分析
 * Created by zhaoyx on 2016/9/13.
 */
@Service
public class ActivityAnalysisServiceImpl implements ActivityAnalysisService {
    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${user.draw.short_url}")
    private String shortUrl;
    @Autowired
    private MarsActivityCampaginMapper marsActivityCampaginMapper;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;
    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;

    /**
     * 互动主题分布
     * 互动利益分布
     * 互动形式分布
     * @param propertyValue
     * @param pid
     * @return
     */
    @Override
    public OpResult<List<UtilVo>> getInteractiveThemeDistribution(String propertyValue, String pid, String groupId) {
        Map<String, Object> map = new HashMap<>();
        List<UtilVo> lists = new ArrayList<>();
        try {
            String endTime = String.valueOf(new Date().getTime()/1000);
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue+ "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime = String.valueOf(jsonObject.get("datas"));
            String jsonProperty;
            if(null == groupId){
                jsonProperty = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_type\": \"vertex\",\n" +
                        "            \"element_name\": \"user\",\n" +
                        "            \"propertyname\": \"life_cycle\",\n" +
                        "            \"up_value\": \""+propertyValue+"\",\n" +
                        "            \"down_value\": \""+propertyValue+"\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}&tagCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_name\": \"tag_class_relation_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \""+pid+"\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"log10_score\",\n" +
                        "                        \"up_value\": \"1\",\n" +
                        "                        \"down_value\": \"0\"\n" +
                        "                    },\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"appid\",\n" +
                        "                        \"up_value\": \"172205760\",\n" +
                        "                        \"down_value\": \"172205760\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
                getReturnResult(pid, lists, jsonProperty);
            }else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (marsUserGroup!=null){
                    Map<String,String> maps = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), pid);
                    Map<String, String> data = new HashMap<>();
                    data.put("appid", "35");
                    data.put("passwd", "8344202725");
                    data.put("appkey", "172205760");
                    data.put("method", "groupTagsByRecursiveFilter");
                    data.put("tagCondition", maps.get("tagCondition"));
                    if (!"".equals(maps.get("propertyCondition"))){
                        data.put("propertyCondition",maps.get("propertyCondition"));
                    }
                    jsonProperty = OkHttpUtils.post(shortUrl,data);
                    //jsonProperty = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&tagCondition=" + json);
                    getReturnResult(pid, lists, jsonProperty);
                }
            }

            return OpResult.createSucResult(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.INTERACTIVE_THEME_EXCEPTION);
    }


    /**
     * 互动意愿分布
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getInteractiveIntentionDistribution(String propertyValue, String pid,String groupId) {
        List<Map<String,Object>> lists = new ArrayList<>();

        try {
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue+"getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject= JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime =  String.valueOf(jsonObject.get("datas"));
            String endTime = String.valueOf(new Date().getTime()/1000);
//            String cityJson = HttpClientFactory.requestGet(urlValue
//                    + "getFansNumOfGroupFieldByPropertyCon" +
//                    "&propertyName=life_cycle" +
//                    "&startTime=" +startTime+
//                    "&endTime=" +endTime+
//                    "&propertyValue=" +propertyValue+
//                    "&statisPropertyName=interact");
            String json1 ;
            String json2;
            String json3;
            Map<String,Object> map1 = new HashMap<>();
            Map<String,Object> map2 = new HashMap<>();
            Map<String,Object> map3 = new HashMap<>();
            if(groupId==null){
                json1 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"query_type\": \"and\",\n" +
                        "            \"queryconditionArr\": [\n" +
                        "                {\n" +
                        "                    \"element_type\": \"vertex\",\n" +
                        "                    \"element_name\": \"user\",\n" +
                        "                    \"propertyname\": \"life_cycle\",\n" +
                        "                    \"up_value\": \""+propertyValue+"\",\n" +
                        "                    \"down_value\": \""+propertyValue+"\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}&tagCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_name\": \"tag_class_relation_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \""+pid+"\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"log10_score\",\n" +
                        "                        \"up_value\": \"0.2\",\n" +
                        "                        \"down_value\": \"0.0\"\n" +
                        "                    },\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"appid\",\n" +
                        "                        \"up_value\": \"172205760\",\n" +
                        "                        \"down_value\": \"172205760\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
                json2 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"query_type\": \"and\",\n" +
                        "            \"queryconditionArr\": [\n" +
                        "                {\n" +
                        "                    \"element_type\": \"vertex\",\n" +
                        "                    \"element_name\": \"user\",\n" +
                        "                    \"propertyname\": \"life_cycle\",\n" +
                        "                    \"up_value\": \""+propertyValue+"\",\n" +
                        "                    \"down_value\": \""+propertyValue+"\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}&tagCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_name\": \"tag_class_relation_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \""+pid+"\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"log10_score\",\n" +
                        "                        \"up_value\": \"0.5\",\n" +
                        "                        \"down_value\": \"0.2\"\n" +
                        "                    },\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"appid\",\n" +
                        "                        \"up_value\": \"172205760\",\n" +
                        "                        \"down_value\": \"172205760\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
                json3 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"query_type\": \"and\",\n" +
                        "            \"queryconditionArr\": [\n" +
                        "                {\n" +
                        "                    \"element_type\": \"vertex\",\n" +
                        "                    \"element_name\": \"user\",\n" +
                        "                    \"propertyname\": \"life_cycle\",\n" +
                        "                    \"up_value\": \""+propertyValue+"\",\n" +
                        "                    \"down_value\": \""+propertyValue+"\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}&tagCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_name\": \"tag_class_relation_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \""+pid+"\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"log10_score\",\n" +
                        "                        \"up_value\": \"1.0\",\n" +
                        "                        \"down_value\": \"0.5\"\n" +
                        "                    },\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"appid\",\n" +
                        "                        \"up_value\": \"172205760\",\n" +
                        "                        \"down_value\": \"172205760\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
                getReturn(pid, lists, json1, json2, json3, map1, map2, map3);
            }else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if(marsUserGroup!=null){
                        Map<String,String> retrunMap =  CombineSendJson.combineJson(marsUserGroup.getQueryCondition(),null);
                    json1 = "{\n" +
                            "    \"query_type\": \"and\",\n" +
                            "    \"result_type\": \"user\",\n" +
                            "    \"queryconditionArr\": [\n" +
                            "        {\n" +
                            "            \"element_name\": \"tag_class_relation_user\",\n" +
                            "            \"element_type\": \"edge\",\n" +
                            "            \"select_tag_id\": \""+pid+"\",\n" +
                            "            \"field_condition\": {\n" +
                            "                \"query_type\": \"and\",\n" +
                            "                \"queryconditionArr\": [\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"log10_score\",\n" +
                            "                        \"up_value\": \"0.2\",\n" +
                            "                        \"down_value\": \"0.0\"\n" +
                            "                    },\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"appid\",\n" +
                            "                        \"up_value\": \"172205760\",\n" +
                            "                        \"down_value\": \"172205760\"\n" +
                            "                    }\n" +
                            "                ]\n" +
                            "            }\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}";
                    json2 = "{\n" +
                            "    \"query_type\": \"and\",\n" +
                            "    \"result_type\": \"user\",\n" +
                            "    \"queryconditionArr\": [\n" +
                            "        {\n" +
                            "            \"element_name\": \"tag_class_relation_user\",\n" +
                            "            \"element_type\": \"edge\",\n" +
                            "            \"select_tag_id\": \""+pid+"\",\n" +
                            "            \"field_condition\": {\n" +
                            "                \"query_type\": \"and\",\n" +
                            "                \"queryconditionArr\": [\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"log10_score\",\n" +
                            "                        \"up_value\": \"0.5\",\n" +
                            "                        \"down_value\": \"0.2\"\n" +
                            "                    },\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"appid\",\n" +
                            "                        \"up_value\": \"172205760\",\n" +
                            "                        \"down_value\": \"172205760\"\n" +
                            "                    }\n" +
                            "                ]\n" +
                            "            }\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}";
                    json3 = "{\n" +
                            "    \"query_type\": \"and\",\n" +
                            "    \"result_type\": \"user\",\n" +
                            "    \"queryconditionArr\": [\n" +
                            "        {\n" +
                            "            \"element_name\": \"tag_class_relation_user\",\n" +
                            "            \"element_type\": \"edge\",\n" +
                            "            \"select_tag_id\": \""+pid+"\",\n" +
                            "            \"field_condition\": {\n" +
                            "                \"query_type\": \"and\",\n" +
                            "                \"queryconditionArr\": [\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"log10_score\",\n" +
                            "                        \"up_value\": \"1.0\",\n" +
                            "                        \"down_value\": \"0.5\"\n" +
                            "                    },\n" +
                            "                    {\n" +
                            "                        \"propertyname\": \"appid\",\n" +
                            "                        \"up_value\": \"172205760\",\n" +
                            "                        \"down_value\": \"172205760\"\n" +
                            "                    }\n" +
                            "                ]\n" +
                            "            }\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}";
                    Map<String, String> data = new HashMap<>();
                    data.put("appid", "35");
                    data.put("passwd", "8344202725");
                    data.put("appkey", "172205760");
                    data.put("method", "groupTagsByRecursiveFilter");
                    data.put("groupFieldName", "spread");
                    data.put("tagCondition", json1);
                    data.put("propertyCondition",retrunMap.get("propertyCondition"));
                    json1 = OkHttpUtils.post(shortUrl,data);
                    data.put("tagCondition", json2);
                    data.put("propertyCondition",retrunMap.get("propertyCondition"));
                    json2 = OkHttpUtils.post(shortUrl,data);
                    data.put("tagCondition", json3);
                    data.put("propertyCondition",retrunMap.get("propertyCondition"));
                    json3 = OkHttpUtils.post(shortUrl,data);
//                    json1 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json1);
//                    json2 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json2);
//                    json3 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json3);
                    getReturn(pid, lists, json1, json2, json3, map1, map2, map3);
                }
            }
            return OpResult.createSucResult(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.INTERACTIVE_THEME_EXCEPTION);
    }

    private void getReturn(String pid, List<Map<String, Object>> lists, String json1, String json2, String json3, Map<String, Object> map1, Map<String, Object> map2, Map<String, Object> map3) {
        Integer id;
        String count;JSONObject jsonObject1 = JSONObject.fromObject(json1);
        String datas = String.valueOf(jsonObject1.get("datas"));
        JSONObject jsonObject2 = JSONObject.fromObject(json2);
        String datas2 = String.valueOf(jsonObject2.get("datas"));
        JSONObject jsonObject3 = JSONObject.fromObject(json3);
        String datas3 = String.valueOf(jsonObject3.get("datas"));
        Map<Integer, String> tagMap1 = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {}.getType());
        Map<Integer, String> tagMap2 = new Gson().fromJson(datas2, new TypeToken<Map<Integer, String>>() {}.getType());
        Map<Integer, String> tagMap3 = new Gson().fromJson(datas3, new TypeToken<Map<Integer, String>>() {}.getType());
        for (Map.Entry<Integer,String> entry:tagMap1.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if(id.equals(tagId) ){
                count = entry.getValue();
                map1.put("name",  "高互动意愿");
                map1.put("value",count);
                lists.add(map1);
                break;
            }
        }
        for (Map.Entry<Integer,String> entry:tagMap2.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if(id.equals(tagId)){
                count = entry.getValue();
                map2.put("name","中互动意愿");
                map2.put("value",count);
                lists.add(map2);
                break;
            }
        }
        for (Map.Entry<Integer,String> entry:tagMap3.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if (id.equals(tagId)){
                count = entry.getValue();
                map3.put("name","低互动意愿");
                map3.put("value",count);
                lists.add(map3);
                break;
            }
        }
    }

    private void getReturnResult(String pid, List<UtilVo> lists, String jsonProperty) {
        Integer id;
        String count;JSONObject productJson = JSONObject.fromObject(jsonProperty);
        String datas = String.valueOf(productJson.get("datas"));
        Map<Integer, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {}.getType());
        for (Map.Entry<Integer,String> entry:tagMap.entrySet()){
            UtilVo utilVo = new UtilVo();
            id = entry.getKey();
            MarsTagsTmp marsTagsTmp = marsTagsTmpMapper.getInteractName(id);
            if(pid.equals(String.valueOf(marsTagsTmp.getClassId()))){
                utilVo.setName(marsTagsTmp.getTagName());
                count = entry.getValue();
                utilVo.setValue(count);
                lists.add(utilVo);
            }
        }
    }


}
