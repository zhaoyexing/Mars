package com.st.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by zhaoyx on 2017/1/22.
 */
public class MarsOrderDs {
    private String tid;
    private String buyerId;
    private String alipayAccount;
    private BigDecimal payablePayment;
    private BigDecimal payableEms;
    private int payableIntegral;
    private BigDecimal totalAmount;
    private int returnIntegral;
    private int actualPayableIntegral;
    private BigDecimal actualPayableAmount;
    private String orderStatus;
    private String buyerMessage;
    private String receiverName;
    private String receiverAddress;
    private String transportMode;
    private String mobilePhone;
    private String telPhone;
    private Timestamp billCreateTime;
    private Timestamp billPaymentTime;
    private String babyTitle;
    private String babyType;
    private String expressNo;
    private String expressCompany;
    private String orderComment;
    private int babyCount;
    private String shopId;
    private String shopName;
    private String combineBarcode;
    private int proNum;
    private String productType;
    private String orderType;
    private String transactionId;
    private String originCode;
    private String destCode;
    private int resultNum;
    private Timestamp deliveryTime;
    private BigDecimal totalNum;
    private String bigMarker;
    private int refundRenum;
    private String paymentStatus;
    private String sendStatus;
    private String orderNetWeight;
    private String grossOrders;
    private String productId;
    private String specifications;
    private String suitDetail;
    private String shopProductName;
    private String shopSpecifications;
    private String premiumQuantity;
    private int productQuotation;
    private String totalProductWeight;
    private int productReturnQuantity;
    private Timestamp receiptTime;
    private BigDecimal discountAmount;
    private String tradeSource;
    private String platformType;
    private String terminalType;
    private String transactionNumber;
    private String province;
    private String city;
    private String district;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String integrationId;
    private int sendNum;

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setPlatformPreference(String platformPreference) {
        this.platformPreference = platformPreference;
    }

    public void setOutTid(String outTid) {
        this.outTid = outTid;
    }

    public String getPlatformPreference() {

        return platformPreference;
    }

    public String getOutTid() {
        return outTid;
    }

    private String platformPreference;
    private String outTid;
    private String barCode;
    private String brandName;
    private BigDecimal proAllAccount;
    private BigDecimal proSaleAccount;
    private BigDecimal proDeductible;
    private BigDecimal proDealPrice;
    private String businessPushFlag;

    public void setBusinessPushFlag(String businessPushFlag) {
        this.businessPushFlag = businessPushFlag;
    }

    public String getBusinessPushFlag() {

        return businessPushFlag;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setProAllAccount(BigDecimal proAllAccount) {
        this.proAllAccount = proAllAccount;
    }

    public void setProSaleAccount(BigDecimal proSaleAccount) {
        this.proSaleAccount = proSaleAccount;
    }

    public void setProDeductible(BigDecimal proDeductible) {
        this.proDeductible = proDeductible;
    }

    public void setProDealPrice(BigDecimal proDealPrice) {
        this.proDealPrice = proDealPrice;
    }

    public String getBarCode() {

        return barCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public BigDecimal getProAllAccount() {
        return proAllAccount;
    }

    public BigDecimal getProSaleAccount() {
        return proSaleAccount;
    }

    public BigDecimal getProDeductible() {
        return proDeductible;
    }

    public BigDecimal getProDealPrice() {
        return proDealPrice;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

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

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public BigDecimal getPayablePayment() {
        return payablePayment;
    }

    public void setPayablePayment(BigDecimal payablePayment) {
        this.payablePayment = payablePayment;
    }

    public BigDecimal getPayableEms() {
        return payableEms;
    }

    public void setPayableEms(BigDecimal payableEms) {
        this.payableEms = payableEms;
    }

    public int getPayableIntegral() {
        return payableIntegral;
    }

    public void setPayableIntegral(int payableIntegral) {
        this.payableIntegral = payableIntegral;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getReturnIntegral() {
        return returnIntegral;
    }

    public void setReturnIntegral(int returnIntegral) {
        this.returnIntegral = returnIntegral;
    }

    public int getActualPayableIntegral() {
        return actualPayableIntegral;
    }

    public void setActualPayableIntegral(int actualPayableIntegral) {
        this.actualPayableIntegral = actualPayableIntegral;
    }

    public BigDecimal getActualPayableAmount() {
        return actualPayableAmount;
    }

    public void setActualPayableAmount(BigDecimal actualPayableAmount) {
        this.actualPayableAmount = actualPayableAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
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

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public Timestamp getBillCreateTime() {
        return billCreateTime;
    }

    public void setBillCreateTime(Timestamp billCreateTime) {
        this.billCreateTime = billCreateTime;
    }

    public Timestamp getBillPaymentTime() {
        return billPaymentTime;
    }

    public void setBillPaymentTime(Timestamp billPaymentTime) {
        this.billPaymentTime = billPaymentTime;
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

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public int getBabyCount() {
        return babyCount;
    }

    public void setBabyCount(int babyCount) {
        this.babyCount = babyCount;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestCode() {
        return destCode;
    }

    public void setDestCode(String destCode) {
        this.destCode = destCode;
    }

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public BigDecimal getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(BigDecimal totalNum) {
        this.totalNum = totalNum;
    }

    public String getBigMarker() {
        return bigMarker;
    }

    public void setBigMarker(String bigMarker) {
        this.bigMarker = bigMarker;
    }

    public int getRefundRenum() {
        return refundRenum;
    }

    public void setRefundRenum(int refundRenum) {
        this.refundRenum = refundRenum;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getOrderNetWeight() {
        return orderNetWeight;
    }

    public void setOrderNetWeight(String orderNetWeight) {
        this.orderNetWeight = orderNetWeight;
    }

    public String getGrossOrders() {
        return grossOrders;
    }

    public void setGrossOrders(String grossOrders) {
        this.grossOrders = grossOrders;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSuitDetail() {
        return suitDetail;
    }

    public void setSuitDetail(String suitDetail) {
        this.suitDetail = suitDetail;
    }

    public String getShopProductName() {
        return shopProductName;
    }

    public void setShopProductName(String shopProductName) {
        this.shopProductName = shopProductName;
    }

    public String getShopSpecifications() {
        return shopSpecifications;
    }

    public void setShopSpecifications(String shopSpecifications) {
        this.shopSpecifications = shopSpecifications;
    }

    public String getPremiumQuantity() {
        return premiumQuantity;
    }

    public void setPremiumQuantity(String premiumQuantity) {
        this.premiumQuantity = premiumQuantity;
    }

    public int getProductQuotation() {
        return productQuotation;
    }

    public void setProductQuotation(int productQuotation) {
        this.productQuotation = productQuotation;
    }

    public String getTotalProductWeight() {
        return totalProductWeight;
    }

    public void setTotalProductWeight(String totalProductWeight) {
        this.totalProductWeight = totalProductWeight;
    }

    public int getProductReturnQuantity() {
        return productReturnQuantity;
    }

    public void setProductReturnQuantity(int productReturnQuantity) {
        this.productReturnQuantity = productReturnQuantity;
    }

    public Timestamp getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Timestamp receiptTime) {
        this.receiptTime = receiptTime;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(String tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
