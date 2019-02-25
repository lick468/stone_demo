package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.ListPriceDao;
import com.nenu.domain.ListPrice;
import com.nenu.service.ListPriceService;

@Service

public class ListPriceServiceImpl implements ListPriceService{
	private static final Logger LOGGER = LoggerFactory.getLogger(ListPriceServiceImpl.class);
	@Autowired
	private ListPriceDao listPriceDao;

	@Override
	public int insertListPrice(ListPrice listPrice) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条标价区间记录");
		return listPriceDao.insertListPrice(listPrice);
	}

	@Override
	public int updateListPrice(ListPrice listPrice) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条标价区间记录");
		return listPriceDao.updateListPrice(listPrice);
	}

	@Override
	public int deleteListPrice(int listPrice_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("删除一条标价区间记录》》"+listPrice_ID);
		return listPriceDao.deleteListPrice(listPrice_ID);
	}

	@Override
	public List<ListPrice> findAllListPrice() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有标价区间记录");
		return listPriceDao.findAllListPrice();
	}

	@Override
	public ListPrice findListPriceById(int listPrice_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("根据ID查找一条标价区间记录》》"+listPrice_ID);
		return listPriceDao.findListPriceById(listPrice_ID);
	}

}
