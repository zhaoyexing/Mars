package com.st.service;

import com.st.model.OpResult;
import com.st.model.UVModel;

import java.util.List;

/**
 * 各小时粉丝数量变动
 * Created by zhaoyx on 2016/9/12.
 */
public interface UserUVService {
    /**
     * 获取各小时UV
     * @return
     */
    OpResult<List<UVModel>> getUVCount();

    /**
     * 获取电商匹配数据
     * @return
     */
    OpResult<String> downLoadEBUser();

}
