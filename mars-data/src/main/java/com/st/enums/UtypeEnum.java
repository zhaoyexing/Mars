package com.st.enums;

/**
 * <pre>
 *     用户类型
 * </pre>
 * UtypeEnum
 * Date: 2016/5/26
 * Time: 22:32
 *
 * @author zhaoyexing@social-touch.com
 */
public enum UtypeEnum {
    USER_WEIBO(0,"微博用户"),
    USER_WEIXIN(1,"微信用户"),
    USER_PC_WEBSITE(2,"pc页面用户"),
    USER_WAP_WEBSITE(3,"wap页面用户"),
    USER_MEMBER(4,"客户会员");
    private int UtypeCode;
    private String UtypeName;
    UtypeEnum(int utypeCode, String utypeName) {
        UtypeCode = utypeCode;
        UtypeName = utypeName;
    }

    public int getUtypeCode() {
        return UtypeCode;
    }

    public String getUtypeName() {
        return UtypeName;
    }

}
