package com.st.vo;

import java.util.List;

/**
 * 数据库中筛选条件
 * Created by zhaoyx on 2016/11/15.
 */
public class ScreeningConditionsVO {
    private String classId;
    private String name;
    private List<TagsVO> value;

    public ScreeningConditionsVO() {
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TagsVO> getValue() {
        return value;
    }

    public void setValue(List<TagsVO> value) {
        this.value = value;
    }

}
