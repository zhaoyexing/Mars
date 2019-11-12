package com.st.mapper;

import com.st.model.MarsTagClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public interface MarsTagClassMapper {
    void saveMarsTagClassBatch(List<MarsTagClass> list);
    void delete();
    /**
     * 通过父Id找子类标签
     * @param pid
     * @return
     */
    List<MarsTagClass> getMarsUserGroupConditions(@Param("pid")String pid);

    Integer selectChild(@Param("classId") String  classId);

}
