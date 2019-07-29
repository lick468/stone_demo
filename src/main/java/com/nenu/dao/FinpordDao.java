package com.nenu.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.Finpord;


public interface FinpordDao {
	List<Finpord> findAllFinpord();
	int downloadfinprod2sql(List<Finpord> list, int level,HttpServletResponse response);
	Finpord findFinpordByMainNo(String mainNo);//根据主石编查找
	Finpord findLastFinpord();
	Finpord findfinpordByfinpordIDAJAX(String finprod_ID);
	int batchInsertFinpord(List<Finpord> list);//批量插入
	int updateFinpord(Finpord finpord);
	List<Finpord> findFinpordByBarcodeNo(String finpord_barcodeNo);//根据条码查询
	List<Finpord> findFinpordByInboundate(Map<String, Object> map);//根据入库日期查询
	List<Finpord> findFinpordByProcordNo(long   finpord_procordNo);//根据订单号查询成品
	
	int downloadprofit(String[][] dataList,HttpServletResponse response);//下载利润
	List<Finpord> findFinpordByTableInfo(Map<String, Object> params);
	List<Finpord> findFinpordForTableInfo(Map<String, Object> params);

    List<Finpord> findDistinctBarcode();
    List<Finpord> findDistinctInBoundate();
    List<Finpord> findDistinctProcordNo();



}
