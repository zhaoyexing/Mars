package com.st.service;

/**
 * Created by admin on 2017/3/17.
 */
public interface MarsOrderHistoryService {
    /**
     * 更新bar_code字段
     */
    void updateBarCode();

    /**
     * 向mars_order_ds表中添加历史数据
     */
    void addMarsOrderDsCount();


}
