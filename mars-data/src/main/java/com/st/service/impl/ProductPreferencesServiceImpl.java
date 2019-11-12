package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.mapper.MarsUserGroupMapper;
import com.st.model.MarsTagClass;
import com.st.model.MarsTagsTmp;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.ProductPreferencesService;
import com.st.utils.CombineSendJson;
import com.st.utils.OkHttpUtils;
import com.st.utils.ProductPreferenceUtils;
import com.st.vo.UtilVo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhaoyx on 2016/9/13.
 */
@Service
public class ProductPreferencesServiceImpl implements ProductPreferencesService {
    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${user.draw.short_url}")
    private String shortUrl;
    private static Map<String, String> pNameMap = new HashMap<>();
    private static Map<String, String> classIdMap = new HashMap<>();
    @Autowired
    private MarsUserGroupMapper marsUserGroupMapper;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;

    static {
        String tagClassDataJson = null;
        try {
            tagClassDataJson = OkHttpUtils.get("http://api.data.social-touch.com:8091/cdapi/req?appid=35&passwd=8344202725&appkey=172205760&method=searchTagClass&pid=12");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObjectProperty = JSONObject.fromObject(tagClassDataJson);
        String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
        List<MarsTagClass> tagMap = new Gson().fromJson(tagStr, new TypeToken<List<MarsTagClass>>() {
        }.getType());
        pNameMap.put("12", "全部");
        tagMap.forEach(val -> {
            if (val.getAppkey().equals("172205760")) {
                pNameMap.put(val.getClassId(), val.getClassName());
            }
        });
        classIdMap.put("200612", "德芙");
        classIdMap.put("200620", "脆香米");
        classIdMap.put("200624", "士力架");
        classIdMap.put("200628", "MMS");
        classIdMap.put("200632", "麦提莎");
    }

    /**
     * 获取品牌特征
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getBrandCharacteristics(String propertyValue, String pid, String groupId) {
        try {
            if ("购买用户".equals(propertyValue)){
                propertyValue = "电商购买用户";
            }
            int count = 0;
            List<Map<String, Object>> list = new ArrayList<>();
            if (groupId == null) {
                String classId = ProductPreferenceUtils.getBandClassId(pid);
                if (null != classId) {
                    getBrandResult(propertyValue, count, list, classId);
                } else {
                    //如果为空，说明前台传的是全部，需要将所有结果返回
                    List<String> classIdList = ProductPreferenceUtils.getBandClassIds();
                    for (String classIds : classIdList) {
                        getBrandResult(propertyValue, count, list, classIds);
                    }
                }
            } else {
                //查询用户组的查询条件
                String classId = ProductPreferenceUtils.getBandClassId(pid);
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != marsUserGroup) {
                    if (null != classId) {
                        getBrandGroupResult(list, classId, marsUserGroup);
                    } else {
                        //如果为空，说明前台传的是全部，需要将所有结果返回
                        List<String> classIdList = ProductPreferenceUtils.getBandClassIds();
                        for (String classIds : classIdList) {
                            getBrandGroupResult(list, classIds, marsUserGroup);
                        }
                    }
                }
            }
            if (null != list && list.size() > 0)
                list.sort((o1, o2) -> String.valueOf(o2.get("value")).compareTo(String.valueOf(o1.get("value"))));
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }


    /**
     * 获取产品特征
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getProductCharacteristics(String propertyValue, String pid, String groupId) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if ("购买用户".equals(propertyValue)){
                propertyValue = "电商购买用户";
            }
            String classId = ProductPreferenceUtils.getProductClassId(pid);
            if (groupId == null) {
                if (null != classId) {
                    getProductJson(propertyValue, pid, list, classId);
                } else {
                    //选全部时
                    List<String> classIdList = ProductPreferenceUtils.getProductClassIds();
                    for (String classIds : classIdList) {
                        getProductJson(propertyValue, classIds, list, classIds);
                    }
                }
            } else {
                //存在用户群组时
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != marsUserGroup) {
                    if (null != classId) {
                        getUserGroupProductResult(list, marsUserGroup, classId);
                    } else {
                        //如果为空，说明前台传的是全部，需要将所有结果返回
                        List<String> classIdList = ProductPreferenceUtils.getProductClassIds();
                        for (String classIds : classIdList) {
                            getUserGroupProductResult(list, marsUserGroup, classIds);
                        }
                    }
                }
            }
            List<Map<String,Object>> list1;
            if (list.size()>10){
                list1 = list.subList(0,10);
            }else {
                list1 = list;
            }

            return OpResult.createSucResult(list1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }


    /**
     * 获取口味特征
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getFlavorCharacteristics(String propertyValue, String pid, String groupId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String classId = ProductPreferenceUtils.getFlavorClassId(pid);
        if ("购买用户".equals(propertyValue)){
            propertyValue = "电商购买用户";
        }
        try {
            if (groupId == null) {
                if (null != classId) {
                    getProductJson(propertyValue, pid, list, classId);
                } else {
                    //选全部时
                    List<String> classIdList = ProductPreferenceUtils.getFlavorClassIds();
                    for (String classIds : classIdList) {
                        getProductJson(propertyValue, pid, list, classIds);
                    }
                }
            } else {
                //存在用户群组时
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != classId) {
                    getUserGroupProductResult(list, marsUserGroup, classId);
                } else {
                    //如果为空，说明前台传的是全部，需要将所有结果返回
                    List<String> classIdList = ProductPreferenceUtils.getFlavorClassIds();
                    for (String classIds : classIdList) {
                        getUserGroupProductResult(list, marsUserGroup, classIds);
                    }
                }
            }

            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }


    /**
     * 获取包装特征
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getPackagingCharacteristics(String propertyValue, String pid, String groupId) {
        List<Map<String, Object>> list = new ArrayList<>();
        if ("购买用户".equals(propertyValue)){
            propertyValue = "电商购买用户";
        }
        try {
            String classId = ProductPreferenceUtils.getPackingClassId(pid);
            if (groupId == null) {
                if (null != classId) {
                    getProductJson(propertyValue, pid, list, classId);
                } else {
                    //选全部时
                    List<String> classIdList = ProductPreferenceUtils.getPackingClassIds();
                    for (String classIds : classIdList) {
                        getProductJson(propertyValue, pid, list, classIds);
                    }
                }
            } else {
                //存在用户群组时
                MarsUserGroup marsUserGroup = marsUserGroupMapper.getUserGroupConditionsById(groupId);
                if (null != classId) {
                    getUserGroupProductResult(list, marsUserGroup, classId);
                } else {
                    //如果为空，说明前台传的是全部，需要将所有结果返回
                    List<String> classIdList = ProductPreferenceUtils.getPackingClassIds();
                    for (String classIds : classIdList) {
                        getUserGroupProductResult(list, marsUserGroup, classIds);
                    }
                }
            }
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }

    /**
     * 获取品牌列表
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, String>>> getBrandClass() {
        List<Map<String, String>> lists = new ArrayList<>();
        try {
            //查找品牌列表
            String tagClassDataJson = OkHttpUtils.get(urlValue + "searchTagClass&pid=12");
            JSONObject jsonObjectProperty = JSONObject.fromObject(tagClassDataJson);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            List<MarsTagClass> tagMap = new Gson().fromJson(tagStr, new TypeToken<List<MarsTagClass>>() {
            }.getType());
            Map<String, String> map = new HashMap<>();
            map.put("name", "全部");
            map.put("value", "12");
            lists.add(map);
            tagMap.forEach(val -> {
                if (val.getAppkey().equals("172205760")) {
                    Map map1 = new HashMap<>();
                    map1.put("name", val.getClassName());
                    map1.put("value", val.getClassId());
                    lists.add(map1);
                }
            });
            return OpResult.createSucResult(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createSucResult(lists);
    }


    /**
     * 封装品牌返回值
     *
     * @param propertyValue
     * @param count
     * @param marsTagsTmpList
     * @return
     * @throws Exception
     */
    private int getBrandCount(String propertyValue, int count, List<MarsTagsTmp> marsTagsTmpList) throws Exception {
        String json;
        for (MarsTagsTmp marsTagsTmp : marsTagsTmpList) {
            json = OkHttpUtils.get(urlValue + "getFansListByPropertiesAndTagIds&tagIds=" + marsTagsTmp.getTagId() + "&page=1&pageSize=1&propertyCondition={\"query_type\":\"and\",\"queryconditionArr\":[{\"propertyname\":\"life_cycle\",\"up_value\":\"" + propertyValue + "\",\"down_value\":\"" + propertyValue + "\"}]}");
            String sum = String.valueOf(JSONObject.fromObject(JSONObject.fromObject(json).get("datas")).get("totalRecordSize"));
            if (null != sum && !sum.equals("0")) {
                count += Integer.parseInt(sum);
            }
        }
        return count;
    }


    private void getBrandGroupResult(List<Map<String, Object>> list, String classId, MarsUserGroup marsUserGroup) throws Exception {
        int count;
        Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), classId);
        Map<String, String> data = new HashMap<>();
        data.put("appid", "35");
        data.put("passwd", "8344202725");
        data.put("appkey", "172205760");
        data.put("method", "groupTagsByRecursiveFilter");
        data.put("tagCondition", mapes.get("tagCondition"));
        if (!"".equals(mapes.get("propertyCondition"))){
            data.put("propertyCondition",mapes.get("propertyCondition"));
        }
        String returnJson = OkHttpUtils.post(shortUrl,data);
//        String returnJson = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&tagCondition=" + json);
        JSONObject productJson = JSONObject.fromObject(returnJson);
        String datas = String.valueOf(productJson.get("datas"));
        Map<Integer, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {
        }.getType());
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            MarsTagsTmp utilNameVO = marsTagsTmpMapper.getInteractName(entry.getKey());
            if (utilNameVO!=null){
                if (classId.equals(String.valueOf(utilNameVO.getClassId()))) {
                    count = Integer.parseInt(entry.getValue());
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", classIdMap.get(classId));
                    map.put("value", count);
                    list.add(map);
                }
            }
        }
    }

    private void getBrandResult(String propertyValue, int count, List<Map<String, Object>> list, String classId) throws Exception {
        List<MarsTagsTmp> marsTagsTmpList = marsTagsTmpMapper.getMarsTagTmpList(classId);
        count = getBrandCount(propertyValue, count, marsTagsTmpList);
        Map<String, Object> map = new HashMap<>();
        map.put("name", classIdMap.get(classId));
        map.put("value", count);
        list.add(map);
    }

    private void getProductJson(String propertyValue, String pid, List<Map<String, Object>> list, String classId) throws Exception {
        String returnJson = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&propertyCondition={\n" +
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
                "            \"select_tag_id\": \""+classId+"\",\n" +
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
                "}");
        JSONObject productJson = JSONObject.fromObject(returnJson);
        String datas = String.valueOf(productJson.get("datas"));
        Map<Integer, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {
        }.getType());
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            UtilVo utilVo = new UtilVo();
            MarsTagsTmp utilNameVO = marsTagsTmpMapper.getInteractName(entry.getKey());
            if (utilNameVO!=null){
                if (pid.equals(String.valueOf(utilNameVO.getClassId()))) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", utilNameVO.getTagName());
                    map.put("value", entry.getValue());
                    list.add(map);
                }
            }
        }
    }

    private void getUserGroupProductResult(List<Map<String, Object>> list, MarsUserGroup marsUserGroup, String classIds) throws Exception {
        Map<String,String> mapes = CombineSendJson.combineJson(marsUserGroup.getQueryCondition(), classIds);
        Map<String, String> data = new HashMap<>();
        data.put("appid", "35");
        data.put("passwd", "8344202725");
        data.put("appkey", "172205760");
        data.put("method", "groupTagsByRecursiveFilter");
        data.put("tagCondition", mapes.get("tagCondition"));
        if (!"".equals(mapes.get("propertyCondition"))){
            data.put("propertyCondition",mapes.get("propertyCondition"));
        }
        String returnJson = OkHttpUtils.post(shortUrl,data);
//        String returnJson = OkHttpUtils.get(urlValue + "groupTagsByRecursiveFilter&tagCondition=" + json);

        JSONObject productJson = JSONObject.fromObject(returnJson);
        String datas = String.valueOf(productJson.get("datas"));
        Map<Integer, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<Integer, String>>() {
        }.getType());
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            MarsTagsTmp utilNameVO = marsTagsTmpMapper.getInteractName(entry.getKey());
            if (utilNameVO!=null){
                if (classIds.equals(String.valueOf(utilNameVO.getClassId()))) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", utilNameVO.getTagName());
                    map.put("value", Integer.parseInt(entry.getValue()));
                    list.add(map);
                }
            }
        }
    }
}
