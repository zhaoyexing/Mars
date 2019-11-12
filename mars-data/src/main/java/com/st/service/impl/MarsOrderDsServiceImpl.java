package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsOrderDsMapper;
import com.st.mapper.MarsOrderMapper;
import com.st.mapper.MarsOrderSummaryMapper;
import com.st.mapper.MarsRankingSummaryMapper;
import com.st.model.*;
import com.st.service.MarsOrderDsService;
import com.st.service.SearchTagsByClassService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.SendDataDsToOLAP;
import com.st.vo.ItemVO;
import com.st.vo.OrderDataVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.assertj.core.util.Compatibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2017/1/23.
 */
@Service
public class MarsOrderDsServiceImpl implements MarsOrderDsService {
    private static Logger logger = LoggerFactory.getLogger(MarsOrderDsServiceImpl.class);
    @Value("${e.business.url}")
    private String urlValue;
    @Value("${e.snickers.url}")
    private String usniValue;
    @Value("${e.mms.url}")
    private String mmsValue;
    @Value("${e.mars.url}")
    private String marsValue;
    @Autowired
    private MarsOrderMapper marsOrderMapper;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;
    @Autowired
    private MarsOrderSummaryMapper marsOrderSummaryMapper;
    @Autowired
    private MarsRankingSummaryMapper marsRankingSummaryMapper;
    @Autowired
    private SearchTagsByClassServiceImpl searchTagsByClassServiceImpl;
    @Autowired
    private SearchTagClassServiceImpl searchTagClassServiceImpl;

    private AtomicInteger integer = new AtomicInteger(0);

    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void saveMarsOrderDs() {
        saveMarsOrder("11");
        saveMarsOrder("13");
        saveMarsOrder("16");
        saveMarsOrder("54");
//        int totalCount = marsOrderDsMapper.getAllCount();
//        int pageSize = 10000;
//        int pages = totalCount/pageSize;
//        int pageNos = totalCount % pageSize > 0 ? ++pages :pages;
//        List<MarsOrderDs> prodList = null;
//        for (int i = 0; i < pageNos; i++){
//            prodList = marsOrderDsMapper.findOrderDsByPage(i * pageSize,pageSize);
//            for (MarsOrderDs marsOrderDs:prodList){
//                SendDataDsToOLAP.sendData(SendDataDsToOLAP.setSendData(marsOrderDs));
//                marsOrderDsMapper.updatePushFlag(marsOrderDs.getIntegrationId());
//            }
//        }
    }


    public void saveMarsOrder(String shopid) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            //开始的时间
            LocalDate startDate = LocalDate.now().minusDays(30);
            //结束的时间
            String endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDateTime);
            //LocalDate endDate = LocalDate.now().minusDays(1);
            String latestTimeJson = HttpClientFactory.requestGet(urlValue + shopid + "&begin_time=" + startDate + "&end_time=" + endDate + "&page_no=1&page_size=1");
            JSONObject jsonObject = JSONObject.fromObject(latestTimeJson);
            JSONArray jsonArray = (JSONArray) JSONObject.fromObject(JSONObject.fromObject(jsonObject.get("Success")).get("items")).get("item");
            JSONObject oneJson = jsonArray.getJSONObject(0);
            OrderDataVO orderNum = (OrderDataVO) JSONObject.toBean(oneJson, OrderDataVO.class);
            List<OrderDataVO> list; //保存订单数据
            int totalNum = Integer.parseInt(orderNum.getTotal_num());
            int pageNo;
            if (totalNum <= 100) {
                pageNo = 1;
            } else {
                pageNo = totalNum / 100;
            }
            for (int i = 0; i < pageNo; i++) {
                list = new ArrayList<>(); //保存订单数据
                String json = HttpClientFactory.requestGet(urlValue + shopid + "&begin_time=" + startDate + "&end_time=" + endDate + "&page_no=" + (i + 1) + "&page_size=100");
                JSONObject jsonObject1 = JSONObject.fromObject(json);
                if (null != jsonObject1.get("Success")) {
                    JSONArray jsonArray1 = (JSONArray) JSONObject.fromObject(JSONObject.fromObject(jsonObject1.get("Success")).get("items")).get("item");
                    for (int j = 0; j < jsonArray1.size(); j++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                        JSONArray jsonArray2 = jsonObject2.getJSONArray("tid_item");
                        List<ItemVO> itemVOList = (List<ItemVO>) JSONArray.toCollection(jsonArray2, ItemVO.class);
                        OrderDataVO orderDataVO = (OrderDataVO) JSONObject.toBean(jsonObject2, OrderDataVO.class);
                        orderDataVO.setTid_item(itemVOList);
                        list.add(orderDataVO);
                    }
                    List<MarsOrderDs> itemVOList = new ArrayList<>();
                    List<String> brandList = new ArrayList<>();
                    if (null != list && list.size() > 0) {
                        list.forEach(listTmp -> {
                            listTmp.getTid_item().forEach(listItem -> {
                                MarsOrderDs marsOrder = new MarsOrderDs();
                                marsOrder.setBabyTitle(listItem.getPro_name());
                                if (listItem.getSend_num() != null && !"".equals(listItem.getSend_num())) {
                                    marsOrder.setSendNum(Integer.parseInt(listItem.getSend_num()));
                                }
                                marsOrder.setBarCode(listItem.getBarcode());
                                marsOrder.setBrandName(listItem.getBrand_name());
                                marsOrder.setActualPayableAmount(new BigDecimal(StringUtils.isEmpty(String.valueOf(listItem.getSell_price())) ? "0" : String.valueOf(listItem.getSell_price())));
                                marsOrder.setCombineBarcode(listItem.getCombine_barcode());
                                marsOrder.setBabyType(listItem.getIscombination());
                                marsOrder.setTid(listItem.getTid());
                                if (null != listItem.getRefund_renum() && !"".equals(listItem.getRefund_renum())) {
                                    marsOrder.setRefundRenum(Integer.parseInt(listItem.getRefund_renum()));
                                }
                                if (null != listTmp.getPay_time() && !"".equals(listTmp.getPay_time())) {
                                    marsOrder.setBillPaymentTime(new Timestamp(DateUtil.strConvertToDate(listTmp.getPay_time(), "yyyy-MM-dd HH:mm:ss").getTime()));
                                }
                                marsOrder.setMobilePhone(listTmp.getReceiver_mobile());
                                marsOrder.setShopName(listTmp.getShop_name());
                                marsOrder.setTransactionId(listTmp.getTransaction_id());
                                marsOrder.setOutTid(listTmp.getOut_tid());
                                marsOrder.setBuyerId(listTmp.getBuyer_id());
                                marsOrder.setReceiverName(listTmp.getReceiver_name());
                                marsOrder.setProvince(listTmp.getProvince());
                                marsOrder.setCity(listTmp.getCity());
                                marsOrder.setDistrict(listTmp.getDistrict());
                                marsOrder.setExpressCompany(listTmp.getExpress());
                                if (null != listTmp.getDelivery_time() && !"".equals(listTmp.getDelivery_time())) {
                                    marsOrder.setDeliveryTime(new Timestamp(DateUtil.strConvertToDate(listTmp.getDelivery_time(), "yyyy-MM-dd HH:mm:ss").getTime()));
                                }
                                marsOrder.setExpressNo(listTmp.getExpress_no());
                                marsOrder.setOrderStatus(listTmp.getPlatform_status());
                                marsOrder.setOrderType(listTmp.getType());
                                marsOrder.setIntegrationId(listItem.getBarcode() + "~" + listTmp.getExpress_no());
                                marsOrder.setCreateTime(new Timestamp(new Date().getTime()));
                                marsOrder.setUpdateTime(new Timestamp(new Date().getTime()));
                                marsOrder.setBusinessPushFlag("N");
                                integer.incrementAndGet();
                                itemVOList.add(marsOrder);
                                brandList.add(listItem.getBarcode() + "~" + listTmp.getExpress_no());
                            });
                        });
                        marsOrderDsMapper.addBatch(itemVOList);
                        for (String brand:brandList){
                            marsOrderDsMapper.updateBarcode(brand);
                        }
                    }
                }
            }
            System.out.println("integer"+integer.get());
            //保存非会员数量
            List<MarsOrderSummary> salesPerDayCount = marsOrderDsMapper.getSaleAllDayCount(null, null);
            marsOrderSummaryMapper.saveNotMemberCountBatch(salesPerDayCount);
            logger.info("保存非会员数量"+salesPerDayCount.size());
            //保存非会员金额
            salesPerDayCount = marsOrderDsMapper.getSalesAllDayPrice(null, null);
            logger.info("保存非会员金额"+salesPerDayCount.size());
            for (MarsOrderSummary sss:salesPerDayCount){
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss 'CST' yyyy", Locale.ENGLISH);
                Date date = dateFormat.parse(String.valueOf(sss.getDate()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String sDate=sdf.format(date);
                logger.info(String.valueOf(sDate));
            }
            marsOrderSummaryMapper.saveNotSellPriceBatch(salesPerDayCount);
            //保存每天商品的数据总量
            List<MarsRankingSummary> marsRankingSummaryList = marsOrderDsMapper.getRankingCount();
            logger.info("保存每天商品的数据总量"+marsRankingSummaryList.size());
            for (MarsRankingSummary marsRankingSummary : marsRankingSummaryList) {
                marsRankingSummary.setCreateTime(new Timestamp(new Date().getTime()));
                marsRankingSummary.setUpdateTime(new Timestamp(new Date().getTime()));
                marsRankingSummary.setIntegrationId(marsRankingSummary.getProName() + "~" + marsRankingSummary.getDeliveryTime());
            }
            marsRankingSummaryMapper.addMarsRankingSummaryBatch(marsRankingSummaryList);
            logger.info("保存标签");
            //保存标签
            searchTagsByClassServiceImpl.getTags();
            logger.info("保存标签类别");
            //保存标签类别
            searchTagClassServiceImpl.getTagClass();
            logger.info("保存筛选条件标签");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveMarsOrderDses(){
        try {
            int count = marsOrderMapper.getAllCount();
            int pageSize = 10000;
            int pageNos = count/pageSize;
            int pageNo = count%10000==0?pageNos:pageNos+1;
            List<MarsOrder> list;
            for (int i = 365;i<pageNo;i++){
                List<MarsOrderDs> itemVOList = new ArrayList<>();
                list = marsOrderMapper.findOrderByPagees(i *pageSize,pageSize);
                list.forEach(marsOrderes -> {
                    MarsOrderDs marsOrderDs = new MarsOrderDs();
                    marsOrderDs.setBabyTitle(marsOrderes.getProName());
                    if (String.valueOf(marsOrderes.getSendNum()) !=null&&!"".equals(String.valueOf(marsOrderes.getSendNum()))){
                        marsOrderDs.setSendNum(marsOrderes.getSendNum());
                    }
                    marsOrderDs.setBarCode(marsOrderes.getBarcode());
                    marsOrderDs.setBrandName(marsOrderes.getBrandName());
                    marsOrderDs.setActualPayableAmount(marsOrderes.getSellPrice());
                    marsOrderDs.setCombineBarcode(marsOrderes.getCombineBarcode());
                    marsOrderDs.setBabyCount(marsOrderes.getProNum());
                    marsOrderDs.setBabyType(marsOrderes.getIsCombination());
                    marsOrderDs.setTid(marsOrderes.getTid());
                    if (String.valueOf(marsOrderes.getRefundRenum())!=null&&!"".equals(String.valueOf(marsOrderes.getRefundRenum()))){
                        marsOrderDs.setRefundRenum(marsOrderes.getRefundRenum());
                    }
                    if (marsOrderes.getPayTime()!=null&&!"".equals(marsOrderes.getPayTime())){
                        marsOrderDs.setBillPaymentTime(marsOrderes.getPayTime());
                    }
                    marsOrderDs.setMobilePhone(marsOrderes.getReceiverMobile());
                    marsOrderDs.setShopName(marsOrderes.getShopName());
                    marsOrderDs.setTransactionId(marsOrderes.getTransactionId());
                    marsOrderDs.setOutTid(marsOrderes.getOutTid());
                    marsOrderDs.setBuyerId(marsOrderes.getBuyerId());
                    marsOrderDs.setReceiverName(marsOrderes.getReceiverName());
                    marsOrderDs.setProvince(marsOrderes.getProvince());
                    marsOrderDs.setCity(marsOrderes.getCity());
                    marsOrderDs.setDistrict(marsOrderes.getDistrict());
                    marsOrderDs.setExpressCompany(marsOrderes.getExpress());
                    if (null!=marsOrderes.getDeliveryTime()&&!"".equals(marsOrderes.getDeliveryTime())){
                        marsOrderDs.setDeliveryTime(new Timestamp(DateUtil.strConvertToDate(String.valueOf(marsOrderes.getDeliveryTime()), "yyyy-MM-dd HH:mm:ss").getTime()));
                    }
                    marsOrderDs.setExpressNo(marsOrderes.getExpressNo());
                    marsOrderDs.setIntegrationId(marsOrderes.getBarcode() + "~" + marsOrderes.getExpressNo());
                    marsOrderDs.setCreateTime(new Timestamp(new Date().getTime()));
                    marsOrderDs.setUpdateTime(new Timestamp(new Date().getTime()));
                    marsOrderDs.setBusinessPushFlag("N");
                    integer.incrementAndGet();
                    itemVOList.add(marsOrderDs);
                });
                integer.incrementAndGet();
                logger.info(String.valueOf(integer.get()));
                marsOrderDsMapper.addBatch(itemVOList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

