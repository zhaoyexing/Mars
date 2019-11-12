package com.st.service.impl;

import com.st.mapper.MarsTagClassMapper;
import com.st.mapper.MarsTagsMapper;
import com.st.mapper.MarsTagsTmpMapper;
import com.st.model.MarsTagClass;
import com.st.model.MarsTagsTmp;
import com.st.service.SearchTagClassService;
import com.st.utils.HttpClientFactory;
import com.st.utils.JSONUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaoyx on 2016/9/18.
 */
@Service
public class SearchTagClassServiceImpl implements SearchTagClassService {

    @Value("${user.draw.url}")
    private String urlValue;
    @Autowired
    private MarsTagClassMapper marsTagClassMapper;
    @Autowired
    private MarsTagsTmpMapper marsTagsTmpMapper;
    @Autowired
    private MarsTagsMapper marsTagsMapper;
    @Override
    public List<MarsTagClass> getTagClass() {
        String tagStr =null;
        try {
            String tagClassDataJson = HttpClientFactory.requestGet(urlValue + "searchTagClass");
            JSONObject jsonObjectProperty = JSONObject.fromObject(tagClassDataJson);
            tagStr = String.valueOf(jsonObjectProperty.get("datas"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MarsTagClass> list = JSONUtil.converBeanFormString(tagStr,MarsTagClass.class);
        list.forEach(marsTagClass -> {
            marsTagClass.setCreateTime(new Timestamp(new Date().getTime()));
            marsTagClass.setUpdateTime(new Timestamp(new Date().getTime()));
        });
        marsTagClassMapper.saveMarsTagClassBatch(list);
        return list;
    }

    @Override
    public void delete() {
        marsTagClassMapper.delete();
    }

    /**
     * 向mars_tags_temp表中添加数据
     */
    @Override
    public void saveTagsTemp() {
        List<MarsTagsTmp> list = marsTagsMapper.getMarsTagsTmpList();

        marsTagsTmpMapper.saveMarsTagsTemp(list);
    }


}
