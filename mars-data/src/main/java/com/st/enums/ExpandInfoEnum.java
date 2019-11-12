package com.st.enums;

/**
 * <pre>
 * </pre>
 * ExpandInfoEnum
 * Date: 2016/5/27
 * Time: 15:06
 *
 * @author zhaoyexing@social-touch.com
 */
public enum ExpandInfoEnum {
    EXP_PROVINCE(0,"省份"),
    EXP_CITY(1,"城市"),
    EXP_GENDER(2,"性别"),
    EXP_AGE(3,"年龄"),
    EXP_USERAGENT(4,"浏览器"),
    EXP_SYSTEM(5,"用户系统"),
    EXP_USER_MOBILE (6,"用户手机号"),
    EXP_USER_SHARE(7,"分享者"),
    EXP_USER_SOURCE(8,"用户来源"),
    EXP_USER_MAIL(9,"邮箱"),
    EXP_USER_QQ(10,"QQ"),
    EXP_USER_NICKNAME(11,"用户呢称"),
    EXP_OTHER_PLATFORM(12,"其他平台"),
    EXP_INTEGRAL(13,"积分"),
    EXP_LEVEL(14,"等级"),
    EXP_LOCATION_X_Y(15,"经纬度"),
    EXP_BILL_CODE(23,"产品所属单据号"),
    EXP_COUNTER_CODE(24,"部门代号，一般即为柜台号"),
    EXP_OPERATOR_CODE(25,"制单员员工代号"),
    EXP_BILL_QUANTITY(26,"销售数量，每个单子一共买了多少产品"),
    EXP_BILL_ORIGINALAMOUNT(27,"订单折前金额（已经进行过单品折扣和其他促销）"),
    EXP_BILL_DISCOUNT(28,"订单整单折扣率"),
    EXP_BILL_DISCOUNTED_AMOUNT(29,"订单折后金额（整单折扣后的金额)"),
    EXP_BILL_DECREASE_AMOUNT(30,"整单去零,在折后金额的基础上又做了扣减"),
    EXP_BILL_PAY_AMOUNT(31,"订单实收金额"),
    EXP_PRODUCT_QUANTITY(32,"数量，每种产品购买的多少"),
    EXP_PRODCT_PAY_PRICE(33,"销售价格"),
    EXP_PRODCT_DISCOUNT(34,"折扣率"),
    EXP_ACTIVITY_CODE(35,"活动代码"),
    EXP_PRODUCT_NAME_CN(36,"产品中文名称"),
    EXP_PRODUCT_CLASSNAME_1(37,"第1层分类名称，对应POS系统中的大分类名称"),
    EXP_PRODUCT_CLASSNAME_2(38,"第2层分类名称，对应POS系统中的大分类名称"),
    EXP_PRODUCT_CLASSNAME_3(39,"第3层分类名称，对应POS系统中的大分类名称"),
    EXP_PRODUCT_SALEPRICE(40,"零售价格，吊牌价"),
    EXP_PRODUCT_MEMPRICE(41,"如果是所有会员同价，即为会员价。如果是不同等级会员不同价格，则对应会员等级一的价格；"),
    EXP_PRODUCT_ISEXCHANGED(42,"是否积分兑换 0：不可 1：可以  默认为1"),
    EXP_PRODUCT_SPEC(43,"规格 20ml、30g等，注意是数值和单位混合在一起，只作为产品描述，不参与合并计算等"),
    EXP_COUNTER_KIND(44,"柜台类型 0：正式柜台  1：测试柜台  2：促销柜台 "),
    EXP_REGIN_NAME(45,"区域CODE"),
    EXP_BUS_DISTRICT(46,"商圈"),
    EXP_JOIN_DATE(47,"入会日期"),
    EXP_COUNTER_CODE_BELONG(48,"发卡柜台号"),
    EXP_ACTIVITY_MAINCODE(49,"活动的主码"),
    EXP_ACTIVITY_NAME(50,"活动名称"),
    EXP_ACTIVITY_DESCRIPTION(51,"活动描述"),
    EXP_ACTIVITY_STARTTIME(52,"活动开始时间"),
    EXP_ACTIVITY_ENDTIME(53,"活动结束时间");


    ExpandInfoEnum(int ExpandInfoCode, String ExpandInfoName) {
        this.ExpandInfoCode = ExpandInfoCode;
        this.ExpandInfoName = ExpandInfoName;
    }
    private int  ExpandInfoCode;
    private String ExpandInfoName;

    public int getExpandInfoCode() {
        return ExpandInfoCode;
    }

    public String getExpandInfoName() {
        return ExpandInfoName;
    }
}
