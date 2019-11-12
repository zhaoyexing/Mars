package com.st.enums;

/**
 * Created by zhaoyx on 2016/9/19.
 */
public enum ErrorCodeEnum {
    SUC(0,"操作成功"),
    SPREAD_INFLUENCE_EXCEPTION(1,"传播影响力接口异常"),
    SPREAD_INGTENDENCY_EXCEPTION(2,"传播内容倾向接口异常"),
    UV_COUNT_EXCEPTION(3,"每小时UV接口异常"),
    USER_COUNT_EXCEPTION(4,"获取用户总数接口异常"),
    AREA_DISTRIBUTION_EXCEPTION(5,"粉丝地域分布接口异常"),
    SEX_DISTRIBUTION_EXCEPTION(6,"性别分布接口异常"),
    BRAND_CHARACTERISTICS_EXCEPTION(7,"品牌特征接口异常"),
    PRODUCT_CHARACTERISTICS_EXCEPTION(8,"产品特征接口异常"),
    Flavor_CHARACTERISTICS_EXCEPTION(9,"口味特征接口异常"),
    PACKAGING_CHARACTERISTICS_EXCEPTION(10,"口味特征接口异常"),
    USER_LIST_EXCEPTION(11,"用户列表接口异常"),
    LIFECYCLE_EXCEPTION(12,"生命周期接口异常"),
    INTERACTIVE_THEME_EXCEPTION(13,"生命周期接口异常"),
    USER_PORTRAIT_EXCEPTION(14,"个人用户画像接口异常"),
    MICROBLOG_HEAD_EXCEPTION(101,"微博头部数据接口异常"),
    MICROBLOG_EXPOSURE_EXCEPTION(102,"微博总曝光统计接口异常"),
    MICROBLOG_INTERACT_EXCEPTION(103,"微博KOL互动接口异常"),
    MICROBLOG_CONTRIBUTION_EXCEPTION(104,"微博总曝光KOL接口异常"),
    MICROBLOG_SURLPV_EXCEPTION(105,"微博页面链接PV接口异常"),
    WECHART_HEAD_EXCEPTION(106,"微信头部统计接口异常"),
    WECHART_READCOVER_EXCEPTION(107,"微信总阅读量统计接口异常"),
    WECHART_ATTITUDES_EXCEPTION(108,"微信KOL点赞接口异常"),
    WECHART_CONTRIBUTION_EXCEPTION(109,"微信总曝光量接口异常"),
    WECHART_SURLPV_EXCEPTION(110,"微信页面链接PV接口异常"),

    WX_ANAKEY_EXCEPTION(120,"昨日关键指标接口异常"),
    WX_ANATREND_EXCEPTION(121,"数据趋势接口异常"),
    WX_ANASPREAD_EXCEPTION(122,"传播用户分析接口异常"),
    WX_ANASURVEY_EXCEPTION(123,"传播指标概览接口异常"),
    WX_ANAARTICLETREND_EXCEPTION(124,"文章数据趋势接口异常"),
    WX_ANAARTICLESPREAD_EXCEPTION(125,"文章传播用户分析接口异常"),

    WX_MONITOR_FANSCOUNT_EXCEPTION(130,"当前粉丝-累计粉丝"),
    WX_MONITOR_FANSCHANNEL_EXCEPTION(131,"来源渠道统计"),
    WX_MONITOR_MENUCLICKDIST_EXCEPTION(132,"菜单点击分布"),
    WX_MONITOR_FANSRANGE_EXCEPTION(133,"获取新增-流失-净增粉丝"),
    WX_MONITOR_GETMENULIST_EXCEPTION(134,"获取菜单列表"),
    WX_MONITOR_MENUCLICKTREND_EXCEPTION(135,"菜单点击趋势"),

    WX_USER_NUMBER_EXCEPTION(141,"微信用户数量接口异常"),
    ELE_USER_NUMBER_EXCEPTION(142,"电商用户数量接口异常"),
    BRAND_AREA_EXCEPTION(143,"品牌、地区接口异常"),
    FIELD_DATA_EXCEPTION(144,"字段维度接口异常"),
    DOWNLOAD_DATA_EXCEPTION(145,"下载任务接口异常"),
    DOWNLOADLIST_DATA_EXCEPTION(146,"电商数据下载列表接口异常"),
    DELETE_FILE_EXCEPTION(147,"电商数据下载文件删除接口异常");



   private int code;
    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
