package com.nenu.service;

import java.util.List;
import java.util.Map;

import com.nenu.domain.Procord;




public interface ProcordService {
	List<Procord> findAllProcord();
	int insertProcord(Procord procord);
	Procord findProcordByProcordNo(long procord_no);
	List<Procord> findProcordBySupplier(String procord_supplier);//根据供应商查询
	int updateProcord(Procord procord);//更新订单
	int updateProcordAccounts(Procord procord);//退石后修改订单中石数
	int updateProcordState(Procord procord );
	int updateProcordPay(Procord procord);
	List<Procord> findProcordByTableInfo(Map<String, Object> params);
}
