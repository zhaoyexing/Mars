package com.st.service;

import com.st.model.OpResult;
import java.util.List;
import java.util.Map;

/**
 * 个人用户画像
 * Created by zhaoyx on 2016/9/19.
 */
public interface SinglePersonService {
    /**
     * 用户画像 --活跃度和用户价值
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param uid   粉丝uid
     * @param tagId 标签id
     * @return
     */
    OpResult<List<Map<String, Object>>> getUserPortrait(String startDate, String endDate, String uid, String tagId);

    /**
     * 用户画像 -- 产品喜好
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param uid   粉丝uid
     * @return
     */

    OpResult<List<Map<String,Object>>> getUserProductPreference(String startDate, String endDate, String uid);

}
