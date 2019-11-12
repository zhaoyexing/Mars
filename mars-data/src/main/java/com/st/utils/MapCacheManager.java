package com.st.utils;

import java.util.*;

/**
 * 缓存标签属性值
 * Created by zhaoyx on 2016/11/15.
 */
public class MapCacheManager {
    private static Map<String, String> sexNameMap = new HashMap<>();
    private static Map<String, String> provinceNameMap = new HashMap<>();
    private static Map<String, String> lifeCycleNameMap = new HashMap<>();
    private static Map<String, String> sexValueMap = new HashMap<>();
    private static Map<String, String> activeNameMap = new HashMap<>();
    private static Map<String, String> loyaltyNameMap = new HashMap<>();
    private static Map<String, String> spreadNameMap = new HashMap<>();
    private static Map<String, String> viscosityNameMap = new HashMap<>();
    private static List<String> NumberModelList = new ArrayList<>();

    static {
        /*********添加属性************************/
        sexNameMap.put("195096", "性别");
        provinceNameMap.put("195108", "省份");
        lifeCycleNameMap.put("11", "生命周期");
        activeNameMap.put("1","互动活跃度");
        loyaltyNameMap.put("157243","用户价值");
        viscosityNameMap.put("3","用户粘度");

        /*********添加数据值型数据*****************/
        NumberModelList.add("164792");
        /************男，女，未知******************/
        sexValueMap.put("未知", "0");
        sexValueMap.put("男", "1");
        sexValueMap.put("女", "2");
    }

    /**
     * 通过classId判断是否是性别，城市，存在，存在，返回true,不存在，返回false
     *
     * @param key
     * @return
     */
    public static boolean getSexNameValue(String key) {
        String value = sexNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }

    /**
     * 通过classId判断是否是性别，城市，存在，存在，返回true,不存在，返回false
     *
     * @param key
     * @return
     */
    public static boolean getProvinceValue(String key) {
        String value = provinceNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }

    public static boolean getloyaltyValue(String key) {
        String value = loyaltyNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }
    public static boolean getActiveValue(String key) {
        String value = activeNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }
    public static boolean getviscosityValue(String key) {
        String value = viscosityNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }
    public static boolean getSpreadValue(String key) {
        String value = spreadNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }

    /**
     * 通过classId判断是否是性别，城市，存在，存在，返回true,不存在，返回false
     *
     * @param key
     * @return
     */
    public static boolean getLifeCycleValue(String key) {
        String value = lifeCycleNameMap.get(key);
        if (null != value)
            return true;
        return false;
    }

    /**
     * 通过classid判断是否是数值类型，存在，存在，返回true,不存在，返回false
     *
     * @param key
     * @return
     */
    public static boolean getNumberModelValue(String key) {
        if (NumberModelList.contains(key))
            return true;
        return false;
    }

    public static String getSexValue(String tagId) {
        return sexValueMap.get(tagId);
    }

}
