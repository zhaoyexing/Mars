package com.st.model;

/**
 * 粉丝分布
 * Created by zhaoyx on 2016/9/12.
 */
public class FanAreaModel {
    private String province;//省份
    private int count;//人数
    private double countPct;//人数占比
    private double NationalPct;//全国网名占比

    public FanAreaModel(String province, int count, double countPct, double nationalPct) {
        this.province = province;
        this.count = count;
        this.countPct = countPct;
        NationalPct = nationalPct;
    }

    public FanAreaModel() {
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCountPct() {
        return countPct;
    }

    public void setCountPct(double countPct) {
        this.countPct = countPct;
    }

    public double getNationalPct() {
        return NationalPct;
    }

    public void setNationalPct(double nationalPct) {
        NationalPct = nationalPct;
    }
}
