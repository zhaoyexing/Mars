package com.st.service;

import com.st.model.OpResult;
import com.st.vo.ButtonVO;
import com.st.vo.UserAnalysisVO;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyx on 2016/10/26.
 */
public interface CampaginMonitorService {
    /**
     * 活动参与用户数
     * @return
     */
    OpResult<List<Map<String,Object>>> getActivityParticipationNumber(String activityName,String startDate,String endDate);

    /**
     * 页面浏览次数/浏览人数
     * @return
     */
    OpResult<List<Map<String,Object>>> getViewVisitorsNumber(String activityName,String startDate,String endDate);

    /**
     * Button点击次数/点击人数
     * @return
     */
    OpResult<Map<String,Object>> getButtonNumber(String activityName,String startDate,String endDate,int pageNum,int pageSize);

    /**
     * 传播用户与非传播用户
     * @return
     */
    OpResult<List<Map<String,Object>>> getCommunicationUser(String activityName,String startDate,String endDate);

    /**
     * 传播用户分析
     */
    OpResult<Map<String,Object>> getUserAnalysis(String activityName,String startDate,String endDate,int pageNo,int pageSize);

    /**
     * 获取活动名称列表
     * @return
     */
    OpResult<List<Map<String, String>>> getActivityNameList();

}
