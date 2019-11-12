package com.st.service.impl;

import com.st.mapper.MarsOrderDsMapper;
import com.st.mapper.MarsOrderHistoryMapper;
import com.st.model.BabyBarcode;
import com.st.model.MarsOrderDs;
import com.st.service.MarsOrderHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2017/3/17.
 */
@Service
public class MarsOrderHistoryServiceImpl implements MarsOrderHistoryService {
    private static Logger logger = LoggerFactory.getLogger(MarsOrderHistoryServiceImpl.class);
    @Autowired
    private MarsOrderHistoryMapper marsOrderHistoryMapper;
    @Autowired
    private MarsOrderDsMapper marsOrderDsMapper;

    private AtomicInteger integer = new AtomicInteger(0);
    @Override
    public void updateBarCode() {
        try {
            int count = marsOrderHistoryMapper.getAllCount();
            int pageSize = 1000;
            int pageNo = count/pageSize +1;
            for (int i = 0;i<pageNo;i++) {
                List<String> inter = new ArrayList<>();
                List<BabyBarcode> list = marsOrderHistoryMapper.findMarsOrderDsByPage(i * 1000, pageSize);
                List<MarsOrderDs> orderDs = new ArrayList<>();
                for (BabyBarcode babyBarcode : list) {
                    MarsOrderDs marsOrderDsing = marsOrderHistoryMapper.findMarsOrderDsByInter(babyBarcode.getOrderNo()+"~"+babyBarcode.getBabyTitle());
                    if (marsOrderDsing!=null){
                        MarsOrderDs marsOrderDs = new MarsOrderDs();
                        marsOrderDs.setIntegrationId(babyBarcode.getOrderNo() + "~" + babyBarcode.getBabyTitle());
                        inter.add(babyBarcode.getOrderNo() + "~" + babyBarcode.getBabyTitle());
                        marsOrderDs.setBarCode(babyBarcode.getBarCode());
                        orderDs.add(marsOrderDs);
                    }
                }
                logger.info(String.valueOf(orderDs.size()));
                marsOrderHistoryMapper.addOrderHistory(orderDs);
                List<MarsOrderDs> listing = new ArrayList<>();
                for (String intger : inter){
                    MarsOrderDs marsOrdering = marsOrderHistoryMapper.findMarsOrderDsByInter(intger);
                    listing.add(marsOrdering);
                }
                integer.incrementAndGet();
                logger.info(String.valueOf(integer.get()));
                marsOrderDsMapper.addBatch(listing);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void addMarsOrderDsCount(){
        try {
            int count = marsOrderHistoryMapper.getCounts();
            int pageSize = 10000;
            int pageNo = count/pageSize==0?count/pageSize:count/pageSize+1;
            int size = 3070000;
            for (int i =307;i<pageNo;i++){
                System.out.println("开始时间"+System.currentTimeMillis());
                List<MarsOrderDs> list = marsOrderHistoryMapper.findMarsOrderDs(i*pageSize,pageSize);
                marsOrderDsMapper.addBatch(list);
                size +=pageSize;
                System.out.println("历史表数据已经添加："+size);
                logger.info(String.valueOf(size));
                System.out.println("结束时间"+System.currentTimeMillis());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
