package com.st.mapper;

import com.st.model.SmsAgain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhaoyx on 2016/9/7.
 */
public interface SmsAgainMapper {
    List<SmsAgain> getAllByDate(@Param("date")String date);
}
