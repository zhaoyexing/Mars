package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.KolInfoDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 2016/9/19.
 */
@RestController
@RequestMapping("/kolStat")
@Api(tags = "2:KOL统计",description = " ")
public class KolStatController extends BaseController{

    @Autowired
    private KolInfoDataService kolInfoDataService;

    /**
     * 微博概览头部统计数据
     * @param proId
     * @return
     */
    @ApiOperation(value="微博概览头部统计数据",notes = "data:{" +
            "<br/>"+
            "cover_count:             --总曝光量," +"<br/>"+
            "pro_id:                  --项目id," +"<br/>"+
            "comments_count:          --项目总评论数," +"<br/>"+
            "reposts_count:           --项目总转发数," +"<br/>"+
            "pro_kol_count:           --kol数量" +"<br/>"+
            "}")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/micBlogHeadData",method= RequestMethod.GET)
    public FacadeResult getMicBlogHeadData(String proId){
        OpResult opResult = kolInfoDataService.microBlogHeadData(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微博总曝光统计走势
     * @param proId
     * @param startDate
     * @param endDate
     * @return
     */
    @ApiOperation(value="微博项目总曝光趋势监测统计",notes = "data:[{" +
            "<br/>"+
            "name:               --日期," +"<br/>"+
            "value:              --数量" +"<br/>"+
            "}]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/micBlogExposure",method= RequestMethod.GET)
    public FacadeResult getMicBlogExposure(String proId, String startDate, String endDate){
        OpResult opResult = kolInfoDataService.microBlogExposure(proId,startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微博KOL互动情况排名
     * @param proId
     * @return
     */
    @ApiOperation(value="微博项目KOL互动情况排名",notes = "data:[\n" + "<br/>"+
            "    {\n" +"<br/>"+
            "      \"values\": --值 " +
            "[\n" +"<br/>"+
            "        {\n" +"<br/>"+
            "          \"name\": \"0号人\",\n --名字" +"<br/>"+
            "          \"value\": 5\n --值" +"<br/>"+
            "        }\n" +"<br/>"+
            "      ],\n" +"<br/>"+
            "      \"type\": \"uv\"\n --类型" +"<br/>"+
            "    }\n" +"<br/>"+
            "  ]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/micBlogChain",method= RequestMethod.GET)
    public FacadeResult getMicBlogChain(String proId){
        OpResult opResult = kolInfoDataService.microBlogChain(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微博KOL总曝光量统计
     * @param proId
     * @return
     */
    @ApiOperation(value="微博项目曝光kol贡献百分比",notes = "data:[{" +
            "<br/>"+
            "name:               --名称," +"<br/>"+
            "value:              --数值" +"<br/>"+
            "}]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/micBlogExposureNum",method= RequestMethod.GET)
    public FacadeResult getMicBlogExposureNum(String proId){
        OpResult opResult = kolInfoDataService.microBlogExposureNum(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微博页面链接pv来源
     * @param proId
     * @return
     */
    @ApiOperation(value="微博链接pv来源百分比",notes = "data:[{" +
            "<br/>"+
            "name:               --名称," +"<br/>"+
            "value:              --数值" +"<br/>"+
            "}]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/micBlogPage",method= RequestMethod.GET)
    public FacadeResult getMicBlogPage(String proId){
        OpResult opResult = kolInfoDataService.microBlogPage(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

//    @ApiOperation(value = "获取微博下载数据",notes = "直接下载数据，无返回字段")
//    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "int")
//    @RequestMapping(value = "/micBlogDown",method= RequestMethod.GET)
//    public void getMicBlogDown(Integer proId){
//        kolInfoDataService.microBlogDown(proId);
//    }

    /**
     * 微信概览头部统计数据
     * @param proId
     * @return
     */
    @ApiOperation(value="微信概览头部统计数据",notes = "data:{" +
            "<br/>"+
            "read:              --总阅读量," +"<br/>"+
            "pro_id:            --项目id," +"<br/>"+
            "attitudes:         --总点赞量," +"<br/>"+
            "pro_kol_count:     --kol数量," +"<br/>"+
            "pv:                --pv数量," +"<br/>"+
            "uv:                --uv数量" +"<br/>"+
            "}")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/weChartHeadData",method= RequestMethod.GET)
    public FacadeResult getWeChartHeadData(String proId){
        OpResult opResult = kolInfoDataService.weChartHeadData(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微信总阅读量统计走势
     * @param proId
     * @param startDate
     * @param endDate
     * @return
     */
    @ApiOperation(value="微信项目总阅读量趋势监测统计",notes = "data:[{" +
            "<br/>"+
            "name:               --日期," +"<br/>"+
            "value:              --数量" +"<br/>"+
            "}]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/weChartReadTrend",method= RequestMethod.GET)
    public FacadeResult getWeChartReadTrend(String proId, String startDate, String endDate){
        OpResult opResult = kolInfoDataService.weChartReadTrend(proId,startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微信Kol点赞情况
     * @param proId
     * @return
     */
    @ApiOperation(value="微信点赞量监测统计",notes = "data:[{" +
            "<br/>"+
            "name:              --名称," +"<br/>"+
            "value:             --数量" +"<br/>"+
            "}]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/weChartAttitudes",method= RequestMethod.GET)
    public FacadeResult getWeChartAttitudes(String proId){
        OpResult opResult = kolInfoDataService.weChartAttitudes(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微信总曝光量kol贡献比
     * @param proId
     * @return
     */
    @ApiOperation(value="微信项目阅读量kol贡献百分比",notes = "data:[{" +
            "<br/>"+
            "name:               --名称，" +"<br/>"+
            "value:              --数值" +"<br/>"+
            "}]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/weChartContribution",method= RequestMethod.GET)
    public FacadeResult getWeChartContribution(String proId){
        OpResult opResult = kolInfoDataService.weChartContribution(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 微信项目概览页面链接pv来源百分比
     * @param proId
     * @return
     */
    @ApiOperation(value="微信页面链接pv来源百分比",notes = "data:[{" +
            "<br/>"+
            "name:               --名称，" +"<br/>"+
            "value:              --数值" +"<br/>"+
            "}]")
    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "String",paramType = "query")
    @RequestMapping(value = "/weChartPage",method= RequestMethod.GET)
    public FacadeResult getWeChartPage(String proId){
        OpResult opResult = kolInfoDataService.weChartPage(proId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

//    @ApiOperation(value = "获取微信下载数据",notes = "直接下载数据，无返回字段")
//    @ApiImplicitParam(name = "proId", value = "项目ID", required = true, dataType = "int")
//    @RequestMapping(value = "/weChartDown",method= RequestMethod.GET)
//    public void getWeChartDown(Integer proId){
//        kolInfoDataService.weChartDown(proId);
//    }
}
