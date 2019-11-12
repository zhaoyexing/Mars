package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsActivityCampaginMapper;
import com.st.model.ButtonList;
import com.st.model.MarsActivityCampagin;
import com.st.model.OpResult;
import com.st.service.CampaginMonitorService;
import com.st.utils.*;
import com.st.vo.ButtonVO;
import com.st.vo.CampaginActivityNameVO;
import com.st.vo.UserAnalysis;
import com.st.vo.UserAnalysisVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * Created by zhaoyx on 2016/10/26.
 */
@Service
public class CampaginMonitorServiceImpl implements CampaginMonitorService {

    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${campagin.activity.url}")
    private String activityUrlValue;
    @Autowired
    private MarsActivityCampaginMapper marsActivityCampaginMapper;

    /**
     * 获取页面浏览次数/浏览人数
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getViewVisitorsNumber(String activityName, String startDate, String endDate) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        try {
            List<MarsActivityCampagin> urlList = marsActivityCampaginMapper.getUrlByActivityName(activityName);
            List<Map<String, Object>> uvList = new ArrayList<>();//访问人数
            List<Map<String, Object>> pvList = new ArrayList<>();//访问次数
            Map<String, Object> map = new HashMap<>();
//            for (MarsActivityCampagin m : urlList) {
//                collectionResultPageByMonth(uvList, m.getPageUrl(), "searchUv", startDate, endDate);
//                collectionResultPageByMonth(pvList, m.getPageUrl(), "searchPv", startDate, endDate);
//            }
            for (MarsActivityCampagin m : urlList){
                collectionResultPageByDay(uvList, m.getPageUrl(), "searchUv", startDate, endDate);
                collectionResultPageByDay(pvList, m.getPageUrl(), "searchPv", startDate, endDate);
            }
            map.put("name", "浏览次数");
            map.put("value", pvList);
            returnList.add(map);
            map = new HashMap<>();
            map.put("name", "浏览人数");
            map.put("value", uvList);
            returnList.add(map);
            return OpResult.createSucResult(returnList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }


    /**
     * 活动参与用户数
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getActivityParticipationNumber(String activityName, String startDate, String endDate) {
        try {
            List<Map<String, Object>> returnList = new ArrayList<>();
            //获取url列表
            List<MarsActivityCampagin> urlList = marsActivityCampaginMapper.getUrlByActivityName(activityName);
            if (null != urlList && urlList.size()>0){
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> uvList = new ArrayList<>();
                List<Map<String, Object>> pvList = new ArrayList<>();
//            for (MarsActivityCampagin m : urlList) {
//                collectionResultByMonth(uvList, m.getPageUrl(), "0", "searchUv", startDate, endDate);
//                collectionResultByMonth(pvList, m.getPageUrl(), "0", "searchPv", startDate, endDate);
//            }
                for (MarsActivityCampagin m : urlList) {
                    collectionResultByDay(uvList, m.getPageUrl(), "0", "searchUv", startDate, endDate);
                    collectionResultByDay(pvList, m.getPageUrl(), "0", "searchPv", startDate, endDate);
                }
                uvList.sort((o1, o2) -> String.valueOf(o1.get("date")).compareTo(String.valueOf(o2.get("date"))));
                pvList.sort((o1, o2) -> String.valueOf(o1.get("date")).compareTo(String.valueOf(o2.get("date"))));
                map.put("name", "访问用户数");
                map.put("value", uvList);
                returnList.add(map);
                map = new HashMap<>();
                map.put("name", "访问次数");
                map.put("value", pvList);
                returnList.add(map);
            }
            return OpResult.createSucResult(returnList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }


    /**
     * Button点击次数/点击人数
     *
     * @return
     */
    @Override
    public  OpResult<Map<String,Object>> getButtonNumber(String activityName, String startDate, String endDate,int pageNum,int pageSize) {
        List<ButtonVO> returnList = new ArrayList<>();
        Map<String, Object> maptemp = new HashMap<>();

        try {
            //获取url列表
            List<MarsActivityCampagin> urlList = marsActivityCampaginMapper.getUrlByActivityName(activityName);
            if (null != urlList && urlList.size()>0){
                //取开始时间的时间戮
                long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
                //取结束日期的秒级时间戮
                long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
                Map<String, String> mapActionId = new HashMap<>();
                //通过url获取actionid集合
                for (MarsActivityCampagin m : urlList) {
                    //获取按钮列表
                    String buttonJson = HttpClientFactory.requestGet(activityUrlValue + m.getPageUrl());
                    if (!Objects.equals(buttonJson, "{}")) {
                        JSONArray jsonArray = JSONArray.fromObject(buttonJson);
                        List<ButtonList> lists = (List<ButtonList>) jsonArray.toCollection(jsonArray, ButtonList.class);
                        for (ButtonList buttonList : lists){
                            ButtonVO buttonVO = new ButtonVO();
                            String uvJson = HttpClientFactory.requestGet(urlValue + "searchUv&itemid=" + buttonList.getItemid() + "&actionid=6&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
                            String pvJson = HttpClientFactory.requestGet(urlValue + "searchPv&itemid=" + buttonList.getItemid() + "&actionid=6&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
                            Map<String, Integer> uvMap = JSONUtil.convertJsonToMap(uvJson);
                            Map<String, Integer> pvMap = JSONUtil.convertJsonToMap(pvJson);
                            int count = uvMap.values().stream().mapToInt(Integer::intValue).sum();//uv总数量
                            int times = pvMap.values().stream().mapToInt(Integer::intValue).sum();//pv总数量
                            buttonVO.setButtonName(buttonList.getName());
                            buttonVO.setClickNumber(count);
                            buttonVO.setClickTimes(times);
                            returnList.add(buttonVO);
                        }
                    }
                }
            }
            //取总人数
            if (returnList.size()>0){
                maptemp.put("total", returnList.size());
                returnList.sort((o1, o2) -> String.valueOf(o2.getClickTimes()).compareTo(String.valueOf(o1.getClickTimes())));
                //假分页
                Pager<ButtonVO> pager = Pager.create(returnList, pageSize);
                //取用户数据集合
                maptemp.put("userlist",  pager.getPagedList(pageNum));
            }else {
                maptemp.put("total", 0);
                //取用户数据集合
                maptemp.put("userlist",  new ArrayList<>());
            }
            return OpResult.createSucResult(maptemp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_CHARACTERISTICS_EXCEPTION);
    }

    /**
     * 传播用户与非传播用户
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getCommunicationUser(String activityName, String startDate, String endDate) {
        int propagate = 0;
        int nPropagate = 0;
        int noPropagate = 0;

        String fefeer;
        String fefeers;
        //取开始时间的时间戮
        long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
        //取结束日期的秒级时间戮
        long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
        try {
            if (null !=activityName && !"".equals(activityName)){
                fefeer = HttpClientFactory.requestGet(urlValue + "searchPv&itemid=" + activityName + "&actionid=" + 8 + "&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
                fefeers = HttpClientFactory.requestGet(urlValue + "searchPv&itemid=" + activityName + "&actionid=" + 0 + "&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
                Map<String, Integer> feMap = JSONUtil.convertJsonToMap(fefeer);
                Map<String, Integer> fesMap = JSONUtil.convertJsonToMap(fefeers);
                propagate += feMap.values().stream().mapToInt(Integer::intValue).sum();
                nPropagate += fesMap.values().stream().mapToInt(Integer::intValue).sum();
                noPropagate = nPropagate - propagate;
            }
            Map<String, Object> map;
            List<Map<String, Object>> list = new ArrayList<>();
            map = new HashMap<>();
            map.put("name", "传播用户");
            map.put("count", propagate);
            list.add(map);
            map = new HashMap<>();
            map.put("name", "非传播用户");
            map.put("count", noPropagate);
            list.add(map);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    /**
     * 传播用户分析
     */
    @Override
    public OpResult<Map<String,Object>> getUserAnalysis(String activityName, String startDate, String endDate,int pageNo,int pageSize) {

        try {
            String noPage = String.valueOf((pageNo-1)*pageSize);
            String pageSizes = String.valueOf(pageSize);
            List<UserAnalysis> sisList = new LinkedList<>();
            Map<String,Object> mapes = new HashMap<>();
            //取开始时间的时间戮
            long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
            //取结束日期的秒级时间戮
            long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
                String userJson = HttpClientFactory.requestGet(urlValue + "searchDaisyList&itemid=" + activityName + "&startTime=" + startTime + "&endTime=" + endTime + "&size=200&flag=3");
                JSONObject datajson = JSONObject.fromObject(userJson);
                JSONObject detail = datajson.getJSONObject("datas");
                String count = detail.getString("total");
                JSONArray list = detail.getJSONArray("detail");
                for(int i=0;i<list.size();i++){
                    UserAnalysis sis = new UserAnalysis();
                    JSONObject info = list.getJSONObject(i);
                    String uId = info.getString("uid");
                    int retUserCount =info.getInt("retUserCount");
                    int allReadUserCount = info.getInt("allReadUserCount");
                    String userNameJson = HttpClientFactory.requestGet(urlValue + "searchFansBaseInfoByPage&uids=" + uId + "&startTime=" + startTime + "&endTime=" + endTime + "&pageSize="+noPage+"&pageRecord="+pageSizes);
                    JSONObject nameJson = JSONObject.fromObject(userNameJson);
                    JSONObject datas = nameJson.getJSONObject("datas");
                    JSONArray lists = datas.getJSONArray("detail");
                    String nickName = null;
                    if (lists.size()!=0){
                        JSONObject infos = lists.getJSONObject(0);
                         nickName = infos.getString("nick_name");
                    }
                    sis.setOriginator(uId);
                    sis.setLevelTwo(retUserCount);
                    sis.setLevelOne(allReadUserCount);
                    sis.setNickName(nickName);
                    sisList.add(sis);
                }
            sisList.sort((o1, o2) -> Integer.valueOf(o2.getLevelOne()).compareTo(Integer.valueOf(o1.getLevelOne())));
            mapes.put("list",sisList);
            mapes.put("total",count);
            return OpResult.createSucResult(mapes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }


    /**
     * 获取活动名称列表
     *
     * @return
     */
    @Override
    public OpResult<List<Map<String, String>>> getActivityNameList() {
        List<Map<String, String>> lists = new ArrayList<>();
        try {
            String activityJson =  OkHttpUtils.get("http://data.social-touch.com/api/getActivityList?appkey=172205760&appid=10001&secretkey=ca536f6151a7bf74d9b969b61c8f9412");
            JSONObject jsonObjectProperty = JSONObject.fromObject(activityJson);
            String tagStr = String.valueOf(jsonObjectProperty.get("wechat"));
            Map<Integer, CampaginActivityNameVO> map = new Gson().fromJson(tagStr, new TypeToken<Map<Integer, CampaginActivityNameVO>>() {
            }.getType());
            map.forEach((key,val)->{
                Map<String, String> mapTmp = new HashMap<>();
                mapTmp.put("name",val.getName());
                mapTmp.put("value",val.getItemkey());
                lists.add(mapTmp);
            });
            return OpResult.createSucResult(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.SEX_DISTRIBUTION_EXCEPTION);
    }

    /**
     * 封装数据
     *
     * @param list
     * @param pageUrl
     * @param methodurl
     * @throws Exception
     */
    private void collectionResultPageByMonth(List<Map<String, Object>> list, String pageUrl, String methodurl, String startDate, String endDate) throws Exception {
        //取开始时间的时间戮
        long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
        //取结束日期的秒级时间戮
        long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
        String pvJson = HttpClientFactory.requestGet(urlValue + methodurl + "&itemid=" + pageUrl + "&actionid=0&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
//        String pvJson = HttpClientFactory.requestGet("http://211.151.59.253:10091/cdapi/req?appid=35&passwd=8344202725&appkey=172205760&method=searchPv&itemid=http%3a%2f%2fwq.jd.com%2fmshop%2fgethomepage%3floginFlag%3d1%26venderid%3d1000002982%26appId%3dwx18e08e92cd265b18%26PTAG%3d17048.1.1&actionid=6&startTime=1469980800&endTime=1477642740&flag=3");
        Map<String, Integer> returnMap = JSONUtil.convertJsonToMap(pvJson);
        int count = 0;
        for (String key : returnMap.keySet()) {
            count += returnMap.get(key);
        }
        Map<String, Object> maptemp = new HashMap<>();
        maptemp.put("url", pageUrl);
        maptemp.put("count", count);
        list.add(maptemp);
    }

    private void collectionResultPageByDay(List<Map<String, Object>> list, String pageUrl, String methodurl, String startDate, String endDate) throws Exception {
        //取开始时间的时间戮
        long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
        //取结束日期的秒级时间戮
        long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
        String pvJson = HttpClientFactory.requestGet(urlValue + methodurl + "&itemid=" + pageUrl + "&actionid=0&startTime=" + startTime + "&endTime=" + endTime + "&flag=1");
        Map<String, Integer> returnMap = JSONUtil.convertJsonToMap(pvJson);
        int count = 0;
        for (String key : returnMap.keySet()) {
            count += returnMap.get(key);
        }
        Map<String, Object> maptemp = new HashMap<>();
        maptemp.put("url", pageUrl);
        maptemp.put("count", count);
        list.add(maptemp);
    }


    private void collectionResultByMonth(List<Map<String, Object>> list, String pageUrl, String actionId, String methodurl, String startDate, String endDate) throws Exception {
        //取开始时间的时间戮
        long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
        //取结束日期的秒级时间戮
        long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
        String pvJson = HttpClientFactory.requestGet(urlValue + methodurl + "&itemid=" + pageUrl + "&actionid=" + actionId + "&startTime=" + startTime + "&endTime=" + endTime + "&flag=3");
        Map<String, Integer> returnMap = JSONUtil.convertJsonToMap(pvJson);
        //以下是补数据过程
        YearMonth start = YearMonth.parse(startDate.substring(0, 7));//开始日期
        YearMonth end = YearMonth.parse(endDate.substring(0, 7));//结束日期;
        System.out.println(start.atEndOfMonth().toEpochDay());
        if (null != returnMap && returnMap.size() > 0) {

            //将map中数据存入list
            returnMap.forEach((key, value) -> {
                int tmpCount = 0;
                boolean flag = false;
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("month", DateUtil.timeStamp2Date(key, "yyyy-MM"));
                if (null != list && list.size() > 0) {
                    for (Map<String, Object> map : list) {
                        if (map.get("month").equals(DateUtil.timeStamp2Date(key, "yyyy-MM"))) {
                            value = Integer.parseInt(String.valueOf(value)) + Integer.parseInt(String.valueOf(map.get("count")));
                            tmpCount = Integer.parseInt(String.valueOf(map.get("count")));
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    mapTmp.put("count", tmpCount);
                    list.remove(mapTmp);
                }
                mapTmp.put("count", value);
                list.add(mapTmp);
            });
            //构建数据，当map中没有对应日期，需要新增当天的数据
            while (start.isBefore(end.plusMonths(1))) {
                //因lambda必须使用final，所以此处需要给start重新赋值给finalStart.
                final YearMonth finalStart = start;
                //如果list里没有包含当前日期的数据，加入数据
                if (!list.stream().anyMatch(map1 -> map1.get("month").equals(finalStart.toString()))) {
                    Map<String, Object> mapTmp = new HashMap<>();
                    mapTmp.put("month", start.toString());
                    mapTmp.put("count", 0);
                    list.add(mapTmp);
                }
                //加一个月
                start = start.plusMonths(1);
            }
        } else {
            while (start.isBefore(end.plusMonths(1))) {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("month", start.toString());
                mapTmp.put("count", 0);
                list.add(mapTmp);
                start = start.plusMonths(1); //加一个月
            }
        }
    }


    private void collectionResultByDay(List<Map<String, Object>> list, String pageUrl, String actionId, String methodurl, String startDate, String endDate) throws Exception {
        //取开始时间的时间戮
        long startTime = DateUtil.strConvertToDate(startDate, "yyyy-MM-dd").getTime() / 1000;
        //取结束日期的秒级时间戮
        long endTime = DateUtil.strConvertToDate(endDate, "yyyy-MM-dd").getTime() / 1000;
        String pvJson = HttpClientFactory.requestGet(urlValue + methodurl + "&itemid=" + pageUrl + "&actionid=" + actionId + "&startTime=" + startTime + "&endTime=" + endTime + "&flag=1");
        Map<String, Integer> returnMap = JSONUtil.convertJsonToMap(pvJson);
        //以下是补数据过程
        LocalDate start = LocalDate.parse(startDate);//开始日期
        LocalDate end =  LocalDate.parse(endDate);//结束日期;
        if (null != returnMap && returnMap.size() > 0) {
            //将map中数据存入list
            returnMap.forEach((key, value) -> {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("date", DateUtil.timeStamp2Date(key, "yyyy-MM-dd"));
                mapTmp.put("count", value);
                list.add(mapTmp);
            });
            //构建数据，当map中没有对应日期，需要新增当天的数据
            while (start.isBefore(end.plusDays(1))) {
                //因lambda必须使用final，所以此处需要给start重新赋值给finalStart.
                final LocalDate finalStart = start;
                //如果list里没有包含当前日期的数据，加入数据
                if (!list.stream().anyMatch(map1 -> map1.get("date").equals(finalStart.toString()))) {
                    Map<String, Object> mapTmp = new HashMap<>();
                    mapTmp.put("date", start.toString());
                    mapTmp.put("count", 0);
                    list.add(mapTmp);
                }
                //日期加一天
                start = start.plusDays(1);
            }

        } else {
            //如果返回没有数据,补全所有从开始日期到结束日期的数据,活跃度为0
            while (start.isBefore(end.plusDays(1))) {
                Map<String, Object> mapTmp = new HashMap<>();
                mapTmp.put("date", start.toString());
                mapTmp.put("count", 0);
                list.add(mapTmp);
                start = start.plusDays(1); //日期加一天
            }
        }
    }

}

