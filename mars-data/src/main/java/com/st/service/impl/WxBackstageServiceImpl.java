package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.model.OpResult;
import com.st.service.WxBackstageService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.NumberUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by admin on 2016/9/28.
 */
@Service
public class WxBackstageServiceImpl implements WxBackstageService {

    //昨日关键指标
    @Value("${wxBackstage.anakey}")
    private String anakeyUrl;
    //数据趋势
    @Value("${wxBackstage.anatrend}")
    private String anatrendUrl;
    //传播用户分析
    @Value("${wxBackstage.anaspread}")
    private String anaspreadUrl;
    //文章指标概览
    @Value("${wxBackstage.anasurvey}")
    private String anasurveyUrl;

    @Value("${wxBackstage.anaarticlespread}")
    private String anaarticlespreadUrl;


    @Override
    public OpResult getKeyIndicator(String startDate, String endDate) {
        try {
            //拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anakeyUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate);
            Map retMap = new HashMap<>();
            //请求SCRM接口返回结果数据
            String dataJsonStr = HttpClientFactory.requestGet(requestUrl.toString());
            JSONObject jsonObj = JSONObject.fromObject(dataJsonStr);
            //返回数据的判断以及处理
         /*   if((Integer)jsonObj.get("status")==200){

            }*/
            JSONObject retJson = jsonObj.getJSONObject("datas");
            return OpResult.createSucResult(retJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANAKEY_EXCEPTION);

    }

    @Override
    public OpResult getIndicatorTrend(String startDate, String endDate, String type) {

        try {
            //参数转换
            String dataType = "1";
            switch (type){
                case "imageText":
                    dataType = "1";
                    break;
                case "originalText":
                    dataType = "2";
                    break;
                case "forward":
                    dataType = "3";
                    break;
                case "store":
                    dataType = "4";
                    break;
            };
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anatrendUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&type=").append(dataType);

            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJsonStr = HttpClientFactory.requestGet(requestUrl.toString());
            String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
            JSONObject jsonObj = JSONObject.fromObject(convertJsonStr);
            //3、返回数据的判断以及处理
            JSONArray retJson = jsonObj.getJSONArray("datas");
            return OpResult.createSucResult(retJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANATREND_EXCEPTION);
    }

    @Override
    public OpResult getArticleSpreadData(String startDate, String endDate, String pageNum, String pageSize) {

        try {
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anaspreadUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&pageNum=").append(pageNum).append("&pageSize=").append(pageSize);
            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJson = HttpClientFactory.requestGet(requestUrl.toString());
           // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
            JSONObject jsonObj = JSONObject.fromObject(dataJson);
            //3、返回数据的判断以及处理
            JSONArray retJson = jsonObj.getJSONArray("datas");

            Iterator<JSONObject> it = retJson.iterator();
            while(it.hasNext()){
                JSONObject articleObj = it.next();
                articleObj.put("sendDate", DateUtil.timeStamp2Date(articleObj.get("sendDate").toString(),"yyyy-MM-dd"));
            }
            return OpResult.createSucResult(retJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANASPREAD_EXCEPTION);

    }

    @Override
    public OpResult getIndicatorByArticleId(String startDate, String endDate, String articleId) {
        try {
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anasurveyUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&articleId=").append(articleId);
            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJson = HttpClientFactory.requestGet(requestUrl.toString());
            // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
            JSONObject jsonObj = JSONObject.fromObject(dataJson);



            //3、返回数据的判断以及处理
            JSONObject retJson = jsonObj.getJSONObject("datas");

            return OpResult.createSucResult(retJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANASURVEY_EXCEPTION);

    }

    @Override
    public OpResult getArticleSpreadDataByArticleId(String startDate, String endDate, String pageNum, String pageSize, String msgId, String articleId) {
        try {
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anaarticlespreadUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&pageNum=").append(pageNum).append("&pageSize=").append(pageSize).append("&articleId=").append(articleId).append("&msgId=").append(msgId);
            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJson = HttpClientFactory.requestGet(requestUrl.toString());
            // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
            JSONObject jsonObj = JSONObject.fromObject(dataJson);

            //3、返回数据的判断以及处理
            JSONArray retJson = jsonObj.getJSONArray("datas");
            //转发次数,阅读次数，转发率
            double forwardReadCount = 0,imgTextReadCount=0, forwardRate=0;
            //遍历获取转发率
            Iterator<JSONObject> it = retJson.iterator();
            while(it.hasNext()){
                JSONObject articleObj = it.next();
                forwardReadCount = Double.valueOf((String)articleObj.get("forwardReadCount"));
                imgTextReadCount = Double.valueOf((String)articleObj.get("imgTextReadCount"));
                //判断除数是否为0
                forwardRate = imgTextReadCount == 0 ? 0 : NumberUtils.Double2point(forwardReadCount/imgTextReadCount)*100;
                articleObj.put("forwardRate",String.valueOf(forwardRate));
            }
            return OpResult.createSucResult(retJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANAARTICLESPREAD_EXCEPTION);
    }

    @Override
    public OpResult getIndicatorTrendByArticleId(String startDate, String endDate, String articleId, String msgId) {
        try {
            //1、拼接请求rul
            StringBuffer requestUrl = new StringBuffer(anaarticlespreadUrl).append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&pageNum=").append("&articleId=").append(articleId).append("&msgId=").append(msgId);
            Map retMap = new HashMap<>();
            //2、请求SCRM接口返回结果数据
            String dataJson = HttpClientFactory.requestGet(requestUrl.toString());
            // String convertJsonStr =  dataJsonStr.replace("total_int_page_read_user","manCount").replace("total_int_page_read_count","readCount");
            JSONObject jsonObj = JSONObject.fromObject(dataJson);

            //3、返回数据的判断以及处理
            JSONArray retJson = jsonObj.getJSONArray("datas");
            //图文阅读人数map
            Map<String,Object> manCountMap = new HashMap<>();
            List<Map<String,Object>> manCountList = new ArrayList<>();
            Map<String,Object> manDataMap = null;
            //图文阅读次数map
            Map<String,Object> readCountMap = new HashMap<>();
            List<Map<String,Object>> readCountList = new ArrayList<>();
            Map<String,Object> readDataMap = null;

            manCountMap.put("name","manCount");
            readCountMap.put("name","readCount");
            //遍历获取转发率
            Iterator<JSONObject> it = retJson.iterator();
            while(it.hasNext()){
                JSONObject articleObj = it.next();
                manDataMap = new HashMap<>();
                readDataMap = new HashMap<>();
                //获取日期
                manDataMap.put("name",articleObj.get("spreadDate"));
                readDataMap.put("name",articleObj.get("spreadDate"));
                //获取阅读人数和阅读次数的值
                manDataMap.put("value",articleObj.get("imgTextManCount"));
                readDataMap.put("value",articleObj.get("imgTextReadCount"));

                manCountList.add(manDataMap);
                readCountList.add(readDataMap);

            }
            manCountMap.put("value",manCountList);
            readCountMap.put("value",readCountList);
            //封装返回的list
            List retList = new ArrayList<>();
            retList.add(manCountMap);
            retList.add(readCountMap);
            return OpResult.createSucResult(retList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回异常错误码
        return OpResult.createFailResult(ErrorCodeEnum.WX_ANAARTICLETREND_EXCEPTION);
    }
}
