package com.st.service.impl;

import com.st.enums.ErrorCodeEnum;
import com.st.mapper.MarsOrderDsMapper;
import com.st.model.EBUser;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.model.UVModel;
import com.st.service.UserCountService;
import com.st.service.UserUVService;
import com.st.utils.DateUtil;
import com.st.utils.HttpClientFactory;
import com.st.utils.StringUtils;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

import static java.util.stream.Collectors.toList;


/**
 * 每小时UV
 * Created by zhaoyx on 2016/9/12.
 */
@Service
public class UserUVServiceImpl implements UserUVService {
    @Autowired
    private UserCountService userCountServiceImpl;
    @Value("${user.draw.url}")
    private String urlValue;
    @Value("${local.path}")
    private String localPath;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;

    /**
     * 获取各小时UV
     * <p/>
     * 因外部接口原因，返回的数据，不是按照标准的时间传过来的，有的时间戮是空值，需要我们自己补齐时间
     * 因为返回的数据是按照时间戮增长的，每个值是针对前一个值进行增减的，
     * 所以要以零点的数据为基准：
     * 1.零点之前的数据需要在此基准上减去相应时间戮对应的粉丝增减数，并补齐缺失的时间戮数据。
     * 2.零点之后的数据需要在此基准上加上相应时间戮对应的粉丝增减数，并补齐缺失的时间戮数据。
     * 注：缺失的时间戮数据按照粉丝增减数为0计算
     * 此方法用了两个list,一个零点之前的数据处理逻辑，一个零点之后的数据处理逻辑
     * 此代码需要优化，利用java8  lambda stream api .
     *
     * @return
     */
    @Override
    public OpResult<List<UVModel>> getUVCount() {
        List<UVModel> list = new ArrayList<>();
        try {
            //今天零点零分零秒的毫秒数
            long zero = System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
            int current = (int) ((System.currentTimeMillis() - zero) / (1000 * 3600));
            //获取粉丝总数
            int count = Integer.parseInt(userCountServiceImpl.getUserCount().getResult());
            //取24小时之前的的时间
            String startTime = DateUtil.getUnixTimestamp(24);
            //取当前的时间
            String endTime = DateUtil.getUnixTimestamp(0);
            //获取各小时UV返回
            String uvJson = HttpClientFactory.requestGet(urlValue + "searchUv&startTime=" + startTime + "&endTime=" + endTime + "&flag=0");
            //将json数据存入treeMap
            TreeMap<String, Integer> treeMap = new TreeMap(JSONObject.fromObject(String.valueOf(JSONObject.fromObject(uvJson).get("datas"))));
            //将treeMap数据存入list中，以便于排序
            List<Map.Entry<String, Integer>> sortList = new ArrayList<>(treeMap.entrySet());
            //对sortLit的key(时间) 进行降序排列

            //返回值构建,如果返回为空，构建7个小时为0的数据，如果返回值不空，循环查询补充数据
            if (null == sortList || sortList.isEmpty() || count == 0) {
                for (int i = 0; i < 24; i++) {
                    //将时间戮转为整点
                    list.add(new UVModel(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(i), "yyyy-MM-dd HH:00:00"), 0, count));
                }
            } else {
                int beforeZeroCount = count;//计算零点之后的用户总量
                int afterZeroCount = count;//计算零点之后的用户总量
                //取零点之后的list,并排序，因为只有排序之后,才能计算距离零点的时间，进行累加减，是降序的
                List<Map.Entry<String, Integer>> afterList = sortList.stream()
                        .filter(map -> Long.valueOf(map.getKey()) >= zero / 1000)
                        .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                        .collect(toList());
                //取零点之前的list,不用排序，因为是按照升序排列的。
                List<Map.Entry<String, Integer>> beforeList = sortList.stream()
                        .filter(map -> Long.valueOf(map.getKey()) < zero / 1000)
                        .collect(toList());
                //循环零点之前的数据,并补全缺失的时间戮数据
                for (int i = 1; i < 24 - current; i++) {
                    int flagCount = 0;
                    if (null != beforeList && beforeList.size() > 0) {
                        for (Map.Entry<String, Integer> map : beforeList) {
                            //如果返回数据有值，加入到list中，并将总记录数减去当前的新增记录
                            //flagCount用于判断是否进入了本次循环
                            if (DateUtil.timeStamp2Date(map.getKey(), "yyyy-MM-dd HH:00:00")
                                    .equals(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(i + current), "yyyy-MM-dd HH:00:00"))) {
                                //每个时刻的总人数
                                beforeZeroCount -= map.getValue();
                                //向list中添加符合要求的数据，也就是符合该整点的数据
                                list.add(new UVModel(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(i + current), "yyyy-MM-dd HH:00:00"), map.getValue(), beforeZeroCount));
                                //用于判断是否进入循环，没有进入循环，说明没有该时间戮数据，需要下面补全
                                flagCount++;
                            }
                        }
                    }
                    //如果没有进入循环，创建当前时间的总记录数
                    if (flagCount == 0) {
                        list.add(new UVModel(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(i + current), "yyyy-MM-dd HH:00:00"), 0, beforeZeroCount));
                    }
                }

                //补零点之后的数据
                for (int i = 0; i <= current; i++) {
                    int flagCount = 0;
                    if (null != afterList && afterList.size() > 0) {
                        for (Map.Entry<String, Integer> map : afterList) {
                            //如果返回数据有值，加入到list中，并将总记录数减去当前的新增记录
                            //flagCount用于判断是否进入了本次循环
                            if (DateUtil.timeStamp2Date(map.getKey(), "yyyy-MM-dd HH:00:00")
                                    .equals(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(current - i), "yyyy-MM-dd HH:00:00"))) {
                                list.add(new UVModel(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(current - i), "yyyy-MM-dd HH:00:00"), map.getValue(), afterZeroCount));
                                afterZeroCount += map.getValue();
                                flagCount++;
                            }
                        }
                    }
                    //如果没有进入循环，创建当前时间的总记录数
                    if (flagCount == 0) {
                        list.add(new UVModel(DateUtil.timeStamp2Date(DateUtil.getUnixTimestamp(current - i), "yyyy-MM-dd HH:00:00"), 0, afterZeroCount));
                    }
                }
            }
            //排序 ，按照升序进行排序
            list.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
            for (int i = 1; i < list.size() - 1; i++) {
                list.get(i).setIncreaseCount(list.get(i + 1).getIncreaseCount());
            }
//            Collections.sort(list, ((o1, o2) -> o2.getTime().compareTo(o1.getTime())));
            return OpResult.createSucResult(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.UV_COUNT_EXCEPTION);
    }

    @Override
    public OpResult<String> downLoadEBUser() {
        try {
            File file = new File(localPath + "电商用户匹配数据.xls");
            if (file.exists()) {
                file.delete();
            }
            String sheetName = "电商匹配数据.xls";
            String[] header = {"微信OpenID", "微信昵称", "订单编号", "买家会员名", "买家实际支付金额", "收货人姓名", "收货人地址", "联系手机", "宝贝标题", "宝贝种类", "订货数量", "店铺ID", "店铺名称","支付时间"};
            String[] keys = {"openId", "memberName", "tid", "buyerId", "actualPayableAmount", "receiverName", "receiverAddress", "mobilePhone", "babyTitle", "babyType", "proNum", "shopId", "shopName","billPaymentTime"};
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            //-----------表格的宽度------------------------------------------------------------
            sheet.setColumnWidth(0, 20 * 256);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            //设置表头样式
            HSSFCellStyle cellStyleHead = wb.createCellStyle();
            // 指定单元格居中对齐
            cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 指定单元格垂直居中对齐
            cellStyleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 指定当单元格内容显示不下时自动换行
            cellStyleHead.setWrapText(true);
            HSSFFont fontHead = wb.createFont();
            fontHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            fontHead.setFontName("宋体");
            fontHead.setFontHeight((short) 200);
            cellStyleHead.setFont(fontHead);
            //插入表头内容
            HSSFRow rowHead = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                HSSFCell cell = rowHead.createCell(i);
                cell.setCellStyle(cellStyleHead);
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }
            OutputStream out = new FileOutputStream(localPath + "电商用户匹配数据.xls");
            int pageSize = 4;
            int count = marsOrderDsMapper.getEBuserCount();
            int pageNo = count%pageSize==0?count/pageSize:count/pageSize+1;
            List<List<EBUser>> list2 = new ArrayList<>();
            int rowNum = 1;
            for (int q = 0;q<pageNo;q++){
                List<EBUser> list1 = marsOrderDsMapper.getEBUser(q*pageSize,pageSize);
                for (EBUser u : list1) {
                    HSSFRow row = sheet.createRow(rowNum);
                    for (int i = 0; i < keys.length; i++) {
                        HSSFCell cell = row.createCell(i);
                        String cellValue = null;
                        if ("openId".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getOpenId()) ? "" : u.getOpenId();
                        }
                        if ("memberName".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getMemberName()) ? "" : u.getMemberName();
                        }
                        if ("tid".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getTid()) ? "" : u.getTid();
                        }
                        if ("buyerId".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getBuyerId()) ? "" : u.getBuyerId();
                        }
                        if ("actualPayableAmount".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getActualPayableAmount()) ? "" : String.valueOf(u.getActualPayableAmount());
                        }
                        if ("receiverName".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getReceiverName()) ? "" : u.getReceiverName();
                        }
                        if ("receiverAddress".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getReceiverAddress()) ? "" : u.getReceiverAddress();
                        }
                        if ("mobilePhone".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getMobilePhone()) ? "" : u.getMobilePhone();
                        }
                        if ("babyTitle".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getBabyTitle()) ? "" : u.getBabyTitle();
                        }
                        if ("babyType".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getBabyType()) ? "" : u.getBabyType();
                        }
                        if ("proNum".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getProNum()) ? "" : u.getProNum();
                        }
                        if ("shopId".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getShopId()) ? "" : u.getShopId();
                        }
                        if ("shopName".equals(keys[i])) {
                            cellValue = StringUtils.isNull(u.getShopName()) ? "" : u.getShopName();
                        }
                        if ("billPaymentTime".equals(keys[i])){
                            cellValue = StringUtils.isNull(u.getBillPaymentTime()) ? "" : u.getBillPaymentTime();
                        }
                        cell.setCellValue(new HSSFRichTextString(cellValue));
                    }
                    rowNum++;
                }
            }
            wb.write(out);
            out.close();
            return OpResult.createSucResult("Succeed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OpResult.createFailResult(ErrorCodeEnum.USER_COUNT_EXCEPTION);
    }


    public void toExcel(){
        try {
            //用于存放文件名称的list
            List<String> fileNames = new ArrayList();
            //OutputStream out = new FileOutputStream(localPath + "电商用户匹配数据.xls");
            int pageSize = 4;
            int count = marsOrderDsMapper.getEBuserCount();
            int pageNo = count%pageSize==0?count/pageSize:count/pageSize+1;
            List<List<EBUser>> list2 = new ArrayList<>();
            int rowNum = 1;
            for (int q = 0;q<pageNo;q++){
                List<EBUser> list1 = marsOrderDsMapper.getEBUser(q*pageSize,pageSize);
                for (int i=0;i<list1.size()/50000+1;i++){
                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet = wb.createSheet("EBuser");
                    String fileName = localPath+i+"电商用户匹配数据.xls";

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
