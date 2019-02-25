package com.nenu.service;

import java.util.List;

import com.nenu.domain.BackStone;

public interface BackStoneService {
	int insertBackStone(BackStone backStone);//插入一条退石记录
	List<BackStone> findAllBackStone();//获取所有退石记录
	List<BackStone> findBackStoneByProcodeNo(long procodeNo);//根据订单号获取退石记录
	List<BackStone> findBackStoneByStoneNo(String stoneNo);
}
