package com.st.mapper;

import com.st.model.EBUser;
import com.st.model.MarsOrderDs;
import com.st.model.MarsOrderSummary;
import com.st.model.MarsRankingSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyx on 2017/1/22.
 */
public interface MarsOrderDsMapper {
   /**
    *添加数据
    * @param lists
     */
   void addBatch(List<? extends MarsOrderDs> lists);

   /**
    *获取Mars_order_ds表中的总记录条数
    */
   int getAllCount();
   /**
    *根据页数获取数据
    */
   List<MarsOrderDs> findOrderDsByPage(@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);
   /**
    * 获取所有日期的非会员数据
    */
   List<MarsOrderSummary> getSaleAllDayCount(@Param("startDate")String startDate, @Param("endDate")String endDate);
   /**
    * 获取每天非会员的金额
    *
    */
   List<MarsOrderSummary> getSalesAllDayPrice(@Param("startDate")String startDate,@Param("endDate")String endDate);
   /**
    * 获取每日商品的数据量
    * @return
    */
   List<MarsRankingSummary> getRankingCount();

   /**
    *根据条件查询下载任务数据
    * @param map
     * @return
     */
   List<MarsOrderDs> selectDownData(Map<String, Object> map);

   /**
    *
    * @param map
     * @return
     */
   int selectTotalData(Map<String, Object> map);

   /**
    * openId去空之后的数量
    * @return
     */
   int selectDistinctData();

   /**
    * 根据唯一标示更改push_flag
    * @param pushFlag
     */
   void updatePushFlag(@Param("pushFlag")String pushFlag);

   /**
    * 查询电商用户和微信用户的匹配数据
    * @return
     */
   List<EBUser> getEBUser(@Param("noPage")Integer noPage,@Param("pageSize") Integer pageSize);

   int getEBuserCount();

   void addEBUser(List<MarsOrderDs> list);

   void updateBarcode(String integrationId);
}
