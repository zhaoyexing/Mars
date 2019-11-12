package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.CampaginMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaoyx on 2016/10/26.
 */
@Api(tags = "4:Campagin监测",description = " ")
@RestController
@RequestMapping("/CampaginMonitor")
public class CampaginMonitorController extends BaseController {
    @Autowired
    private CampaginMonitorService CampaginMonitorServiceImpl;


    @ApiOperation(value="活动参与用户数",notes="datas:" +"<br/>"+
            "    [{\n" +"<br/>"+
            "        \"name\": \"访问用户数\",\n" +"<br/>"+
            "        \"value\": [\n" +"<br/>"+
            "            {\n" +"<br/>"+
            "                \"date\": \"2016-11-11\",,--日期\n" +"<br/>"+
            "                \"count\": 2344  --数量 \n" +"<br/>"+
            "            }\n" +"<br/>"+
            "        ]\n" +"<br/>"+
            "    }\n" +"<br/>"+
            "]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "activityName", value = "活动名", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/activityParticipationNumber",method= RequestMethod.GET)
    public FacadeResult getActivityParticipationNumber(String activityName,String startDate,String endDate){
        OpResult opResult = CampaginMonitorServiceImpl.getActivityParticipationNumber(activityName,startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
    @ApiOperation(value="页面浏览次数/浏览人数",notes="datas:" +"<br/>"+
            "[{\n" +"<br/>"+
            "        \"name\": \"浏览人数\",\n --浏览人数" +"<br/>"+
            "        \"value\": [\n" +"<br/>"+
            "            {\n" +"<br/>"+
            "                \"count\": 2344,\n --数量 " +"<br/>"+
            "                \"url\": \"url1\"\n -- url标签" +"<br/>"+
            "            }]\n" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "activityName", value = "活动名", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/viewVisitorsNumber",method= RequestMethod.GET)
    public FacadeResult getViewVisitorsNumber(String activityName,String startDate,String endDate){
        OpResult opResult = CampaginMonitorServiceImpl.getViewVisitorsNumber(activityName,startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value="Button点击次数/点击人数",notes="datas:" +"<br/>"+
            "{\n" +"<br/>"+
            "    \"userlist\": [\n --button列表" +"<br/>"+
            "      {\n" +"<br/>"+
            "        \"buttonName\": \"MENU\",\n --按钮名字" +"<br/>"+
            "        \"clickTimes\": 0,\n --点击次数" +"<br/>"+
            "        \"clickNumber\": 0\n --点击人数" +"<br/>"+
            "      }\n" +"<br/>"+
            "    ],\n" +"<br/>"+
            "    \"total\": 5\n --总数" +"<br/>"+
            "  }")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "activityName", value = "活动名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示多少", required = true, dataType = "int",paramType = "query"),
    })
    @RequestMapping(value = "/buttonNumber",method= RequestMethod.GET)
    public FacadeResult getButtonNumber(String activityName,String startDate,String endDate,int pageNum,int pageSize){
        OpResult opResult = CampaginMonitorServiceImpl.getButtonNumber(activityName,startDate,endDate,pageNum,pageSize);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    @ApiOperation(value="传播用户与非传播用户",notes="datas:" +"<br/>"+
            "[{\n" +"<br/>"+
            "        \"name\": \"传播用户\",\n -- 传播与非传播名称" +"<br/>"+
            "        \"count\": 100\n --人数" +"<br/>"+
            "    }\n" +"<br/>"+
            "]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "activityName", value = "活动名", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/communicationUser",method= RequestMethod.GET)
    public FacadeResult getCommunicationUser(String activityName,String startDate,String endDate){
        OpResult opResult = CampaginMonitorServiceImpl.getCommunicationUser(activityName,startDate,endDate);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value="传播用户分析",notes="datas:" +"<br/>"+
            " {\n" +
            "    \"total\": \"4\",\n --获取的总数" +"<br/>"+
            "    \"list\": [\n" +
            "      {\n" +
            "        \"originator\": \"oRKZfs6EbpTtWCeF8n432zsqVWBw\",\n -- 传播用户id" +"<br/>"+
            "        \"nickName\": \"unknown\",\n --昵称" +"<br/>"+
            "        \"levelOne\": 1,\n --影响力覆盖用户" +"<br/>"+
            "        \"levelTwo\": 0\n --二次传播用户" +"<br/>"+
            "      }\n" +
            "    ]\n" +
            "  }")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "activityName", value = "活动名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示多少", required = true, dataType = "int",paramType = "query"),
    })
    @RequestMapping(value = "/userAnalysis",method= RequestMethod.GET)
    public FacadeResult getUserAnalysis(String activityName,String startDate,String endDate,int pageNum,int pageSize){
        OpResult opResult = CampaginMonitorServiceImpl.getUserAnalysis(activityName,startDate,endDate,pageNum,pageSize);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    @ApiOperation(value="活动列表",notes="datas:" +"<br/>"+
            " [\n" +"<br/>"+
            "    {\n" +"<br/>"+
            "      \"name\": \"测试2016-11-08\",--活动名称\n" +"<br/>"+
            "      \"value\": \"dmp_418_172177660\" --活动itemKey(传播与非传播用户和传播用户分析需要传这个参数) \n" +"<br/>"+
            "    }\n" +"<br/>"+
            "  ]")
    @RequestMapping(value = "/getActivityNameList",method= RequestMethod.GET)
    public FacadeResult getActivityNameList(){
        OpResult opResult = CampaginMonitorServiceImpl.getActivityNameList();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

}
