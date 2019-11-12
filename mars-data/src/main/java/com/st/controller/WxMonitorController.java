package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.WxMonitorService;
import com.st.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by admin on 2016/9/19.
 */
@Controller
@RequestMapping("/wxMonitor")
@Api(tags="3:SCRM统计",description = " ")
public class WxMonitorController extends BaseController{

    @Autowired
    private WxMonitorService wxMonitorServiceImpl;

    @ApiOperation(value="招募统计", notes="datas:" +
            "{" +"<br />"+
            "      \"total_fans\": 1000,--累计粉丝数" +"<br />"+
            "      \"current_total_fans\": 308, --当前粉丝数" +"<br />"+
            "    }",
            position = 0
    )
    @RequestMapping(value="/wxFansCount",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult wxFansCount(HttpServletRequest request, HttpServletResponse response){
        OpResult opResult = wxMonitorServiceImpl.wxFansCount();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value="关注渠道统计", notes="datas:" +
            "[<br />" +
            "   {" +"<br />"+
            "      \"name\": \"电商二维码\",--渠道名称" +"<br />"+
            "      \"value\": 308 --关注粉丝数" +"<br />"+
            "    },<br />" +
            "   {" +"<br />"+
            "      \"name\": \"微信搜索\",--渠道名称" +"<br />"+
            "      \"value\": 308 --关注粉丝数" +"<br />"+
            "    },<br />" +
            "   {" +"<br />"+
            "      \"name\": \"产品二维码\",--渠道名称" +"<br />"+
            "      \"value\": 308 --关注粉丝数" +"<br />"+
            "    },<br />" +
            "   {" +"<br />"+
            "      \"name\": \"其他\",--渠道名称" +"<br />"+
            "      \"value\": 308 --关注粉丝数" +"<br />"+
            "    }<br />" +
            "]",
            position = 0
    )
    @RequestMapping(value="/fansFollowChannelCount",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult fansFollowChannelCount(HttpServletRequest request, HttpServletResponse response){
        OpResult opResult = wxMonitorServiceImpl.getFansFollowChannelCount();
        if(opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);

    }



    @ApiOperation(value="新增-流失-净增粉丝统计", notes="datas:" +
            "[<br />" +
            "    { <br /> "+
            "      \"name\": \"流失粉丝\",--阅读人数" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–粉丝人数" +"<br />"+
            "                  }<br /> ..."+

            "                ]<br /> "+
            "       }<br /> ,"+
            "    { <br /> "+
            "      \"name\": \"readCount\",--新增粉丝" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–粉丝人数" +"<br />"+
            "                  }<br /> ..."+

            "                ]<br /> "+
            "       }<br /> ,"+
            "    { <br /> "+
            "      \"name\": \"净增粉丝\"," +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–粉丝人数" +"<br />"+
            "                  }<br />... "+
            "                ]<br /> "+
            "   }<br />]",
            position = 1
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "dataCycle", value = "获取的周期，dataCycle为1按日统计，dataCycle=3按月统计", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getFansCountRange",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getFansCountRange(HttpServletRequest request, HttpServletResponse response){
        //参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String dataCycle = request.getParameter("dataCycle");
        if (StringUtils.isNull(startDate)||StringUtils.isNull(endDate)){
            return generateException(1001);
        }
        OpResult opResult = wxMonitorServiceImpl.getFansCountRange(startDate,endDate,dataCycle);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value="菜单点击分布", notes="datas:" +
            "[<br />" +
            "    { <br /> "+
            "      \"name\": \"点击次数\", --类型名称" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"重磅活动\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–点击数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"兑奖中心\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–点击数" +"<br />"+
            "                  },<br /> "+
                            "{ <br />" +
            "                   \"name\": \"个人中心\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–粉丝人数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "       }<br /> "+
            "    { <br /> "+
            "      \"name\": \"点击人数\", --类型名称" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"重磅活动\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–点击人数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"兑奖中心\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–点击人数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"个人中心\", --菜单名称" +"<br />"+
            "                   \"value\": 100,–点击人数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "       }<br /> "+
            "    { <br /> "+
            "      \"name\": \"点击人数比例\", --类型名称" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"重磅活动\", --菜单名称" +"<br />"+
            "                   \"value\": 20,–百分数（%）" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"兑奖中心\", --菜单名称" +"<br />"+
            "                   \"value\": 10,––百分数（%）" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"个人中心\", --菜单名称" +"<br />"+
            "                   \"value\": 70,––百分数（%）" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "       }<br /> "+
            "   <br />]",
            position = 1
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "menuLevel", value = "菜单级别（1为微信主菜单，2为主菜单下子菜单）", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/menuClickDist",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult menuClickDist(HttpServletRequest request, HttpServletResponse response){
        //参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String menuLevel = request.getParameter("menuLevel");
       //  String dataCycle = request.getParameter("dataCycle");
        if (StringUtils.isNull(startDate)||StringUtils.isNull(endDate)||StringUtils.isNull(menuLevel)){
            return generateException(1001);
        }
        OpResult opResult = wxMonitorServiceImpl.menuClickDist(startDate, endDate, menuLevel);
        if(opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }



    @ApiOperation(value="获取菜单列表", notes="datas:" +
            "[<br />" +
            "   {" +"<br />"+
            "      \"menuName\": \"重磅中心\", --菜单名称" +"<br />"+
            "   }," +"<br />"+
            "   {" +"<br />"+
            "      \"menuName\": \"兑奖中心\", --菜单名称" +"<br />"+
            "   }," +"<br />"+
            "   {" +"<br />"+
            "      \"menuName\": \"个人中心\", --菜单名称" +"<br />"+
            "   }" +"<br />"+

            "<br />]" +
            "",
            position = 2
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuLevel", value = "菜单级别（1为微信主菜单，2为主菜单下子菜单）", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getMenuList",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getMenuList(HttpServletRequest request, HttpServletResponse response){
        //参数验证
        String menuLevel = request.getParameter("menuLevel");
        if (StringUtils.isNull(menuLevel)){
            return generateException(1001);
        }
        OpResult opResult = wxMonitorServiceImpl.getMenuList(menuLevel);
        if(opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value="微信点击趋势", notes="datas:" +
            "[<br />" +
            "    { <br /> "+
            "      \"name\": \"点击次数\"," +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,--粉丝人数" +"<br />"+
            "                  },<br />... "+

            "                ]<br /> "+
            "       }<br /> ,"+
            "    { <br /> "+
            "      \"name\": \"点击人数\"," +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,--粉丝人数" +"<br />"+
            "                  },<br /> ..."+
            "                ]<br /> "+
            "       }<br /> "+
            "   }<br />]",
            position = 1
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "menuName", value = "菜单名称(中文字符串编码)", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "menuLevel", value = "菜单级别（1为微信主菜单，2为主菜单下子菜单）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "dataCycle", value = "获取的周期，dataCycle为1按日统计，dataCycle=3按月统计", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getMenuClickTrend",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getMenuClickTrend(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        //1\参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String menuName = request.getParameter("menuName");
        String menuLevel = request.getParameter("menuLevel");
        String dataCycle = request.getParameter("dataCycle");
        if(StringUtils.isNull(startDate)||StringUtils.isNull(endDate)||StringUtils.isNull(menuName)||StringUtils.isNull(menuLevel)){
            return generateException(1001);
        }
        menuName = URLDecoder.decode(menuName,"UTF-8");
        OpResult opResult = wxMonitorServiceImpl.getMenuClickTrend(startDate,endDate,menuName,menuLevel,dataCycle);
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);

    }


}
