package com.st;

import com.st.service.EBusinessStatisticsService;
import com.st.service.MarsEBusinessPushService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhaoyx on 2017/1/20.
 */
public class MarsEBusinessPushServiceTest extends BaseTest {
    @Autowired
    private MarsEBusinessPushService marsEBusinessPushServiceImpl;
    @Autowired
    private EBusinessStatisticsService eBusinessStatisticsServiceImpl;

    @Test
    public void testPushEBusiness() {
        marsEBusinessPushServiceImpl.pushEBusiness();
    }

    @Test
    public void testDownloadE() {
        String json = "{\"brandName\":[\"德芙\"],\"areas\":[\"上海\"],\"startData\":\"2017-02-01\",\"endData\":\"2017-03-03\",\"fileName\":\"赵test2\",\"userName\":\"null\",\"fields\":[\"transaction_id\",\"tid\"]}";
//        String json = "{\"brandName\":[\"德芙\"],\"areas\":[\"北京\"],\"startData\":\"2016-11-30\",\"endData\":\"2017-01-01\",\"fileName\":\"1111\",\"userName\":\"null\",\"fields\":[\"transaction_id\",\"bill_payment_time\",\"buyer_message\",\"city\",\"brand_name\",\"delivery_time\",\"express_no\",\"shop_name\",\"buyer_id\",\"express_company\",\"baby_title\",\"district\",\"province\"]}";
//        String json = "{\"brandName\":[\"士力架\"],\"areas\":[\"全部\"],\"startData\":\"2017-02-01\",\"endData\":\"2017-02-03\",\"fileName\":\"赵test1\",\"userName\":\"null\",\"fields\":[\"全部\"]}";
        eBusinessStatisticsServiceImpl.getEleColunmData(json);
    }

}
