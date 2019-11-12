package com.st.service;

import com.st.model.OpResult;
import com.st.vo.UserDetail;

import java.util.List;
import java.util.Map;

/**
 * 生命周期
 * Created by zhaoyx on 2016/9/12.
 */
public interface LifeCycleService {
    /**
     * 获取生命周期标签
     *
     *  @return
     */
    OpResult<List<Map<String, Object>>> getLifeCycleAttribute();

    /**
     *  获取指定标签用户列表
     * @param page 当前页数
     * @param pageSize  每页的记录数
     * @param propertyValue 属性值--参考生命周期
     * @return
     */
    OpResult<Map<String,Object>> getUserList(int page,int pageSize,String propertyValue,String groupId);

    /**
     * 获取指定生命周期下的人数
     * @param propertyValue 属性名(生命周期下的)
     * @return
     */
    OpResult<Integer> getListCycleCountByProperty(String propertyValue);
}
