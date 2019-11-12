package com.st.service;

import com.st.model.ExposureModel;
import com.st.model.OpResult;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/9/12.
 * Kol统计
 */
public interface KolInfoDataService {

    //微博概览头部统计数据
    public OpResult<Map<String, Object>> microBlogHeadData(String proId);

    //微博总曝光统计走势
    public OpResult<List<ExposureModel>> microBlogExposure(String proId, String startDate, String endDate);

    //微博KOL互动情况排名
    public OpResult<List<Map<String, Object>>> microBlogChain(String proId);

    //微博KOL总曝光量统计
    public OpResult<Object> microBlogExposureNum(String proId);

    //微博页面链接pv来源
    public OpResult<Object> microBlogPage(String proId);

    //微博概览数据下载
    //public void microBlogDown(Integer proId);

    //微信概览头部统计数据
    public OpResult<Map<String, Object>> weChartHeadData(String proId);

    //微信总阅读量统计走势
    public OpResult<List<ExposureModel>> weChartReadTrend(String proId, String startDate, String endDate);

    //微信Kol点赞情况
    public OpResult<List<ExposureModel>> weChartAttitudes(String proId);

    //微信阅读量kol贡献比
    public OpResult<Object> weChartContribution(String proId);

    //微信项目概览页面链接pv来源百分比
    public OpResult<Object> weChartPage(String proId);

    //微信概览数据下载
    //public void weChartDown(Integer proId);
}
