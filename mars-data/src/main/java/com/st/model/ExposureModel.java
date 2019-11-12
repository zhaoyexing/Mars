package com.st.model;

/**
 * Created by admin on 2016/9/21.
 */
public class ExposureModel {
    private String name;
    private Long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public ExposureModel(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public ExposureModel() {
    }
}
