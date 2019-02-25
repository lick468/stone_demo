package com.nenu.service;

import java.util.List;

import com.nenu.domain.Counter;

public interface CounterService {
	List<Counter> findAllCounter();

	int insertCounter(Counter counter);

	int updateCounter(Counter counter);

	int deleteCounter(int counter_ID);

	Counter findCounterById(int counter_ID);

}
