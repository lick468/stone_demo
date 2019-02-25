package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Belong;

public interface BelongDao {
	int insertBelong(Belong belong);

	int updateBelong(Belong belong);

	int deleteBelong(int Belong_ID);

	List<Belong> findAllBelong();
	List<Belong> findAllBelongByBelongName(String belong_name);
	List<Belong> findAllBelongAndCounter();

	Belong findBelongById(int Belong_ID);

}
