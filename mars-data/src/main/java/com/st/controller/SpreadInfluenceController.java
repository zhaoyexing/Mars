package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.SpreadInfluenceService;
import com.st.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 传播影响力控制层
 *
 * Created by zhaoyx on 2016/9/19.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/spreadInfluence")
public class SpreadInfluenceController extends BaseController {

    @Autowired
    private SpreadInfluenceService spreadInfluenceServiceImpl;

    /**
     * 传播影响力
     *
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取传播影响力", notes="datas:[\n" +"<br/>"+
            "    {\n" +"<br/>"+
            "      \"name\": \"高\",\n   --传播影响力属性名称" +"<br/>"+
            "      \"count\": 1682\n     --数量" +"<br/>"+
            "    }")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/influence",method= RequestMethod.GET)
    public FacadeResult getSpreadInfluence(String propertyValue,String groupId) {
        Map<String, Object> returnMap = new HashMap<>();
        String pid = "17";
        OpResult opResult = spreadInfluenceServiceImpl.getSpreadInfluence(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 传播内容倾向
     *
     * @param propertyValue 属性值
     * @return
     */

    @ApiOperation(value="获取传播内容倾向", notes="datas:" +"<br/>"+
            "[{\n" +"<br/>"+
            "      \"name\": \"单品一\",\n   --传播倾向属性名称" +"<br/>"+
            "      \"count\": 1682\n           --数量" +"<br/>"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/tendency",method= RequestMethod.GET)
    public FacadeResult getSpreadingTendency(String propertyValue,String groupId) {
        String pid = "195085";
        OpResult opResult = spreadInfluenceServiceImpl.getSpreadingTendency(propertyValue, pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

}
