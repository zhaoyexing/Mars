package com.st.vo;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/14.
 */
public class UserDatas {
    private List<UserDetail> detail ;

    private int totalRecordSize;

    public List<UserDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<UserDetail> detail) {
        this.detail = detail;
    }

    public int getTotalRecordSize() {
        return totalRecordSize;
    }

    public void setTotalRecordSize(int totalRecordSize) {
        this.totalRecordSize = totalRecordSize;
    }
}
