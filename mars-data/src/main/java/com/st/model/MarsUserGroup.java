package com.st.model;


import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户群组
 * Created by zhaoyx on 2016/11/4.
 */
public class MarsUserGroup {
    private Integer id;
    private String groupName;
    private String queryCondition;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String lifeCycle;

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public MarsUserGroup(String groupName, String queryCondition, Timestamp createTime, Timestamp updateTime,String lifeCycle) {
        this.groupName = groupName;
        this.queryCondition = queryCondition;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.lifeCycle = lifeCycle;
    }

    public MarsUserGroup(Integer id, String groupName, String queryCondition, Timestamp updateTime,String lifeCycle) {
        this.id = id;
        this.groupName = groupName;
        this.queryCondition = queryCondition;
        this.updateTime = updateTime;
        this.lifeCycle = lifeCycle;
    }

    public MarsUserGroup() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
