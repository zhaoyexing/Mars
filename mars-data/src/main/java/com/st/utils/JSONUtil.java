package com.st.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public class JSONUtil {
    public static  <T extends Object> List<T> converBeanFormString(String json, Class<T> t)  {
        List<T> list = null;
        try {
            if (json == null || json.equals(""))
                return new ArrayList();
            JSONArray jsonArray = JSONArray.fromObject(json);
            list = (List) JSONArray.toCollection(jsonArray,
                    t.newInstance().getClass());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * json转map
     * @param json
     * @return
     */
    public static Map convertJsonToMap(String json){
        return new HashMap<>(JSONObject.fromObject(String.valueOf(JSONObject.fromObject(json).get("datas"))));
    }
    /**
     * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如：
     * {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...},
     * beansList:[{}, {}, ...]}
     * @param jsonString
     * @param clazz
     * @param map 集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" : Bean.class)
     * @return
     */
    public static List getVOList(String jsonString, Class clazz, Map<String,Class> map){
        JSONArray array = JSONArray.fromObject(jsonString);
        List list = new ArrayList();
        for(Iterator iter = array.iterator(); iter.hasNext();){
            JSONObject jsonObject = (JSONObject)iter.next();
            list.add(JSONObject.toBean(jsonObject, clazz, map));
        }
        return list;
    }
}

