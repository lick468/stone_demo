package com.nenu.serviceImpl;

import com.nenu.dao.GoldWeightDao;
import com.nenu.domain.GoldWeight;
import com.nenu.service.GoldWeightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoldWeightServiceImpl implements GoldWeightService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoldWeightServiceImpl.class);

    @Autowired
    private GoldWeightDao goldWeightDao;
    @Override
    public int insertGoldWeight(GoldWeight goldWeight) {
        LOGGER.info("插入一条金重区间记录");
        return goldWeightDao.insertGoldWeight(goldWeight);
    }

    @Override
    public int updateGoldWeight(GoldWeight goldWeight) {
        LOGGER.info("更新一条金重区间记录");
        return goldWeightDao.updateGoldWeight(goldWeight);
    }

    @Override
    public int deleteGoldWeight(int goldWeight_ID) {
        LOGGER.info("删除一条金重区间记录");
        return goldWeightDao.deleteGoldWeight(goldWeight_ID);
    }

    @Override
    public List<GoldWeight> findAllGoldWeight() {
        LOGGER.info("获取所有金重区间记录");
        return goldWeightDao.findAllGoldWeight();
    }

    @Override
    public GoldWeight findGoldWeightById(int goldWeight_ID) {
        LOGGER.info("根据ID查找一条金重区间记录》》"+goldWeight_ID);
        return goldWeightDao.findGoldWeightById(goldWeight_ID);
    }
}
