package com.st.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
public interface MarsMemberInfoMapper {
    /**
     * 保存会员信息
     * @param paramMap
     */
    void saveMemberInfo(Map<String, Object> paramMap);

    /**
     * 获取还没推送过的会员
     * @return
     */
    List<Map<String,Object>> getPushMemberInfoList();

    /**
     * 获取匹配的订单数据
     * @return
     */
    List<Map<String,Object>> getMatchOrderList();

    /**
     * 更新会员推送成功标识
     * @param successMemberList
     */
    void updateMemberPushFlag(List<Map<String, Object>> successMemberList);

    /**
     * 更新订单推送
     * @param successOrderList
     */
    void updateOrderPushFlag(List<Map<String, Object>> successOrderList);

    /**
     * 获取推送会员数据量
     * @return
     */
    int getPushMemberCount();

    /**
     * 分页获取会员数据
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> getPushMemberInfoByPage(Map paramMap);
}
