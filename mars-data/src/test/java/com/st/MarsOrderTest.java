package com.st;

import com.st.mapper.MarsOrderDsMapper;
import com.st.mapper.MarsOrderMapper;
import com.st.model.MarsOrder;
import com.st.model.MarsOrderDs;
import com.st.service.MarsOrderService;
import com.st.utils.SendDataDsToOLAP;
import com.st.utils.SendDataToOLAP;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zhaoyx on 2017/1/17.
 */
public class MarsOrderTest extends BaseTest {
    private static Logger logger = LoggerFactory.getLogger(MarsOrderTest.class);
    @Autowired
    private MarsOrderMapper marsOrderMapper;
    @Autowired
    private MarsOrderService marsOrderServiceImpl;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;

    @Test
    public void testPushOrder() {
        int totalCount = marsOrderMapper.getAllCount();
        int pageSize = 10000;

        int pages = totalCount / pageSize;
        int pageNo = totalCount % pageSize > 0 ? ++pages : pages;

        List<MarsOrder> prodList = null;
        for (int i = 0; i < pageNo; i++) {
            prodList = marsOrderMapper.findOrderByPage(i * pageSize, pageSize);

            for (MarsOrder marsOrder : prodList) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendDataToOLAP.sendData(SendDataToOLAP.setSendData(marsOrder));
            }
        }
    }
    @Test
    public void testPushDsOrder(){
        int totalCount = marsOrderDsMapper.getAllCount();
        int pageSize = 10000;
        int pages = totalCount/pageSize;
        int pageNo = totalCount % pageSize > 0 ? ++pages :pages;
        List<MarsOrderDs> prodList = null;
        for (int i = 0; i < pageNo; i++){
            prodList = marsOrderDsMapper.findOrderDsByPage(i * pageSize,pageSize);
            for (MarsOrderDs marsOrderDs:prodList){
                SendDataDsToOLAP.sendData(SendDataDsToOLAP.setSendData(marsOrderDs));
                //marsOrderDsMapper.updatePushFlag(marsOrderDs.getIntegrationId());
            }
            marsOrderDsMapper.addEBUser(prodList);
        }
    }

    @Test
    public void onlinePushOrder(){
        marsOrderServiceImpl.saveMarsOrder();
    }

}
