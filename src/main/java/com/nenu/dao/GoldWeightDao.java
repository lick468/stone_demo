package com.nenu.dao;

import com.nenu.domain.GoldWeight;


import java.util.List;

public interface GoldWeightDao {
	int insertGoldWeight(GoldWeight goldWeight);

	int updateGoldWeight(GoldWeight goldWeight);

	int deleteGoldWeight(int goldWeight_ID);

	List<GoldWeight> findAllGoldWeight();

	GoldWeight findGoldWeightById(int goldWeight_ID);
}
