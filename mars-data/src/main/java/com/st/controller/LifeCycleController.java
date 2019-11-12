package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.LifeCycleService;
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
 * 生命周期控制层
 * Created by zhaoyx on 2016/9/19.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/lifeCycle")
public class LifeCycleController extends BaseController {

    @Autowired
    private LifeCycleService lifeCycleServiceImpl;

    /**
     * 获取生命周期
     * @return
     */
    @ApiOperation(value="获取生命周期", notes="data:" +
            "[{\n" +"<br />"+
            "      \"name\": \"微信粉丝\",\n  --生命周期属性" +"<br />"+
            "      \"value\": \"124\"\n  --生命周期下的粉丝数量" +"<br />"+
            "    }\n" +"<br />"+
            "  ]")
    @RequestMapping(value = "/getLifeCycle",method= RequestMethod.GET)
    public FacadeResult getLifeCycleAttribute(){
        OpResult opResult = lifeCycleServiceImpl.getLifeCycleAttribute();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取用户列表
     * @return
     */
    @ApiOperation(value="获取用户列表", notes="datas: {" +
            "[{\n" +"<br />"+
            "      \"uid\": \"ovZ6uuA2uLiB4Hmv5I9vL3pILj-0\",--用户uid" +"<br />"+
            "      \"subscribe_status\": 1,                 --关注状态" +"<br />"+
            "      \"sex\": 2,\n                             --性别" +"<br />"+
            "      \"life_cycle\": \"微信粉丝\",\n             --生命周期" +"<br />"+
            "      \"appid\": 172205760,\n                  --appid" +"<br />"+
            "      \"unsubscribe_time\": 0,\n               --取消关注时间" +"<br />"+
            "      \"contact\": \"unknown\",                --联系人\n" +"<br />"+
            "      \"city\": \"海淀\",\n                     --城市" +"<br />"+
            "      \"subscribe_time\": 1473239418,\n        --关注时间" +"<br />"+
            "      \"nick_name\": \"娜娜\",\n                --呢称" +"<br />"+
            "      \"province\": \"北京\",\n                 --省份" +"<br />"+
            "      \"openid\": \" \",                       --openid " +"<br />"+
            "    }]," +"<br />"+
            "\"total:\" : 23232, (总数)}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "propertyValue", value = "属性值(例如：微信粉丝,值集参考获取生命周期接口)", required = false, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "用户群组主键值", required = false, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/userList",method= RequestMethod.GET)
    public FacadeResult getUserList(int pageNum, int pageSize, String propertyValue,String groupId){
        //数据检查
        if(pageNum ==0 || pageSize == 0){
            return generateException(1001);
        }
        OpResult opResult = lifeCycleServiceImpl.getUserList(pageNum,pageSize,propertyValue,groupId);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
