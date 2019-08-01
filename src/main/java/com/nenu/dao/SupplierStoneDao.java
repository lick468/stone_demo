package com.nenu.dao;

import java.util.List;
import java.util.Map;

import com.nenu.domain.SupplierStone;

public interface SupplierStoneDao {
	List<SupplierStone> findAllSupplierStone();
	List<SupplierStone> findSupplierStoneCountWithSupplierName();
	
	List<SupplierStone> findAllSupplierStoneWithSupplierName(String supplier_name);
	List<SupplierStone> findSupplierStoneByTableInfo(Map<String, Object> params);
	List<SupplierStone> findSupplierStoneForTableInfo(Map<String, Object> params);
	List<SupplierStone> findSupplierStoneByTableInfoWithSupplierName(Map<String, Object> params);
	
	SupplierStone getSupplierStoneByID(int id);
	void insertSupplierStone(SupplierStone supplierStone);
	
	void deleteSupplierStoneByMainNo(String mainNo);
	void deleteSupplierStoneBySubNo(String subNo);

	List<SupplierStone> findMainStoneInfo(Map<String, Object> params);
	List<SupplierStone> findSubStoneInfo(Map<String, Object> params);

}
