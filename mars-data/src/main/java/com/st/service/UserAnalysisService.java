package com.st.service;

import com.st.model.FanAreaModel;
import com.st.model.OpResult;
import com.st.model.UserModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 用户分析
 * Created by zhaoyx on 2016/9/12.
 */
public interface UserAnalysisService {
    /**
     * 获取地域分布
     * @param propertyValue 属性值--参考生命周期
     * @param statisPropertyName 属性名称
     * @return
     */
    OpResult<List<FanAreaModel>> getFanAreaDistribution(String propertyValue, String statisPropertyName,String groupId);

    /**
     * 获取性别分布
     * @param propertyValue  属性值--参考生命周期
     * @param statisPropertyName 属性名称
     * @return
     */
    OpResult<List<Map<String,Object>>> getSexDistribution(String propertyValue,String statisPropertyName,String groupId);

    /**
     * 获取年龄分布
     * @param propertyValue
     *
     * @param groupId
     * @return
     */
    OpResult<List<String>> getAgeDistribution(String propertyValue, String groupId);

    /**
     * 获取婚姻状况
     * @param propertyValue
     *
     * @param groupId
     * @return
     */
    OpResult<List<String>> getMaritalStatus(String propertyValue,String groupId);

    /**
     * 有无小孩
     * @param propertyValue
     * @param groupId
     * @return
     */
    OpResult<List<String>> getBabyNum(String propertyValue,String groupId);

    /**
     * 获取兴趣爱好
     * @param propertyValue
     * @param groupId
     * @return
     */
    OpResult<List<String>> getHobby(String propertyValue,String groupId);
}
