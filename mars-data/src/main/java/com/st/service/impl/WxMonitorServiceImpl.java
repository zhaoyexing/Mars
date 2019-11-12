package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.enums.ErrorCodeEnum;
import com.st.model.OpResult;
import com.st.service.WxMonitorService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.NumberUtils;
import com.st.utils.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;


/**
 * 招募统计
 * Created by admin on 2016/9/22.
 */
@Service
public class WxMonitorServiceImpl implements WxMonitorService{

    @Value("${user.draw.url}")
    private String userValue;
    @Value("${user.draw.url}")
    private String scrmValue;
    @Value("${menu.url}")
    private String menuValue;

    @Value("${scrm.fans_count}")
    private String fansCountUrl;
    @Value("${wxMonitor.menuList}")
    private String menuListUrl;
    @Value("${wxMonitor.getMenuClick}")
    private String getMenuClickUrl;

    @Value("${wxMonitor.menuPVTrend}")
    private String menuPVTrendUrl;

    @Value("${wxMonitor.menuUVTrend}")
    private String menuUVTrendUrl;

    //菜单级别1表示为主菜单
    private static String MENU_LEVEL_MAIN_STR = "1";
    //菜单级别2表示为子菜单
    private static String MENU_LEVEL_SUB_STR = "2";

    /**
     * 获取当前粉丝数和累计粉丝数
     * @return
     */
    @Override
        public OpResult<Map<String,Object>> wxFansCount() {
        Map<String,Object> retMap = new HashMap<>();
        try {
            String endTime = String.valueOf(new Date().getTime()/1000);
            //取开始时间
            Date date = DateUtil.getNextDay(new Date(),-1);
            String startTime = String.valueOf(date.getTime()/1000);
            String jsonProperty = HttpClientFactory.requestGet(userValue + "getDashboardPage&startTime="+startTime+"&endTime="+endTime);
            JSONObject jsonObjectProperty = JSONObject.fromObject(jsonProperty);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));

            Map fansCountMap = (Map)toMap(tagStr).get("recruit_fans");
            //封装返回结果的数据map
            retMap.put("current_total_fans",fansCountMap.get("current_fans"));
            retMap.put("total_fans",fansCountMap.get("total_fans"));

            return OpResult.createSucResult(retMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_FANSCOUNT_EXCEPTION);
    }

    /**
     * 获取新增-流失-净增粉丝数量
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param dataCycle
     * @return
     */
   /* @Override
    public OpResult<List<Map<String, Object>>> getFansCountRange(String startDate, String endDate, String dataCycle) {
        try {
            List list = new ArrayList<>();
            ArrayList listAdd = new ArrayList();
            ArrayList listLost = new ArrayList();
            ArrayList listNet = new ArrayList();
            //1、请求SCRM接口粉丝数据
            JSONObject json = getWxMenuFans(startDate, endDate,dataCycle);
            Map<String, Object> map = null;
            //2、遍历json数据
            for (Iterator iter = json.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                JSONObject jsonTag = JSONObject.fromObject(json.get(key));
                String subscribe_fans = jsonTag.get("subscribe_fans").toString();
                String unsubscribe_fans = jsonTag.get("unsubscribe_fans").toString();
                //2.1把时间戳转换成日期格式
                String dateTime = DateUtil.timeStamp2Date(key, "yyyy-MM-dd");
                //2.2存新增数量
                Map<String,Object> mapAdd = new HashMap<String, Object>();
                mapAdd.put("name",dateTime);
                mapAdd.put("value",subscribe_fans);
                listAdd.add(mapAdd);
                //2.2存流失的数量
                Map<String,Object> mapLost = new HashMap<String, Object>();
                mapLost.put("name",dateTime);
                mapLost.put("value",unsubscribe_fans);
                listLost.add(mapLost);
                //2.3存净增的数量
                Map<String,Object> mapNet = new HashMap<String, Object>();
                mapNet.put("name",dateTime);
                mapNet.put("value",Double.valueOf(subscribe_fans)-Double.valueOf(unsubscribe_fans));
                listNet.add(mapNet);
            }
                //3、把数据存入list
                //3.1把流失粉丝的数量存入list
                Map<String,Object> mapLost = new HashMap<String, Object>();
                mapLost.put("name","流失粉丝");
                mapLost.put("value",listLost);
                list.add(mapLost);
                //3.2把新增粉丝的数量存入list
                Map<String,Object> mapAdd = new HashMap<String, Object>();
                mapAdd.put("name","新增粉丝");
                mapAdd.put("value",listAdd);
                list.add(mapAdd);
                //3.3把净增粉丝的数量存入list
                Map<String,Object> mapNet = new HashMap<String, Object>();
                mapNet.put("name","净增粉丝");
                mapNet.put("value",listNet);
                list.add(mapNet);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_FANSRANGE_EXCEPTION);
    }*/


    @Override
    public OpResult<List<Map<String, Object>>> getFansCountRange(String startDate, String endDate, String dataCycle) {
        try {
            List list = new ArrayList<>();
            ArrayList listAdd = new ArrayList();
            ArrayList listLost = new ArrayList();
            ArrayList listNet = new ArrayList();
            //日期格式化形式如果是3e
            String dateFormat = "3".equals(dataCycle)?"yyyy-MM":"yyyy-MM-dd";
            //1、获取粉丝关注取关的map并根据key进行排序
            Map<String,Object> fansInfoMap = StringUtils.sortMapByKey(getWxMenuFans(startDate, endDate,dataCycle));
            Map<String, Object> map = null;
            //2、根据日期遍历map
            for (Map.Entry<String,Object> entry:fansInfoMap.entrySet()) {

                //2.1把时间戳转换成日期格式

                String dateTime = DateUtil.timeStamp2Date(entry.getKey(), dateFormat);
                //将字符串转为jsonObject并且获取关注粉丝和取关粉丝数

                //不为空的话赋值
                int subscribe_fans = 0;
                int unsubscribe_fans = 0;
                if(!StringUtils.isNull(entry.getValue())){
                    JSONObject jsonTag = JSONObject.fromObject(entry.getValue());
                    subscribe_fans = Integer.valueOf(jsonTag.get("subscribe_fans").toString());
                    unsubscribe_fans = -Integer.valueOf(jsonTag.get("unsubscribe_fans").toString());
                }


                //2.2存新增数量
                Map<String,Object> mapAdd = new HashMap<String, Object>();
                mapAdd.put("name",dateTime);
                mapAdd.put("value",subscribe_fans);
                listAdd.add(mapAdd);
                //2.2存流失的数量
                Map<String,Object> mapLost = new HashMap<String, Object>();
                mapLost.put("name",dateTime);
                mapLost.put("value",unsubscribe_fans);
                listLost.add(mapLost);
                //2.3存净增的数量
                Map<String,Object> mapNet = new HashMap<String, Object>();
                mapNet.put("name",dateTime);
                mapNet.put("value",subscribe_fans+unsubscribe_fans);
                listNet.add(mapNet);
            }
            //3、把数据存入list
            //3.1把流失粉丝的数量存入list
            Map<String,Object> mapLost = new HashMap<String, Object>();
            mapLost.put("name","流失粉丝");
            mapLost.put("value",listLost);
            list.add(mapLost);
            //3.2把新增粉丝的数量存入list
            Map<String,Object> mapAdd = new HashMap<String, Object>();
            mapAdd.put("name","新增粉丝");
            mapAdd.put("value",listAdd);
            list.add(mapAdd);
            //3.3把净增粉丝的数量存入list
            Map<String,Object> mapNet = new HashMap<String, Object>();
            mapNet.put("name","净增粉丝");
            mapNet.put("value",listNet);
            list.add(mapNet);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_FANSRANGE_EXCEPTION);
    }

    /**
     * 获取菜单列表
     * @param menuLevel 菜单级别
     * @return
     */
    @Override
    public OpResult<List<Map>> getMenuList(String menuLevel) {
        try {
          //  List mList = new ArrayList<>();
            //1、获取菜单列表
            List<Map> wxMenuList = getWxMenuList(menuLevel);
            //2、把数据存入list并返回
           // mList.add(wxMenuList);
            return  OpResult.createSucResult(wxMenuList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_GETMENULIST_EXCEPTION);
    }



    public Map<String, Object> toMap(String str) {
        Gson gson = new Gson();
        Map<String, Object> gsonMap = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
        }.getType());
        return gsonMap;
    }



    //=======================================modify by haozi=======================================================

    @Override
    public OpResult<List<Map<String, Object>>> menuClickDist(String startDate, String endDate, String menuLevel) {

        try {
            List retList = new ArrayList<>();
            //1、首先获取菜单列表
            List<Map> menuList = getWxMenuAndUrl(menuLevel);

            //2、请求SCRM接口获取菜单剪辑分布数据
            JSONObject menuClickObj = getWxMenuClick(startDate,endDate);
            JSONObject menuPVObj = menuClickObj.getJSONObject("menu_pv");
            JSONObject menuUVObj = menuClickObj.getJSONObject("menu_uv");

            //3处理请求的数据，返回结果
            //3.1点击次数，点击人数map初始化
            Map<String,Object> pVMap = new HashMap<>();
            Map<String,Object> uVMap = new HashMap<>();
            Map<String,Object> rateMap = new HashMap<>();
            pVMap.put("name","点击次数");
            uVMap.put("name","点击人数");
            rateMap.put("name","点击人数比例");

            int totalUV = getTotalClickManCount(menuList,menuUVObj);
            //3.2遍历菜单url的list，获取每个菜单点击人数和点击次数的值
            List pVMenuList = new ArrayList<>();
            List uVMenuList = new ArrayList<>();
            List rateMenuList = new ArrayList<>();
            Map pVMenuMap = null;
            Map uVMenuMap = null;
            Map rateMenuMap = null;
            double pVNum = 0;
            double uVNum = 0;
            for(int i=0;i<menuList.size();i++){
                pVMenuMap = new HashMap<>();
                uVMenuMap = new HashMap<>();
                rateMenuMap = new HashMap<>();
                //从菜单url列表中获取菜单url和name
                Map<String,Object> menuUrlMap = menuList.get(i);

                String menuName = (String)menuUrlMap.get("name");
                List<String> menuUrlList = (List)menuUrlMap.get("url");

                //存放点击次数map
                pVMenuMap.put("name",menuName);
                uVMenuMap.put("name",menuName);
                rateMenuMap.put("name",menuName);
                pVNum = 0;
                uVNum = 0;
                //初始化点击人数、点击次数
                //如果menuUrl为空，则点击数为0，否则从点击次数json对象中或从点击人数的返回数据中取值
                for (String menuUrl:menuUrlList){
                    if(!(StringUtils.isNull(menuUrl))){
                        pVNum += StringUtils.isNull(menuPVObj.get(menuUrl))?0d:Double.valueOf((int)menuPVObj.get(menuUrl));
                        uVNum += StringUtils.isNull(menuUVObj.get(menuUrl))?0d:Double.valueOf((int)menuUVObj.get(menuUrl));
                    }
                }

                pVMenuMap.put("value", pVNum);
                uVMenuMap.put("value", uVNum);
                if(totalUV==0){
                    rateMenuMap.put("value", 0d);
                }else{
                    rateMenuMap.put("value", NumberUtils.Double2point(uVNum/totalUV)*100);
                }

                pVMenuList.add(pVMenuMap);
                uVMenuList.add(uVMenuMap);
                rateMenuList.add(rateMenuMap);
            }
            //3.3将数据存入，并返回
            pVMap.put("value",pVMenuList);
            uVMap.put("value",uVMenuList);
            rateMap.put("value",rateMenuList);

            retList.add(pVMap);
            retList.add(uVMap);
            retList.add(rateMap);

            return OpResult.createSucResult(retList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_MENUCLICKDIST_EXCEPTION);
    }


    @Override
    public OpResult getMenuClickTrend(String startDate, String endDate, String menuName, String menuLevel, String dataCycle) {
        try {

            List retList = new ArrayList<>();
            //1、首先获取菜单url的map
            Map<String,List> menuMap  = getWxMenuAndUrlMap(menuLevel);
            List<String> menuUrlList = menuMap.get(menuName);

            //然后通过srcm接口获取菜单点击趋势
            Map<String,Object> pVMap = new HashMap<>();
            pVMap.put("name","点击次数");

            //然后通过srcm接口获取菜单人数趋势
            Map<String,Object> uVMap = new HashMap<>();
            uVMap.put("name","点击人数");

            Map<String,Object> pVUrlMap = null;
            Map<String,Object> uVUrlMap = null;
            if("3".equals(dataCycle)){
                pVUrlMap =  DateUtil.getMonthDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM"),DateUtil.strConvertToDate(endDate,"yyyy-MM"),0,"yyyy-MM");
                uVUrlMap =  DateUtil.getMonthDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM"),DateUtil.strConvertToDate(endDate,"yyyy-MM"),0,"yyyy-MM");
            }else {
                pVUrlMap =  DateUtil.getDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM-dd"),DateUtil.strConvertToDate(endDate,"yyyy-MM-dd"),0,"yyyy-MM-dd");
                uVUrlMap =  DateUtil.getDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM-dd"),DateUtil.strConvertToDate(endDate,"yyyy-MM-dd"),0,"yyyy-MM-dd");
            }

            //遍历urllist 将返回key日期，数量的map


            for(String menuUrl:menuUrlList){
                pVUrlMap = StringUtils.sortMapByKey(getClickTrendFromUrl(menuPVTrendUrl,menuUrl,startDate,endDate,pVUrlMap,dataCycle));
                uVUrlMap = StringUtils.sortMapByKey(getClickTrendFromUrl(menuUVTrendUrl,menuUrl,startDate,endDate,uVUrlMap,dataCycle));
            }

            List<Map<String,Object>> pVUrlList = new ArrayList<>();
            Map<String,Object> pVdataMap = null;
            //遍历日期map，组装数据
            for(Map.Entry<String,Object> entry:pVUrlMap.entrySet()){
                pVdataMap = new HashMap<>();
                pVdataMap.put("name",entry.getKey());
                pVdataMap.put("value",entry.getValue());
                pVUrlList.add(pVdataMap);
            }
            //遍历日期map，组装数据
            List<Map<String,Object>> uVUrlList = new ArrayList<>();
            Map<String,Object> uVdataMap = null;
            for(Map.Entry<String,Object> entry:uVUrlMap.entrySet()){
                uVdataMap = new HashMap<>();
                uVdataMap.put("name",entry.getKey());
                uVdataMap.put("value",entry.getValue());
                uVUrlList.add(uVdataMap);
            }

            //
            pVMap.put("value",pVUrlList);
            uVMap.put("value",uVUrlList);

            retList.add(pVMap);
            retList.add(uVMap);

            return OpResult.createSucResult(retList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_MENUCLICKTREND_EXCEPTION);

    }

    @Override
    public OpResult getFansFollowChannelCount() {
        try {
            List retList = new ArrayList<>();
            //处理时间参数
            long startTime = DateUtil.getNextDay(new Date(),-1).getTime()/1000;
            long endTime = new Date().getTime()/1000;
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(fansCountUrl).append("&startTime=").append(startTime).append("&endTime=").append(startTime);
            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJson = HttpClientFactory.requestGet(requestUrl.toString());
            JSONObject jsonObj = JSONObject.fromObject(dataJson);

            //3、返回数据的判断以及处理
            JSONObject retJson = jsonObj.getJSONObject("datas").getJSONObject("recruit_fans");
            retMap.put("name","其他");
            retMap.put("value",retJson.get("current_fans"));

            retList.add(retMap);
            return OpResult.createSucResult(retList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_MONITOR_FANSCHANNEL_EXCEPTION);
    }

    /**
     * 获取Wx菜单和url
     * @return
     * @throws Exception
     */
    public List<Map> getWxMenuAndUrl(String menuLevel) throws Exception {
        //1、拼接请求rul
        String menuListRequestUrl = menuListUrl;
        //2、请求SCRM接口返回结果数据
        String menuListdataJson = HttpClientFactory.requestGet(menuListRequestUrl);
        // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
        JSONObject menuListjsonObj = JSONObject.fromObject(menuListdataJson);
        //3、返回数据的判断以及处理
        JSONObject menuObj = menuListjsonObj.getJSONObject("menu");
        JSONArray  btnArr = menuObj.getJSONArray("button");
        Iterator<JSONObject> btnIt = btnArr.iterator();
        //3.1遍历返回的menu数据，然后存入menuList
        Map menuMap = null;
        List<String> urlList = null;
        List<Map> menuList = new ArrayList<>();
        while(btnIt.hasNext()){
            JSONObject mainBtnObj = btnIt.next();
            //3.1.1如果是主菜单
            if(MENU_LEVEL_MAIN_STR.equals(menuLevel)){
                menuMap = new HashMap<>();
                urlList = new ArrayList<>();

                menuMap.put("name",mainBtnObj.get("name"));
                //这里统一处理了，没有url获取不到存null
                urlList.add((String)mainBtnObj.get("url"));
                urlList.add((String)mainBtnObj.get("key"));
                //3.1.2如果需要取子菜单，获取sub_button
                JSONArray subBtnArr = mainBtnObj.getJSONArray("sub_button");
                //遍历主菜单下子菜单内容
                Iterator<JSONObject> subBtnIt = subBtnArr.iterator();
                while(subBtnIt.hasNext()){
                    JSONObject subBtnObj = subBtnIt.next();
                    urlList.add((String)subBtnObj.get("url"));
                    urlList.add((String)subBtnObj.get("key"));
                }
                menuMap.put("url",urlList);
                menuList.add(menuMap);
            }else{
                //3.1.2如果需要取子菜单，获取sub_button
                JSONArray subBtnArr = mainBtnObj.getJSONArray("sub_button");
                //遍历主菜单下子菜单内容
                Iterator<JSONObject> subBtnIt = subBtnArr.iterator();
                while(subBtnIt.hasNext()){
                    menuMap = new HashMap<>();
                    urlList = new ArrayList<>();
                    JSONObject subBtnObj = subBtnIt.next();
                    menuMap.put("name",subBtnObj.get("name"));
                    //这里统一处理了，没有url获取不到存null
                    urlList.add((String)subBtnObj.get("url"));
                    urlList.add((String)subBtnObj.get("key"));
                    //这里统一处理了，没有url获取不到存null
                    menuMap.put("url",urlList);
                    menuList.add(menuMap);
                }
            }

        }
        return menuList;
    }


    /**
     * 获取Wx菜单和url
     * @return
     * @throws Exception
     */
    public Map getWxMenuAndUrlMap(String menuLevel) throws Exception {
        //1、拼接请求rul
        String menuListRequestUrl = menuListUrl;
        //2、请求SCRM接口返回结果数据
        String menuListdataJson = HttpClientFactory.requestGet(menuListRequestUrl);
        // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
        JSONObject menuListjsonObj = JSONObject.fromObject(menuListdataJson);
        //3、返回数据的判断以及处理
        JSONObject menuObj = menuListjsonObj.getJSONObject("menu");
        JSONArray  btnArr = menuObj.getJSONArray("button");
        Iterator<JSONObject> btnIt = btnArr.iterator();
        //3.1遍历返回的menu数据，然后存入menuList
        Map menuMap = new HashMap<>();
        List<String> urlList = null;
        while(btnIt.hasNext()){
            JSONObject mainBtnObj = btnIt.next();
            urlList = new ArrayList<>();
            //3.1.1如果是主菜单
            if(MENU_LEVEL_MAIN_STR.equals(menuLevel)){
                //3.1.2如果需要取子菜单，获取sub_button
                JSONArray subBtnArr = mainBtnObj.getJSONArray("sub_button");
                if(subBtnArr.size()>0){
                    //遍历主菜单下子菜单内容
                    Iterator<JSONObject> subBtnIt = subBtnArr.iterator();
                    while(subBtnIt.hasNext()){
                        JSONObject subBtnObj = subBtnIt.next();
                        urlList.add((String)subBtnObj.get("url"));
                        urlList.add((String)subBtnObj.get("key"));
                    }
                }else{
                    urlList.add((String)mainBtnObj.get("url"));
                    urlList.add((String)mainBtnObj.get("key"));
                }
                menuMap.put(mainBtnObj.get("name"),urlList);

            }else{
                //3.1.2如果需要取子菜单，获取sub_button
                JSONArray subBtnArr = mainBtnObj.getJSONArray("sub_button");
                //遍历主菜单下子菜单内容
                Iterator<JSONObject> subBtnIt = subBtnArr.iterator();
                while(subBtnIt.hasNext()){
                    urlList = new ArrayList<>();
                    JSONObject subBtnObj = subBtnIt.next();
                    urlList.add((String)subBtnObj.get("url"));
                    urlList.add((String)subBtnObj.get("key"));
                    menuMap.put(subBtnObj.get("name"),urlList);
                }
            }

        }
        return menuMap;
    }

    /**
     * 通过SCRM接口获取数据
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public JSONObject getWxMenuClick(String startDate, String endDate) throws Exception {
        //处理dataCycle 1按天，3按月

        //开始时间v
        long startTime = DateUtil.parse(startDate).getTime()/1000;
        //结束时间
        long endTime = DateUtil.parse(endDate).getTime()/1000;

        /*if("3".equals(dataCycle)){
            //起始时间月份第一天
            Date firstDayStartMonth = DateUtil.strConvertToDate(startDate,"yyyy-MM");
            //结束时间月份第一天
            Date firstDayEndMonth = DateUtil.strConvertToDate(endDate,"yyyy-MM");
            //结束时间下一个月第一天
            Date firstDayEndNextMonth = DateUtil.getNextMonth(firstDayEndMonth);
            //修改startTime 和endTime参数
            startTime = firstDayStartMonth.getTime()/1000;
            endTime = firstDayEndNextMonth.getTime()/1000;
        }*/
      //  dataCycle = "3".equals(dataCycle)?"3":"1";

        //2、SCRM接口接口获取菜单点击人数，点击次数
        //2.1、拼接请求rul
        StringBuffer menuClickSb = new StringBuffer(getMenuClickUrl).append("&startTime=").append(startTime).append("&endTime=").append(endTime).append("&dataCycle=").append("1");
        //2.2、请求SCRM接口返回结果数据
        String menuClickJson = HttpClientFactory.requestGet(menuClickSb.toString());
        JSONObject menuClickObj = JSONObject.fromObject(menuClickJson).getJSONObject("datas");
        return menuClickObj;
    }



    /**
     * 获取点击次数或者点击人数
     * @param baseUrl
     * @param menuUrl
     * @param startDate
     * @param endDate
     * @param dataCycle
     * @return
     * @throws Exception
     */
    public Map<String,Object> getClickTrendFromUrl(String baseUrl, String menuUrl, String startDate, String endDate, Map<String, Object> urlMap, String dataCycle) throws Exception {
        //返回的结果数据结构
       // List retList = new ArrayList<>();
        //如果为空直接返回空数组
        if(StringUtils.isNull(menuUrl)){
            return urlMap;
        }
        //开始时间
        long startTime = DateUtil.parse(startDate).getTime()/1000;
        //结束时间
        long endTime = DateUtil.getNextDay(DateUtil.parse(endDate),1).getTime()/1000;


        //按日还是按月
        dataCycle = "3".equals(dataCycle)?"3":"1";

         if("3".equals(dataCycle)){
            //起始时间月份第一天
            Date firstDayStartMonth = DateUtil.strConvertToDate(startDate,"yyyy-MM");
            //结束时间月份第一天
            Date firstDayEndMonth = DateUtil.strConvertToDate(endDate,"yyyy-MM");
            //结束时间下一个月第一天
            Date firstDayEndNextMonth = DateUtil.getNextMonth(firstDayEndMonth);
            //修改startTime 和endTime参数
            startTime = firstDayStartMonth.getTime()/1000;
            endTime = firstDayEndNextMonth.getTime()/1000;
        }



        //1、拼接请求url获取返回数据
        String requestUrl = new StringBuffer(baseUrl).append("&startTime=").append(startTime).append("&endTime=").append(endTime).append("&itemid=").append(URLEncoder.encode(menuUrl,"UTF-8")).append("&flag=").append(dataCycle).toString();
        String dataJson = HttpClientFactory.requestGet(requestUrl);
        JSONObject dataObj = JSONObject.fromObject(dataJson).getJSONObject("datas");
        //数据map
        String dateFormat = "3".equals(dataCycle)?"yyyy-MM":"yyyy-MM-dd";
        //遍历日期key-value数据结构
        Iterator dataIt = dataObj.keys();
        while(dataIt.hasNext()){
           // dateMap = new HashMap<>();
            String dataKey = (String)dataIt.next();
            //转化日期字符串
            String dateStr =  DateUtil.timeStamp2Date(dataKey,dateFormat);
           // dateMap.put("name",dateStr);
           // dateMap.put("value",dataObj.get(dataKey));
            urlMap = addKvToMap(dateStr,(Integer) dataObj.get(dataKey),urlMap);

        }
        return urlMap;
    }

    /**
     * 获取Wx菜单
     * @return
     * @throws Exception
     */
    public List<Map> getWxMenuList(String menuLevel) throws Exception {
        //1、拼接请求rul
        String menuListRequestUrl = menuListUrl;
        //2、请求SCRM接口返回结果数据
        String menuListdataJson = HttpClientFactory.requestGet(menuListRequestUrl);
        JSONObject menuListjsonObj = JSONObject.fromObject(menuListdataJson);
        //3、返回数据的判断以及处理
        JSONObject menuObj = menuListjsonObj.getJSONObject("menu");
        JSONArray  btnArr = menuObj.getJSONArray("button");
        Iterator<JSONObject> btnIt = btnArr.iterator();
        //3.1遍历返回的menu数据，然后存入menuList
        Map menuMap = null;
        List<Map> menuList = new ArrayList<>();
        while(btnIt.hasNext()){

            JSONObject mainBtnObj = btnIt.next();
            //3.1.1如果是主菜单
            if(MENU_LEVEL_MAIN_STR.equals(menuLevel)){
                menuMap = new HashMap<>();
                menuMap.put("menuName",mainBtnObj.get("name"));
                menuList.add(menuMap);
            }else{
                //3.1.2如果需要取子菜单，获取sub_button
                JSONArray subBtnArr = mainBtnObj.getJSONArray("sub_button");
                //遍历主菜单下子菜单内容
                Iterator<JSONObject> subBtnIt = subBtnArr.iterator();
                while(subBtnIt.hasNext()){
                    menuMap = new HashMap<>();
                    JSONObject subBtnObj = subBtnIt.next();
                    menuMap.put("menuName",subBtnObj.get("name"));
                    menuList.add(menuMap);
                }
            }

        }
        return menuList;
    }

    /**
     * 通过SCRM接口获取数据
     * @param startDate
     * @param endDate
     * @param dataCycle
     * @return
     * @throws Exception
     */
    public Map getWxMenuFans(String startDate, String endDate, String dataCycle) throws Exception {
            //1.处理参数
           //处理dataCycle 1按天，3按月
            dataCycle = "3".equals(dataCycle)?"3":"1";
            //默认开始时间、结束时间处理方式
            long startTime = DateUtil.parse(startDate).getTime()/1000;
            //当前时间后一天
            long endTime = DateUtil.getNextDay(DateUtil.parse(endDate),1).getTime()/1000;

            Map sourceDateMap = null;
            if("3".equals(dataCycle)){
                startTime = DateUtil.strConvertToDate(startDate,"yyyy-MM").getTime()/1000;
                sourceDateMap =  DateUtil.getMonthDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM"),DateUtil.strConvertToDate(endDate,"yyyy-MM"),null);
            }else{
                sourceDateMap =  DateUtil.getDatesBetweenTwoDate(DateUtil.strConvertToDate(startDate,"yyyy-MM-dd"),DateUtil.strConvertToDate(endDate,"yyyy-MM"),null);
            }

          //根据起始时间和终止时间获取全部日期map

            //1.2拼接请求url
            StringBuffer requestUrl = new StringBuffer(scrmValue).append("getRecruitFansPage&dataCycle=").append(dataCycle).append("&region=all&subregion=&city=&startTime=").append(startTime).append("&endTime=").append(endTime);
            //获取返回数据
            String jsonProperty = HttpClientFactory.requestGet(requestUrl.toString());
            JSONObject dataJsonObj = JSONObject.fromObject(jsonProperty);
            //获取新增流失粉丝数
            String  fansInfoStr  = dataJsonObj.getJSONObject("datas").get("fans_itemid_info").toString();

            Map fansInfoMap = StringUtils.jsonStrToMap(fansInfoStr);
           //添加值map到源日期map
            return  addValueMapToSourceMap(fansInfoMap,sourceDateMap);
    }


    /**
     * 获取 总的点击人数
     * @param menuList
     * @param menuUVObj
     * @return
     */
    public int  getTotalClickManCount( List<Map> menuList,JSONObject menuUVObj){

        //初始化点击人数
        int uVNum = 0;
        for(int i=0;i<menuList.size();i++){
            //从菜单url列表中获取菜单url和name
            Map<String,Object> menuUrlMap = menuList.get(i);
            List<String> menuUrlList = (List)menuUrlMap.get("url");

            for (String menuUrl:menuUrlList){
                if(!StringUtils.isNull(menuUrl)&&!StringUtils.isNull(menuUVObj)){
                    //点击人数累加
                    uVNum += StringUtils.isNull(menuUVObj.get(menuUrl))?0:(int)menuUVObj.get(menuUrl);
                }
            }
        }
        return uVNum;
    }

    /**
     * 判断map 是否存在，存在则相加
     * @param key
     * @param value
     * @param sourceMap
     * @return
     */

    public Map addKvToMap(String key,Integer value,Map<String,Object> sourceMap){
        if(sourceMap.get(key)!=null){
            sourceMap.put(key,(Integer)sourceMap.get(key)+value);
        }else{
            sourceMap.put(key,value);
        }
        return sourceMap;
    }

    /**
     * 添加map
     * @param key
     * @param value
     * @param sourceMap
     * @return
     */
    public Map<String,Object> addValueToSourceMap(String key,Object value,Map<String,Object> sourceMap){

        if(sourceMap.get(key)!=null){
            sourceMap.put(key,value);
        }else{
            sourceMap.put(key,value);
        }
        return sourceMap;
    }

    public Map<String,Object> addValueMapToSourceMap(Map<String,Object> valueMap,Map<String,Object> sourceMap){
        //处理valueMap
        if(StringUtils.isNull(valueMap)){
            valueMap = new HashMap<>();
        }

        //遍历数据map添加到sourceMap
        Iterator entries = valueMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            sourceMap =  addValueToSourceMap( (String)entry.getKey(),entry.getValue(),sourceMap);
        }
        return sourceMap;
    }
}
