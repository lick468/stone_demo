package com.nenu.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.Fediff;



public interface FediffService {
	List<Fediff> findAllFediff();
	int insertFediff(Fediff fediff);
	Fediff findFediffByTwoParam(long fediff_procordNo,String fediff_stoneNo);
	int downloadFediff(List<Fediff> list,HttpServletResponse response);
	List<Fediff> findFediffByTime(Date fediff_time);
	List<Fediff> findFediffByTableInfo(Map<String, Object> params);

	List<Fediff> findDistinctFediffTime();

}
