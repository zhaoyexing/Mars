package com.st.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.st.mapper.MarsProductPreferencesMapper;
import com.st.model.MarsProductPreferences;
import com.st.model.MarsTags;
import com.st.service.MarsProductPreferencesService;
import com.st.utils.OkHttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhaoyx on 2016/11/2.
 */
@Service
public class MarsProductPreferencesServiceImpl implements MarsProductPreferencesService {
    @Autowired
    private MarsProductPreferencesMapper marsProductPreferencesMapper;

    @Value("${e.business.url}")
    private String urlValue;

    /**
     * 添加产品喜好离线数据
     */
    @Override
    public void addProPreferences() {
        try {
            //查找品牌列表
            String brandClassId = "";
            Map<Integer, MarsTags> brandTagsMap = packageJsonData(brandClassId);

            String productClassId = "";
            //查询产品喜好下产品特征的标签
            String productTagsJson = OkHttpUtils.get(urlValue + "searchTagsByClass&classIds=" + productClassId);
            //转换json数据
            JSONObject jsonObjectProperty = JSONObject.fromObject(productTagsJson);
            String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
            Map<Integer, MarsTags> gsonMap = new Gson().fromJson(tagStr, new TypeToken<Map<Integer, MarsTags>>() {
            }.getType());
            //循环map
            gsonMap.forEach((key, marsTags) -> System.out.println(key));
            MarsProductPreferences marsProductPreferences = new MarsProductPreferences();
            marsProductPreferencesMapper.addProPreferences(marsProductPreferences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装调用过程
     * @param classId
     * @return
     * @throws Exception
     */
    private Map<Integer, MarsTags> packageJsonData(String classId) throws Exception {
        String tagClassDataJson = OkHttpUtils.get(urlValue + "searchTagsByClass&classIds="+classId);
        JSONObject jsonObjectProperty = JSONObject.fromObject(tagClassDataJson);
        String tagStr = String.valueOf(jsonObjectProperty.get("datas"));
        Map<Integer, MarsTags> tagMap = new Gson().fromJson(tagStr, new TypeToken<Map<Integer, MarsTags>>() {}.getType());
        return tagMap;
    }
}
