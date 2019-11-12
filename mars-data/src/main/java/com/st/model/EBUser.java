package com.st.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by admin on 2017/2/22.
 */
public class EBUser {
    private String openId;
    private String memberName;
    private String tid;
    private String buyerId;
    private BigDecimal actualPayableAmount;
    private String receiverName;
    private String receiverAddress;
    private String mobilePhone;
    private String babyTitle;
    private String babyType;
    private String proNum;
    private String shopId;
    private String shopName;
    private String billPaymentTime;

    public String getBillPaymentTime() {
        return billPaymentTime;
    }

    public void setBillPaymentTime(String billPaymentTime) {
        this.billPaymentTime = billPaymentTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getActualPayableAmount() {
        return actualPayableAmount;
    }

    public void setActualPayableAmount(BigDecimal actualPayableAmount) {
        this.actualPayableAmount = actualPayableAmount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBabyTitle() {
        return babyTitle;
    }

    public void setBabyTitle(String babyTitle) {
        this.babyTitle = babyTitle;
    }

    public String getBabyType() {
        return babyType;
    }

    public void setBabyType(String babyType) {
        this.babyType = babyType;
    }

    public String getProNum() {
        return proNum;
    }

    public void setProNum(String proNum) {
        this.proNum = proNum;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}