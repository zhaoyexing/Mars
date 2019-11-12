package com.st.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyx on 2016/11/21.
 */
public class ProductPreferenceUtils {

    /**
     * 获取所有品牌集合
     *
     * @return
     */
    public static List<String> getBandClassIds() {
        List<String> classIds = new ArrayList<>();
        classIds.add("200612");
        classIds.add("200620");
        classIds.add("200624");
        classIds.add("200628");
        classIds.add("200632");
        return classIds;
    }

    /**
     * 获取所有产品特征集合
     *
     * @return
     */
    public static List<String> getProductClassIds() {
        List<String> classIds = new ArrayList<>();
        classIds.add("200613");
        classIds.add("200621");
        classIds.add("200625");
        classIds.add("200629");
        classIds.add("200633");
        return classIds;
    }

    /**
     * 获取所有包装特征
     *
     * @return
     */
    public static List<String> getPackingClassIds() {
        List<String> classIds = new ArrayList<>();
        classIds.add("200615");
        classIds.add("200623");
        classIds.add("200627");
        classIds.add("200631");
        classIds.add("200635");
        return classIds;
    }

    /**
     * 获取所有口味特征集合
     *
     * @return
     */
    public static  List<String> getFlavorClassIds() {
        List<String> classIds = new ArrayList<>();
        classIds.add("200614");
        classIds.add("200622");
        classIds.add("200626");
        classIds.add("200630");
        classIds.add("200634");
        return classIds;
    }
    /**
     * 品牌特征
     *
     * @param pid
     * @return
     */
    public static  String getBandClassId(String pid) {
        String classId = null;
        if (pid.equals("200603")) {
            classId = "200612";
        } else if (pid.equals("200604")) {
            classId = "200620";
        } else if (pid.equals("200605")) {
            classId = "200624";
        } else if (pid.equals("200607")) {
            classId = "200628";
        } else if (pid.equals("200608")) {
            classId = "200632";
        }
        return classId;
    }



    /**
     * 产品特征
     *
     * @param pid
     * @return
     */
    public static  String getProductClassId(String pid) {
        String classId = null;
        if (pid.equals("200603")) {
            classId = "200613";
        } else if (pid.equals("200604")) {
            classId = "200621";
        } else if (pid.equals("200605")) {
            classId = "200625";
        } else if (pid.equals("200607")) {
            classId = "200629";
        } else if (pid.equals("200608")) {
            classId = "200633";
        }
        return classId;
    }


    /**
     * 口味特征
     *
     * @return
     */
    public static  String getFlavorClassId(String pid) {
        String classId = null;
        if (pid.equals("200603")) {
            classId = "200614";
        } else if (pid.equals("200604")) {
            classId = "200622";
        } else if (pid.equals("200605")) {
            classId = "200626";
        } else if (pid.equals("200607")) {
            classId = "200630";
        } else if (pid.equals("200608")) {
            classId = "200634";
        }
        return classId;
    }



    /**
     * 包装特征
     *
     * @param pid
     * @return
     */
    public static  String getPackingClassId(String pid) {
        String classId = null;
        if (pid.equals("200603")) {
            classId = "200615";
        } else if (pid.equals("200604")) {
            classId = "200623";
        } else if (pid.equals("200605")) {
            classId = "200627";
        } else if (pid.equals("200607")) {
            classId = "200631";
        } else if (pid.equals("200608")) {
            classId = "200635";
        }
        return classId;
    }
}
