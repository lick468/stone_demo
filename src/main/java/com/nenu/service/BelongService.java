package com.nenu.service;

import java.util.List;

import com.nenu.domain.Belong;

public interface BelongService {
	int insertBelong(Belong belong);

	int updateBelong(Belong belong);

	int deleteBelong(int Belong_ID);

	List<Belong> findAllBelong();
	
	List<Belong> findAllBelongByBelongName(String belong_name);
	
	List<Belong> findAllBelongAndCounter();

	Belong findBelongById(int Belong_ID);
}
