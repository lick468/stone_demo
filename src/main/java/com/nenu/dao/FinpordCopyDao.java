package com.nenu.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;


import com.nenu.domain.FinpordCopy;


public interface FinpordCopyDao {
	List<FinpordCopy> findAllFinpordCopy();
	List<FinpordCopy> finprodexcel2sql(String fileName, MultipartFile mfile, int level);
	int insertFinpordByExcel(FinpordCopy finpordCopy);
	int batchInsertFinpordCopy(List<FinpordCopy> list);//批量插入
	int downloadfinprod2sql(List<FinpordCopy> list, int level);
	FinpordCopy findFinpordByMainNo(String mainNo);
	FinpordCopy findLastFinpordCopy();
	FinpordCopy findfinpordCopyByfinpordIDAJAX(String finprod_ID);
	int updateFinpordCopy(FinpordCopy finpordCopy);
	int copyToFinpord();
	int clearCopy();
	int deleteFinpordCopy(String finprod_ID);
	int deleteAllData();
	List<FinpordCopy> findFinpordCopyByTableInfo(Map<String, Object> params);
}
