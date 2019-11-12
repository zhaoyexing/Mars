package com.st.service;

import com.st.model.OpResult;
import com.st.model.UserModel;
import com.st.vo.UtilVo;

import java.util.List;
import java.util.Map;

/**
 * 产品喜好
 * Created by zhaoyx on 2016/9/12.
 */
public interface ProductPreferencesService {
    /**
     * 获取品牌特征
     * @param propertyValue 属性值--参考生命周期
     * @param pid 父id
     * @return
     */
    OpResult<List<Map<String, Object>>> getBrandCharacteristics(String propertyValue, String pid,String groupId);

    /**
     * 获取产品特征
     * @return
     */
    OpResult<List<Map<String,Object>>> getProductCharacteristics(String propertyValue, String pid, String groupId);

    /**
     * 获取口味特征
     * @return
     */
    OpResult<List<Map<String, Object>>> getFlavorCharacteristics(String propertyValue, String pid,String groupId);

    /**
     * 获取包装特征
     * @return
     */
    OpResult<List<Map<String, Object>>> getPackagingCharacteristics(String propertyValue, String pid,String groupId);

    /**
     * 获取品牌
     * @return
     */
    OpResult<List<Map<String,String>>>  getBrandClass();

}
