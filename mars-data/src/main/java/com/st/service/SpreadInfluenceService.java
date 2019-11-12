package com.st.service;

import com.st.model.OpResult;
import com.st.vo.UtilVo;

import java.util.List;
import java.util.Map;

/**
 * 传播影响力
 * Created by zhaoyx on 2016/9/13.
 */
public interface SpreadInfluenceService {
    /**
     * 获取传播影响力
     * @param propertyValue 属性值--参考生命周期
     * @return
     */
    OpResult<List<Map<String,Object>>> getSpreadInfluence(String propertyValue,String pid,String groupId);

    /**
     * 传播内容倾向
     * @param propertyValue 属性值--参考生命周期
     * @param pid 父结点
     * @return
     */
    OpResult<List<UtilVo>> getSpreadingTendency(String propertyValue, String pid, String groupId);

}
