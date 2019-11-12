package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.service.PushMemberService;
import com.st.service.SyncMemberAndOrderService;
import com.st.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
@ApiIgnore
@Controller
@RequestMapping("/push")
public class PushMemberInfoController extends BaseController{

    @Autowired
    private PushMemberService pushMemberSerive;

    @Autowired
    private SyncMemberAndOrderService syncMemberAndOrderService;
    @RequestMapping(value="/pushMemberInfo",method = RequestMethod.POST)
    public  @ResponseBody
    FacadeResult  pushMemberInfo(HttpServletRequest request){

        //如果open_id为空则返回错误码
        if(StringUtils.isNull(request.getParameter("open_id"))){
            return  generateException(1001);
        }
        //将参数封装到map里面
        Map<String,Object> paramMap = StringUtils.getParameterMap(request);

        pushMemberSerive.saveMemberInfo(paramMap);

        return  generateData(500);
    }

    @RequestMapping("/schedSyncMemberInfo")
    public @ResponseBody
    FacadeResult  schedSyncMemberInfo(HttpServletRequest request){



     syncMemberAndOrderService.syncMemberAndOrderData();


        return  generateData(500);

    }

}
