package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.mapper.MarsUserGroupMapper;
import com.st.model.MarsTagsTmp;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.SpreadInfluenceService;
import com.st.utils.CombineSendJson;
import com.st.utils.HttpClientFactory;
import com.st.utils.OkHttpUtils;
import com.st.vo.UtilVo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 传播影响力
 * Created by zhaoyx on 2016/9/13.
 */
@Service
public class SpreadInfluenceServiceImpl implements SpreadInfluenceService {
    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${user.draw.short_url}")
    private String shortUrl;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;
    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;
    /**
     * 传播影响力
     * @return
     */
    @Override
    public OpResult<List<Map<String,Object>>> getSpreadInfluence(String propertyValue,String pid,String groupId) {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            String endTime = String.valueOf(new Date().getTime()/1000);
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime = String.valueOf(jsonObject.get("datas"));
            String jsonProperty1;
            String jsonProperty2;
            String jsonProperty3;
            Map<String,Object> map1 = new HashMap<>();
            Map<String,Object> map2 = new HashMap<>();
            Map<String,Object> map3 = new HashMap<>();
            //返回结果
            if("".equals(groupId)){
                jsonProperty1 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
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
                        "                        \"up_value\": \"0.1\",\n" +
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
                jsonProperty2 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
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
                        "                        \"down_value\": \"0.1\"\n" +
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
                jsonProperty3 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\n" +
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
                        "                        \"up_value\": \"0.3\",\n" +
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
                getReturn(pid, list, jsonObject, jsonProperty1, jsonProperty2, jsonProperty3, map1, map2, map3);
            }else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if(marsUserGroup!=null){
                    Map<String,String> retrunMap =  CombineSendJson.combineJson(marsUserGroup.getQueryCondition(),null);
                    String json1 = "{\n" +
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
                    String json2 = "{\n" +
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
                    String json3 = "{\n" +
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
                    String url = "http://211.151.59.253:10091/cdapi/req\n";
                    jsonProperty1 = OkHttpUtils.post(shortUrl,data);
                    data.put("tagCondition", json2);
                    data.put("propertyCondition",retrunMap.get("propertyCondition"));
                    jsonProperty2 = OkHttpUtils.post(shortUrl,data);
                    data.put("tagCondition", json3);
                    data.put("propertyCondition",retrunMap.get("propertyCondition"));
                    jsonProperty3 = OkHttpUtils.post(shortUrl,data);
//                    jsonProperty1 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json1);
//                    jsonProperty2 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json2);
//                    jsonProperty3 = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json3);
                    getReturn(pid, list, jsonObject, jsonProperty1, jsonProperty2, jsonProperty3, map1, map2, map3);
                }
            }


            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SPREAD_INFLUENCE_EXCEPTION);
    }

    private void getReturn(String pid, List<Map<String, Object>> list, JSONObject jsonObject, String jsonProperty1, String jsonProperty2, String jsonProperty3, Map<String, Object> map1, Map<String, Object> map2, Map<String, Object> map3) {
        Integer id;
        String count;
        JSONObject jsonObject1 = JSONObject.fromObject(jsonProperty1);
        String datas1 = String.valueOf(jsonObject1.get("datas"));
        JSONObject jsonObject2 = JSONObject.fromObject(jsonProperty2);
        String datas2 = String.valueOf(jsonObject2.get("datas"));
        JSONObject jsonObject3 = jsonObject.fromObject(jsonProperty3);
        String datas3 = String.valueOf(jsonObject3.get("datas"));
        Map<Integer, String> tagMap1 = new Gson().fromJson(datas1, new TypeToken<Map<Integer, String>>() {}.getType());
        Map<Integer, String> tagMap2 = new Gson().fromJson(datas2, new TypeToken<Map<Integer, String>>() {}.getType());
        Map<Integer, String> tagMap3 = new Gson().fromJson(datas3, new TypeToken<Map<Integer, String>>() {}.getType());
        for (Map.Entry<Integer,String> entry:tagMap1.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if(id.equals(tagId)){
                count = entry.getValue();
                map1.put("name","高级影响力");
                map1.put("value",count);
                list.add(map1);
                break;
            }
        }
        for (Map.Entry<Integer,String> entry:tagMap2.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if(id.equals(tagId)){
                count = entry.getValue();
                map2.put("name","中级影响力");
                map2.put("value",count);
                list.add(map2);
                break;
            }
        }
        for (Map.Entry<Integer,String> entry:tagMap3.entrySet()){
            id = entry.getKey();
            Integer tagId = marsTagsTmpMapper.getTagId(pid);
            if(id.equals(tagId)){
                count = entry.getValue();
                map3.put("name","低级影响力");
                map3.put("value",count);
                list.add(map3);
                break;
            }
        }
    }

    /**
     * 传播内容倾向
     * @param propertyValue
     * @param pid
     * @return
     */

    @Override
    public OpResult<List<UtilVo>> getSpreadingTendency(String propertyValue,String pid,String groupId) {
        List<UtilVo> list = new ArrayList<>();
        try {
            String endTime = String.valueOf(new Date().getTime()/1000);
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            String startTime = String.valueOf(jsonObject.get("datas"));
            //返回结果
            String jsonProperty;
            if(groupId==null){
                jsonProperty = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&propertyCondition={\n" +
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

                //jsonProperty = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&propertyCondition={\"query_type\":\"and\",\"result_type\":\"user\",\"queryconditionArr\":[{\"query_type\":\"pair\",\"queryconditionArr\":[{\"propertyname\":\"appid\",\"up_value\":\"172205760\",\"down_value\":\"172205760\"},{\"element_name\":\"tag_class_relation_user\",\"up_value\":\""+pid+"\",\"down_value\":\""+pid+"\"},{\"propertyname\":\"log10_score\",\"up_value\":\"1\",\"down_value\":\"0\"}]},{\"query_type\":\"and\",\"queryconditionArr\":[{\"element_type\":\"vertex\",\"element_name\":\"user\",\"propertyname\":\"life_cycle\",\"up_value\":\""+propertyValue+"\",\"down_value\":\""+propertyValue+"\"}]}]}");
                getReturn(pid, list, jsonProperty);
            }else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (marsUserGroup!=null){
                    Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), pid);
                    Map<String, String> data = new HashMap<>();
                    data.put("appid", "35");
                    data.put("passwd", "8344202725");
                    data.put("appkey", "172205760");
                    data.put("method", "groupTagsByRecursiveFilter");
                    data.put("tagCondition", mapes.get("tagCondition"));
                    if (!"".equals(mapes.get("propertyCondition"))){
                        data.put("propertyCondition",mapes.get("propertyCondition"));
                    }
                    jsonProperty = OkHttpUtils.post(shortUrl,data);
//                    jsonProperty = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&tagCondition=" + json);
                    getReturn(pid, list, jsonProperty);
                }
            }

            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SPREAD_INGTENDENCY_EXCEPTION);
    }

    private void getReturn(String pid, List<UtilVo> list, String jsonProperty) {
        Integer id;
        String count;JSONObject jsonObject1 = JSONObject.fromObject(jsonProperty);
        String datas = String.valueOf(jsonObject1.get("datas"));
        Map<Integer, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {}.getType());
        for (Map.Entry<Integer,String> entry:tagMap.entrySet()){
            UtilVo utilVo = new UtilVo();
            id = entry.getKey();
            MarsTagsTmp marsTagsTmp = marsTagsTmpMapper.getInteractName(id);
            if(pid.equals(String.valueOf(marsTagsTmp.getClassId()))){
                count = entry.getValue();
                utilVo.setValue(count);
                utilVo.setName(marsTagsTmp.getTagName());
                list.add(utilVo);
            }
        }
    }
}
