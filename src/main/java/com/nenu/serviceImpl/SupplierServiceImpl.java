package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.SupplierDao;
import com.nenu.domain.Supplier;
import com.nenu.service.SupplierService;
@Service

public class SupplierServiceImpl implements SupplierService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private SupplierDao supplierDao;

	@Override
	public int insertSupplier(Supplier supplier) {
		LOGGER.info("插入供应商：" + supplier);
		return supplierDao.insertSupplier(supplier);
	}

	@Override
	public int updateSupplier(Supplier supplier) {
		LOGGER.info("更新供应商信息：" + supplier);
		return supplierDao.updateSupplier(supplier);
	}

	@Override
	public int deleteSupplier(int supplier_ID) {
		LOGGER.info("删除供应商信息（ID）：" + supplier_ID);
		return supplierDao.deleteSupplier(supplier_ID);
	}

	@Override
	public List<Supplier> findAllSupplier() {
		List<Supplier> list = new ArrayList<Supplier>();
		list = supplierDao.findAllSupplier();
		LOGGER.info("查询所有供应商信息：" + list.size());
		return list;
	}

	@Override
	public Supplier findSupplierById(int supplier_ID) {
		LOGGER.info("根据ID查询供应商信息：" + supplier_ID);
		return supplierDao.findSupplierById(supplier_ID);
	}

}
