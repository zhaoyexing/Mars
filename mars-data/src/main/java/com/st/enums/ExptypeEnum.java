package com.st.enums;

/**
 * <pre>
 * </pre>
 * ExptypeEnum
 * Date: 2016/5/26
 * Time: 22:25
 *
 * @author zhaoyexing@social-touch.com
 */
public enum ExptypeEnum {

    EXP_TYPE_STAT(0,"计数型统计"),
    EXP_TYPE_NOT_STAT(1,"只存储，不统计");
    ExptypeEnum(int ExpTypeCode, String ExpTypeName) {
        this.ExpTypeCode = ExpTypeCode;
        this.ExpTypeName = ExpTypeName;
    }
    private int  ExpTypeCode;
    private String ExpTypeName;


    public int getExpTypeCode() {
        return ExpTypeCode;
    }

    public String getExpTypeName() {
        return ExpTypeName;
    }
}
