package com.st.mapper;

import com.st.model.MarsOrder;
import com.st.model.MarsOrderSummary;
import com.st.model.MarsRankingSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * 订单数据
 * Created by zhaoyx on 2016/10/19.
 */
public interface MarsOrderMapper {
    /**
     * 添加订单数据
     * @param list
     */
    void addBatchOrder(List<MarsOrder> list);


    /**
     * 统计指定天的非会员数量
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,String>> getSalesPerDayCount(@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 获取所有日期的非会员数量
     * @param startDate
     * @param endDate
     * @return
     */
    List<MarsOrderSummary> getSaleAllDayCount(@Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 统计指定日期的非会员金额
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,String>> getSalesPerDayPrice(@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 获取每天的非会员金额
     * @param startDate
     * @param endDate
     * @return
     */
    List<MarsOrderSummary> getSalesAllDayPrice(@Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 获取每日商品的数据量
     * @return
     */
    List<MarsRankingSummary> getRankingCount();

    /**
     *获取旗舰店
     * @return
     */
    List<MarsOrder> getShopName();

    /**
     * 获取品牌名字
     * @return
     */
    List<MarsOrder> getBrandName();

    /**
     * 获取地域
     * @return
     */
    List<MarsOrder> getProvince();

    /**
     * 获取旗舰让销量
     * @return
     */
    List<MarsOrder> getSaleByShopName();

    int getAllCount();

    List<MarsOrder> findOrderByPage(@Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

    List<MarsOrder> findOrderByPagees(@Param("pageNo")int pageNo,@Param("pageSize")int PageSize);

}
