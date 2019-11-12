package com.st.model;

/**
 * Created by admin on 2017/2/16.
 */
public class UserGroup {
    private String openId;
    private String nikeName;
    private String provice;
    private String uId;

    @Override
    public String toString() {
        return "UserGroup{" +
                "openId='" + openId + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", provice='" + provice + '\'' +
                ", uId='" + uId + '\'' +
                '}';
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
