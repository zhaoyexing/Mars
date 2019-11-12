package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.WxBackstageService;
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
import java.util.*;

/**
 * Created by admin on 2016/9/19.
 */
@Controller
@RequestMapping("/wxBackstage")
@Api(tags="3:SCRM统计",description = " ")
public class WxBackstageController extends BaseController{

    @Autowired
    private WxBackstageService wxBackstageService;

    @RequestMapping(value = "/getKeyIndicator",method= RequestMethod.GET)
    @ApiOperation(value="昨日关键指标", notes="datas:" +
            "{" +"<br />"+
            "      \"imageTextCount\": 3368,--图文阅读次数" +"<br />"+
            "      \"originalTextCount\": 3368,--原文阅读次数" +"<br />"+
            "      \"forwardRate\": 3 (-- 转发率 (3%)" +"<br />"+
            "      \"imgTextCNVRate\": 8 (-- 转发率 (3%)" +"<br />"+
            "      \"origTextCNVRate\": 5 (-- 转发率 (3%)" +"<br />"+
            "    }",
            position = 1

    )
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query")
    })
    public FacadeResult getKeyIndicator(HttpServletRequest request, HttpServletResponse response){

        //1、参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        if(StringUtils.isValidDate(startDate)||StringUtils.isValidDate(endDate)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getKeyIndicator(startDate,endDate);

        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        //4、请求失败返回异常错误码
        return generateException(500);

    }


    @ApiOperation(value="数据趋势", notes="datas:" +
            "[<br />" +
            "    { <br /> "+
            "      \"typeName\": \"manCount\",--阅读人数" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
                             "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读人数" +"<br />"+
            "                  },<br /> "+
                             "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读人数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "       }<br /> ,"+
            "    { <br /> "+
            "      \"typeName\": \"readCount\",--阅读次数" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读次数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读次数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "   }<br />]",
            position = 2
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "type", value = "查询分类（阅读图文人数imageText、阅读原文人数originalText、转发forward、收藏store）", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getIndicatorTrend",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getIndicatorTrend(HttpServletRequest request, HttpServletResponse response){

        //1、参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String type = request.getParameter("type");
        if(StringUtils.isValidDate(startDate)||StringUtils.isValidDate(endDate)||StringUtils.isNull(type)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getIndicatorTrend(startDate,endDate,type);
        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        //4、请求失败返回异常错误码
        return generateException(500);

    }


    @ApiOperation(value="数据趋势通过文章Id", notes="datas:" +
            "[<br />" +
            "    { <br /> "+
            "      \"typeName\": \"manCount\",--阅读人数" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读人数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读人数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "       }<br /> ,"+
            "    { <br /> "+
            "      \"typeName\": \"readCount\",--阅读次数" +"<br />"+
            "      \"value\":" +
            "              [<br />" +
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读次数" +"<br />"+
            "                  },<br /> "+
            "{ <br />" +
            "                   \"name\": \"2016-08-01\", --日期" +"<br />"+
            "                   \"value\": 100,–阅读次数" +"<br />"+
            "                  }<br /> "+
            "                ]<br /> "+
            "   }<br />]",
            position = 2
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "articleId", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "msgId", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getIndicatorTrendByArticleId",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getIndicatorTrendByArticleId(HttpServletRequest request, HttpServletResponse response){

        //1、参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String articleId = request.getParameter("articleId");
        String msgId = request.getParameter("msgId");
        if(StringUtils.isValidDate(startDate)||StringUtils.isValidDate(endDate)||StringUtils.isNull(msgId)||StringUtils.isNull(articleId)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getIndicatorTrendByArticleId(startDate,endDate,articleId,msgId);
        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        //4、请求失败返回异常错误码
        return generateException(500);
    }


    @ApiOperation(value="传播用户分析", notes="datas:" +
            "[<br />{" +"<br />"+
            "      \"articleId\":1,-图文消息Id" +"<br />"+
            "      \"title\": \"获奖名单一起为哺乳妈妈喝彩\",--图文标题" +"<br />"+
            "      \"sendDate\": \"2016-08-12\",--发送时间" +"<br />"+
            "      \"arrivedManCount\": 421, -- 送达人数" +"<br />"+
            "      \"imgTextManCount\": 10, -- 图文页阅读人数" +"<br />"+
            "      \"imgTextReadCount\": 10 --图文页阅读次数" +"<br />"+
            "      \"originalTextManCount\": 10 --原文页阅读人数" +"<br />"+
            "      \"originalTextReadCount\": 10 --原文页阅读次数" +"<br />"+
            "      \"forwardManCount\": 10 --转发数 人数" +"<br />"+
            "      \"forwardReadCount\": 10 --转发数次数" +"<br />"+
            "      \"storeCount\": 10 -- 总收藏人数" +"<br />"+
            "    }...<br />]",
            position = 3
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "String",paramType = "query"),
    })
    @RequestMapping(value="/getArticleSpreadData",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getArticleSpreadData(HttpServletRequest request, HttpServletResponse response){

        //1、参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isValidDate(startDate)||StringUtils.isValidDate(endDate)||StringUtils.isNull(pageNum)||StringUtils.isNull(pageSize)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getArticleSpreadData(startDate,endDate,pageNum,pageSize);
        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        //4、请求失败返回异常错误码
        return generateException(500);


    }



    @ApiOperation(value="传播用户分析-根据文章Id", notes="datas:" +
            "[<br />{" +"<br />"+
            "      \"articleId\":1,-图文消息Id" +"<br />"+
            "      \"msgId\":1,-消息Id" +"<br />"+
            "      \"title\": \"获奖名单一起为哺乳妈妈喝彩\",--图文标题" +"<br />"+
            "      \"sendDate\": \"2016-08-12\",--发送时间" +"<br />"+
            "      \"arrivedManCount\": 421, -- 送达人数" +"<br />"+
            "      \"imgTextManCount\": 10, -- 图文页阅读人数" +"<br />"+
            "      \"imgTextReadCount\": 10 --图文页阅读次数" +"<br />"+
            "      \"originalTextManCount\": 10 --原文页阅读人数" +"<br />"+
            "      \"originalTextReadCount\": 10 --原文页阅读次数" +"<br />"+
            "      \"forwardManCount\": 10 --转发数 人数" +"<br />"+
            "      \"forwardReadCount\": 10 --转发数次数" +"<br />"+
            "      \"storeCount\": 10 -- 总收藏人数" +"<br />"+
            "    }...<br />]",
            position = 3
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "msgId", value = "消息id", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getArticleSpreadDataByArticleId",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getArticleSpreadDataByArticleId(HttpServletRequest request, HttpServletResponse response){

        //参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String msgId = request.getParameter("msgId");
        String articleId = request.getParameter("articleId");
        if(StringUtils.isNull(startDate)||StringUtils.isNull(endDate)||StringUtils.isNull(pageNum)||StringUtils.isNull(pageSize)||StringUtils.isNull(msgId)||StringUtils.isNull(articleId)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getArticleSpreadDataByArticleId(startDate,endDate,pageNum,pageSize,msgId,articleId);
        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }

        //4、请求失败返回异常错误码
        return generateException(500);
    }

    @ApiOperation(value="文章指标概览", notes="datas:" +
           "[<br />" +
            "      {"+
            "      \"name\": \"arrivedManCount\", --送达 <br/>"+
            "      \"value\": \"100\"  <br/>" +
            "       },<br/>"+
            "      {"+
            "      \"name\": \"storeCount\", -- 收藏 <br/>"+
            "      \"value\": \"100\"  <br/>" +
            "       },<br/>"+
            "      {"+
            "      \"name\": \"forwardCount\",   --转发<br/>"+
            "      \"value\": \"100\"  <br/>" +
            "       },<br/>"+
            "      {"+
            "      \"name\": \"imgTextReadCount\", --图文阅读次数 <br/>"+
            "      \"value\": \"100\"  <br/>" +
            "       },<br/>"+
            "      {"+
            "      \"name\": \"origTextReadCount\",  --原文阅读次数<br/>"+
            "      \"value\": \"100\"  <br/>" +
            "       }"+
            "<br />]" ,

            position = 4
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "起始时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "终止时间（yyyy-MM-dd）", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value="/getIndicatorByArticleId",method= RequestMethod.GET)
    @ResponseBody
    public FacadeResult getIndicatorByArticleId(HttpServletRequest request, HttpServletResponse response){

        //参数验证
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String articleId = request.getParameter("articleId");
        if(StringUtils.isValidDate(startDate)||StringUtils.isValidDate(endDate)||StringUtils.isNull(articleId)){
            return generateException(1001);
        }

        //2、service层处理逻辑获取返回结果
        OpResult opResult = wxBackstageService.getIndicatorByArticleId(startDate,endDate,articleId);

        //3、请求成功返回结果
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        //4、请求失败返回异常错误码
        return generateException(500);



    }



}
