package com.nenu.dao;

import java.util.List;

import com.nenu.domain.BelongAndCounter;

public interface BelongAndCounterDao {
	int insertBelongAndCounter(BelongAndCounter belongAndCounter);

	int updateBelongAndCounter(BelongAndCounter belongAndCounter);
	int deleteBelongAndCounterByBelongAndCounter(BelongAndCounter belongAndCounter);
	int deleteBelongAndCounterByBelong(BelongAndCounter belongAndCounter);

	

	List<BelongAndCounter> findAllBelongAndCounter();
	List<BelongAndCounter> findAllBelongAndCounterByBelong(String bc_belong_name);
	List<BelongAndCounter> findAllBelongAndCounterByCounter(String bc_counter_name);
	List<BelongAndCounter> findAllBelongAndCounterByBelongAndCounter(BelongAndCounter belongAndCounter);

	BelongAndCounter findBelongAndCounterById(int BelongAndCounter_ID);

}
