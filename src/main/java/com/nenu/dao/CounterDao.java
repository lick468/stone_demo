package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Counter;

public interface CounterDao {
	int insertCounter(Counter counter);

	int updateCounter(Counter counter);

	int deleteCounter(int counter_ID);

	List<Counter> findAllCounter();

	Counter findCounterById(int counter_ID);

}
