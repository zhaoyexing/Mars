package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.SinglePersonService;
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
 * Created by zhaoyx on 2016/9/19.
 */
@Api(tags = "1:用户画像",description = " ")
@RestController
@RequestMapping("/singlePerson")
public class SinglePersonController extends BaseController {
    @Autowired
    private SinglePersonService singlePersonServiceImpl;

    /**
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param uid   粉丝UID
     * @return
     */
    @ApiOperation(value="获取个人活跃度", notes="datas:" +"<br />"+
            "[{\n" +"<br />"+
            "      \"name\": \"2016/09/09\",\n  --日期 " +"<br />"+
            "      \"value\": \"0.9\"\n    --活跃度指数" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "粉丝uid", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/active",method= RequestMethod.GET)
    public FacadeResult getActiveDegree(String startDate,String endDate,String uid) {
        //数据检查
        if(StringUtils.isValidDate(startDate) || StringUtils.isValidDate(endDate) || StringUtils.isNull(uid)){
            return generateException(1001);
        }
        OpResult opResult = singlePersonServiceImpl.getUserPortrait(startDate,endDate,uid,"1");
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取个人用户价值
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param uid   粉丝UID
     * @return
     */
    @ApiOperation(value="获取个人用户价值", notes="datas:" +"<br />"+
            "[{\n" +"<br />"+
            "      \"name\": \"2016/09/09\",\n  --日期 " +"<br />"+
            "      \"value\": \"0.9\"\n    --活跃度指数" +"<br />"+
            "    }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "粉丝uid", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/userValue",method= RequestMethod.GET)
    public FacadeResult getUserValue(String startDate,String endDate,String uid) {
        //数据检查
        if(StringUtils.isValidDate(startDate) || StringUtils.isValidDate(endDate) || StringUtils.isNull(uid)){
            return generateException(1001);
        }
        OpResult opResult = singlePersonServiceImpl.getUserPortrait(startDate,endDate,uid,"157243");
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    /**
     * 获取个人产品喜好
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param uid   粉丝UID
     * @return
     */
    @ApiOperation(value="获取个人产品喜好", notes="datas:" +"<br />"+
            "[{\n" +"<br />"+
            "      \"name\": \"唇妆\",\n   --产品喜好名称" +"<br />"+
            "      \"value\": [\n" +"<br />"+
            "        {\n" +"<br />"+
            "          \"date\": \"2016-09-01\",\n  --日期" +"<br />"+
            "          \"count\": 2344\n     --数量" +"<br />"+
            "        }" +"<br />"+
            "}]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束日期(yyyy-MM-dd)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "粉丝uid", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/product",method= RequestMethod.GET)
    public FacadeResult getUserProductPreference(String startDate,String endDate,String uid) {
        //数据检查
        if(StringUtils.isValidDate(startDate) || StringUtils.isValidDate(endDate) || StringUtils.isNull(uid)){
            return generateException(1001);
        }
        OpResult opResult = singlePersonServiceImpl.getUserProductPreference(startDate,endDate,uid);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
