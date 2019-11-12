package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.ActivityAnalysisService;
import com.st.utils.StringUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动分析控制层
 * Created by zhaoyx on 2016/9/18.
 */
@RestController
@RequestMapping("/activityAnalysis")
@Api(tags = "1:用户画像",description = " ")
public class ActivityAnalysisController extends BaseController {
    @Autowired
    private ActivityAnalysisService activityAnalysisServiceImpl;

    /**
     * 互动主题分布
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取互动主题分布", notes="datas:" +
            "[{\n" +"<br />"+
            "      \"name\": \"大转盘\",\n --主题名称" +"<br />"+
            "      \"value\": 23322\n   --数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/theme",method= RequestMethod.GET)
    public FacadeResult getInteractiveThemeDistribution(String propertyValue,String groupId){
        String pid = "6";
        OpResult opResult = activityAnalysisServiceImpl.getInteractiveThemeDistribution(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 互动意愿分布
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取互动意愿分布", notes="datas:" +
            "[{\n" +"<br />"+
            "      \"name\": \"高\",\n --意愿名称" +"<br />"+
            "      \"value\": 23322\n   --数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/intention",method= RequestMethod.GET)
    public FacadeResult getInteractiveIntentionDistribution(String propertyValue,String groupId){
        String pid = "195079";
        OpResult opResult = activityAnalysisServiceImpl.getInteractiveIntentionDistribution(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 互动利益分布
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取互动利益分布", notes="datas:" +
            "[{\n" +"<br />"+
            "      \"name\": \"Rose1\",\n --利益名称" +"<br />"+
            "      \"value\": 23322\n   --数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/interest",method= RequestMethod.GET)
    public FacadeResult getInteractiveInterestDistribution(String propertyValue,String groupId){
        String pid = "8";
        OpResult opResult = activityAnalysisServiceImpl.getInteractiveThemeDistribution(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 互动形式分布
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取互动形式分布", notes="datas:" +
            "[{\n" +"<br />"+
            "      \"name\": \"积分游戏\",\n --形式名称" +"<br />"+
            "      \"value\": 23322\n   --数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/form",method= RequestMethod.GET)
    public FacadeResult getInteractiveFormDistribution(String propertyValue,String groupId){
        String pid = "7";
        OpResult opResult = activityAnalysisServiceImpl.getInteractiveThemeDistribution(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
