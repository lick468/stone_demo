package com.nenu.dao;

import java.util.List;


import com.nenu.domain.MainStone;

public interface MainStoneDao {
	int insertMainStone(MainStone MainStone);

	int updateMainStone(MainStone MainStone);

	int deleteMainStone(int mainStnoe_ID);

	List<MainStone> findAllMainStone();

	MainStone findMainStoneById(int mainStnoe_ID);
}
