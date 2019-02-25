package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.QualityDao;

import com.nenu.domain.Quality;

import com.nenu.service.QualityService;
@Service

public class QualityServiceImpl implements QualityService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityServiceImpl.class);
	@Autowired
	private QualityDao qualityDao;

	@Override
	public int insertQuality(Quality quality) {
		LOGGER.info("插入供应商：" + quality);
		return qualityDao.insertQuality(quality);
	}

	@Override
	public int updateQuality(Quality quality) {
		LOGGER.info("更新供应商信息：" + quality);
		return qualityDao.updateQuality(quality);
	}

	@Override
	public int deleteQuality(int quality_ID) {
		LOGGER.info("删除供应商信息（ID）：" + quality_ID);
		return qualityDao.deleteQuality(quality_ID);
	}

	@Override
	public List<Quality> findAllQuality() {
		List<Quality> list = new ArrayList<Quality>();
		list = qualityDao.findAllQuality();
		LOGGER.info("查询所有供应商信息：" + list.size());
		return list;
	}

	@Override
	public Quality findQualityById(int quality_ID) {
		LOGGER.info("根据ID查询供应商信息：" + quality_ID);
		return qualityDao.findQualityById(quality_ID);
	}

}
