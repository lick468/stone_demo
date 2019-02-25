package com.nenu.service;

import java.util.List;

import com.nenu.domain.ListPrice;

public interface ListPriceService {
	int insertListPrice(ListPrice listPrice);

	int updateListPrice(ListPrice listPrice);

	int deleteListPrice(int listPrice_ID);

	List<ListPrice> findAllListPrice();

	ListPrice findListPriceById(int listPrice_ID);
}
