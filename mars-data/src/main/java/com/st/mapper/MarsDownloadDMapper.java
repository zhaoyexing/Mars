package com.st.mapper;

import com.st.model.MarsDownloadD;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/14.
 */
public interface MarsDownloadDMapper {
    /**
     * 增加记录
     * @param marsDownloadD
     */
    void addMarsDownload(MarsDownloadD marsDownloadD);

    /**
     * 根据id删除记录
     * @param id
     */
    void deleteMarsDownload(int id);

    /**
     * 分页查找下载地址列表
     * @param pageNo
     * @return
     */
    List<MarsDownloadD> findMarsDownload(@Param("pageNo")String pageNo);

    /**
     * 更新最新添加的记录
     * @param marsDownloadD1
     */
    void updateMarsDownload(MarsDownloadD marsDownloadD1);

    /**
     * 根据文件名查找是否存在改文件
     * @param fileName
     * @return
     */
    int selectCountByFileName(String fileName);

    /**
     * 查询电商数据下载列表
     * @return
     */
    List<MarsDownloadD> getDownloadDList(Map<String,Object> map);

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    void deleteFile(String fileName);

    /**
     * 查询数据总数
     * @return
     */
    int getDownloadCount();

    /**
     * 根据文件名查询这条记录的json数据
     * @param fileName
     * @return
     */
    MarsDownloadD selectJsonData(String fileName);
}
