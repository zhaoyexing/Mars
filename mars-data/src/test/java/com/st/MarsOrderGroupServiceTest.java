package com.st;

import com.st.service.MarsOrderDsService;
import com.st.service.MarsOrderHistoryService;
import com.st.service.impl.MarsOrderServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2016/12/13.
 */
public class MarsOrderGroupServiceTest extends BaseTest{
    @Autowired
    private MarsOrderServiceImpl marsOrderService;
    @Autowired
    private MarsOrderDsService marsOrderDsServiceImpl;
    @Autowired
    private MarsOrderHistoryService marsOrderHistoryServiceImpl;

    @Test
    public void saveMarsOrderTest(){
        marsOrderService.saveMarsOrder();
    }
    @Test
    public void saveMarsOrderDsTest(){
            marsOrderDsServiceImpl.saveMarsOrderDs();
    }
    @Test
    public  void saveMarsOrderDsesTest(){
        marsOrderDsServiceImpl.saveMarsOrderDses();
    }
    @Test
    public void updateBarCodeTest(){
        marsOrderHistoryServiceImpl.updateBarCode();
    }
    @Test
    public void addMarsOrderDsCountTest(){
        marsOrderHistoryServiceImpl.addMarsOrderDsCount();
    }

}
