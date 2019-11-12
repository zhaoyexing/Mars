package com.st.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * </pre>
 * PushLogVO
 * Date: 2016/5/27
 * Time: 14:22
 *
 * @author zhaoyexing@social-touch.com
 */
public class PushLogVO implements Serializable {
    private int appkey;
    private String itemid;
    private int actionid;
    private String uid;
    private int utype;
    private String clientip;
    private int itemtype;
    private int actiontime;
    private List<ExpandInfo> expand_info;

    public int getAppkey() {
        return appkey;
    }

    public void setAppkey(int appkey) {
        this.appkey = appkey;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getActionid() {
        return actionid;
    }

    public void setActionid(int actionid) {
        this.actionid = actionid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUtype() {
        return utype;
    }

    public void setUtype(int utype) {
        this.utype = utype;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public int getItemtype() {
        return itemtype;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public int getActiontime() {
        return actiontime;
    }

    public void setActiontime(int actiontime) {
        this.actiontime = actiontime;
    }

    public List<ExpandInfo> getExpand_info() {
        return expand_info;
    }

    public void setExpand_info(List<ExpandInfo> expand_info) {
        this.expand_info = expand_info;
    }

    @Override
    public String toString() {
        return "PushLogVO{" +
                "appkey='" + appkey + '\'' +
                ", itemid='" + itemid + '\'' +
                ", actionid='" + actionid + '\'' +
                ", uid='" + uid + '\'' +
                ", utype='" + utype + '\'' +
                ", clientip='" + clientip + '\'' +
                ", itemtype='" + itemtype + '\'' +
                ", actiontime='" + actiontime + '\'' +
                ", expand_info=" + expand_info +
                '}';
    }
}
