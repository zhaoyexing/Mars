package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.model.MarsTagsTmp;
import com.st.model.OpResult;
import com.st.service.SinglePersonService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.ProductPreferenceUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * 个人用户画像接口
 * Created by zhaoyx on 2016/9/19.
 */
@Service
public class SinglePersonServiceImpl implements SinglePersonService {

    @Value("${user.draw.url}")
    private String urlValue;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;

    /**
     * 获取个人用户画像
     *
     * @param startDate
     * @param endDate
     * @param uid
     * @param tagId
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getUserPortrait(String startDate, String endDate, String uid, String tagId) {
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            //取开始时间的时间戮
            long startTime = DateUtil.parse(startDate).getTime() / 1000;
            //取结束日期的秒级时间戮
            long endTime = DateUtil.parse(endDate).getTime() / 1000;
            //开始日期->循环开始的时间点
            LocalDate start;
            //开始日期->循环结束的时间点
            LocalDate end;
            //取个人数据返回
            String userPortraitJson = HttpClientFactory.requestGet(urlValue + "searchTagRes&uid=" + uid + "&startTime=" + startTime + "&endTime=" + endTime + "&tagIds=" + tagId);
            JSONObject jsonObjectProperty = JSONObject.fromObject(userPortraitJson);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            JSONObject jsonObjectClass = JSONObject.fromObject(tagStr);
            //将json数据转换为map
            for (Iterator iter = jsonObjectClass.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                JSONObject jsonObjectTag = JSONObject.fromObject(jsonObjectClass.get(key));
                map = toMap(String.valueOf(jsonObjectTag));
            }
            SupplementaryData(startDate, endDate, map, list);
            //根据日期进行排序
            list.sort((map1, map2) -> (String.valueOf(map1.get("name")).compareTo(String.valueOf(map2.get("name")))));
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_PORTRAIT_EXCEPTION);
    }



    /**
     * 个人 产品喜好
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param uid       粉丝uid
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getUserProductPreference(String startDate, String endDate, String uid) {
        try {
            //取开始时间的时间戮
            long startTime = DateUtil.parse(startDate).getTime() / 1000;
            //取结束日期的秒级时间戮
            long endTime = DateUtil.parse(endDate).getTime() / 1000;
            List<String> productClassIds = ProductPreferenceUtils.getProductClassIds();
            List<MarsTagsTmp> tagsTmpLists= new ArrayList<>();
            for (String classId : productClassIds){
                tagsTmpLists.addAll(marsTagsTmpMapper.getMarsTagTmpList(classId));
            }
            List<String> list = new ArrayList<>();
            tagsTmpLists.forEach(tagsTmpList ->list.add(String.valueOf(tagsTmpList.getTagId())));
            String coinString = String.join(",",list);
            System.out.println(coinString);
            List<Map<String, Object>> returnList = new ArrayList<>();
            String returnJson = HttpClientFactory.requestGet(urlValue+"searchTagRes&uid="+uid+"&startTime="+startTime+"&endTime="+endTime+"&tagIds="+coinString);
            JSONObject jsonObject = JSONObject.fromObject(returnJson);
            String datas = String.valueOf(jsonObject.get("datas"));
            Map<String, String> tagMap = new Gson().fromJson(datas, new TypeToken<Map<String, String>>() {}.getType());
            List<Map<String,Object>> lists = new ArrayList<>();
            Map<String,Object> mapes = new HashMap<>();
            for (Map.Entry<String,String> entry:tagMap.entrySet()){
                String keys = entry.getKey();
                String values = entry.getValue();
                Integer count = 0;
                Map<String, String> tagMap1 = new Gson().fromJson(values, new TypeToken<Map<String, String>>() {}.getType());
                if(tagMap1.size()>1){
                    for (Map.Entry<String,String> entry1:tagMap1.entrySet()){
                        count += Integer.parseInt(entry.getValue());
                    }
                }else {
                    count = Integer.parseInt(entry.getValue());
                }
                mapes.put("id",keys);
                mapes.put("total",count);
                lists.add(mapes);
            }
            lists.sort((mapes1, mapes2) -> (String.valueOf(mapes1.get("total")).compareTo(String.valueOf(mapes2.get("total")))));
            Map<String,Object> map = new HashMap<>();
            List<Map<String,Object>> listes = new ArrayList<>();
            for (int i=0;i<lists.size();i++){
                if (i<5){
                    map.put("id",lists.get(i).get("id"));
                    map.put("count",lists.get(i).get("count"));
                    map.put("name",marsTagsTmpMapper.getInteractName(Integer.parseInt(String.valueOf(lists.get(i).get("id")))));
                    listes.add(map);
                }
            }


//            List<Map<String, Object>> list = new ArrayList<>();
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", "唇妆");
//            Map<String, Object> maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-01");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-02");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-03");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-04");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            map.put("value", list);
//            returnList.add(map);
//            list = new ArrayList<>();
//            map = new HashMap<>();
//            map.put("name", "底妆");
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-01");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-02");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-03");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-04");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            map.put("value", list);
//            returnList.add(map);
//            list = new ArrayList<>();
//            map = new HashMap<>();
//            map.put("name", "眼妆");
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-01");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-02");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-03");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            maptemp = new HashMap<>();
//            maptemp.put("date", "2016-09-04");
//            maptemp.put("count", 2344);
//            list.add(maptemp);
//            map.put("value", list);
//            returnList.add(map);
            return OpResult.createSucResult(listes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_PORTRAIT_EXCEPTION);
    }

    public Map<String, Object> toMap(String str) {
        Gson gson = new Gson();
        Map<String, Object> gsonMap = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
        return gsonMap;
    }

    /**
     * 补数据，因为返回的数据，有的天没有时间戮，需要补全
     * @param startDate
     * @param endDate
     * @param map
     * @param list
     */
    private void SupplementaryData(String startDate, String endDate, Map<String, Object> map, List<Map<String, Object>> list) {
        LocalDate start = LocalDate.parse(startDate);//开始日期
        LocalDate end =  LocalDate.parse(endDate);//结束日期;
        end = end.minusDays(1L);
        if (null != map && map.size() > 0) {
            //将map中数据存入list
            map.forEach((key, value) -> {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("name", DateUtil.timeStamp2Date(key, "yyyy-MM-dd"));
                mapTmp.put("value", value);
                list.add(mapTmp);
            });
            //构建数据，当map中没有对应日期，需要新增当天的数据
            while (start.isBefore(end.plusDays(1))) {
                //因lambda必须使用final，所以此处需要给start重新赋值给finalStart.
                final LocalDate finalStart = start;
                //如果list里没有包含当前日期的数据，加入数据
                if (!list.stream().anyMatch(map1 -> map1.get("name").equals(finalStart.toString()))) {
                    Map<String, Object> mapTmp = new HashMap<>();
                    mapTmp.put("name", start.toString());
                    mapTmp.put("value", 0);
                    list.add(mapTmp);
                }
                //日期加一天
                start = start.plusDays(1);
            }
        } else {
            //如果返回没有数据,补全所有从开始日期到结束日期的数据,活跃度为0
            while (start.isBefore(end.plusDays(1))) {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("name", start.toString());
                mapTmp.put("value", 0);
                list.add(mapTmp);
                start = start.plusDays(1); //日期加一天
            }

        }
    }

}
