package com.st.service.impl;

import com.st.conf.ApplicationContextConfiguration;
import com.st.mapper.MarsDownloadDMapper;
import com.st.mapper.MarsOrderDsMapper;
import com.st.model.MarsDownloadD;
import com.st.model.MarsOrderDs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/3/3.
 */
public class DownloadExcelRunnable implements Runnable {

    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;
    @Autowired
    private MarsDownloadDMapper marsDownloadDMapper;

    private Map map;

    private List<String> fields;

    private List<String> columnList;

    private List<String> fieldName;

    private int downloadId;

    private String saveUrl;

    private String fileName;

    public DownloadExcelRunnable(Map map,List<String> fields,List<String> columnList,int downloadId,List<String> fieldName,String fileName,String saveUrl){
        this.map = map;
        this.fields = fields;
        this.columnList = columnList;
        this.downloadId = downloadId;
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.saveUrl = saveUrl;
    }


    /**
     * 创建单个sheet
     */
    @Override
    public void run() {
        MarsOrderDsMapper marsOrderDsMapper = ApplicationContextConfiguration.getBean(MarsOrderDsMapper.class);
        MarsDownloadDMapper marsDownloadDMapper =ApplicationContextConfiguration.getBean(MarsDownloadDMapper.class);
        /**--------------------------------------------*/
        //查询数据总数
        int totalCount = marsOrderDsMapper.selectTotalData(map);
        int pageIndex = 0;
        if (totalCount % 10000 > 0) {
            pageIndex = (totalCount / 10000) + 1;
        } else if (totalCount % 10 == 0) {
            pageIndex = totalCount / 10000;
        }
        MarsDownloadD marsDownloadD1;
        int rowAccess = 100;//内存中缓存记录行数
        SXSSFWorkbook wb = new SXSSFWorkbook(rowAccess);
        //创建一个sheet
        Sheet sh = wb.createSheet("sheet1");
        for (int i = 0; i < fields.size(); i++) {
            sh.setColumnWidth((short) i, (short) (35.7 * 150));//设置所有的列宽
        }
        // 创建第一行
        Row row = sh.createRow((short) 0);
        //设置列名
        for (int i = 0; i < columnList.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnList.get(i));//设置列名
        }
        int count = 0;
        for (int i = 0; i < pageIndex; i++) {
//            if (i != 0 && i % 10 == 0) {
//                sh = wb.createSheet("sheet" + i / 10);
//                count = 0;
//            }
            map.put("pageIndex", i * 10000);
            List<MarsOrderDs> orderDsList = marsOrderDsMapper.selectDownData(map);
            marsDownloadD1 = new MarsDownloadD();
            marsDownloadD1.setId(downloadId);
            marsDownloadD1.setCount(orderDsList.size());
            marsDownloadD1.setFileStatus("未完成");
            double comPer = (orderDsList.size() * 1.0) / totalCount;
            String comStr = twopercent(comPer);
            marsDownloadD1.setCompletePre(comStr);
            marsDownloadDMapper.updateMarsDownload(marsDownloadD1);
            List<Map<String, Object>> list = createOrderDsExcel(orderDsList);
            for (int a = 1; a < list.size(); a++) {
                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建一行，在页sheet上
                Row row1 = sh.createRow(a + count * 10000);
//                if (i != 0 && i / 10 > 0) {
//                    row1 = sh.createRow(a + count * 10000);
//                } else {
//                    row1 = sh.createRow(a + i * 10000);
//                }
                // 在row行上创建一个方格
                for (short j = 0; j < fieldName.size(); j++) {
                    Cell cell = row1.createCell(j);
                    cell.setCellValue(list.get(a).get(fieldName.get(j)) == null ? " " : list.get(a).get(fieldName.get(j)).toString());
                }
                //每当行数达到设置的值就刷新数据到硬盘,以清理内存
                if (a % rowAccess == 0) {
                    try {
                        ((SXSSFSheet) sh).flushRows();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            count++;
        }

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(saveUrl + fileName + ".csv");
            wb.write(os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MarsDownloadD marsDownloadD2 = new MarsDownloadD();
        marsDownloadD2.setId(downloadId);
        marsDownloadD2.setCount(totalCount);
        marsDownloadD2.setCompletePre("100%");
        marsDownloadD2.setFileStatus("已完成");
        marsDownloadDMapper.updateMarsDownload(marsDownloadD2);
    }

    /**
     * 创建多个sheet
     */
//    @Override
//    public void run() {
//        MarsOrderDsMapper marsOrderDsMapper = ApplicationContextConfiguration.getBean(MarsOrderDsMapper.class);
//        MarsDownloadDMapper marsDownloadDMapper =ApplicationContextConfiguration.getBean(MarsDownloadDMapper.class);
//        /**--------------------------------------------*/
//        //查询数据总数
//        int totalCount = marsOrderDsMapper.selectTotalData(map);
//        int pageIndex = 0;
//        if (totalCount % 10000 > 0) {
//            pageIndex = (totalCount / 10000) + 1;
//        } else if (totalCount % 10 == 0) {
//            pageIndex = totalCount / 10000;
//        }
//        MarsDownloadD marsDownloadD1;
//        int rowAccess = 100;//内存中缓存记录行数
//        SXSSFWorkbook wb = new SXSSFWorkbook(rowAccess);
//        Sheet sh = wb.createSheet("sheet0");
//        for (int i = 0; i < fields.size(); i++) {
//            sh.setColumnWidth((short) i, (short) (35.7 * 150));
//        }
//        // 创建第一行
//        Row row = sh.createRow((short) 0);
//        //设置列名
//        for (int i = 0; i < columnList.size(); i++) {
//            Cell cell = row.createCell(i);
//            cell.setCellValue(columnList.get(i));
//        }
//        int count = 0;
//        for (int i = 0; i < pageIndex; i++) {
//            if (i != 0 && i % 10 == 0) {
//                sh = wb.createSheet("sheet" + i / 10);
//                count = 0;
//            }
//            map.put("pageIndex", i * 10000);
//            List<MarsOrderDs> orderDsList = marsOrderDsMapper.selectDownData(map);
//            marsDownloadD1 = new MarsDownloadD();
//            marsDownloadD1.setId(downloadId);
//            marsDownloadD1.setCount(orderDsList.size());
//            marsDownloadD1.setFileStatus("未完成");
//            double comPer = (orderDsList.size() * 1.0) / totalCount;
//            String comStr = twopercent(comPer);
//            marsDownloadD1.setCompletePre(comStr);
//            marsDownloadDMapper.updateMarsDownload(marsDownloadD1);
//            List<Map<String, Object>> list = createOrderDsExcel(orderDsList);
//            for (int a = 1; a < list.size(); a++) {
//                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
//                // 创建一行，在页sheet上
//                Row row1;
//                if (i != 0 && i / 10 > 0) {
//                    row1 = sh.createRow(a + count * 10000);
//                } else {
//                    row1 = sh.createRow(a + i * 10000);
//                }
//                // 在row行上创建一个方格
//                for (short j = 0; j < fieldName.size(); j++) {
//                    Cell cell = row1.createCell(j);
//                    cell.setCellValue(list.get(a).get(fieldName.get(j)) == null ? " " : list.get(a).get(fieldName.get(j)).toString());
//                }
//                //每当行数达到设置的值就刷新数据到硬盘,以清理内存
//                if (a % rowAccess == 0) {
//                    try {
//                        ((SXSSFSheet) sh).flushRows();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            count++;
//        }
//
//        FileOutputStream os = null;
//        try {
//            os = new FileOutputStream(saveUrl + fileName + ".xlsx");
//            wb.write(os);
//            os.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MarsDownloadD marsDownloadD2 = new MarsDownloadD();
//        marsDownloadD2.setId(downloadId);
//        marsDownloadD2.setCount(totalCount);
//        marsDownloadD2.setCompletePre("100%");
//        marsDownloadD2.setFileStatus("已完成");
//        marsDownloadDMapper.updateMarsDownload(marsDownloadD2);
//    }
    private String twopercent(Double param) {
        String perStr = "";
        if (!StringUtils.isEmpty(param)) {
            NumberFormat fmt = NumberFormat.getPercentInstance();
            fmt.setMinimumFractionDigits(0);
            perStr = fmt.format(param);
        }
        return perStr;
    }

    private List<Map<String, Object>> createOrderDsExcel(List<MarsOrderDs> orderDsList) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (MarsOrderDs orderDs : orderDsList) {
            Map<String, Object> mapValue = new HashMap<>();
            mapValue.put("bill_payment_time", orderDs.getBillPaymentTime());
            mapValue.put("out_tid", orderDs.getOutTid());
            mapValue.put("shop_name", orderDs.getShopName());
            mapValue.put("baby_title", orderDs.getBabyTitle());
            mapValue.put("send_num", orderDs.getSendNum());
            mapValue.put("bar_code", orderDs.getBarCode());
            mapValue.put("brand_name", orderDs.getBrandName());
            mapValue.put("actual_payable_amount", orderDs.getActualPayableAmount());
            mapValue.put("combine_barcode", orderDs.getCombineBarcode());
            mapValue.put("pro_num", orderDs.getProNum());
            mapValue.put("product_type", orderDs.getProductType());
            mapValue.put("tid", orderDs.getTid());
            mapValue.put("transaction_id", orderDs.getTransactionId());
            mapValue.put("express_company", orderDs.getExpressCompany());
            mapValue.put("mobile_phone", orderDs.getMobilePhone());
            mapValue.put("delivery_time", orderDs.getDeliveryTime());
            mapValue.put("express_no", orderDs.getExpressNo());
            mapValue.put("receiver_name", orderDs.getReceiverName());
            mapValue.put("buyer_id", orderDs.getBuyerId());
            mapValue.put("refund_renum", orderDs.getRefundRenum());
            mapValue.put("province", orderDs.getProvince());
            mapValue.put("city", orderDs.getCity());
            mapValue.put("district", orderDs.getDistrict());
            mapValue.put("buyer_message", orderDs.getBuyerMessage());
            listmap.add(mapValue);
        }
        return listmap;
    }
}
