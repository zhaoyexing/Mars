package com.st;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoyx on 2016/12/6.
 */
public class ActivityAnalysisControllerTest extends BaseTest {

    @Test
    public void testGetInteractiveThemeDistribution() {
        String uri = "/activityAnalysis/theme";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }
    @Test
    public void testGetInteractiveIntentionDistribution() {
        String uri = "/activityAnalysis/intention";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }

    @Test
    public void testGetInteractiveInterestDistribution() {
        String uri = "/activityAnalysis/interest";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }

    @Test
    public void testGetInteractiveFormDistribution() {
        String uri = "/activityAnalysis/form";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("propertyValue","微信会员");
        assertMethodByParams(uri,parameters);
    }

}
