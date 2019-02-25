package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.CounterDao;

import com.nenu.domain.Counter;
import com.nenu.service.CounterService;
@Service

public class CounterServiceImpl implements CounterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterServiceImpl.class);
	@Autowired
	private CounterDao counterDao;

	@Override
	public int insertCounter(Counter counter) {
		LOGGER.info("插入柜台信息：" + counter);
		return counterDao.insertCounter(counter);
	}

	@Override
	public int updateCounter(Counter counter) {
		LOGGER.info("更新柜台信息：" + counter);
		return counterDao.updateCounter(counter);
	}

	@Override
	public int deleteCounter(int counter_ID) {
		LOGGER.info("删除柜台信息（ID）：" + counter_ID);
		return counterDao.deleteCounter(counter_ID);
	}

	@Override
	public List<Counter> findAllCounter() {
		List<Counter> list = new ArrayList<Counter>();
		list = counterDao.findAllCounter();
		LOGGER.info("查询所有柜台信息：" + list.size());
		return list;
	}

	@Override
	public Counter findCounterById(int counter_ID) {
		LOGGER.info("根据ID查询柜台信息：" + counter_ID);
		return counterDao.findCounterById(counter_ID);
	}

}
