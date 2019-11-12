package com.st.mapper;

import com.st.model.MarsTags;
import com.st.model.MarsTagsTmp;
import com.st.vo.UtilNameVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/11/11.
 */
public interface MarsTagsTmpMapper {

    void addMarsTagsTmBatch(List<MarsTagsTmp> marsTagsTmp);

    List<MarsTagsTmp> getMarsTagTmpList(@Param("classId")String ClassId);

    /**
     * 根据tagid获取标签
     * @param tagId
     * @return
     */
//    MarsTagsTmp getMarsTmpByTagsId(@Param("tagId") String tagId);

    /**
     * 根据id获取
     * @return
     */
    MarsTagsTmp getInteractName(Integer id);

    Integer getTagId(String id);


    List<MarsTagsTmp> getAllTags();

    void saveMarsTagsTemp(List<MarsTagsTmp> list);

}
