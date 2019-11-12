package com.st.service;

import com.st.model.OpResult;

/**
 * Created by admin on 2017/2/10.
 */
public interface WeChatAndElectricityUserNumService {
    OpResult getWeChatUserNum();

    OpResult getElectricityUserNum();

    OpResult getUserCount();
}
