package com.st.service;

import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IntegerField;
import sun.security.x509.DeltaCRLIndicatorExtension;

import java.util.List;
import java.util.Map;

/**
 * 用户群组条件
 * Created by zhaoyx on 2016/11/8.
 */
public interface UserGroupService {
    /**
     * 获取筛选条件
     * @param classId
     * @return
     */
    OpResult<Map<String, Object>> getScreeningConditions();

    /**
     *
     * 获取用户群组信息
     */
    OpResult<List<Map<String, Object>>> selectUserGroup(int pageNo,int pageSize);


    /**
     *
     * 删除用户群组信息
     *
     */
    OpResult<Map<String,Object>> deleteUserGroup(String id);

    /**
     *
     * 修改用户群组信息
     */

    OpResult<String> updateUserGroup(String id,String lifeCycle,String groupName,String conditions);

    /**
     *
     * 增加用户群组信息
     *
     */
    OpResult<String> saveUserGroup(String groupName,String lifeCycle, String conditions);

    /**
     * 根据id获取群组信息
     * @param id
     * @return
     */
    MarsUserGroup getMarsUserGroup(String id);

    OpResult<String> getFileExcis(String id);
}
