package com.st.service;

import com.st.model.OpResult;

/**
 * 获取粉丝总数
 * Created by zhaoyx on 2016/9/12.
 */
public interface UserCountService {
    /**
     * 获取粉丝总数
     * @return
     */
    OpResult<String> getUserCount();
}
