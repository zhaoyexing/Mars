package com.st.service;

import com.st.model.MarsDownloadD;
import com.st.model.OpResult;
import com.st.vo.BusinessBrandVO;

import java.util.List;
import java.util.Map;

/**
 * 电商数据统计
 * Created by zhaoyx on 2016/9/21.
 */
public interface EBusinessStatisticsService {
    /**
     * 销售量排名
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    OpResult<List<BusinessBrandVO>> getSalesRanking(String startDate, String endDate);

    /**
     * 消费金额汇总
     * @param startDate  开始日期
     * @param endDate   结束日期
     * @return
     */
    OpResult<List<Map<String,Object>>>  getPurchaseAmount(String startDate, String endDate);

    /**
     * 品牌、地区
     * @return
     */
    OpResult getBrandAndArea();

    /**
     * 字段维度
     * @return
     */
    OpResult getFields();

    /**
     * 下载任务接口
     * @return
     */
    OpResult getEleColunmData(String jsonData);

    /**
     * 查询电商数据下载列表
     * @return
     */
    OpResult getDownloadDList(Integer pageNum,Integer pageSize);

    /**
     * 删除文件接口
     * @param fileName
     * @return
     */
    OpResult deleteFile(String fileName);

    /**
     * 根据文件名查询这条记录的json数据
     * @param fileName
     * @return
     */
    MarsDownloadD selectJsonData(String fileName);
}
