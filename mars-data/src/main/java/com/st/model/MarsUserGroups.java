package com.st.model;

import java.sql.Timestamp;

/**
 * Created by admin on 2016/11/24.
 */
public class MarsUserGroups {
    private Integer id;
    private String groupName;
    private String queryCondition;
    private String lifeCycle;

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getLifeCycle() {

        return lifeCycle;
    }

    private String createTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public String getCreateTime() {
        return createTime;
    }
}
