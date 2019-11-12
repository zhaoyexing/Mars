package com.st.mapper;

import com.st.model.MarsProductPreferences;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/11/2.
 */
public interface MarsProductPreferencesMapper {
    /**
     * 添加记录
     * @param marsProductPreferences
     */
    void addProPreferences(MarsProductPreferences marsProductPreferences);

    /**
     * 清空数据
     */
    void deleteProPreferences();

    /**
     * 查询特征name
     */
    String getCharacterName(Integer id);
}
