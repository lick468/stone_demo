package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.BelongDao;

import com.nenu.domain.Belong;
import com.nenu.service.BelongService;

@Service

public class BelongServiceImpl implements BelongService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BelongServiceImpl.class);
	@Autowired
	private BelongDao BelongDao;

	@Override
	public int insertBelong(Belong Belong) {
		LOGGER.info("插入统计对象信息：" + Belong);
		return BelongDao.insertBelong(Belong);
	}

	@Override
	public int updateBelong(Belong Belong) {
		LOGGER.info("更新统计对象信息：" + Belong);
		return BelongDao.updateBelong(Belong);
	}

	@Override
	public int deleteBelong(int Belong_ID) {
		LOGGER.info("删除统计对象信息（ID）：" + Belong_ID);
		return BelongDao.deleteBelong(Belong_ID);
	}

	@Override
	public List<Belong> findAllBelong() {
		List<Belong> list = new ArrayList<Belong>();
		list = BelongDao.findAllBelong();
		LOGGER.info("查询所有统计对象信息：" + list.size());
		return list;
	}

	@Override
	public Belong findBelongById(int belong_ID) {
		LOGGER.info("根据ID查询统计对象信息：" + belong_ID);
		return BelongDao.findBelongById(belong_ID);
	}

	@Override
	public List<Belong> findAllBelongAndCounter() {
		// TODO Auto-generated method stub
		LOGGER.info("查询所有统计对象及包含柜台的信息");
		return BelongDao.findAllBelongAndCounter();
	}

	@Override
	public List<Belong> findAllBelongByBelongName(String belong_name) {
		// TODO Auto-generated method stub
		LOGGER.info("根据对象查询统计对象信息：" + belong_name);
		return BelongDao.findAllBelongByBelongName(belong_name);
	}

}
