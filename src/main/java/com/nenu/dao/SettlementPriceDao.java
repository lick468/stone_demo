package com.nenu.dao;

import java.util.List;


import com.nenu.domain.SettlementPrice;

public interface SettlementPriceDao {
	int insertSettlementPrice(SettlementPrice settlementPrice);//插入一条记录

	int updateSettlementPrice(SettlementPrice settlementPrice);//更新一条记录

	int deleteSettlementPrice(int settlementPrice_ID);//删除一条记录

	List<SettlementPrice> findAllSettlementPrice();//查找所有记录

	SettlementPrice findSettlementPriceById(int settlementPrice_ID);//根据ID查找一条记录
}
