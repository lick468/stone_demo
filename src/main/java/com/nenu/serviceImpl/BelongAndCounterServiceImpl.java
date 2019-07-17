package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nenu.dao.BelongAndCounterDao;

import com.nenu.domain.BelongAndCounter;
import com.nenu.service.BelongAndCounterService;

@Service
public class BelongAndCounterServiceImpl implements BelongAndCounterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BelongAndCounterServiceImpl.class);
	@Autowired
	private BelongAndCounterDao BelongAndCounterDao;

	@Override
	public int insertBelongAndCounter(BelongAndCounter BelongAndCounter) {
		LOGGER.info("插入统计对象和柜台信息：" + BelongAndCounter);
		return BelongAndCounterDao.insertBelongAndCounter(BelongAndCounter);
	}

	@Override
	public int updateBelongAndCounter(BelongAndCounter BelongAndCounter) {
		LOGGER.info("更新统计对象和柜台信息：" + BelongAndCounter);
		return BelongAndCounterDao.updateBelongAndCounter(BelongAndCounter);
	}


	@Override
	public List<BelongAndCounter> findAllBelongAndCounter() {
		List<BelongAndCounter> list = new ArrayList<BelongAndCounter>();
		list = BelongAndCounterDao.findAllBelongAndCounter();
		LOGGER.info("查询所有统计对象和柜台信息：" + list.size());
		return list;
	}

	@Override
	public BelongAndCounter findBelongAndCounterById(int BelongAndCounter_ID) {
		LOGGER.info("根据ID查询统计对象和柜台信息：" + BelongAndCounter_ID);
		return BelongAndCounterDao.findBelongAndCounterById(BelongAndCounter_ID);
	}

	@Override
	public List<BelongAndCounter> findAllBelongAndCounterByBelong(String bc_belong_name) {
		// TODO Auto-generated method stub
		LOGGER.info("根据统计对象查询统计对象和柜台信息：" + bc_belong_name);
		return BelongAndCounterDao.findAllBelongAndCounterByBelong(bc_belong_name);
	}

	@Override
	public List<BelongAndCounter> findAllBelongAndCounterByCounter(String bc_counter_name) {
		// TODO Auto-generated method stub
		LOGGER.info("根据柜台查询统计对象和柜台信息：" + bc_counter_name);
		return BelongAndCounterDao.findAllBelongAndCounterByCounter(bc_counter_name);
	}

	@Override
	public int deleteBelongAndCounterByBelongAndCounter(BelongAndCounter belongAndCounter) {
		// TODO Auto-generated method stub
		LOGGER.info("删除统计对象和柜台信息（统计对象和柜台）：" + belongAndCounter);
		return BelongAndCounterDao.deleteBelongAndCounterByBelongAndCounter(belongAndCounter);
	}

	@Override
	public int deleteBelongAndCounterByBelong(BelongAndCounter belongAndCounter) {
		// TODO Auto-generated method stub
		LOGGER.info("删除统计对象和柜台信息（统计对象）：" + belongAndCounter);
		return BelongAndCounterDao.deleteBelongAndCounterByBelong(belongAndCounter);
	}

	@Override
	public List<BelongAndCounter> findAllBelongAndCounterByBelongAndCounter(BelongAndCounter belongAndCounter) {
		// TODO Auto-generated method stub
		LOGGER.info("根据统计对象和柜台查询统计对象和柜台信息：" + belongAndCounter);
		return BelongAndCounterDao.findAllBelongAndCounterByBelongAndCounter(belongAndCounter);
	}

}
