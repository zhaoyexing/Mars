package com.st.service.impl;

import com.st.mapper.MarsMemberInfoMapper;
import com.st.service.SyncMemberAndOrderService;
import com.st.task.SyncMatchOrderDataTask;
import com.st.task.SyncMemberInfoTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/11/21.
 */
@Service
public class SyncMemberAndOrderServiceImpl implements SyncMemberAndOrderService{

    private static Logger logger = LoggerFactory.getLogger(SyncMemberAndOrderServiceImpl.class);
    @Autowired
    private MarsMemberInfoMapper marsMemberInfoMapper;

    @Scheduled(cron = "0 0 3 * * ?")
    @Override
    public void syncMemberAndOrderData() {
        logger.info("=====================================推送会员和订单定时任务执行===========================");
          /* pushMemberSerive.schedSyncMemberInfo();
        return  generateData(200);*/
        //步骤1.1 获取未推送的会员信息
        List<Map<String,Object>> memberDataList = marsMemberInfoMapper.getPushMemberInfoList();
        //步骤1.2 获取匹配好的订单列表
        List<Map<String,Object>>  matchOrderDataList = marsMemberInfoMapper.getMatchOrderList();
        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            //步骤2 同步会员信息和订单信息
            //2.1同步会员任务
            SyncMemberInfoTask memberTask = new SyncMemberInfoTask(memberDataList,0,memberDataList.size());
            //提交同步会员任务
            Future<List<Map<String,Object>>> memberResult = forkJoinPool.submit(memberTask);
            List<Map<String,Object>> successMemberList = memberResult.get();
            logger.info("=====================================同步会员数据执行完成===========================");

            //2.2同步订单任务
            SyncMatchOrderDataTask orderTask = new SyncMatchOrderDataTask(matchOrderDataList,0,matchOrderDataList.size());
            //提交同步订单任务
            Future<List<Map<String,Object>>> orderResult = forkJoinPool.submit(orderTask);
            List<Map<String,Object>> successOrderList = orderResult.get();

             logger.info("总共同步会员数据："+memberDataList.size()+"成功推送："+successMemberList.size());
            //3.更新会员信息和订单的更新状态
            if(successMemberList.size()>0){
                marsMemberInfoMapper.updateMemberPushFlag(successMemberList);
            }
            logger.info("总共同步订单数据："+matchOrderDataList.size()+"成功推送："+successOrderList.size());
            if(successOrderList.size()>0){
                marsMemberInfoMapper.updateOrderPushFlag(successOrderList);
            }
            logger.info("=====================================同步订单数据完成===========================");

        }catch (Exception e){

            e.printStackTrace();
        }

    }


}
