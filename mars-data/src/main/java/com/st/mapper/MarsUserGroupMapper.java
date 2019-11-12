package com.st.mapper;

import com.st.model.MarsUserGroup;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyx on 2016/11/4.
 */
public interface MarsUserGroupMapper {

    void saveUserGroupBatch(MarsUserGroup userGroup);

    List<MarsUserGroup> getUserGroups(@Param("noPage")Integer noPage,@Param("pageSize") Integer pageSize);

    void delete(@Param("id")String id);

    void updateUserGroup(MarsUserGroup userGroup);

    /**
     * 根据组id(即主键获取类对象)
     * @param groupId
     * @return
     */
    MarsUserGroup getUserGroupConditionsById(@Param("groupId")String groupId);


    Integer getUserGroupCount();
}
