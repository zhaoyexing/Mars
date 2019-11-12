package com.st.utils;

import com.st.model.UserGroup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * 大数据量下生成多个excel，并压缩
 * Created by admin on 2017/3/3.
 */
public class ManipulationUtil {


    public static String toExcel(List<UserGroup> list, String localPath,int bili, String groupName,String lifeCycle, String[] alis) throws IOException {
        // 用于存放生成的文件名称s
        //List<String> fileNames = new ArrayList();
        //File zip = new File(localPath +"电商匹配数据.zip");// 压缩文件
        // 生成excel
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("用户匹配数据");
        String file = localPath + groupName +bili+ ".xls";
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(file);
            Row row = sheet.createRow(0);
            int a;
            for (a = 0; a < alis.length; a++) {
                row.createCell(a).setCellValue(alis[a]);
            }
            int m = 0;
            if ("微信用户".equals(lifeCycle)){
                for (int i = 0; i < list.size(); i++) {
                    m++;
                    UserGroup userGroup = list.get(i);
                    row = sheet.createRow(m);
                    row.createCell(0).setCellValue(userGroup.getOpenId());
                    row.createCell(1).setCellValue(userGroup.getNikeName());
                    row.createCell(2).setCellValue(userGroup.getProvice());
                }
            }else {
                for (int i = 0; i < list.size(); i++) {
                    m++;
                    UserGroup userGroup = list.get(i);
                    row = sheet.createRow(m);
                    row.createCell(0).setCellValue(userGroup.getuId());
                    row.createCell(1).setCellValue(userGroup.getNikeName());
                    row.createCell(2).setCellValue(userGroup.getProvice());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            book.write(o);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            o.flush();
            o.close();
        }
//        File srcfile[] = new File[fileNames.size()];
//        for (int i = 0, n = fileNames.size(); i < n; i++) {
//            srcfile[i] = new File(fileNames.get(i));
//        }
//        ZipFiles(srcfile, zip);
//        FileInputStream inStream = new FileInputStream(zip);
//        byte[] buf = new byte[4096];
//        int readLength;
//        while (((readLength = inStream.read(buf)) != -1)) {
//            out.write(buf, 0, readLength);
//        }
//        inStream.close();
        return file;
    }



    /**
     *
     * @param srcfile 文件名数组
     * @param zipfile 压缩后文件
     */
    public static void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

