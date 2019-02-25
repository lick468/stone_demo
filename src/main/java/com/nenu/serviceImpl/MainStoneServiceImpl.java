package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.MainStoneDao;
import com.nenu.domain.MainStone;
import com.nenu.service.MainStoneService;

@Service

public class MainStoneServiceImpl implements MainStoneService{
	private static final Logger LOGGER = LoggerFactory.getLogger(MainStoneServiceImpl.class);
	@Autowired
	private MainStoneDao mainStoneDao;

	@Override
	public int insertMainStone(MainStone MainStone) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条主石区间记录");
		return mainStoneDao.insertMainStone(MainStone);
	}

	@Override
	public int updateMainStone(MainStone MainStone) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条主石区间记录");
		return mainStoneDao.updateMainStone(MainStone);
	}

	@Override
	public int deleteMainStone(int mainStnoe_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("删除一条主石区间记录》》"+mainStnoe_ID);
		return mainStoneDao.deleteMainStone(mainStnoe_ID);
	}

	@Override
	public List<MainStone> findAllMainStone() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有主石区间记录");
		return mainStoneDao.findAllMainStone();
	}

	@Override
	public MainStone findMainStoneById(int mainStnoe_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("根据ID查找一条主石区间记录》》"+mainStnoe_ID);
		return mainStoneDao.findMainStoneById(mainStnoe_ID);
	}

}
