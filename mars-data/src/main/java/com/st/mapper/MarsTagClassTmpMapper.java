package com.st.mapper;

import com.st.model.MarsTagClassTmp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/11/11.
 */
public interface MarsTagClassTmpMapper {

    List<String> getAllClassId();

    List<MarsTagClassTmp> getMarsTagClassTmpList(@Param("pid")Integer pid);

    void saveMarsTagClassTmpBatch(List<MarsTagClassTmp> list);
}
