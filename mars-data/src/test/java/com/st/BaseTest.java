package com.st;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by zhaoyx on 2016/9/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {
    static MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 不带参数请求
     * @param uri
     */
    public static void assertMethod(String uri) {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc
                    .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int status = mvcResult.getResponse().getStatus();
        assert status == 200;
    }

    /**
     * 带参数请求
     * @param uri
     * @param parameters
     */
    public static void assertMethodByParams(String uri, MultiValueMap<String, String> parameters) {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc
                    .perform(MockMvcRequestBuilders.get(uri).params(parameters).accept(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }
        int status = mvcResult.getResponse().getStatus();
        assert status == 200;
    }

}
