package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.BackStoneDao;
import com.nenu.domain.BackStone;
import com.nenu.service.BackStoneService;
@Service

public class BackStoneServiceImpl implements BackStoneService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BackStoneServiceImpl.class);
	@Autowired
	private BackStoneDao backStoneDao;

	@Override
	public int insertBackStone(BackStone backStone) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条退石记录：>>" );
		return backStoneDao.insertBackStone(backStone);
	}

	@Override
	public List<BackStone> findAllBackStone() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有的退石记录：>>" );
		return backStoneDao.findAllBackStone();
	}

	@Override
	public List<BackStone> findBackStoneByProcodeNo(long procodeNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号查找所有的退石记录：>>"+ procodeNo);
		return backStoneDao.findBackStoneByProcodeNo(procodeNo);
	}

	@Override
	public List<BackStone> findBackStoneByStoneNo(String stoneNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据石编查找所有的退石记录：>>"+ stoneNo);
		return backStoneDao.findBackStoneByStoneNo(stoneNo);
	}

}
