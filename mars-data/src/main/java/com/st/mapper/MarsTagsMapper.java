package com.st.mapper;

import com.st.model.MarsTags;
import com.st.model.MarsTagsTmp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public interface MarsTagsMapper {
    void saveMarsTagsBatch(List<MarsTags> list);

    void deleteDate();

    List<MarsTagsTmp> getMarsTagsTmp(@Param("classId")String classId);

    List<MarsTagsTmp> getMarsTagsTmpList();

}
