package com.st.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2016/9/19.
 */
public class StringUtils {

    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
    /**
     * 判断字符串是否为空 如空返回True,否则返回false
     *
     * @return
     */
    public static <T> boolean isNull(T t) {

        if (null == t || "".equals(t) || "null".equals(t) || "undefined".equals(t) || "{}".equals(t))
            return true;
        else
            return false;
    }

    /**
     * 判断一个字符串是不是一个合法的日期格式
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = false;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy-MM-dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007-02-29会被接受，并转换成2007-03-01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = true;
        }
        return convertSuccess;
    }


    /**
     * json字符串转换为map
     * @param jsonStr
     * @throws IOException
     */
    public static Map<String,String> jsonStrToMap(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> paramMap = null;
        try {
            paramMap = mapper.readValue(jsonStr,Map.class);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return paramMap;
    }
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map map) {
        if (map == null || map.isEmpty()) {
            return new TreeMap<>();
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }


    /**
     * 从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            logger.info("request URI::{}",request.getRequestURI());
            logger.info("request parameter:::::::::::::name:{},value:{}",name,value);
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * map对象转换为json字符串
     * @param strMap
     * @return
     */
    public static String mapToJsonStr(Map strMap){
        //map to json
        ObjectMapper mapper = new ObjectMapper();
        String jsonMap=null;
        try {
            jsonMap = mapper.writeValueAsString(strMap);
        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        return jsonMap;
    }


}
