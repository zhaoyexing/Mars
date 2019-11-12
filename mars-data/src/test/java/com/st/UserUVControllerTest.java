package com.st;

import org.junit.Test;

/**
 * Created by zhaoyx on 2016/12/7.
 */
public class UserUVControllerTest extends BaseTest {
    @Test
    public void testGetUserUV() {
        String uri = "/useruv";
        assertMethod(uri);
    }
}
