package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.mapper.MarsTagsMapper;
import com.st.model.MarsTags;
import com.st.service.SearchTagsByClassService;
import com.st.utils.HttpClientFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyx on 2016/9/18.
 */
@Service
public class SearchTagsByClassServiceImpl implements SearchTagsByClassService {
    @Value("${user.draw.url}")
    private String urlValue;
    @Autowired
    private MarsTagsMapper marsTagsMapper;
    @Override
    public List<MarsTags> getTags() {
        List<MarsTags> list = new ArrayList<>();
        try {
            String tagClassDataJson = HttpClientFactory.requestGet(urlValue + "searchTagsByClass");
            JSONObject jsonObjectProperty = JSONObject.fromObject(tagClassDataJson);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            Map<Integer, MarsTags> map = toMap(tagStr);
            map.forEach((key,tagsVal)->{
                MarsTags marsTags = new MarsTags();
                marsTags.setId(key);
                marsTags.setAppkey(tagsVal.getAppkey());
                marsTags.setCalcType(tagsVal.getCalcType());
                marsTags.setClassId(tagsVal.getClassId());
                marsTags.setDefWeight(tagsVal.getDefWeight());
                marsTags.setGeneralPropName(tagsVal.getGeneralPropName());
                marsTags.setGeneralTagType(tagsVal.getGeneralTagType());
                marsTags.setTagDescription(tagsVal.getTagDescription());
                marsTags.setTagName(tagsVal.getTagName());
                marsTags.setCreateTime(new Timestamp(new Date().getTime()));
                marsTags.setUpdateTime(new Timestamp(new Date().getTime()));
                list.add(marsTags);
            });
            marsTagsMapper.saveMarsTagsBatch(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void delete() {
        marsTagsMapper.deleteDate();
    }

    public Map<Integer, MarsTags> toMap(String str) {
        Gson gson = new Gson();
        Map<Integer, MarsTags> gsonMap = gson.fromJson(str, new TypeToken<Map<Integer, MarsTags>>() {
        }.getType());
        return gsonMap;
    }
}
