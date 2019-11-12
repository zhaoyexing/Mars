package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.MarsUserGroup;
import com.st.model.OpResult;
import com.st.service.MarsOrderDsService;
import com.st.service.UserUVService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 用户UV控制层
 * Created by zhaoyx on 2016/9/19.
 */
@RestController
@Api(tags = "1:用户画像",description = " ")
public class UserUVController extends BaseController {
    @Autowired
    private UserUVService userUVServiceImpl;
    @Autowired
    private MarsOrderDsService marsOrderDsServiceImpl;
    @Value("${local.path}")
    private String localPath;
    /**
     * 查询用户粉丝UV值
     * @return
     */
    @ApiOperation(value="获取每小时UV", notes="datas:" +
            "{" +"<br />"+
            "      \"time\": \"2016-09-23 10:00:00\",--时间格式(yyyy-MM-dd HH:00:00)" +"<br />"+
            "      \"increaseCount\": 1,--增长数量,如果为正数或0，表示增加，如果为负数，表示为减少。" +"<br />"+
            "      \"count\": 3610 --用户总数" +"<br />"+
            "    }")
    @RequestMapping(value = "/useruv",method= RequestMethod.GET)
    public FacadeResult getUserUV(){
        OpResult opResult = userUVServiceImpl.getUVCount();
        if (opResult.isSucceed()){
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }

    @ApiOperation(value = "下载匹配数据", notes = "")
    @RequestMapping(value = "/downLoadEBUser", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downLoadUserGroup(HttpServletResponse response) throws IOException {
        //String filePath="E:\\MF-数据推送-字段对应表.xls";
       OpResult<String> opResult = userUVServiceImpl.downLoadEBUser();
        String filePath = localPath + "电商用户匹配数据.xls";
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
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(file.getInputStream()));



    }
}
