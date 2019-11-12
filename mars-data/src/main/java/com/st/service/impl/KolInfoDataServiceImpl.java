package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.model.ExposureModel;
import com.st.model.OpResult;
import com.st.service.KolInfoDataService;
import com.st.utils.HttpClientFactory;
import com.st.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by admin on 2016/9/12.
 * Kol统计
 */
@Service
public class KolInfoDataServiceImpl implements KolInfoDataService {

    @Value("${kol.url}")
    private String kolUrl;
    @Value("${kol.third_uname}")
    private String thridUname;
    @Value("${kol.token}")
    private String token;

    /**
     * 调用接口的公共方法
     * @param paramsMap
     * @return
     */
    public JSONObject commonPort(String url,Map<String,String> paramsMap){
        JSONObject dataNode = null;
        try {
            String dataJson = HttpClientFactory.requestPost(kolUrl+url,paramsMap);
            JSONObject jsonObject = JSONObject.fromObject(dataJson);
            if(Integer.parseInt(String.valueOf(jsonObject.get("code")))==200){
                dataNode = JSONObject.fromObject(jsonObject.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataNode;
    }

    /**
     * 微博概览头部统计数据
     * @param proId
     * @return
     */
    @Override
    public OpResult<Map<String, Object>> microBlogHeadData(String proId) {
        String url = "overview-header?pro_id="+proId;
        Map<String,Object> resultMap;
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            JSONObject dataNode = commonPort(url,paramsMap);
            resultMap = new HashMap<>();
            if(!StringUtils.isNull(dataNode)){
                dataNode.forEach((key,val)->resultMap.put(String.valueOf(key),Integer.parseInt(String.valueOf(val))));
            }
            return OpResult.createSucResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.MICROBLOG_HEAD_EXCEPTION);
    }

    /**
     * 微博总曝光统计走势
     * @param proId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public OpResult<List<ExposureModel>> microBlogExposure(String proId, String startDate, String endDate) {
        String url = "cover-list?pro_id="+proId+"&start_time="+startDate+"&end_time="+endDate;
        ExposureModel exposure = null;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            List<ExposureModel> list = new ArrayList<>();
            JSONObject dataNode = commonPort(url,paramsMap);
            if(!StringUtils.isNull(dataNode)){
                List<String> dateList = (List<String>) dataNode.get("date");
                List<Integer> valueList = (List<Integer>) dataNode.get("value");

                for(int i=0;i<dateList.size();i++){
                    exposure = new ExposureModel();
                    exposure.setName(dateList.get(i));
                    exposure.setValue(Long.valueOf(valueList.get(i)));
                    list.add(exposure);
                }

            }
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.MICROBLOG_EXPOSURE_EXCEPTION);
    }

    /**
     * 微博KOL互动情况排名
     * @param proId
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> microBlogChain(String proId) {
        String url = "kol-interact?pro_id="+proId;
        List<Map<String,Object>> resultlist = new ArrayList<>();
        List<ExposureModel> exList ;
        Map<String,Object> map ;
        ExposureModel ex ;
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            JSONObject dataNode = commonPort(url,paramsMap);
            if(!StringUtils.isNull(dataNode)){
                List<String> nameList = (List<String>) dataNode.get("name");
                List<String> dateList = (List<String>) dataNode.get("date");
                List<List<Integer>> valueList = (List<List<Integer>>)dataNode.get("value");
                for(int i=0;i<nameList.size();i++){
                    map = new HashMap<>();
                    exList = new ArrayList<>();
                    for (int j=0;j<dateList.size();j++){
                        ex = new ExposureModel();
                        ex.setName(dateList.get(j));
                        ex.setValue(Long.valueOf(valueList.get(i).get(j)));
                        exList.add(ex);
                    }
                    map.put("type",nameList.get(i));
                    map.put("values",exList);
                    resultlist.add(map);
                }
            }
            return OpResult.createSucResult(resultlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.MICROBLOG_INTERACT_EXCEPTION);
    }

    /**
     * 微博KOL总曝光量统计
     * @param proId
     * @return
     */
    @Override
    public OpResult<Object> microBlogExposureNum(String proId) {
        String url = "kol-contribution?pro_id="+proId;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            String dataJson = HttpClientFactory.requestPost(kolUrl+url,paramsMap);
            JSONObject jsonObject = JSONObject.fromObject(dataJson);
            if(Integer.parseInt(String.valueOf(jsonObject.get("code")))==200){
                return OpResult.createSucResult(jsonObject.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.MICROBLOG_CONTRIBUTION_EXCEPTION);
    }

    /**
     * 微博页面链接pv来源
     * @param proId
     * @return
     */
    @Override
    public OpResult<Object> microBlogPage(String proId) {
        String url = "surl-pv?pro_id="+proId;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            String dataJson = HttpClientFactory.requestPost(kolUrl+url,paramsMap);
            JSONObject jsonObject = JSONObject.fromObject(dataJson);
            if(Integer.parseInt(String.valueOf(jsonObject.get("code")))==200){
                return OpResult.createSucResult(jsonObject.get("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.MICROBLOG_SURLPV_EXCEPTION);
    }

    /**
     * 微信概览头部统计数据
     * @param proId
     * @return
     */
    @Override
    public OpResult<Map<String, Object>> weChartHeadData(String proId) {
        String url = "wechat-overview-header?pro_id="+proId;
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            JSONObject dataNode = commonPort(url,paramsMap);
            if(!StringUtils.isNull(dataNode)){
                resultMap.put("pro_id",Integer.parseInt(dataNode.get("pro_id").toString()));
                resultMap.put("pro_kol_count",Integer.parseInt(dataNode.get("pro_kol_count").toString()));
                resultMap.put("read",Integer.parseInt(dataNode.get("read").toString()));
                resultMap.put("attitudes",Integer.parseInt(dataNode.get("attitudes").toString()));
                resultMap.put("pv",Integer.parseInt(dataNode.get("pv").toString()));
                resultMap.put("uv",Integer.parseInt(dataNode.get("uv").toString()));

            }
            return OpResult.createSucResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WECHART_HEAD_EXCEPTION);
    }

    /**
     * 微信总阅读量统计走势
     * @param proId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public OpResult<List<ExposureModel>> weChartReadTrend(String proId, String startDate, String endDate) {
        String url = "wechat-read-cover?pro_id="+proId+"&start_time="+startDate+"&end_time="+endDate;
        ExposureModel exposure = null;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            JSONObject dataNode = commonPort(url,paramsMap);
            List<ExposureModel> list = new ArrayList<>();
            if(!StringUtils.isNull(dataNode)){
                List<String> dateList = (List<String>) dataNode.get("date");
                List<Integer> valueList = (List<Integer>) dataNode.get("value");
                for(int i=0;i<dateList.size();i++){
                    exposure = new ExposureModel();
                    exposure.setName(dateList.get(i));
                    exposure.setValue(Long.valueOf(valueList.get(i)));
                    list.add(exposure);
                }

            }
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WECHART_READCOVER_EXCEPTION);
    }

    /**
     * 微信Kol点赞情况
     * @param proId
     * @return
     */
    @Override
    public OpResult<List<ExposureModel>> weChartAttitudes(String proId) {
        String url = "wechat-kol-attitudes?pro_id="+proId;
        ExposureModel exposure = null;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            JSONObject dataNode = commonPort(url,paramsMap);
            List<ExposureModel> list = new ArrayList<ExposureModel>();

            if(!StringUtils.isNull(dataNode)){
                List<String> dateList = (List<String>) dataNode.get("date");
                List<Integer> valueList = (List<Integer>) dataNode.get("value");
                for(int i=0;i<dateList.size();i++){
                    exposure = new ExposureModel();
                    exposure.setName(dateList.get(i));
                    exposure.setValue(Long.valueOf(valueList.get(i)));
                    list.add(exposure);
                }

            }
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WECHART_ATTITUDES_EXCEPTION);
    }

    /**
     * 微信阅读量kol贡献比
     * @param proId
     * @return
     */
    @Override
    public OpResult<Object> weChartContribution(String proId) {
        String url = "wechat-kol-contribution?pro_id="+proId;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            String dataJson = HttpClientFactory.requestPost(kolUrl+url,paramsMap);
            JSONObject jsonObject = JSONObject.fromObject(dataJson);
            if(Integer.parseInt(String.valueOf(jsonObject.get("code")))==200)
                return OpResult.createSucResult(jsonObject.get("data"));
            else
                return OpResult.createSucResult(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WECHART_CONTRIBUTION_EXCEPTION);
    }

    /**
     * 微信项目概览页面链接pv来源百分比
     * @param proId
     * @return
     */
    @Override
    public OpResult<Object> weChartPage(String proId) {
        String url = "wechat-surl-pv?pro_id="+proId;
        Map<String,String> paramsMap = new HashMap<String,String>();
        paramsMap.put("third_uname",thridUname);
        paramsMap.put("token",token);
        try {
            String dataJson = HttpClientFactory.requestPost(kolUrl+url,paramsMap);
            JSONObject jsonObject = JSONObject.fromObject(dataJson);
            if(Integer.parseInt(String.valueOf(jsonObject.get("code")))==200)
                return OpResult.createSucResult(jsonObject.get("data"));
            else
                return OpResult.createSucResult(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.WECHART_SURLPV_EXCEPTION);
    }
}
