package com.st.service;

import com.st.model.OpResult;


/**
 * Created by zhaoyx on 2016/10/19.
 */
public interface MarsOrderService {
    /**
     * 定时任务保存标签
     */
    void saveMarsOrder();

    /**
     * 更新标签
     */
    OpResult<String> updateTags();
}
