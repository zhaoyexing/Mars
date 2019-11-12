package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.MarsOrderDs;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.MarsOrderDsService;
import com.st.service.MarsOrderService;
import com.st.service.UserGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.assertj.core.api.Fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.ServletContext;

/**
 * 用户群组
 * Created by zhaoyx on 2016/11/14.
 */
@Api(tags = "6:用户群组", description = " ")
@RestController
@RequestMapping("/userGroup")
public class UserGroupController extends BaseController {
    @Value("${local.path}")
    private String localPath;
    @Autowired
    private UserGroupService userGroupServiceImpl;
    @Autowired
    private MarsOrderDsService marsOrderDsServiceImpl;
    @Autowired
    private MarsOrderService marsOrderServiceImpl;

    @ApiOperation(value = "创建用户群组", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "用户群组名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lifeCycle",value = "用户类型",required = false,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "conditions", value = "筛选条件(JSON串形式,如[{男,女},{18-20},...])", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/createUserGroup", method = RequestMethod.GET)
    public FacadeResult createUserGroup(String groupName, String lifeCycle,String conditions) {
        OpResult opResult = userGroupServiceImpl.saveUserGroup( groupName,lifeCycle,conditions);
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        return generateException(2000);
    }

    @ApiOperation(value = "编辑用户群组", notes = "\"datas:\" +\"<br/>\"+\n" +
            "            \"[{\\n\" +\"<br/>\"+\n" +
            "            \"      \\\"id\\\": \\\"5\\\",\\n   --用户群组主键 \" +\"<br/>\"+\n" +
            "            \"      \\\"groupName\\\": \\\"粉丝\\\"\\n   --用户群组名\" +\"<br/>\"+\n" +
            "            \"      \\\"conditions\": \"[{男，女},{18-20}]\"\n   --筛选条件" +"<br/>"+
            "            \"    }]\"")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户群组id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lifeCycle",value = "用户类型",required =false,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "groupName", value = "用户群组名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "conditions", value = "筛选条件(JSON串形式,如[{男,女},{18-20},...])", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/updateUserGroup", method = RequestMethod.GET)
    public FacadeResult updateUserGroup(String id,String lifeCycle,String groupName, String conditions) {
        OpResult opResult = userGroupServiceImpl.updateUserGroup(id,lifeCycle, groupName, conditions);
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    @ApiOperation(value = "删除用户群组", notes = "\"data\": {\n" +
            "    \"total\": 2\n  --剩余用户数" +"<br />"+
            "  }")
    @ApiImplicitParam(name = "id", value = "用户群组id", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/deleteUserGroup", method = RequestMethod.GET)
    public FacadeResult deleteUserGroup(String id) {
        OpResult opResult = userGroupServiceImpl.deleteUserGroup(id);
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        //marsOrderDsServiceImpl.saveMarsOrderDs();
        //marsOrderServiceImpl.saveMarsOrder();
        return generateException(500);
    }

    @ApiOperation(value = "查找用户群组", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", required = true, dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "显示多少", required = true, dataType = "int",paramType = "query"),
    })
    @RequestMapping(value = "/findAllUserGroup", method = RequestMethod.GET)
    public FacadeResult findAllUserGroup(int pageNo,int pageSize ) {

        OpResult opResult = userGroupServiceImpl.selectUserGroup( pageNo, pageSize);
        // 总记录数
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }


    @ApiOperation(value = "获取筛选条件", notes = "datas:" + "<br />" +
            "{\n" + "<br />" +
            "    \"name\": \"用户质量\",\n --属性名" + "<br />" +
            "    \"id\": [\n" + "<br />" +
            "        {\n" + "<br />" +
            "            \"name\": \"互动活跃度\",\n --标签名" + "<br />" +
            "            \"id\": 1,\n --标签id" + "<br />" +
            "            \"value\": [\n -- 标签值" + "<br />" +
            "                {\n" + "<br />" +
            "                    \"id\": 0,\n --标签值id(向后台传的数据)" + "<br />" +
            "                    \"value\": \"0.0-0.1\"\n --指数" + "<br />" +
            "                }\n" + "<br />" +
            "            ]\n" + "<br />" +
            "        }\n" + "<br />" +
            "    ]\n" + "<br />" +
            "}")
    @RequestMapping(value = "/screeningConditions", method = RequestMethod.GET)
    public FacadeResult getScreeningConditions() {
        OpResult opResult = userGroupServiceImpl.getScreeningConditions();
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    @ApiOperation(value = "下载用户群组", notes = "")
    @ApiImplicitParam(name = "id", value = "用户群组id", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/downLoadUserGroup", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downLoadUserGroup(String id, HttpServletResponse response) throws IOException {
            OpResult opResult =userGroupServiceImpl.getFileExcis(id);
            //String filePath="E:\\MF-数据推送-字段对应表.xls";
        MarsUserGroup userGroup = userGroupServiceImpl.getMarsUserGroup(id);
        String filePath = localPath + userGroup.getGroupName()+".zip";
        if (!opResult.isSucceed()) {
            File files = new File(filePath);
            if (!files.exists()){
                files.createNewFile();
            }
        }
            FileSystemResource file = new FileSystemResource(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-type", "text/html;charset=UTF-8");
            headers.setContentDispositionFormData("attachment", new String(file.getFilename().getBytes("UTF-8"), "ISO8859-1"));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(file.getInputStream()));



    }

}
