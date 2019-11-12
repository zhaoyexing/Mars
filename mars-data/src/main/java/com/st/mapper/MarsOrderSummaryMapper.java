package com.st.mapper;

import com.st.model.MarsOrderSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/10/25.
 */
public interface MarsOrderSummaryMapper {
    /**
     * 保存非会员数量
     * @param list
     */
    void saveNotMemberCountBatch(List<MarsOrderSummary> list);

    /**
     * 保存会员数量
     * @param list
     */
    void saveMemberCountBatch(List<MarsOrderSummary> list);

    /**
     * 保存非会员金额
     * @param list
     */
    void saveNotSellPriceBatch(List<MarsOrderSummary> list);

    /**
     * 保存会员金额
     * @param list
     */
    void saveSellPriceBatch(List<MarsOrderSummary> list);

    /**
     * 获取指定日期汇总数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<MarsOrderSummary> getMarsOrderSummaryForDate(@Param("startDate")String startDate,@Param("endDate")String endDate);

}
