package com.st.mapper;

import com.st.model.BabyBarcode;
import com.st.model.MarsOrder;
import com.st.model.MarsOrderDs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/3/17.
 */
public interface MarsOrderHistoryMapper {

    /**
     * 添加并更新mars_order_history表的bay_code字段
     */
    void addOrderHistory(List<MarsOrderDs> list);

    /**
     * 对baby_barcode进行分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<BabyBarcode> findMarsOrderDsByPage(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    /**
     * 获取Baby_barcode表的总数
     * @return
     */
    int getAllCount();

    /**
     * 获取Mars_order_history表的总数
     * @return
     */
    int getCount();

    /**
     * 分页查询mars_order_histroy表的数据
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<MarsOrderDs> findOrderByPagees(int pageNo,int pageSize);

    /**
     * 根据唯一标示查询
     * @param integrationId
     * @return
     */
    MarsOrderDs findMarsOrderDsByInter(@Param("integrationId") String integrationId);

    /**
     * 分页查取唯一主键
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<String> findIntegrationId(@Param("pageNo")Integer pageNo,@Param("pageSize")Integer pageSize);

    /**
     * 根据唯一标识更改mars_order_ds表里的barcode字段
     * @param barCode
     * @param integrationId
     */
    void updateBarcode(@Param("barCode")String barCode,@Param("integrationId")String integrationId);

    /**
     * 获取历史表中的记录条数
     * @return
     */
    Integer getCounts();

    /**
     * 分页查取历史表的记录
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<MarsOrderDs> findMarsOrderDs(@Param("pageNo")Integer pageNo,@Param("pageSize")Integer pageSize);
}
