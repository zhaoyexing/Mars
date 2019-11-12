package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.UserAnalysisService;
import com.st.utils.StringUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户分析控制层
 * Created by zhaoyx on 2016/9/19.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/userAnalysis")
public class UserAnalysisController extends BaseController {
    @Autowired
    private UserAnalysisService userAnalysisServiceImpl;

    /**
     * 获取粉丝地域分布
     * @param propertyValue 属性性
     * @return
     */
    @ApiOperation(value="获取粉丝地域分布", notes="datas:" +
            "[{\n" +"<br/>"+
            "      \"province\": \"北京\",--省份" +"<br/>"+
            "      \"count\": 28,--数量" +"<br/>"+
            "      \"countPct\": 18.74,--占比"+"\\*"+"100,前台使用时，可直接加个%号" +"<br/>"+
            "      \"nationalPct\": 11.29 --全国网名占比"+"\\*"+"100,前台使用时，可直接加个%号" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/area",method= RequestMethod.GET)
    public FacadeResult getFanAreaDistribution(String propertyValue,String groupId){
        String statisPropertyName = "province";
        OpResult opResult = userAnalysisServiceImpl.getFanAreaDistribution(propertyValue,statisPropertyName,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取性别分布
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取性别分布", notes="datas:" +"<br/>"+
            "[{\n" +"<br/>"+
            "      \"name\": \"女\",\n   --性别 " +"<br/>"+
            "      \"count\": \"68\"\n   --数量" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/sex",method= RequestMethod.GET)
    public FacadeResult getSexDistribution(String propertyValue,String groupId){
        String statisPropertyName = "sex";
        OpResult opResult = userAnalysisServiceImpl.getSexDistribution(propertyValue,statisPropertyName,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
    @ApiOperation(value="获取年龄分布", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/age",method= RequestMethod.GET)
    public FacadeResult getAgeDistribution(String propertyValue,String groupId){
        OpResult opResult = userAnalysisServiceImpl.getAgeDistribution(propertyValue,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
    @ApiOperation(value="获取婚姻状况", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/maritalstatus",method= RequestMethod.GET)
    public FacadeResult getMaritalStatus(String propertyValue,String groupId){
        String statisPropertyName = "marital";
        OpResult opResult = userAnalysisServiceImpl.getMaritalStatus(propertyValue,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
    @ApiOperation(value="有无小孩", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/babyNum",method= RequestMethod.GET)
    public FacadeResult getBabyNum(String propertyValue,String groupId){
        OpResult opResult = userAnalysisServiceImpl.getBabyNum(propertyValue,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
    @ApiOperation(value="获取兴趣爱好", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/hobby",method= RequestMethod.GET)
    public FacadeResult getHobby(String propertyValue,String groupId){
        OpResult opResult = userAnalysisServiceImpl.getHobby(propertyValue,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

}
