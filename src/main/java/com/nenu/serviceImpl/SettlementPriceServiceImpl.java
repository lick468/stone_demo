package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.SettlementPriceDao;
import com.nenu.domain.SettlementPrice;
import com.nenu.service.SettlementPriceService;


@Service

public class SettlementPriceServiceImpl implements SettlementPriceService{
	private static final Logger LOGGER = LoggerFactory.getLogger(SettlementPriceServiceImpl.class);
	@Autowired
	private SettlementPriceDao settlementPriceDao;

	@Override
	public int insertSettlementPrice(SettlementPrice settlementPrice) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条结算价区间记录");
		return settlementPriceDao.insertSettlementPrice(settlementPrice);
	}

	@Override
	public int updateSettlementPrice(SettlementPrice settlementPrice) {
		// TODO Auto-generated method stub
		LOGGER.info("更新一条结算价区间记录");
		return settlementPriceDao.updateSettlementPrice(settlementPrice);
	}

	@Override
	public int deleteSettlementPrice(int settlementPrice_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("删除一条主石结算价区间记录》》"+settlementPrice_ID);
		return settlementPriceDao.deleteSettlementPrice(settlementPrice_ID);
	}

	@Override
	public List<SettlementPrice> findAllSettlementPrice() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有结算价区间记录");
		return settlementPriceDao.findAllSettlementPrice();
	}

	@Override
	public SettlementPrice findSettlementPriceById(int settlementPrice_ID) {
		// TODO Auto-generated method stub
		LOGGER.info("根据ID查找一条主石区间记录》》"+settlementPrice_ID);
		return settlementPriceDao.findSettlementPriceById(settlementPrice_ID);
	}

}
