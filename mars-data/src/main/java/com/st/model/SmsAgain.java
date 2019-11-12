package com.st.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zhaoyx on 2016/9/6.
 */
public class SmsAgain {
    private String mobilePhone;
    private Timestamp createDate;
    private Date communicateTime;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Date getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(Date communicateTime) {
        this.communicateTime = communicateTime;
    }
}
