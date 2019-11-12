package com.st.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyx on 2016/10/27.
 */
public class MapUtils {
    public static Map<String, Object> toMap(String str) {
        Gson gson = new Gson();
        Map<String, Object> gsonMap = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
        return gsonMap;
    }
    public static void getaVoid(Map<String, Object> map, List<Map<String, Object>> list) {
        if (null != map && map.size()>0){
            map.forEach((key,value)->{
                Map<String,Object> mapTmp = new HashMap<String, Object>();
                mapTmp.put("name",key);
                mapTmp.put("value",value);
                list.add(mapTmp);
            });
        }
    }
}
