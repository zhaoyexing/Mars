package com.st.utils;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/**
 * <pre>
 * </pre>
 * BaseAPI
 * Date: 2016/5/26
 * Time: 21:15
 *
 * @author zhaoyexing@social-touch.com
 */
public abstract class BaseAPI {
    protected static final String BASE_URI_PRODUCT = "http://api.data.social-touch.com:8091/cdapi/req";
//    protected static final String BASE_URI_TEST = "http://61.49.48.84:10091/cdapi/req";

    /** Json类型数据Header */
    protected static Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

    protected static final int APPKEY = 172205760;

    protected static final String CLIENTIP = "127.0.0.1";




}
