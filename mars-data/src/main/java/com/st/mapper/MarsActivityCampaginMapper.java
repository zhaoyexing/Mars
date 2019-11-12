package com.st.mapper;

import com.st.model.MarsActivityCampagin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/10/28.
 */
public interface MarsActivityCampaginMapper {
    /**
     * 获取活动名称列表
     * @return
     */
    List<String> getActivityNameList();

    /**
     * 根据名称获取url列表
     * @param activityName
     * @return
     */
    List<MarsActivityCampagin> getUrlByActivityName(@Param("activityName")String activityName);

    /**
     * 根据id查询name
     * @param id
     * @return
     */
    String getInteractName(Integer id);
}
