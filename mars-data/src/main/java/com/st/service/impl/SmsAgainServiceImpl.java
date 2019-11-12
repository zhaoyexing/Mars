package com.st.service.impl;

import com.st.mapper.SmsAgainMapper;
import com.st.model.SmsAgain;
import com.st.service.SmsAgainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/9.
 */
@Service
public class SmsAgainServiceImpl implements SmsAgainService {
    @Autowired
    private SmsAgainMapper smsAgainMapper;

    @Override
    public List<SmsAgain> getMobilePhone() {
        return smsAgainMapper.getAllByDate("2016-09-08");
    }
}
