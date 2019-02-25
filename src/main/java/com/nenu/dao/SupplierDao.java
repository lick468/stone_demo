package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Supplier;

public interface SupplierDao {
	int insertSupplier(Supplier supplier);

	int updateSupplier(Supplier supplier);

	int deleteSupplier(int supplier_ID);

	List<Supplier> findAllSupplier();

	Supplier findSupplierById(int supplier_ID);

}
