package com.st.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaoyx on 2016/10/25.
 */
public class MarsOrderSummary {
    private Date date;
    private int notMemberCount;
    private int memberCount;
    private BigDecimal notSellPrice;
    private BigDecimal sellPrice;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNotMemberCount() {
        return notMemberCount;
    }

    public void setNotMemberCount(int notMemberCount) {
        this.notMemberCount = notMemberCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public BigDecimal getNotSellPrice() {
        return notSellPrice;
    }

    public void setNotSellPrice(BigDecimal notSellPrice) {
        this.notSellPrice = notSellPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}
