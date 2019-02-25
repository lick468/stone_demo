package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import com.nenu.dao.ProcordDao;
import com.nenu.domain.Procord;


import com.nenu.service.ProcordService;

@Service

public class ProcordServiceImpl implements ProcordService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcordServiceImpl.class);
	@Autowired
	private ProcordDao procordDao;

	@Override
	public List<Procord> findAllProcord() {
		// TODO Auto-generated method stub
		List<Procord> list = new ArrayList<Procord>();
		list = procordDao.findAllProcord();
		LOGGER.info("查询所有订单:>>");
		return list;
	}

	@Override
	public int insertProcord(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("送石出库:>>");
		return procordDao.insertProcord(procord);
	}

	@Override
	public Procord findProcordByProcordNo(long procord_no) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号查询:>>"+procord_no);
		return procordDao.findProcordByProcordNo(procord_no);
	}

	@Override
	public int updateProcord(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("更新订单信息:>>");
		return procordDao.updateProcord(procord);
	}

	@Override
	public int updateProcordState(Procord procord ) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号更新订单状态:>>");
		return procordDao.updateProcordState(procord);
	}

	@Override
	public int updateProcordAccounts(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("退石后修改订单中石数:>>");
		return procordDao.updateProcordAccounts(procord);
	}

	@Override
	public int updateProcordPay(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("退石后修改货款金额:>>");
		return procordDao.updateProcordPay(procord);
	}

	@Override
	public List<Procord> findProcordBySupplier(String procord_supplier) {
		// TODO Auto-generated method stub
		LOGGER.info("根据供应商查询:>>"+procord_supplier);
		return procordDao.findProcordBySupplier(procord_supplier);
	}

	@Override
	public List<Procord> findProcordByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return procordDao.findProcordByTableInfo(params);
	}

}
