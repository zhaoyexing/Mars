package com.st.model;

/**
 * 用户实体类
 * Created by zhaoyx on 2016/9/12.
 */
public class UVModel {
    private String time;//时间
    private int increaseCount;//增长或减少人数
    private int count;//用户总数

    public UVModel(String time, int increaseCount, int count) {
        this.time = time;
        this.increaseCount = increaseCount;
        this.count = count;
    }

    public UVModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIncreaseCount() {
        return increaseCount;
    }

    public void setIncreaseCount(int increaseCount) {
        this.increaseCount = increaseCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
