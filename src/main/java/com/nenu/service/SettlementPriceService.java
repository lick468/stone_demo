package com.nenu.service;

import java.util.List;

import com.nenu.domain.SettlementPrice;

public interface SettlementPriceService {
	int insertSettlementPrice(SettlementPrice settlementPrice);

	int updateSettlementPrice(SettlementPrice settlementPrice);

	int deleteSettlementPrice(int settlementPrice_ID);

	List<SettlementPrice> findAllSettlementPrice();

	SettlementPrice findSettlementPriceById(int settlementPrice_ID);
}
