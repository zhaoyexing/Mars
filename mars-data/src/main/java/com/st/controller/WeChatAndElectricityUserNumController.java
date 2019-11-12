package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.WeChatAndElectricityUserNumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2017/2/10.
 * 获取微信用户数量和电商用户数量控制层
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/weChatAndEleUserNum")
public class WeChatAndElectricityUserNumController extends BaseController{

    @Autowired
    private WeChatAndElectricityUserNumService weChatAndElectricityUserNumService;

    /**
     * 获取微信用户数量
     * @return
     */
    @ApiOperation(value="微信用户数量",notes = "data:{" +
            "<br/>"+
            "weChatUserNum:             --微信用户数量"+"<br/>"+
            "}")
    @RequestMapping(value = "/getWeChatUserNum",method= RequestMethod.GET)
    public FacadeResult getWeChatUserNum(){
        OpResult opResult = weChatAndElectricityUserNumService.getWeChatUserNum();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取电商用户数量
     * @return
     */
    @ApiOperation(value="电商用户数量",notes = "data:{" +
            "<br/>"+
            "electricityUserNum:             --电商用户数量"+"<br/>"+
            "}")
    @RequestMapping(value = "/getElectricityUserNum",method= RequestMethod.GET)
    public FacadeResult getElectricityUserNum(){
        OpResult opResult = weChatAndElectricityUserNumService.getElectricityUserNum();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
