package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.UserCountService;
import com.st.service.WeChatAndElectricityUserNumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户总数控制层
 * Created by zhaoyx on 2016/9/13.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/UserCount")
public class UserCountController extends BaseController {
    @Autowired
    private UserCountService userCountServiceImpl;
    @Autowired
    private WeChatAndElectricityUserNumService weChatAndElectricityUserNumService;

    /**
     * 获取用户总数
     * @return 数量
     */
    @ApiOperation(value="获取用户总数", notes="data: '3614' --用户总数")
    @RequestMapping(value = "/getUserCount",method= RequestMethod.GET)
    public FacadeResult getUserCount(){
//        OpResult opResult = userCountServiceImpl.getUserCount();
        OpResult opResult = weChatAndElectricityUserNumService.getUserCount();
        if(opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
