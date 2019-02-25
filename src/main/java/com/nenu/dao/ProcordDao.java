package com.nenu.dao;

import java.util.List;
import java.util.Map;

import com.nenu.domain.Procord;



public interface ProcordDao {
	List<Procord> findAllProcord();//查询所有的订单
	int insertProcord(Procord procord);//生成订单
	Procord findProcordByProcordNo(long procord_no);//根据订单号查询
	List<Procord> findProcordBySupplier(String procord_supplier);//根据供应商查询
	int updateProcord(Procord procord);//更新订单
	int updateProcordAccounts(Procord procord);//退石后修改订单中石数
	int updateProcordPay(Procord procord);//退石后修改货款金额
	int updateProcordState(Procord procord );//根据订单号更新订单状态
	List<Procord> findProcordByTableInfo(Map<String, Object> params);
}
