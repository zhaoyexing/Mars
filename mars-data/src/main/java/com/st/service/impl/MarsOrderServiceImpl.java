package com.st.service.impl;

import com.st.mapper.*;
import com.st.model.*;
import com.st.service.MarsOrderService;
import com.st.service.SearchTagClassService;
import com.st.service.SearchTagsByClassService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.Mail;
import com.st.vo.ItemVO;
import com.st.vo.OrderDataVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，存储订单信息
 * Created by zhaoyx on 2016/10/19.
 */
@Service
public class MarsOrderServiceImpl implements MarsOrderService {
    private static Logger logger = LoggerFactory.getLogger(MarsOrderServiceImpl.class);
    @Value("${e.business.url}")
    private String urlValue;
    @Autowired
    private MarsOrderMapper marsOrderMapper;
    @Autowired
    private MarsOrderSummaryMapper marsOrderSummaryMapper;
    @Autowired
    private MarsRankingSummaryMapper marsRankingSummaryMapper;
    @Autowired
    private SearchTagsByClassService searchTagsByClassServiceImpl;
    @Autowired
    private SearchTagClassService searchTagClassServiceImpl;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;
    @Autowired
    private MarsTagClassTmpMapper marsTagClassTmpMapper;
    @Autowired
    private MarsTagsMapper marsTagsMapper;

    @Scheduled(cron = "0 0 4 * * ?")
    @Override
    public void saveMarsOrder() {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            /** 昨天 */
            LocalDate startDate = LocalDate.now().minusDays(7);
            /** 今天*/
            String endDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDateTime);
            /** 取总的数据 */
                String latestTimeJson = HttpClientFactory.requestGet(urlValue + "&begin_time=" + startDate + "&end_time=" + endDate + "&page_no=1&page_size=1");
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
                for (int i = 4; i < pageNo; i++) {
                    list = new ArrayList<>(); //保存订单数据
                    String json = HttpClientFactory.requestGet(urlValue + "&begin_time=" + startDate + "&end_time=" + endDate + "&page_no=" + (i + 1) + "&page_size=100");
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
                        List<MarsOrder> itemVOList = new ArrayList<>();
                        if (null != list && list.size() > 0) {
                            list.forEach(listTmp -> {
                                listTmp.getTid_item().forEach(listItem -> {
                                    MarsOrder marsOrder = new MarsOrder();
                                    marsOrder.setProName(listItem.getPro_name());
                                    if (listItem.getSend_num() != null && !"".equals(listItem.getSend_num())) {
                                        marsOrder.setSendNum(Integer.parseInt(listItem.getSend_num()));
                                    }
                                    marsOrder.setBarcode(listItem.getBarcode());
                                    marsOrder.setBrandName(listItem.getBrand_name());
                                    marsOrder.setSellPrice(new BigDecimal(StringUtils.isEmpty(String.valueOf(listItem.getSell_price())) ? "0" : String.valueOf(listItem.getSell_price())));
                                    marsOrder.setCombineBarcode(listItem.getCombine_barcode());
                                    marsOrder.setProName(listItem.getPro_name());
                                    marsOrder.setIsCombination(listItem.getIscombination());
                                    marsOrder.setTid(listItem.getTid());
                                    if (null != listItem.getRefund_renum() && !"".equals(listItem.getRefund_renum())) {
                                        marsOrder.setRefundRenum(Integer.parseInt(listItem.getRefund_renum()));
                                    }
                                    if (null != listTmp.getPay_time() && !"".equals(listTmp.getPay_time())) {
                                        marsOrder.setPayTime(new Timestamp(DateUtil.strConvertToDate(listTmp.getPay_time(), "yyyy-MM-dd HH:mm:ss").getTime()));
                                    }
                                    marsOrder.setReceiverMobile(listTmp.getReceiver_mobile());
                                    marsOrder.setShopName(listTmp.getShop_name());
                                    marsOrder.setTransactionId(listTmp.getTransaction_id());
                                    marsOrder.setOutTid(listTmp.getOut_tid());
                                    marsOrder.setBuyerId(listTmp.getBuyer_id());
                                    marsOrder.setReceiverName(listTmp.getReceiver_name());
                                    marsOrder.setProvince(listTmp.getProvince());
                                    marsOrder.setCity(listTmp.getCity());
                                    marsOrder.setDistrict(listTmp.getDistrict());
                                    marsOrder.setExpress(listTmp.getExpress());
                                    if (null != listTmp.getDelivery_time() && !"".equals(listTmp.getDelivery_time())) {
                                        marsOrder.setDeliveryTime(new Timestamp(DateUtil.strConvertToDate(listTmp.getDelivery_time(), "yyyy-MM-dd HH:mm:ss").getTime()));
                                    }
                                    marsOrder.setExpressNo(listTmp.getExpress_no());
                                    marsOrder.setIntegrationId(listItem.getBarcode() + "~" + listTmp.getExpress_no());
                                    marsOrder.setCreateTime(new Timestamp(new Date().getTime()));
                                    marsOrder.setUpdateTime(new Timestamp(new Date().getTime()));
                                    itemVOList.add(marsOrder);
                                });
                            });
                            marsOrderMapper.addBatchOrder(itemVOList);
                        }
                    }
                }
            //保存非会员数量
            List<MarsOrderSummary> salesPerDayCount = marsOrderMapper.getSaleAllDayCount(null, null);
            marsOrderSummaryMapper.saveNotMemberCountBatch(salesPerDayCount);
            logger.info("保存非会员数量"+salesPerDayCount.size());
            //保存非会员金额
            salesPerDayCount = marsOrderMapper.getSalesAllDayPrice(null, null);
            logger.info("保存非会员金额"+salesPerDayCount.size());
            marsOrderSummaryMapper.saveNotSellPriceBatch(salesPerDayCount);
            //保存每天商品的数据总量
            List<MarsRankingSummary> marsRankingSummaryList = marsOrderMapper.getRankingCount();
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
            //保存筛选条件标签
//            packagingTags();
            //发送邮件
            Mail mail=new Mail();
            mail.setSmtpHost("smtp.126.com");/** 设置SMTP **/
            String mailFrom="zhaoyexing2006@126.com";
            String mailpassword="1319625";
            String email = "zhaoyexing@social-touch.com";
            mail.isHtmlModeMail();
            mail.setFrom(mailFrom);
            mail.setSmtpAuthentication(mailFrom, mailpassword);/** 账号及密码 **/
            mail.setTo(email);/** 发送给谁 **/
            mail.setContentType(Mail.MODE_HTML);
            mail.setSubject("电商接收数据汇总");/** 邮件主题 **/
            //定义发送的html字符串
            String htmlStr="测试服务器定时任务,每天4点定时执行";
            mail.sendMail(htmlStr);
        } catch (Exception e) {
            logger.error(e + "");
        }
    }

    /**
     * 更新筛选条件接口
     */
    @Override
    public OpResult<String> updateTags() {
        //保存标签
        searchTagsByClassServiceImpl.getTags();
        //保存标签类别
        searchTagClassServiceImpl.getTagClass();
        //保存筛选条件标签
        packagingTags();
        logger.info("更新完成===================================");
        return OpResult.createSucResult("success");
    }

    /**
     * 保存筛选条件标签封装
     */
    private void packagingTags() {
        List<String> parentList = marsTagClassTmpMapper.getAllClassId();
        parentList.forEach(classId -> {
            List<MarsTagsTmp> childList = marsTagsMapper.getMarsTagsTmp(classId);
            if (null != childList && childList.size() > 0) {
                childList.forEach(child -> {
                    child.setCreateTime(new Timestamp(new Date().getTime()));
                    child.setUpdateTime(new Timestamp(new Date().getTime()));
                    child.setType("0");
                });
                marsTagsTmpMapper.addMarsTagsTmBatch(childList);
            }
        });
    }

}
