package com.st.vo;

/**
 * 电商
 * Created by zhaoyx on 2016/9/21.
 */
public class BusinessBrandVO {
    private String brandName;
    private int saleCount;
    private String saleCountPct;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public String getSaleCountPct() {
        return saleCountPct;
    }

    public void setSaleCountPct(String saleCountPct) {
        this.saleCountPct = saleCountPct;
    }
}
