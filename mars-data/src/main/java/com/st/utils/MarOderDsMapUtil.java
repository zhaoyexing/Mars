package com.st.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/3.
 */
public class MarOderDsMapUtil {
    public static final Map<String,String> fieldMap = new HashMap<>();
    static {
        fieldMap.put("bill_payment_time", "订单付款时间");
        fieldMap.put("out_tid", "外部平台单号");
        fieldMap.put("shop_name", "店铺名称");
        fieldMap.put("baby_title", "宝贝标题");
        fieldMap.put("send_num", "发货数量");
        fieldMap.put("bar_code", "条形码");
        fieldMap.put("brand_name", "品牌名称");
        fieldMap.put("actual_payable_amount", "买家实际支付金额");
        fieldMap.put("combine_barcode", "套装条形码");
        fieldMap.put("pro_num", "订货数量");
        fieldMap.put("product_type", "宝贝类型");
        fieldMap.put("tid", "订单编号");
        fieldMap.put("transaction_id", "交易编号");
        fieldMap.put("express_company", "物流公司");
        fieldMap.put("mobile_phone", "联系手机");
        fieldMap.put("delivery_time", "发货时间");
        fieldMap.put("express_no", "物流单号");
        fieldMap.put("receiver_name", "收货人姓名");
        fieldMap.put("buyer_id", "买家会员名");
        fieldMap.put("refund_renum", "退货数量");
        fieldMap.put("province", "省、直辖市");
        fieldMap.put("city", "市");
        fieldMap.put("district", "区");
        fieldMap.put("buyer_message", "买家留言");
    }
    public static String getField(String key){
        return fieldMap.get(key);
    }
}
