package com.st.service;

import com.st.model.OpResult;

/**
 * Created by admin on 2016/9/28.
 */
public interface WxBackstageService {
    /**
     * 昨日关键指标
     * @param startDate
     * @param endDate
     * @return
     */
    OpResult getKeyIndicator(String startDate, String endDate);

    /**
     * 数据趋势
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    OpResult getIndicatorTrend(String startDate, String endDate, String type);

    /**
     * 传播用户分析
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    OpResult getArticleSpreadData(String startDate, String endDate, String pageNum, String pageSize);

    /**
     * 文章指标概览
     * @param startDate
     * @param endDate
     * @param articleId
     * @return
     */
    OpResult getIndicatorByArticleId(String startDate, String endDate, String articleId);

    /**
     * 获取文章的传播用户分析
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @param msgId
     * @param articleId
     * @return
     */
    OpResult getArticleSpreadDataByArticleId(String startDate, String endDate, String pageNum, String pageSize, String msgId, String articleId);

    /**
     * 获取文章的数据趋势
     * @param startDate
     * @param endDate
     * @param articleId
     * @param msgId
     * @return
     */
    OpResult getIndicatorTrendByArticleId(String startDate, String endDate, String articleId, String msgId);
}
