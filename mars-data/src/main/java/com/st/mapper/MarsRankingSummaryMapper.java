package com.st.mapper;

import com.st.model.MarsRankingSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/10/26.
 */
public interface MarsRankingSummaryMapper {
    /**
     * 批量增加数据
     * @param list
     */
    void addMarsRankingSummaryBatch(List<MarsRankingSummary> list);

    /**
     * 查询top10产品
     * @param startDate
     * @param endDate
     * @return
     */
    List<MarsRankingSummary> getSalesTopTen(@Param("startDate")String startDate, @Param("endDate")String endDate);
    /**
     * 获取日期内销售总量
     * @param startDate
     * @param endDate
     * @return
     */
    Integer getSalesTotalCount(@Param("startDate")String startDate,@Param("endDate")String endDate);
}
