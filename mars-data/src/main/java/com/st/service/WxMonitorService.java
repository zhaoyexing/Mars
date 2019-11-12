package com.st.service;

import com.st.model.OpResult;

import java.util.List;
import java.util.Map;

/**
 * 招募统计
 * Created by zhangzq on 2016/9/22.
 */
public interface WxMonitorService {
    /**
     * 获取当前粉丝数和累计粉丝数
     * @return
     */
    OpResult<Map<String,Object>> wxFansCount ();

    /**
     * 获取新增流失净增粉丝数量
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param dataCycle
     * @return
     */
    OpResult<List<Map<String,Object>>> getFansCountRange(String startDate, String endDate, String dataCycle);

    /**
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param menuLevel 菜单级别
     * @return
     */
    OpResult<List<Map<String,Object>>> menuClickDist(String startDate, String endDate, String menuLevel);

    /**
     *
     * @param menuLevel 菜单级别
     * @return
     */
    OpResult<List<Map>> getMenuList (String menuLevel);

    /**
     * 获取菜单点击趋势
     * @param startDate
     * @param endDate
     * @param menuName
     * @param menuLevel @return
     * @param dataCycle
     */
    OpResult getMenuClickTrend(String startDate, String endDate, String menuName, String menuLevel, String dataCycle);

    OpResult getFansFollowChannelCount();
}
