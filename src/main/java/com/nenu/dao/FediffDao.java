package com.nenu.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.Fediff;



public interface FediffDao {
	List<Fediff> findAllFediff();
	int insertFediff(Fediff fediff);
	Fediff findFediffByTwoParam(long fediff_procordNo,String fediff_stoneNo);//查看订单和石编是否已经在库里
	int downloadFediff(List<Fediff> list,HttpServletResponse response);
	List<Fediff> findFediffByTime(Date fediff_time);
	List<Fediff> findFediffByTableInfo(Map<String, Object> params);

}
