package com.st.model;

import java.sql.Timestamp;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public class MarsTags {
    private int id;
    private String appkey;
    private String tagName;
    private String tagDescription;
    private String classId;
    private String defWeight;
    private String calcType;
    private String generalTagType;
    private String generalPropName;
    private Timestamp createTime;
    private Timestamp updateTime;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDefWeight() {
        return defWeight;
    }

    public void setDefWeight(String defWeight) {
        this.defWeight = defWeight;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public String getGeneralTagType() {
        return generalTagType;
    }

    public void setGeneralTagType(String generalTagType) {
        this.generalTagType = generalTagType;
    }

    public String getGeneralPropName() {
        return generalPropName;
    }

    public void setGeneralPropName(String generalPropName) {
        this.generalPropName = generalPropName;
    }
}
