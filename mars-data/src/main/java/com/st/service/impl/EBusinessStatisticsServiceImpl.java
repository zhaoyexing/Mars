package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsDownloadDMapper;
import com.st.mapper.MarsOrderDsMapper;
import com.st.mapper.MarsOrderSummaryMapper;
import com.st.mapper.MarsRankingSummaryMapper;
import com.st.model.*;
import com.st.service.EBusinessStatisticsService;
import com.st.utils.DateUtil;
import com.st.utils.MapUtils;
import com.st.utils.MarOderDsMapUtil;
import com.st.vo.BusinessBrandVO;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by zhaoyx on 2016/9/21.
 */
@Service
public class EBusinessStatisticsServiceImpl implements EBusinessStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(EBusinessStatisticsServiceImpl.class);
    @Value("${e.business.url}")
    private String urlValue;
    @Value("${local.business}")
    private String saveUrl;
    @Autowired
    private MarsOrderSummaryMapper marsOrderSummaryMapper;
    @Autowired
    private MarsRankingSummaryMapper marsRankingSummaryMapper;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;
    @Autowired
    private MarsDownloadDMapper marsDownloadDMapper;

    private FileOutputStream os;

    /**
     * 销售量排名
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @Override
    public OpResult<List<BusinessBrandVO>> getSalesRanking(String startDate, String endDate) {
        List<BusinessBrandVO> brands = new ArrayList<>();
        try {
            List<MarsRankingSummary> marsOrdersList = marsRankingSummaryMapper.getSalesTopTen(startDate, endDate);
            Integer count = marsRankingSummaryMapper.getSalesTotalCount(startDate, endDate);
            if (null != count) {
                for (MarsRankingSummary marsRankingSummary : marsOrdersList) {
                    BusinessBrandVO businessBrandVO = new BusinessBrandVO();
                    businessBrandVO.setBrandName(marsRankingSummary.getProName());
                    businessBrandVO.setSaleCount(marsRankingSummary.getSendNum());
                    NumberFormat nt = NumberFormat.getPercentInstance();
                    nt.setMinimumFractionDigits(2);
                    businessBrandVO.setSaleCountPct(nt.format((double) marsRankingSummary.getSendNum() / (double) count));
                    brands.add(businessBrandVO);
                }
            }
            return OpResult.createSucResult(brands);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }

    /**
     * 消费金额汇总
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @Override
    public OpResult<List<Map<String, Object>>> getPurchaseAmount(String startDate, String endDate) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        try {
            List<MarsOrderSummary> marsOrderSummaryList = marsOrderSummaryMapper.getMarsOrderSummaryForDate(startDate, endDate);
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            List<Map<String, Object>> list1 = new ArrayList<>();
            map.put("name", "非会员");
            for (MarsOrderSummary m : marsOrderSummaryList) {
                Map<String, Object> maptemp = new HashMap<>();
                maptemp.put("date", DateUtil.dateConvertToStr(m.getDate(), "yyyy-MM-dd"));
                maptemp.put("count", m.getNotMemberCount());
                maptemp.put("amount", m.getNotSellPrice());
                list.add(maptemp);
                maptemp = new HashMap<>();
                maptemp.put("date", DateUtil.dateConvertToStr(m.getDate(), "yyyy-MM-dd"));
                maptemp.put("count", m.getMemberCount());
                maptemp.put("amount", m.getSellPrice());
                list1.add(maptemp);
            }
            map.put("value", list);
            returnList.add(map);
            map = new HashMap<>();
            map.put("name", "会员");
            map.put("value", list1);
            returnList.add(map);
            return OpResult.createSucResult(returnList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_PORTRAIT_EXCEPTION);
    }

    /**
     * 品牌、地区
     *
     * @return
     */
    @Override
    public OpResult<Map<String, Object>> getBrandAndArea() {
        String[] brands = {"德芙", "士力架", "M&MS", "脆米香", "麦提沙"};
        String[] areas = {"上海", "广州", "天津", "北京", "河北", "河南", "山东", "吉林", "辽宁", "江苏", "安徽", "云南", "福建", "浙江", "湖北",
                "湖南", "江西", "陕西", "山西", "四川", "青海", "海南", "广东", "贵州", "广西", "新疆", "台湾", "宁夏", "香港", "甘肃", "澳门", "西藏",
                "黑龙江", "内蒙古"};
        Map<String, Object> resultMap;
        try {
            resultMap = new HashMap<>();
            resultMap.put("brands", brands);
            resultMap.put("areas", areas);
            return OpResult.createSucResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.BRAND_AREA_EXCEPTION);
    }

    /**
     * 字段维度
     *
     * @return
     */
    @Override
    public OpResult getFields() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        try {

            fieldMap.put("transaction_id", "交易编号");
            fieldMap.put("tid", "订单编号");
            fieldMap.put("brand_name", "品牌名称");
            fieldMap.put("baby_title", "宝贝标题");
            fieldMap.put("product_type", "宝贝类型");
            fieldMap.put("pro_num", "订货数量");
            fieldMap.put("send_num", "发货数量");
            fieldMap.put("delivery_time", "发货时间");
            fieldMap.put("refund_renum", "退货数量");
            fieldMap.put("actual_payable_amount", "买家实际支付金额");
            fieldMap.put("bill_payment_time", "订单付款时间");
            fieldMap.put("receiver_name", "收货人姓名");
            fieldMap.put("buyer_id", "买家会员名");
            fieldMap.put("mobile_phone", "联系手机");
            fieldMap.put("province", "省、直辖市");
            fieldMap.put("city", "市");
            fieldMap.put("district", "区");
            fieldMap.put("buyer_message", "买家留言");
            fieldMap.put("bar_code", "条形码");
            fieldMap.put("combine_barcode", "套装条形码");
            fieldMap.put("out_tid", "外部平台单号");
            fieldMap.put("express_company", "物流公司");
            fieldMap.put("express_no", "物流单号");
            fieldMap.put("shop_name", "店铺名称");
            //将数据加入list中
            MapUtils.getaVoid(fieldMap, list);
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.FIELD_DATA_EXCEPTION);
    }

    /**
     * 下载任务接口
     *
     * @return
     */
    @Override
    public OpResult getEleColunmData(String jsonData) {
        String messageResult = "";
        List<String> conList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonData);
            String startData = (String) jsonObject.get("startData"); //开始日期
            String endData = (String) jsonObject.get("endData");//结束日期
            String fileName = (String) jsonObject.get("fileName");//文件名字(前台传过来的)
            String userName = (String) jsonObject.get("userName");//登录用户名

            List<String> brandName = null; //品牌名
            List<String> areas = null; //地域
            List<String> fieldName = null; //字段名
            if (!"全部".equals(((List<String>) jsonObject.get("brandName")).get(0)))
                brandName = (List<String>) jsonObject.get("brandName");
            if (!"全部".equals(((List<String>) jsonObject.get("areas")).get(0)))
                areas = (List<String>) jsonObject.get("areas");
            if (!"全部".equals(((List<String>) jsonObject.get("fields")).get(0)))
                fieldName = (List<String>) jsonObject.get("fields");

            List<String> columnList;//取列名
            List<String> fields;
            if (null != fieldName && fieldName.size() > 0) {
                columnList = fieldName.stream().map(field -> MarOderDsMapUtil.getField(field)).collect(Collectors.toList());
                //字段条件
                fields = fieldName.stream().map(field -> field + " " + lineToHump(field)).collect(Collectors.toList());
            } else {
                fieldName = MarOderDsMapUtil.fieldMap.keySet().stream().collect(Collectors.toList());
                columnList = MarOderDsMapUtil.fieldMap.values().stream().collect(Collectors.toList());
                fields = MarOderDsMapUtil.fieldMap.keySet().stream().map(field -> field + " " + lineToHump(field)).collect(Collectors.toList());
            }

            File file = new File(saveUrl + fileName + ".xlsx");
            if (!file.exists()) {
                /** -----------------拼接查询条件--------------------*/
                String condition = "where 1=1";
                String returnBrandName;
                String returnAreas;
                String conResult = "";
                conList.add(condition);
                if (brandName != null && brandName.size() > 0) {
                    brandName = brandName.stream().map(brand -> "brand_name='" + brand + "'").collect(Collectors.toList());
                    returnBrandName = String.join(" or ", brandName);
                    conList.add("(" + returnBrandName + ")");
                    conResult = String.join(" and ", conList);
                }
                if (areas != null && areas.size() > 0) {
                    areas = areas.stream().map(area -> "province='" + area + "'").collect(Collectors.toList());
                    returnAreas = String.join(" or ", areas);
                    conList.add("(" + returnAreas + ")");
                    conResult = String.join(" and ", conList);
                }
                if (startData != null && !"".equals(startData)) {
                    startData = "bill_payment_time >='" + startData + "'";
                    conList.add(startData);
                    conResult = String.join(" and ", conList);
                }
                if (endData != null && !"".equals(endData)) {
                    endData = "bill_payment_time <='" + endData + "'";
                    conList.add(endData);
                    conResult = String.join(" and ", conList);
                }
                /**--------------------------------------------*/

                map.put("conResult", conResult);
                map.put("fields", String.join(",", fields));

                //添加一条下载任务记录
                MarsDownloadD marsDownloadD = new MarsDownloadD();
                marsDownloadD.setCreateUser(userName);
                marsDownloadD.setDownloadName(fileName);
                marsDownloadD.setCreateTime(new Timestamp(new Date().getTime()));
                marsDownloadD.setJsonData(jsonData);
                marsDownloadD.setFileStatus("未完成");
                marsDownloadD.setCount(0);
                marsDownloadD.setCompletePre("0%");
                marsDownloadDMapper.addMarsDownload(marsDownloadD);
                logger.info("Add a download record!!!");
                DownloadExcelRunnable downloadExcelRunnable = new DownloadExcelRunnable(map,fields,columnList,marsDownloadD.getId(),fieldName,fileName,saveUrl);
                new Thread(downloadExcelRunnable).start();
                messageResult = "The file has been generated.Successed!!!";
            } else {
                messageResult = "File already exists!!!";
            }
            return OpResult.createSucResult(messageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.DOWNLOAD_DATA_EXCEPTION);
    }


    /**
     * 查询电商数据下载列表
     *
     * @return
     */
    @Override
    public OpResult getDownloadDList(Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        int totalPageNum = 0;//总页数
        map.put("pageIndex", (pageNum - 1) * pageSize);
        map.put("pageSize", pageSize);
        try {
            int totalCount = marsDownloadDMapper.getDownloadCount();
            logger.info("The Data total are {}", totalCount);
            List<MarsDownloadD> downloadDList = marsDownloadDMapper.getDownloadDList(map);
//            if (totalCount % pageSize > 0) {
//                totalPageNum = (totalCount / pageSize) + 1;
//            } else if (totalCount % pageSize == 0) {
//                totalPageNum = totalCount / pageSize;
//            }
            resultMap.put("downloadDList", downloadDList);
            resultMap.put("totalPageNum", totalCount);
            return OpResult.createSucResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.DOWNLOADLIST_DATA_EXCEPTION);
    }

    /**
     * 删除文件接口
     *
     * @param fileName
     * @return
     */
    @Override
    public OpResult deleteFile(String fileName) {
        try {
            File file = new File(saveUrl + fileName + ".xlsx");
            if (file.exists()) {
                file.delete();
            }
            marsDownloadDMapper.deleteFile(fileName);
            return OpResult.createSucResult("Deleted success!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.DELETE_FILE_EXCEPTION);
    }

    /**
     * 根据文件名查询这条记录的json数据
     *
     * @param fileName
     * @return
     */
    @Override
    public MarsDownloadD selectJsonData(String fileName) {
        return marsDownloadDMapper.selectJsonData(fileName);
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

//    private List<Map<String, Object>> createOrderDsExcel(List<MarsOrderDs> orderDsList) {
//        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("sheetName", "sheet1");
//        listmap.add(map);
//        for (MarsOrderDs orderDs : orderDsList) {
//            Map<String, Object> mapValue = new HashMap<>();
//            mapValue.put("bill_payment_time", orderDs.getBillPaymentTime());
//            mapValue.put("out_tid", orderDs.getOutTid());
//            mapValue.put("shop_name", orderDs.getShopName());
//            mapValue.put("baby_title", orderDs.getBabyTitle());
//            mapValue.put("send_num", orderDs.getSendNum());
//            mapValue.put("bar_code", orderDs.getBarCode());
//            mapValue.put("brand_name", orderDs.getBrandName());
//            mapValue.put("actual_payable_amount", orderDs.getActualPayableAmount());
//            mapValue.put("combine_barcode", orderDs.getCombineBarcode());
//            mapValue.put("pro_num", orderDs.getProNum());
//            mapValue.put("product_type", orderDs.getProductType());
//            mapValue.put("tid", orderDs.getTid());
//            mapValue.put("transaction_id", orderDs.getTransactionId());
//            mapValue.put("express_company", orderDs.getExpressCompany());
//            mapValue.put("mobile_phone", orderDs.getMobilePhone());
//            mapValue.put("delivery_time", orderDs.getDeliveryTime());
//            mapValue.put("express_no", orderDs.getExpressNo());
//            mapValue.put("receiver_name", orderDs.getReceiverName());
//            mapValue.put("buyer_id", orderDs.getBuyerId());
//            mapValue.put("refund_renum", orderDs.getRefundRenum());
//            mapValue.put("province", orderDs.getProvince());
//            mapValue.put("city", orderDs.getCity());
//            mapValue.put("district", orderDs.getDistrict());
//            mapValue.put("buyer_message", orderDs.getBuyerMessage());
//            listmap.add(mapValue);
//        }
//        return listmap;
//    }

    /**
     * double转百分比,保留两位小数
     *
     * @param param
     * @return
     */
//    private String twopercent(Double param) {
//        String perStr = "";
//        if (!StringUtils.isEmpty(param)) {
//            NumberFormat fmt = NumberFormat.getPercentInstance();
//            fmt.setMinimumFractionDigits(0);
//            perStr = fmt.format(param);
//        }
//        return perStr;
//    }


}