package com.nenu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.Stoninproc;


public interface StoninprocService {
	List<Stoninproc> findAllStoninproc();
	List<Stoninproc> findAllStoninprocWithStateEqualZero();
	Stoninproc   findLastStoninproc();
	int insertStoninproc(Stoninproc stoninproc);
	Stoninproc findStoninprocByStoneNo(String stoninproc_stoneNo);
	List<Stoninproc> findStoninprocBySubStoneNo(String stoninproc_stoneNo);
	List<Stoninproc> findAllStoninprocByProcordNo(long stoninproc_procordNo);
	int deleteStoneFromProcord(Map<String, Object> params);
	Stoninproc findStoneNumberFromStoninproc(Map<String, Object> params);
	int updateStoninprocState(Stoninproc stoninproc);
	int downloadProcordDetails(String dataList,HttpServletResponse response);//下载订单详情

}
