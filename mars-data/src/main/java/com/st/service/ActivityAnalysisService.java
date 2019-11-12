package com.st.service;

import com.st.model.OpResult;
import com.st.model.UserModel;
import com.st.vo.UtilVo;

import java.util.List;
import java.util.Map;

/**
 * 活动分析
 * Created by zhaoyx on 2016/9/13.
 */
public interface ActivityAnalysisService {
    /**
     * 互动主题分布
     *
     * 互动形式分布
     *
     * 互动利益分布
     * @return
     */
    OpResult<List<UtilVo>> getInteractiveThemeDistribution(String propertyValue, String pid, String groupId);

    /**
     * 互动意愿分布
     *
     * @return
     */
    OpResult<List<Map<String, Object>>> getInteractiveIntentionDistribution(String propertyValue, String pid, String groupId);


}
