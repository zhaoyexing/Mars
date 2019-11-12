package com.st.service;

import com.st.model.MarsTagClass;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/18.
 */
public interface SearchTagClassService {
    List<MarsTagClass> getTagClass();
    void delete();

    void saveTagsTemp();
}
