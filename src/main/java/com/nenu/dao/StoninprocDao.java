package com.nenu.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.Stoninproc;


public interface StoninprocDao {
	List<Stoninproc> findAllStoninproc();
	List<Stoninproc> findAllStoninprocWithStateEqualZero();
	
	Stoninproc   findLastStoninproc();
	int insertStoninproc(Stoninproc stoninproc);
	Stoninproc findStoninprocByStoneNo(String stoninproc_stoneNo);//根据主石编查找
	List<Stoninproc> findStoninprocBySubStoneNo(String stoninproc_stoneNo);//根据副石编查找
	List<Stoninproc> findAllStoninprocByProcordNo(long stoninproc_procordNo);
	int deleteStoneFromProcord(Map<String, Object> params);
	Stoninproc findStoneNumberFromStoninproc(Map<String, Object> params);//根据订单号和石编查询石数
	int updateStoninprocState(Stoninproc stoninproc);//更改石头状态
	int downloadProcordDetails(String dataList,HttpServletResponse response);//下载订单详情
}
