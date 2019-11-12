package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.ProductPreferencesService;
import com.st.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品喜好控制层
 * Created by zhaoyx on 2016/9/18.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/productPreference")
public class ProductPreferencesController extends BaseController {
    @Autowired
    private ProductPreferencesService productPreferencesServiceImpl;

    /**
     * 品牌特征
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取品牌特征", notes=" data: \n" +"<br />"+
            "    [{\n" +"<br />"+
            "      \"name\": \"德芙\",\n --品牌特征名称" +"<br />"+
            "      \"value\": \"23322\"\n --品牌特征数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "品牌id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/brand",method= RequestMethod.GET)
    public FacadeResult getBrandCharacteristics(String propertyValue,String pid,String groupId){
        //数据检查
        if(StringUtils.isNull(groupId) && StringUtils.isNull(pid)){
            return generateException(1001);
        }
//        String pid = "13";
        OpResult opResult = productPreferencesServiceImpl.getBrandCharacteristics(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 产品特征
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取产品特征", notes=" data: \n" +"<br />"+
            "    [{\n" +"<br />"+
            "      \"name\": \"产品1\",\n --产品特征名称" +"<br />"+
            "      \"value\": \"23322\"\n --产品特征数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "品牌id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/product",method= RequestMethod.GET)
    public FacadeResult getProductCharacteristics(String propertyValue,String pid,String groupId){
        //数据检查
        if(StringUtils.isNull(pid)){
            return generateException(1001);
        }
//        String pid = "18";
        OpResult opResult = productPreferencesServiceImpl.getProductCharacteristics(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 口味特征
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取口味特征", notes=" data: \n" +"<br />"+
            "    [{\n" +"<br />"+
            "      \"name\": \"口味1\",\n --口味特征名称" +"<br />"+
            "      \"value\": \"23322\"\n --口味特征数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "品牌id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/flavor",method= RequestMethod.GET)
    public FacadeResult getFlavorCharacteristics(String propertyValue,String pid,String groupId){
        //数据检查
        if(StringUtils.isNull(pid)){
            return generateException(1001);
        }
//        String pid = "198458";
        OpResult opResult = productPreferencesServiceImpl.getFlavorCharacteristics(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 包装特征
     * @param propertyValue 属性值
     * @return
     */
    @ApiOperation(value="获取包装特征", notes=" data: \n" +"<br />"+
            "    [{\n" +"<br />"+
            "      \"name\": \"Rose1\",\n --包装特征名称" +"<br />"+
            "      \"value\": \"23322\"\n --包装特征数量" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "品牌id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/packaging",method= RequestMethod.GET)
    public FacadeResult getPackagingCharacteristics(String propertyValue,String pid,String groupId){
        //数据检查
        if(StringUtils.isNull(pid)){
            return generateException(1001);
        }
//        String pid = "198459";
        OpResult opResult = productPreferencesServiceImpl.getPackagingCharacteristics(propertyValue,pid,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取品牌列表
     * @return
     */
    @ApiOperation(value="获取品牌列表", notes=" data: \n" +"<br />"+
            "    [{\n" +"<br />"+
            "      \"name\": \"全部\",\n --品牌名称" +"<br />"+
            "      \"value\": \"0\"\n --品牌id" +"<br />"+
            "    }]")
    @RequestMapping(value = "/brandClass",method= RequestMethod.GET)
    public FacadeResult getBrandClass(){
        OpResult opResult = productPreferencesServiceImpl.getBrandClass();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

}
