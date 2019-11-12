package com.st.controller;

import com.st.bo.FacadeResult;
import com.st.model.OpResult;
import com.st.service.MarsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by zhaoyx on 2016/11/14.
 */
@ApiIgnore
@RestController
@RequestMapping("/updateScreening")
public class UpdateScreeningController extends BaseController {
    @Autowired
    private MarsOrderService marsOrderServiceImpl;
    @RequestMapping("/")
    public FacadeResult updateScreeningConditions() {
        OpResult opResult = marsOrderServiceImpl.updateTags();
        if (opResult.isSucceed()) {
            return generateData(opResult.getResult());
        }
        return generateException(500);
    }
}
