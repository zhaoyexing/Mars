package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.conf.CityConfiguration;
import com.st.conf.PrivinceConfiguration;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.mapper.MarsUserGroupMapper;
import com.st.model.FanAreaModel;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.LifeCycleService;
import com.st.service.UserAnalysisService;
import com.st.utils.CombineSendJson;
import com.st.utils.HttpClientFactory;
import com.st.utils.OkHttpUtils;
import com.st.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.util.*;

import static java.util.Comparator.comparing;

/**
 * 用户分析
 * Created by zhaoyx on 2016/9/12.
 */
@Service
public class UserAnalysisServiceImpl implements UserAnalysisService {

    @Value("${user.draw.url}")
    private String urlValue;

    @Value("${user.draw.short_url}")
    private String shortUrl;

    @Autowired
    private LifeCycleService lifeCycleServiceImpl;

    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;

    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;

    @Autowired
    private CityConfiguration cityConfiguration;

    @Autowired
    private PrivinceConfiguration privinceConfiguration;

    /**
     * 获取粉丝地域分布
     * 全国网名占比是在配置文件中写死的，需要查询出来的省份去配置文件中找
     * 因为用了注入，把全国网占比的数据存入了Map<String, Double> city = CityConfig.name;
     * 以便于查询
     *
     * @param propertyValue
     * @return
     */
    @Override
    public OpResult<List<FanAreaModel>> getFanAreaDistribution(String propertyValue, String statisPropertyName, String groupId) {
        List<FanAreaModel> list = new ArrayList<>();
        Integer allCount = 0;
        //取得全国网名占比
        Map<String, Double> city = cityConfiguration.getName();
        Map<String,String> province = privinceConfiguration.getName();
        DecimalFormat df = new DecimalFormat("0.00");//计算占比时，保留两位小数
        try {
            if (null == groupId) {
                //获取指定appId的离线统计结果的最新时间
                JSONObject jsonObjectDistribution = getReturnJson(propertyValue, statisPropertyName, groupId);
                if (jsonObjectDistribution.size()!=0){
                    if ("购买用户".equals(propertyValue)){
                        for (Iterator iter = jsonObjectDistribution.keys(); iter.hasNext(); ) {
                            String key = (String) iter.next();
                            //如在配置文件找不到，说明是国外的省份，直接继续下次循环
                            if (StringUtils.isNull(province.get(key)))
                                continue;
                            allCount = allCount + Integer.parseInt(Objects.toString(jsonObjectDistribution.get(key)));
                            FanAreaModel uvModel = new FanAreaModel();
                            //省份
                            uvModel.setProvince(province.get(key));
                            //数量
                            uvModel.setCount(Integer.parseInt(String.valueOf(jsonObjectDistribution.get(key))));
                            //占比
                            uvModel.setCountPct(Double.valueOf(df.format((double) uvModel.getCount() / allCount * 100)));
                            //全国网名占比
                            uvModel.setNationalPct(city.get(province.get(key)));
                            list.add(uvModel);
                        }
                    }else {
                        for (Iterator iter = jsonObjectDistribution.keys(); iter.hasNext(); ) {
                            String key = (String) iter.next();
                            //如在配置文件找不到，说明是国外的省份，直接继续下次循环
                            if (StringUtils.isNull(city.get(key)))
                                continue;
                            allCount = allCount + Integer.parseInt(Objects.toString(jsonObjectDistribution.get(key)));
                            FanAreaModel uvModel = new FanAreaModel();
                            //省份
                            uvModel.setProvince(key);
                            //数量
                            uvModel.setCount(Integer.parseInt(String.valueOf(jsonObjectDistribution.get(key))));
                            //占比
                            uvModel.setCountPct(Double.valueOf(df.format((double) uvModel.getCount() / allCount * 100)));
                            //全国网名占比
                            uvModel.setNationalPct(city.get(key));
                            list.add(uvModel);
                        }
                    }
                }
                final Integer finalAllCount = allCount;//lambda表达式中变量必须为final
                list.forEach(u -> u.setCountPct(Double.valueOf(df.format((double) u.getCount() / finalAllCount * 100))));
            } else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != marsUserGroup){
                    Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), null);
                    //Map<String, String> maping = new Gson().fromJson(marsUserGroup.getQueryCondition(), new TypeToken<Map<String, String>>() {}.getType());
                    JSONObject jsonObject = JSONObject.fromObject(marsUserGroup.getQueryCondition());
                    Map<String,String> maping = (Map)jsonObject;
                    if ("电商用户".equals(maping.get("lifeCycle"))){
                        String tags = mapes.get("tagCondition").substring(0, mapes.get("tagCondition").length() - 3);
                        tags += ",\n" +
                                "        {\n" +
                                "            \"element_name\": \"tag_class_relation_user\",\n" +
                                "            \"element_type\": \"edge\",\n" +
                                "            \"select_tag_id\": \"195108\",\n" +
                                "            \"field_condition\": {\n" +
                                "                \"query_type\": \"and\",\n" +
                                "                \"queryconditionArr\": [\n" +
                                "                    {\n" +
                                "                        \"propertyname\": \"log10_score\",\n" +
                                "                        \"up_value\": \"1\",\n" +
                                "                        \"down_value\": \"-1\"\n" +
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
                        Map<String,String> data = new HashMap<>();
                        data.put("appid", "35");
                        data.put("passwd", "8344202725");
                        data.put("appkey", "172205760");
                        data.put("method", "groupTagsByRecursiveFilter");
                        data.put("tagCondition", tags);
                        if (!"".equals(mapes.get("propertyCondition"))) {
                            data.put("propertyCondition", mapes.get("propertyCondition"));
                        }
                        String returnJson = OkHttpUtils.post(shortUrl, data);
                        JSONObject productJson = JSONObject.fromObject(returnJson);
                        String datas = String.valueOf(productJson.get("datas"));
                        if (null != datas && !datas.equals("执行失败")) {
                            Map<String, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<String, String>>() {
                            }.getType());
                            for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                                FanAreaModel uvNodes = new FanAreaModel();
                                if (!StringUtils.isNull(province.get(entry.getKey()))){
                                    allCount = allCount + Integer.parseInt(Objects.toString(entry.getValue()));
                                    //省份
                                    uvNodes.setProvince(province.get(entry.getKey()));
                                    //人数
                                    uvNodes.setCount(Integer.parseInt(entry.getValue()));
                                    //占比
                                    uvNodes.setCountPct(Double.valueOf(df.format((double) uvNodes.getCount() / allCount * 100)));
                                    //全国网名占比
                                    uvNodes.setNationalPct(city.get(province.get(entry.getKey())));
                                    list.add(uvNodes);
                                }
                            }
                        }
                    }else {
                        Map<String, String> data = new HashMap<>();
                        data.put("appid", "35");
                        data.put("passwd", "8344202725");
                        data.put("appkey", "172205760");
                        data.put("method", "groupByByCollectionQuery");
                        data.put("groupFieldName", "province");
                        data.put("sampleCount","1000000");
                        data.put("tagCondition", mapes.get("tagCondition"));
                        if (!"".equals(mapes.get("propertyCondition"))){
                            data.put("propertyCondition",mapes.get("propertyCondition"));
                        }
                        String returnJsons = OkHttpUtils.post(shortUrl,data);
                        JSONObject productJson = JSONObject.fromObject(returnJsons);
                        String datas = String.valueOf(productJson.get("datas"));
                        Map<String, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<String, String>>() {
                        }.getType());
                        tagMap.remove("unknown");
                        for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                            if (StringUtils.isNull(city.get(entry.getKey())))
                                continue;
                            FanAreaModel uvModel = new FanAreaModel();
                            allCount = allCount + Integer.parseInt(Objects.toString(entry.getValue()));
                            //省份
                            uvModel.setProvince(entry.getKey());
                            //人数
                            uvModel.setCount(Integer.parseInt(entry.getValue()));
                            //占比
                            uvModel.setCountPct(Double.valueOf(df.format((double) uvModel.getCount() / allCount * 100)));
                            //全国网名占比
                            uvModel.setNationalPct(city.get(entry.getKey()));
                            list.add(uvModel);
                        }
                    }

                }
                final Integer finalAllCount = allCount;//lambda表达式中变量必须为final
                list.forEach(u -> u.setCountPct(Double.valueOf(df.format((double) u.getCount() / finalAllCount * 100))));
            }
            //排序,按照人数进行排序
            list.sort(comparing(FanAreaModel::getCount).reversed());
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.AREA_DISTRIBUTION_EXCEPTION);
    }

    /**
     * 获取性别分布
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getSexDistribution(String propertyValue, String statisPropertyName, String groupId) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        String method = "sex";
        try {
            if (null == groupId) {
                JSONObject jsonObjectDistribution = getReturnJsons(propertyValue, statisPropertyName, groupId);
                if (jsonObjectDistribution.size()!=0){
                    for (Iterator iter = jsonObjectDistribution.keys(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        if (Objects.equals("1", key)) {
                            map = new HashMap<>();
                            map.put("name", "男");
                            map.put("count", String.valueOf(jsonObjectDistribution.get(key)));
                            list.add(map);
                        } else if (Objects.equals("2", key)) {
                            map = new HashMap<>();
                            map.put("name", "女");
                            map.put("count", String.valueOf(jsonObjectDistribution.get(key)));
                            list.add(map);
                        } else {
                            map = new HashMap<>();
                            map.put("name", "未知");
                            map.put("count", String.valueOf(jsonObjectDistribution.get(key)));
                            list.add(map);
                        }
                    }
                }
            } else {
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != marsUserGroup){
                    Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), null);
                    //String jsons = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&groupFieldName=spread&tagCondition="+json);
//                    json = json + "," + CombineSendJson.getDataCityAndSex();
//                    json = "{" +
//                            "    \"query_type\": \"and\"," +
//                            "    \"result_type\": \"user\"," +
//                            "    \"queryconditionArr\": [" + json + "]}".replaceAll(" ", "");
                    Map<String, String> data = new HashMap<>();
                    data.put("appid", "35");
                    data.put("passwd", "8344202725");
                    data.put("appkey", "172205760");
                    data.put("method", "groupByByCollectionQuery");
                    data.put("sampleCount","1000000");
                    data.put("groupFieldName", "sex");
                    data.put("tagCondition", mapes.get("tagCondition"));
                    if (!"".equals(mapes.get("propertyCondition"))){
                        data.put("propertyCondition",mapes.get("propertyCondition"));
                    }
                    String returnJson = OkHttpUtils.post(shortUrl,data);
                    JSONObject productJson = JSONObject.fromObject(returnJson);
                    String datas = String.valueOf(productJson.get("datas"));
                    if (null != datas && !datas.equals("执行失败")) {
                        Map<String, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<String, String>>() {
                        }.getType());
                        for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                            if (Objects.equals("1", entry.getKey())) {
                                map = new HashMap<>();
                                map.put("name", "男");
                                map.put("count", String.valueOf(entry.getValue()));
                                list.add(map);
                            } else if (Objects.equals("2", entry.getKey())) {
                                map = new HashMap<>();
                                map.put("name", "女");
                                map.put("count", String.valueOf(entry.getValue()));
                                list.add(map);
                            } else {
                                map = new HashMap<>();
                                map.put("name", "未知");
                                map.put("count", String.valueOf(entry.getValue()));
                                list.add(map);
                            }
                        }
                    }
                }

            }

            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    /**
     * 获取年龄分布
     * @param propertyValue
     *
     * @param groupId
     * @return
     */
    @Override
    public OpResult<List<String>> getAgeDistribution(String propertyValue, String groupId) {
        //Map<String, Object> map = null;
        List<String> list = new ArrayList<>();
        try {
//            map.put("1-10岁","200");
//            map.put("10-20岁","343");
//            map.put("20-30岁","432");
//            map.put("30-40岁","231");
//            map.put("40-50岁","637");
//            map.put("50-60岁","734");
//            map.put("60-70岁","232");
//            map.put("70-80岁","232");
//            map.put("80-90岁","321");
//            map.put("90-100岁","53");
            //list.add(map);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    /**
     * 获取婚姻状况
     * @param propertyValue
     *
     * @param groupId
     * @return
     */
    @Override
    public OpResult<List<String>> getMaritalStatus(String propertyValue, String groupId) {
        //Map<String, Object> map = null;
        List<String> list = new ArrayList<>();
        try {
//            map.put("结婚","200");
//            map.put("未婚","343");
//            map.put("未知","432");
            //list.add(map);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    @Override
    public OpResult<List<String>> getBabyNum(String propertyValue, String groupId) {
        //Map<String, Object> map = null;
        List<String> list = new ArrayList<>();
        try {
//            map.put("0个","200");
//            map.put("1个","343");
//            map.put("2个","432");
//            map.put("2个以上","231");
            //list.add(map);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    @Override
    public OpResult<List<String>> getHobby(String propertyValue, String groupId) {
        //Map<String, Object> map = null;
        List<String> list = new ArrayList<>();
        try {
//            map.put("运动","200");
//            map.put("美食","343");
//            map.put("摄影","432");
//            map.put("购物","231");
//            map.put("阅读","637");
//            map.put("电影","734");
            //list.add(map);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }


    /**
     * 获取用户分析结果集通用方法
     *
     * @param propertyValue
     * @param statisPropertyName
     * @return
     */
    public JSONObject getReturnJson(String propertyValue, String statisPropertyName, String groupId) {
        String endTime = String.valueOf(new Date().getTime());
        List<FanAreaModel> list = new ArrayList<>();
        JSONObject jsonObjectClass = null;
        String startTime = null;
        UserGroupServiceImpl userGroupService = new UserGroupServiceImpl();
        try {
            if ("购买用户".equals(propertyValue)){
                String province = OkHttpUtils.get("http://api.data.social-touch.com:8091/cdapi/req?appid=35&passwd=8344202725&appkey=172205760&method=groupTagsByRecursiveFilter&tagCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        {\n" +
                        "            \"element_name\": \"appuser_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \"172205760\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"appid\",\n" +
                        "                        \"up_value\": \"172205760\",\n" +
                        "                        \"down_value\": \"172205760\"\n" +
                        "                    }\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"element_name\": \"tag_class_relation_user\",\n" +
                        "            \"element_type\": \"edge\",\n" +
                        "            \"select_tag_id\": \"195108\",\n" +
                        "            \"field_condition\": {\n" +
                        "                \"query_type\": \"and\",\n" +
                        "                \"queryconditionArr\": [\n" +
                        "                    {\n" +
                        "                        \"propertyname\": \"log10_score\",\n" +
                        "                        \"up_value\": \"1\",\n" +
                        "                        \"down_value\": \"-1\"\n" +
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
                        "}&propertyCondition={\n" +
                        "    \"query_type\": \"and\",\n" +
                        "    \"result_type\": \"user\",\n" +
                        "    \"queryconditionArr\": [\n" +
                        "        \n" +
                        "        {\n" +
                        "            \"query_type\": \"or\",\n" +
                        "            \"queryconditionArr\": [\n" +
                        "                {\n" +
                        "                    \"element_type\": \"vertex\",\n" +
                        "                    \"element_name\": \"user\",\n" +
                        "                    \"propertyname\": \"life_cycle\",\n" +
                        "                    \"up_value\": \"电商购买用户\",\n" +
                        "                    \"down_value\": \"电商购买用户\"\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}\n");
                JSONObject provinceObject = JSONObject.fromObject(province);
                jsonObjectClass = (JSONObject) provinceObject.get("datas");
            }else {
                //获取指定appId的离线统计结果的最新时间
                String latestTimeJson;
                latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
                JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
                //取开始时间
                startTime = String.valueOf(jsonObject.get("datas"));
                String cityJson = HttpClientFactory.requestGet(urlValue
                        + "getFansNumOfGroupFieldByPropertyCon" +
                        "&propertyName=life_cycle" +
                        "&startTime=" + startTime +
                        "&endTime=" + endTime +
                        "&propertyValue=" + propertyValue +
                        "&statisPropertyName=" + statisPropertyName);
                JSONObject cityJsonObject = JSONObject.fromObject(cityJson);
                String cityJsonStr = String.valueOf(cityJsonObject.get("datas"));
                jsonObjectClass = JSONObject.fromObject(cityJsonStr);
                return JSONObject.fromObject(jsonObjectClass.get(startTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("购买用户".equals(propertyValue)){
            return jsonObjectClass;
        }else {
            return JSONObject.fromObject(jsonObjectClass.get(startTime));
        }
    }
    public JSONObject getReturnJsons(String propertyValue, String statisPropertyName, String groupId) {
        if ("购买用户".equals(propertyValue)){
            propertyValue = "电商购买用户";
        }
        String endTime = String.valueOf(new Date().getTime());
        JSONObject jsonObjectClass = null;
        String startTime = null;
        UserGroupServiceImpl userGroupService = new UserGroupServiceImpl();
        try {
            //获取指定appId的离线统计结果的最新时间
            String latestTimeJson;
            latestTimeJson = HttpClientFactory.requestGet(urlValue + "getLatestTimeOfFansPropertyTagCount");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            //取开始时间
            startTime = String.valueOf(jsonObject.get("datas"));
            String cityJson = HttpClientFactory.requestGet(urlValue
                    + "getFansNumOfGroupFieldByPropertyCon" +
                    "&propertyName=life_cycle" +
                    "&startTime=" + startTime +
                    "&endTime=" + endTime +
                    "&propertyValue=" + propertyValue +
                    "&statisPropertyName=" + statisPropertyName);
            JSONObject cityJsonObject = JSONObject.fromObject(cityJson);
            String cityJsonStr = String.valueOf(cityJsonObject.get("datas"));
            jsonObjectClass = JSONObject.fromObject(cityJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(jsonObjectClass.get(startTime));
    }

}
