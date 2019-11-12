package com.st.service;

import com.st.model.MarsTags;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public interface SearchTagsByClassService {
    List<MarsTags> getTags();
    void delete();
}
