package com.st.service;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
public interface PushMemberService {

    /**
     * 保存推送过来的会员信息
     * @param paramMap
     */
    void saveMemberInfo(Map<String, Object> paramMap);

    /**
     * 同步会员数据
     */
  //  void schedSyncMemberInfo();
}
