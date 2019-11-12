package com.st.service.impl;

import com.st.mapper.MarsOrderMapper;
import com.st.model.MarsOrder;
import com.st.service.MarsEBusinessPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaoyx on 2017/1/20.
 */
@Service
public class MarsEBusinessPushServiceImpl implements MarsEBusinessPushService {
    @Autowired
    private MarsOrderMapper marsOrderMapper;

    @Override
    public void pushEBusiness() {
        int totalCount = marsOrderMapper.getAllCount();
        int pageSize = 10000;
        int pages = totalCount / pageSize;
        int pageNo = totalCount % pageSize > 0 ? ++pages : pages;
        List<MarsOrder> orderList;
        for (int i = 0; i < pageNo; i++) {
            orderList = marsOrderMapper.findOrderByPage(i * pageSize, pageSize);
//            orderList.forEach(uid->SendDataToOLAP.sendData(SendDataToOLAP.setSendData(uid)));
        }
        marsOrderMapper.findOrderByPage(1,1000);
    }
}
