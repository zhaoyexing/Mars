package com.st;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by zhaoyx on 2016/12/7.
 */
public class UserAnalysisControllerTest extends BaseTest {

    @Test
    public void testGetFanAreaDistribution(){
        String uri = "/userAnalysis/area";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }

    @Test
    public void testGetSexDistribution(){
        String uri = "/userAnalysis/sex";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }
}
