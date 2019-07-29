package com.nenu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Finpord;
import com.nenu.domain.FinpordCopy;




public interface FinpordService {
	List<Finpord> findAllFinpord();
	List<FinpordCopy> findAllFinpordCopy();
	List<FinpordCopy> finprodexcel2sql(String fileName, MultipartFile mfile, int level);
	int insertFinpordByExcel(FinpordCopy finpordCopy);
	int batchInsertFinpordCopy(List<FinpordCopy> list);//批量插入
	int batchInsertFinpord(List<Finpord> list);//批量插入
	int downloadfinprod2sql(List<Finpord> list, int level,HttpServletResponse response);
	Finpord findFinpordByMainNo(String mainNo);
	Finpord findLastFinpord();
	FinpordCopy findLastFinpordCopy();
	Finpord findfinpordByfinpordIDAJAX(String finprod_ID);
	FinpordCopy findfinpordCopyByfinpordIDAJAX(String finprod_ID);
	int updateFinpord(Finpord finpord);
	int updateFinpordCopy(FinpordCopy finpordCopy);
	List<Finpord> findFinpordByInboundate(Map<String, Object> map);
	List<Finpord> findFinpordByProcordNo(long   finpord_procordNo);
	List<Finpord> findFinpordByBarcodeNo(String finpord_barcodeNo);
	int copyToFinpord();
	int clearCopy();
	int downloadprofit(String[][] dataList,HttpServletResponse response);
	int deleteFinpordCopy(String finprod_ID);
	int deleteAllData();
	List<Finpord> findFinpordByTableInfo(Map<String, Object> params);
	List<Finpord> findFinpordForTableInfo(Map<String, Object> params);
	List<FinpordCopy> findFinpordCopyByTableInfo(Map<String, Object> params);

	List<Finpord> findDistinctBarcode();
	List<Finpord> findDistinctInBoundate();
	List<Finpord> findDistinctProcordNo();

}
