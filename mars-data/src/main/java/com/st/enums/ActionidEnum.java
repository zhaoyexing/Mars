package com.st.enums;

/**
 * <pre>
 *  用户动作
 * </pre>
 * ActionidEnum
 * Date: 2016/5/26
 * Time: 22:04
 *
 * @author zhaoyexing@social-touch.com
 */
public enum ActionidEnum {
    ACTION_ACCESS(0,"访问"),
    ACTION_BUG(1,"购买"),
    ACTION_ADD(2,"添加"),
    ACTION_DEL(3,"删除"),
    ACTION_EXCHANGE(4,"换购"),
    ACTION_REG(5,"注册"),
    ACTION_CLICK(6,"点击"),
    ACTION_SCAN(7,"扫描"),
    ACTION_SHARE(8,"分享"),
    ACTION_COLLECT(9,"收藏"),
    ACTION_MOD(10,"修改"),
    ACTION_SEARCH(11,"搜索"),
    ACTION_SORT(12,"排序"),
    ACTION_REMOVE(13,"退货"),
    ACTION_LOOK(14,"查看"),
    ACTION_UP(15,"上行"),
    ACTION_DOUM(16,"下行"),
    ACTION_SUBSCRIBE(17,"关注"),
    ACTION_BINDING(18,"绑定"),
    ACTION_CONVERT(19,"兑换");
    private int ActionCode;
    private String ActionName;

    ActionidEnum(int ActionCode, String ActionName) {
        this.ActionCode = ActionCode;
        this.ActionName = ActionName;
    }


    public int getActionCode() {
        return ActionCode;
    }

    public String getActionName() {
        return ActionName;
    }
}
