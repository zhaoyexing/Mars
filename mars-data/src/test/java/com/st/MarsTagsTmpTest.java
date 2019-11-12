package com.st;

import com.st.service.SearchTagClassService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/2/28.
 */
public class MarsTagsTmpTest extends BaseTest {
    @Autowired
    private SearchTagClassService searchTagClassServiceImpl;

    @Test
    public void saveTagsTempTest(){
        searchTagClassServiceImpl.saveTagsTemp();
    }
}
