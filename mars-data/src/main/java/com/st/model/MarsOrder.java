package com.st.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by zhaoyx on 2016/10/19.
 */
public class MarsOrder {
    private int id;
    private String proName;
    private int sendNum;
    private String barcode;
    private String brandName;
    private BigDecimal sellPrice;
    private String combineBarcode;
    private int proNum;
    private String isCombination;
    private String tid;
    private int refundRenum;
    private Timestamp payTime;
    private String receiverMobile;
    private String  shopName;
    private String transactionId;
    private String outTid;
    private String buyerId;
    private String receiverName;
    private String province;
    private String city;
    private String district;
    private String express;
    private Timestamp deliveryTime;
    private String expressNo;
    private String integrationId;
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

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getCombineBarcode() {
        return combineBarcode;
    }

    public void setCombineBarcode(String combineBarcode) {
        this.combineBarcode = combineBarcode;
    }

    public int getProNum() {
        return proNum;
    }

    public void setProNum(int proNum) {
        this.proNum = proNum;
    }

    public String getIsCombination() {
        return isCombination;
    }

    public void setIsCombination(String isCombination) {
        this.isCombination = isCombination;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getRefundRenum() {
        return refundRenum;
    }

    public void setRefundRenum(int refundRenum) {
        this.refundRenum = refundRenum;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTid() {
        return outTid;
    }

    public void setOutTid(String outTid) {
        this.outTid = outTid;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }
}
