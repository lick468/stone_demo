package com.nenu.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nenu.dao.SupplierStoneDao;
import com.nenu.domain.SupplierStone;
import com.nenu.service.SupplierStoneService;
@Service
public class SupplierStoneServiceImpl implements SupplierStoneService {
	@Autowired
	private SupplierStoneDao supplierStoneDao;

	@Override
	public List<SupplierStone> findAllSupplierStone() {
		// TODO Auto-generated method stub
		return supplierStoneDao.findAllSupplierStone();
	}

	@Override
	public void insertSupplierStone(SupplierStone supplierStone) {
		// TODO Auto-generated method stub
		supplierStoneDao.insertSupplierStone(supplierStone);
		
	}

	@Override
	public void deleteSupplierStoneByMainNo(String mainNo) {
		// TODO Auto-generated method stub
		supplierStoneDao.deleteSupplierStoneByMainNo(mainNo);
		
	}

	@Override
	public void deleteSupplierStoneBySubNo(String subNo) {
		// TODO Auto-generated method stub
		supplierStoneDao.deleteSupplierStoneBySubNo(subNo);
		
	}

	@Override
	public List<SupplierStone> findSupplierStoneByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return supplierStoneDao.findSupplierStoneByTableInfo(params);
	}

	@Override
	public List<SupplierStone> findSupplierStoneForTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return supplierStoneDao.findSupplierStoneForTableInfo(params);
	}

	@Override
	public List<SupplierStone> findAllSupplierStoneWithSupplierName(String supplier_name) {
		// TODO Auto-generated method stub
		return supplierStoneDao.findAllSupplierStoneWithSupplierName(supplier_name);
	}

	@Override
	public List<SupplierStone> findSupplierStoneByTableInfoWithSupplierName(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return supplierStoneDao.findSupplierStoneByTableInfoWithSupplierName(params);
	}

	@Override
	public List<SupplierStone> findSupplierStoneCountWithSupplierName() {
		// TODO Auto-generated method stub
		return supplierStoneDao.findSupplierStoneCountWithSupplierName();
	}

	@Override
	public SupplierStone getSupplierStoneByID(int id) {
		// TODO Auto-generated method stub
		return supplierStoneDao.getSupplierStoneByID(id);
	}

}
