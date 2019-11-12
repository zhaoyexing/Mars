package com.st.vo;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * ExpandInfo
 * Date: 2016/5/26
 * Time: 21:54
 *
 * @author zhaoyexing@social-touch.com
 */
public class ExpandInfo implements Serializable {
    private int expkey;
    private String expvalue;
    private int exptype;

    public int getExpkey() {
        return expkey;
    }

    public void setExpkey(int expkey) {
        this.expkey = expkey;
    }

    public String getExpvalue() {
        return expvalue;
    }

    public void setExpvalue(String expvalue) {
        this.expvalue = expvalue;
    }

    public int getExptype() {
        return exptype;
    }

    public void setExptype(int exptype) {
        this.exptype = exptype;
    }

    @Override
    public String toString() {
        return "ExpandInfo{" +
                "expkey='" + expkey + '\'' +
                ", expvalue='" + expvalue + '\'' +
                ", exptype='" + exptype + '\'' +
                '}';
    }
}
