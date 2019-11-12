package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.MarsDownloadD;
import com.st.model.OpResult;
import com.st.service.EBusinessStatisticsService;
import com.st.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by zhaoyx on 2016/9/21.
 */
@Api(tags = "5:电商数据统计",description = " ")
@RestController
@RequestMapping("/EBusiness")
public class EBusinessStatisticsController extends BaseController {
    @Autowired
    private EBusinessStatisticsService eBusinessStatisticsServiceImpl;

    @Value("${local.business}")
    private String saveUrl;

    /**
     * 获取销量排行
     * @param startDate
     * @param endDate
     * @return
     */
    @ApiOperation(value="销售量排名", notes="datas:" +
            "[{\n" +"<br/>"+
            "      \"brandName\": \"SKU1\", --品牌名" +"<br/>"+
            "      \"saleCount\": 100000,\n --销售数量" +"<br/>"+
            "      \"saleCountPct\": 18.22\n --销售占比" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/salesRanking",method= RequestMethod.GET)
    public FacadeResult getSalesRanking(String startDate,String endDate){
        //数据检查
        if(StringUtils.isValidDate(startDate) || StringUtils.isValidDate(endDate)){
            return generateException(1001);
        }
        OpResult opResult = eBusinessStatisticsServiceImpl.getSalesRanking(startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 消费金额汇总
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @ApiOperation(value="消费金额汇总", notes="datas:" +
            "[{\n" +"<br/>"+
            "      \"name\": \"会员\",\n " +"<br/>"+
            "      \"value\": [\n" +"<br/>"+
            "        {\n" +"<br/>"+
            "          \"amount\": 1233.33,\n   --金额" +"<br/>"+
            "          \"date\": \"2016-09-01\",\n --日期" +"<br/>"+
            "          \"count\": 2344\n       --数量" +"<br/>"+
            "        },\n" +"<br/>"+
            "      ]\n" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/purchaseAmount",method= RequestMethod.GET)
    public FacadeResult getPurchaseAmount(String startDate,String endDate){
        //数据检查
        if(StringUtils.isValidDate(startDate) || StringUtils.isValidDate(endDate)){
            return generateException(1001);
        }
        OpResult opResult = eBusinessStatisticsServiceImpl.getPurchaseAmount(startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取品牌、地区
     * @return
     */
    @ApiOperation(value="品牌、地区",notes = "data:{" +
            "<br/>"+
            "brand: [德芙、士力架、脆米香]            --品牌/店铺"+"<br/>"+
            "area: [北京、上海、重庆]            --地区"+"<br/>"+
            "}")
    @RequestMapping(value = "/getBrandAndArea",method= RequestMethod.GET)
    public FacadeResult getBrandAndArea(){
        OpResult opResult = eBusinessStatisticsServiceImpl.getBrandAndArea();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 字段维度
     * @return
     */
    @ApiOperation(value="字段维度",notes="data:" +
            "[{\n" +"<br />"+
            "      \"name\": \"buyer_id\",\n  --字段属性" +"<br />"+
            "      \"value\": \"买家会员名\"\n  --字段名称" +"<br />"+
            "    }\n" +"<br />"+
            "  ]")
    @RequestMapping(value = "/getFields",method= RequestMethod.GET)
    public FacadeResult getFields(){
        OpResult opResult = eBusinessStatisticsServiceImpl.getFields();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 下载任务接口
     * @return
     */
    @ApiOperation(value="下载任务接口",notes="data:" +
            "{'brandName':['MMS'], --品牌名称" +"<br />"+
            "'areas':['广东省'], --省份" +"<br />"+
            "startData': '2017-02-09', --开始时间" +"<br />"+
            "'endData': '2017-02-10', --结束时间" +"<br />"+
            "'fileName':'aaaa', --文件名称" +"<br />"+
            "'userName':'时趣运维', --创建用户" +"<br />"+
            "'fields': ['brand_name','province','buyer_id']--字段名称" +"<br />"+
            "}")
    @ApiImplicitParam(name = "jsonData", value = "json字符串(格式:" +"<br />"+
            "{\n" +"<br />"+
            "    \"brandName\": [\n" +"<br />"+
            "        \"MMS\"\n" +"<br />"+
            "    ],\n" +"<br />"+
            "    \"areas\": [\n" +"<br />"+
            "        \"广东省\"\n" +"<br />"+
            "    ],\n" +"<br />"+
            "    \"startData\": \"2017-02-09\",\n" +"<br />"+
            "    \"endData\": \"2017-02-10\",\n" +"<br />"+
            "    \"fileName\": \"aaa\",\n" +"<br />"+
            "    \"userName\": \"时趣运维\",\n" +"<br />"+
            "    \"fields\": [\n" +"<br />"+
            "        \"brand_name\",\n" +"<br />"+
            "        \"province\",\n" +"<br />"+
            "        \"buyer_id\"\n" +"<br />"+
            "    ]\n" +"<br />"+
            "}" +"<br />"+
            ")", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/getEleColunmData",method= RequestMethod.GET)
    public FacadeResult getEleColunmData(String jsonData){
        String jsonData1 = "{'brandName': ['MMS'],'areas': ['广东省']," +
                "'startData': '2017-02-09','endData': '2017-02-10','fileName':'aaa','userName':'时趣运维',"+
                "'fields': ['brand_name','province','buyer_id']}";
        OpResult opResult = eBusinessStatisticsServiceImpl.getEleColunmData(jsonData);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 查询电商数据下载列表
     * @return
     */
    @ApiOperation(value="查询电商数据下载列表接口",notes="data:" +
            "{'downloadDList':[{'id':1, --id序列号" +"<br />"+
            "'create_user':'时趣运维', --创建用户" +"<br />"+
            "'download_name': 'aaa', --文件名称" +"<br />"+
            "'create_time': '2017-02-15 11:44:13', --创建时间" +"<br />"+
            "'file_status':'未完成/已完成', --文件状态" +"<br />"+
            "'count':'13000', --数据量" +"<br />"+
            "'complete_pre': '35%'--完成度" +"<br />"+
            "}]," +"<br />"+
            "'totalPageNum':'3400'--总页数}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "Integer",paramType = "query"),
    })
    @RequestMapping(value = "/getDownloadDList",method= RequestMethod.GET)
    public FacadeResult getDownloadDList(Integer pageNum,Integer pageSize){
        OpResult opResult = eBusinessStatisticsServiceImpl.getDownloadDList(pageNum,pageSize);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 删除文件接口
     * @param fileName
     * @return
     */
    @ApiOperation(value="删除文件接口",notes="data:" +
            "{'fileName':'aaa'--文件名称}")
    @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/deleteFile",method= RequestMethod.GET)
    public FacadeResult deleteFile(String fileName) throws Exception {
        fileName = URLDecoder.decode(URLDecoder.decode(fileName,"utf-8"));
        OpResult opResult = eBusinessStatisticsServiceImpl.deleteFile(fileName);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 下载文件接口
     * @param fileName
     * @return
     */
    @ApiOperation(value="下载文件接口",notes="data:" +
            "{'fileName':'aaa'--文件名称}")
    @ApiImplicitParam(name = "fileName", value = "文件名称", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/getDownloadFile",method= RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getDownloadFile(String fileName) throws IOException {
        fileName = URLDecoder.decode(URLDecoder.decode(fileName,"utf-8"));
        File files = new File(saveUrl+fileName+".xlsx");
        if(!files.exists()){
            //根据文件名查询这条记录的json数据
            MarsDownloadD downloadD = eBusinessStatisticsServiceImpl.selectJsonData(fileName);
            eBusinessStatisticsServiceImpl.getEleColunmData(downloadD.getJsonData());
        }
        String filePath = saveUrl + fileName+".csv";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-type", "text/html;charset=UTF-8");
        headers.setContentDispositionFormData("attachment", new String(file.getFilename().getBytes("UTF-8"), "ISO8859-1"));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
