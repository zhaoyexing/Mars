package com.st.utils;

import com.st.enums.ExptypeEnum;
import com.st.model.MarsOrder;
import com.st.model.MarsOrderDs;
import com.st.vo.ExpandInfo;
import com.st.vo.PushLogVO;
import com.st.vo.ReturnData;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/14.
 */
public class SendDataDsToOLAP extends BaseAPI {
    private static Logger logger = LoggerFactory.getLogger(SendDataDsToOLAP.class);

    /**
     * 发送数据转换为json字符串
     *
     * @param
     * @return
     */
    public static String setSendData(final MarsOrderDs marsOrderDs) {
        List<ExpandInfo> expandInfos = new ArrayList<>();
        //省
        ExpandInfo expandInfoProvince = new ExpandInfo();
        expandInfoProvince.setExpkey(89);
        expandInfoProvince.setExpvalue(marsOrderDs.getProvince());
        expandInfoProvince.setExptype(ExptypeEnum.EXP_TYPE_STAT.getExpTypeCode());
        //收货人姓名
        ExpandInfo expandInfoName = new ExpandInfo();
        expandInfoName.setExpkey(94);
        expandInfoName.setExpvalue(marsOrderDs.getReceiverName());
        expandInfoName.setExptype(ExptypeEnum.EXP_TYPE_STAT.getExpTypeCode());

        ExpandInfo expandInfoNames = new ExpandInfo();
        expandInfoNames.setExpkey(11);
        expandInfoNames.setExpvalue(marsOrderDs.getReceiverName());
        expandInfoNames.setExptype(ExptypeEnum.EXP_TYPE_STAT.getExpTypeCode());
        //买家ID
        ExpandInfo expandInfoID = new ExpandInfo();
        expandInfoID.setExpkey(110);
        expandInfoID.setExpvalue(marsOrderDs.getBuyerId());
        expandInfoID.setExptype(ExptypeEnum.EXP_TYPE_STAT.getExpTypeCode());
        //买家ID
        ExpandInfo expandInfoSendNum = new ExpandInfo();
        expandInfoSendNum.setExpkey(111);
        expandInfoSendNum.setExpvalue(String.valueOf(marsOrderDs.getSendNum()));
        expandInfoSendNum.setExptype(ExptypeEnum.EXP_TYPE_STAT.getExpTypeCode());
        expandInfos.add(expandInfoProvince);
        expandInfos.add(expandInfoName);
        expandInfos.add(expandInfoID);
        expandInfos.add(expandInfoSendNum);
        PushLogVO pushLogVO = new PushLogVO();
        //手机号
        pushLogVO.setUid(marsOrderDs.getMobilePhone());
        long millionSeconds = marsOrderDs.getBillPaymentTime().getTime();
        //付款时间
        pushLogVO.setActiontime(new Long(millionSeconds / 1000).intValue());
        //购买
        pushLogVO.setActionid(1);
        pushLogVO.setItemid(marsOrderDs.getBarCode());
        pushLogVO.setItemtype(0);
        pushLogVO.setAppkey(APPKEY);
        pushLogVO.setClientip(CLIENTIP);
        pushLogVO.setExpand_info(expandInfos);
        pushLogVO.setUtype(5);
        String returnJson = null;
        try {
            returnJson =  JsonMapper.obj2json(pushLogVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnJson;
    }


    /**
     * httpclient发送数据
     *
     * @param menuJsonStr
     * @throws IOException
     */
    public static boolean sendData(String menuJsonStr) {
        HttpPost httppost = HttpClientUtils.getHttpPost();
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appid", "35"));
            params.add(new BasicNameValuePair("passwd", "8344202725"));
            params.add(new BasicNameValuePair("method", "pushLogToServer"));
            params.add(new BasicNameValuePair("log", menuJsonStr));
            logger.info("send data:"+menuJsonStr);
            CloseableHttpClient httpclient = HttpClientUtils.getHttpclient();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity, "utf-8");
            ReturnData returnData = (ReturnData) JSONObject.toBean(JSONObject.fromObject(jsonStr), ReturnData.class);
            if (returnData.getDatas().isResult()){
                logger.info(jsonStr);
            }else {
                logger.error("send data:"+menuJsonStr);
                logger.error(jsonStr);
            }
            return returnData.getDatas().isResult();
        } catch (Exception e) {
            logger.error("[sendData] error"+e);
            return false;
        } finally {
            httppost.releaseConnection();
        }

    }

}
