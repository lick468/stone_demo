package com.nenu.service;

import java.util.List;

import com.nenu.domain.Supplier;

public interface SupplierService {
	int insertSupplier(Supplier supplier);

	int updateSupplier(Supplier supplier);

	int deleteSupplier(int supplier_ID);

	List<Supplier> findAllSupplier();

	Supplier findSupplierById(int supplier_ID);

}
