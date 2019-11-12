package com.st.vo;

/**
 * Created by zhaoyx on 2016/9/21.
 */
public class PurchaseAmountVO {
    private String date;
    private String amount;
    private String count;

    public PurchaseAmountVO() {
    }

    public PurchaseAmountVO(String date, String amount, String count) {
        this.date = date;
        this.amount = amount;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
