package com.st.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.conf.ApplicationContextConfiguration;
import com.st.conf.ProvinceConfiguration;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.model.MarsTagsTmp;
import com.st.vo.ScreeningConditionsVO;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拼接发送的json
 * Created by zhaoyx on 2016/11/16.
 */
public class CombineSendJson {
    public static Map<String, String> tagIds = new HashMap<>();

    static {
        MarsTagsTmpMapper marsTagsTmpMapper = ApplicationContextConfiguration.getBean(MarsTagsTmpMapper.class);
        List<MarsTagsTmp> list = marsTagsTmpMapper.getAllTags();
        list.forEach(marsTagsTmp -> tagIds.put(String.valueOf(marsTagsTmp.getTagId()), marsTagsTmp.getTagName()));
    }

    /**
     * 合并json,即返回的json
     *
     * @param queryCondition
     * @return
     */
    public static Map<String,String> combineJson(String queryCondition, String classId) {
        JSONObject productJson = JSONObject.fromObject(queryCondition);
        String datas = String.valueOf(productJson.get("lifeCycle"));
        if ("".equals(datas)){
            String life = ",\"lifeCycle\": \"\"";
            queryCondition = queryCondition.substring(0,queryCondition.length()-life.length());
            queryCondition +="}";

        }
        if ("电商用户".equals(datas)){
            String life = ",\"lifeCycle\": \"电商用户\"";
            queryCondition = queryCondition.substring(0,queryCondition.length()-life.length());
            queryCondition +="}";
        }
        //查询用户组的查询条件
        Gson gson = new Gson();
        Map<String, List<ScreeningConditionsVO>> gsonMap = gson.fromJson(queryCondition, new TypeToken<Map<String, List<ScreeningConditionsVO>>>() {
        }.getType());
        List<ScreeningConditionsVO> scList = new ArrayList<>();
        gsonMap.forEach((key, val) -> scList.addAll(val));
        Map<String, List<String>> nModels = new HashMap<>();
        List<String> sexList = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
        List<String> lifeList = new ArrayList<>();
        Map<String, List<String>> tagIdMap = new HashMap<>();
        Map<String, List<String>> activeLists = new HashMap<>();
        Map<String, List<String>> loyaltyLists = new HashMap<>();
        Map<String, List<String>> viscosityVals = new HashMap<>();
        scList.forEach(screeningConditionsVO -> {
            List<String> activeList = new ArrayList<String>();
            List<String> loyaltyList = new ArrayList<String>();
            List<String> viscosityVal = new ArrayList<String>();
            /***************如果是数据值型***********************/
            if (MapCacheManager.getNumberModelValue(screeningConditionsVO.getClassId())) {
                List<String> nModelsList = screeningConditionsVO.getValue()
                        .stream()
                        .map(tagsVO -> tagsVO.getName())
                        .collect(Collectors.toList());
                nModels.put(screeningConditionsVO.getClassId(), nModelsList);
            }
            /****************如果是性别数据*******************/
            else if (MapCacheManager.getSexNameValue(screeningConditionsVO.getClassId())) {
                screeningConditionsVO.getValue().forEach(sex -> sexList.add(sex.getName()));
            }
            /****************如果是城市数据*******************/
            else if (MapCacheManager.getProvinceValue(screeningConditionsVO.getClassId())) {
                screeningConditionsVO.getValue().forEach(city -> cityList.add(city.getName()));
            }
            /****************如果是活跃度*******************************/
            else if(MapCacheManager.getActiveValue(screeningConditionsVO.getClassId())){
                screeningConditionsVO.getValue().forEach(active->activeList.add(active.getName()));
                activeLists.put(screeningConditionsVO.getClassId(),activeList);
            }
            /******************如果是用户价值*********************************/
            else if (MapCacheManager.getloyaltyValue(screeningConditionsVO.getClassId())){
                screeningConditionsVO.getValue().forEach(loyalty->loyaltyList.add(loyalty.getName()));
                loyaltyLists.put(screeningConditionsVO.getClassId(),loyaltyList);
            }
            /*******************如果是用户粘度****************************************/
            else if (MapCacheManager.getviscosityValue(screeningConditionsVO.getClassId())){
                screeningConditionsVO.getValue().forEach(viscosity->viscosityVal.add(viscosity.getName()));
                viscosityVals.put(screeningConditionsVO.getClassId(),viscosityVal);
            }
            /****************如果是生命周期数据****************/
            else if (MapCacheManager.getLifeCycleValue(screeningConditionsVO.getClassId())) {
                screeningConditionsVO.getValue().forEach(lifeCycle -> lifeList.add(lifeCycle.getName()));
            }
            /*****************如果是tag类型*******************************/
            else {
                List<String> tagIdList = screeningConditionsVO.getValue()
                        .stream()
                        .map(tagIds->tagIds.getId())
                        .collect(Collectors.toList());
                tagIdMap.put(screeningConditionsVO.getClassId(), tagIdList);
            }
        });
        /********属性集合***************/
        List<String> propertyList = new ArrayList<>();
        /**********边集合***************/
        List<String> pairList = new ArrayList<>();
        String pairString = null;
        String propertyString = null;
        if (null != tagIdMap && tagIdMap.size() > 0) {
            pairList.add(findTagsJson(tagIdMap));
        }
        if (null != nModels && nModels.size() > 0) {
            pairList.add(getNumericalModelJson(nModels));
        }
        if (null != sexList && sexList.size() > 0) {
            propertyList.add(getSexJson(sexList));
        }
        if (null != cityList && cityList.size() > 0) {
            if ("电商用户".equals(datas)){
                List<String> list = new ArrayList<>();
                for (String city:cityList){
                    if ("台湾".equals(city)){
                        list.add("164791");
                    }
                    if ("香港".equals(city)){
                        list.add("164790");
                    }
                    if ("澳门".equals(city)){
                        list.add("164789");
                    }
                    if ("重庆".equals(city)){
                        list.add("164788");
                    }
                    if ("浙江".equals(city)){
                        list.add("164787");
                    }
                    if ("云南".equals(city)){
                        list.add("164786");
                    }
                    if ("新疆".equals(city)){
                        list.add("164785");
                    }
                    if ("西藏".equals(city)){
                        list.add("164784");
                    }
                    if ("天津".equals(city)){
                        list.add("164783");
                    }
                    if ("四川".equals(city)){
                        list.add("164782");
                    }
                    if ("上海".equals(city)){
                        list.add("164781");
                    }
                    if ("陕西".equals(city)){
                        list.add("164780");
                    }
                    if ("山西".equals(city)){
                        list.add("164779");
                    }
                    if ("山东".equals(city)){
                        list.add("164778");
                    }
                    if ("青海".equals(city)){
                        list.add("164777");
                    }
                    if ("宁夏".equals(city)){
                        list.add("164776");
                    }
                    if ("内蒙古".equals(city)){
                        list.add("164775");
                    }
                    if ("辽宁".equals(city)){
                        list.add("164774");
                    }
                    if ("江西".equals(city)){
                        list.add("164773");
                    }
                    if ("江苏".equals(city)){
                        list.add("164772");
                    }
                    if ("吉林".equals(city)){
                        list.add("164771");
                    }
                    if ("湖南".equals(city)){
                        list.add("164770");
                    }
                    if ("湖北".equals(city)){
                        list.add("164769");
                    }
                    if ("黑龙江".equals(city)){
                        list.add("164768");
                    }
                    if ("河南".equals(city)){
                        list.add("164767");
                    }
                    if ("河北".equals(city)){
                        list.add("164766");
                    }
                    if ("海南".equals(city)){
                        list.add("164765");
                    }
                    if ("贵州".equals(city)){
                        list.add("164764");
                    }
                    if ("广西".equals(city)){
                        list.add("164763");
                    }
                    if ("广东".equals(city)){
                        list.add("164762");
                    }
                    if ("甘肃".equals(city)){
                        list.add("164761");
                    }
                    if ("福建".equals(city)){
                        list.add("164760");
                    }
                    if ("北京".equals(city)){
                        list.add("164759");
                    }
                    if ("安徽".equals(city)){
                        list.add("164758");
                    }
                }
                pairList.add(getProvinceJson(list));
            }else {
                propertyList.add(getPrinceJson(cityList, "province"));
            }
        }
        if (null !=activeLists && activeLists.size()>0){
            propertyList.add(getPropertyJson(activeLists,"active_val"));
        }
        if (null !=loyaltyLists && loyaltyLists.size()>0){
            propertyList.add(getPropertyJson(loyaltyLists,"loyalty_val"));
        }
        if (null !=viscosityVals && viscosityVals.size()>0){
            propertyList.add(getPropertyJson(viscosityVals,"viscosity_val"));
        }
        if (null != lifeList && lifeList.size() > 0) {
            propertyList.add(getLifeJson(lifeList));
        }
        if ("电商用户".equals(datas)){
            propertyList.add("{\n" +
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
                    "        }");
        }
        if (null != classId) {
            pairList.add(findClassJson(Arrays.asList(classId)));
        }
        if (!"电商用户".equals(datas)){
            pairList.add("{\n" +
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
                    "        }");
        }
        /***********子集合(最后需要拼接的集合)*****************/
        List<String> childString = new ArrayList<>();
        /******1.边*********/
        if (pairList.size() > 0) {
//            pairString = "{\n" +
//                    "            \"query_type\": \"and\",\n" +
//                    "            \"queryconditionArr\": [" + String.join(",", pairList) + "]}";
            pairString = "{\n" +
                    "    \"query_type\": \"or\",\n" +
                    "    \"result_type\": \"user\",\n" +
                    "    \"queryconditionArr\": ["+ String.join(",", pairList) +"]\n" +
                    "}";
        }
        /*******2.属性**********/
        if (propertyList.size() > 0) {
//            propertyString = "{\n" +
//                    "            \"query_type\": \"and\",\n" +
//                    "            \"queryconditionArr\": [" + String.join(",", propertyList) + "]}";
            propertyString = "{\n" +
                    "    \"query_type\": \"and\",\n" +
                    "    \"result_type\": \"user\",\n" +
                    "    \"queryconditionArr\": [\n" +
                    "       "+String.join(",", propertyList)+" \n" +
                    "    ]\n" +
                    "}";
        }
//        /********3.固定边*************/
//        childString.add(" {\n" +
//                "            \"query_type\": \"pair\",\n" +
//                "            \"queryconditionArr\": [\n" +
//                "                {\n" +
//                "                    \"element_name\": \"appuser_user\",\n" +
//                "                    \"up_value\": \"172205760\",\n" +
//                "                    \"down_value\": \"172205760\"\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }");
//        return "{\n" +
//                "    \"query_type\": \"and\",\n" +
//                "    \"result_type\": \"user\",\n" +
//                "    \"queryconditionArr\": [\n" + String.join(",", childString) + "]}";
        if (null==propertyString){
            propertyString = "";
        }
        Map<String,String> map = new HashMap<>();
        map.put("propertyCondition",propertyString);
        map.put("tagCondition",pairString);
        return map;
    }
    /**
     * 拼接活跃度和用户价值和用户粘度
     * @param nModels
     * @return
     */
    private static String getPropertyJson(Map<String, List<String>> nModels,String propertyName) {
        List<String> nModelJson = new ArrayList<>();
        nModels.forEach((key, val) -> val.forEach(list -> {
            String[] nModelValue = list.split("-");
            if (nModelValue.length > 1) {
                nModelJson.add("{" +
                        "                    \"element_type\": \"vertex\"," +
                        "                    \"element_name\": \"user\"," +
                        "                    \"propertyname\": \""+propertyName+"\"," +
                        "                    \"up_value\": \"" + nModelValue[1] + "\"," +
                        "                    \"down_value\": \"" + nModelValue[0] + "\"" +
                        "                }");
            }
        }));
        String coinJson = String.join(",", nModelJson);
        String returnJson = "{" +
                "            \"query_type\": \"or\"," +
                "            \"queryconditionArr\": [" + coinJson +
                "            ]" +
                "        }";
        return String.join(",", returnJson);
    }
    /**
     * 查询性别下男，女，未知（sex(性别) 0（未知）、1（男）、2（女））
     *
     * @param sexs
     * @return
     */
    private static String getSexJson(List<String> sexs) {
        List<String> sexJson = new ArrayList<>();
        sexs.forEach(sex -> sexJson.add("{" +
                "                    \"element_type\": \"vertex\"," +
                "                    \"element_name\": \"user\"," +
                "                    \"propertyname\": \"sex\"," +
                "                    \"up_value\": \"" + MapCacheManager.getSexValue(sex) + "\"," +
                "                    \"down_value\": \"" + MapCacheManager.getSexValue(sex) + "\"" +
                "                }"));
        String coinJson = String.join(",", sexJson);
        String returnJson = "{" +
                "            \"query_type\": \"or\"," +
                "            \"queryconditionArr\": [" + coinJson +
                "            ]" +
                "        }";
        return returnJson;
    }

    /**
     * 拼接省份数据
     *
     * @param provinces
     * @return
     */
    private static String getPrinceJson(List<String> provinces, String propertyName) {
        List<String> cityJson = new ArrayList<>();
        provinces.forEach(province -> cityJson.add("{" +
                "                    \"element_type\": \"vertex\"," +
                "                    \"element_name\": \"user\"," +
                "                    \"propertyname\": \"" + propertyName + "\"," +
                "                    \"up_value\": \"" + province + "\"," +
                "                    \"down_value\": \"" + province + "\"" +
                "                }"));

        String coinJson = String.join(",", cityJson);
        String returnJson = "{" +
                "            \"query_type\": \"or\"," +
                "            \"queryconditionArr\": [" + coinJson +
                "            ]" +
                "        }";
        return returnJson;
    }

    private static String getProvinceJson(List<String> list){
        List<String> province = list.stream().map(lists -> " {\n" +
                "            \"element_name\": \"tag_config_user\",\n" +
                "            \"element_type\": \"edge\",\n" +
                "            \"select_tag_id\": \""+lists+"\",\n" +
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
                "        }").collect(Collectors.toList());
        return String.join(",", province);

    }


    /**
     * 拼接生命周期数据
     *
     * @param lifeCycles
     * @return
     */
    private static String getLifeJson(List<String> lifeCycles) {
        List<String> lifeJson = new ArrayList<>();
        lifeCycles.forEach(lifeCycle -> lifeJson.add("{" +
                "                    \"element_type\": \"vertex\"," +
                "                    \"element_name\": \"user\"," +
                "                    \"propertyname\": \"life_cycle\"," +
                "                    \"up_value\": \"" + lifeCycle + "\"," +
                "                    \"down_value\": \"" + lifeCycle + "\"" +
                "                }"));

        String coinJson = String.join(",", lifeJson);
        String returnJson = "{" +
                "            \"query_type\": \"or\"," +
                "            \"queryconditionArr\": [" + coinJson +
                "            ]" +
                "        }";
        return returnJson;
    }


    /**
     * 组装带有tagid查询的json
     *
     * @param tagsIds
     * @return
     */
    private static String findTagsJson(Map<String, List<String>> tagsIds) {
        List<String> allTags = new ArrayList<>();
        tagsIds.forEach((key, val) -> {
//            List<String> tagsJson = val.stream().map(tagId -> "{" +
//                    "            \"query_type\": \"pair\"," +
//                    "            \"queryconditionArr\": [" +
//                    "                {" +
//                    "                    \"propertyname\": \"appid\"," +
//                    "                    \"up_value\": \"172205760\"," +
//                    "                    \"down_value\": \"172205760\"" +
//                    "                }," +
//                    "                {" +
//                    "                    \"element_name\": \"tag_config_user\"," +
//                    "                    \"up_value\": \"" + tagId + "\"," +
//                    "                    \"down_value\": \"" + tagId + "\"" +
//                    "                }," +
//                    "                {" +
//                    "                    \"propertyname\": \"log10_score\"," +
//                    "                    \"up_value\": \"1\"," +
//                    "                    \"down_value\": \"-1\"" +
//                    "                }" +
//                    "            ]" +
//                    "        }").collect(Collectors.toList());
            List<String> tagsJson = val.stream().map(tagId -> "{\n" +
                    "            \"element_name\": \"tag_config_user\",\n" +
                    "            \"element_type\": \"edge\",\n" +
                    "            \"select_tag_id\": \""+tagId+"\",\n" +
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
                    "        }").collect(Collectors.toList());
            String coinJson = String.join(",", tagsJson);
//            String returnJson = "{" +
//                    "            \"query_type\": \"or\"," +
//                    "            \"queryconditionArr\": [" + coinJson +
//                    "            ]" +
//                    "        }";
            allTags.add(coinJson);
        });
        return String.join(",", allTags);
    }

    /**
     * 组装带有类别id查询的json
     *
     * @param classIds
     * @return
     */
    private static String findClassJson(List<String> classIds) {
//        List<String> classJson = classIds.stream().map(classId -> "{" +
//                "            \"query_type\": \"pair\"," +
//                "            \"queryconditionArr\": [" +
//                "                {" +
//                "                    \"propertyname\": \"appid\"," +
//                "                    \"up_value\": \"172205760\"," +
//                "                    \"down_value\": \"172205760\"" +
//                "                }," +
//                "                {" +
//                "                    \"element_name\": \"tag_class_relation_user\"," +
//                "                    \"up_value\": \"" + classId + "\"," +
//                "                    \"down_value\": \"" + classId + "\"" +
//                "                }," +
//                "                {" +
//                "                    \"propertyname\": \"log10_score\"," +
//                "                    \"up_value\": \"1\"," +
//                "                    \"down_value\": \"-1\"" +
//                "                }" +
//                "            ]" +
//                "        }").collect(Collectors.toList());
        List<String> classJson = classIds.stream().map(classId -> "{\n" +
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
                "        }").collect(Collectors.toList());
        return String.join(",", classJson);
    }

    /**
     * 数值型
     *
     * @param nModels
     * @return
     */
    private static String getNumericalModelJson(Map<String, List<String>> nModels) {
        List<String> allNodeJson = new ArrayList<>();
        nModels.forEach((key, val) ->{
            List<String> nModelJson = new ArrayList<>();
//            val.forEach(list -> {
//                String[] nModelValue = list.split("-");
//                if (nModelValue.length > 1) {
//                    nModelJson.add("{" +
//                            "            \"query_type\": \"pair\"," +
//                            "            \"queryconditionArr\": [" +
//                            "                {" +
//                            "                    \"propertyname\": \"appid\"," +
//                            "                    \"up_value\": \"172205760\"," +
//                            "                    \"down_value\": \"172205760\"" +
//                            "                }," +
//                            "                {" +
//                            "                    \"element_name\": \"tag_config_user\"," +
//                            "                    \"up_value\": \"" + key + "\"," +
//                            "                    \"down_value\": \"" + key + "\"" +
//                            "                }," +
//                            "                {" +
//                            "                    \"propertyname\": \"log10_score\"," +
//                            "                    \"up_value\": \"" + nModelValue[1] + "\"," +
//                            "                    \"down_value\": \"" + nModelValue[0] + "\"" +
//                            "                }" +
//                            "            ]" +
//                            "        }");
//
//                }
//            });
            val.forEach(list -> {
                String[] nModelValue = list.split("-");
                if (nModelValue.length > 1) {
                    nModelJson.add(" {\n" +
                            "                        \"propertyname\": \"log10_score\",\n" +
                            "                        \"up_value\": \""+nModelValue[1]+"\",\n" +
                            "                        \"down_value\": \""+nModelValue[0]+"\"\n" +
                            "                    }");

                }
            });
            String coinJson = String.join(",", nModelJson);
//            String returnJson = "{" +
//                    "            \"query_type\": \"or\"," +
//                    "            \"queryconditionArr\": [" + coinJson +
//                    "            ]" +
//                    "        }";
            String returnJson = "{\n" +
                    "            \"element_name\": \"tag_config_user\",\n" +
                    "            \"element_type\": \"edge\",\n" +
                    "            \"select_tag_id\": \"1\",\n" +
                    "            \"field_condition\": {\n" +
                    "                \"query_type\": \"and\",\n" +
                    "                \"queryconditionArr\": [\n" +
                    "                    {\n" +
                    "                        \"propertyname\": \"appid\",\n" +
                    "                        \"up_value\": \"172139920\",\n" +
                    "                        \"down_value\": \"172139920\"\n" +
                    "                    },\n" +
                    "              "+coinJson+"  ]\n" +
                    "            }\n" +
                    "        }";
            allNodeJson.add(returnJson);
        });
        return String.join(",", allNodeJson);
    }

}