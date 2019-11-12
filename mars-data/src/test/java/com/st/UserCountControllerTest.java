package com.st;

import org.junit.Test;

/**
 * Created by zhaoyx on 2016/12/7.
 */
public class UserCountControllerTest extends BaseTest {

    @Test
    public void testGetUserCount(){
        String uri = "/UserCount/getUserCount";
        assertMethod(uri);
    }

}
