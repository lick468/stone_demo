package com.nenu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Stone;
import com.nenu.domain.StoneCopy;
import com.nenu.domain.Stoninproc;
import com.nenu.domain.SupplierStone;

public interface StoneService {
	List<Stone> findStoneID();
	List<Stone> findAllStoneForPath();
	List<Stone> findAllStoneNum();

	List<Stone> findAllStone();

	List<Stone> findAllNo();
	
	List<StoneCopy> findAllCopyNo();


	//List<Stone> excel2sql(String fileName, MultipartFile mfile, String stone_channelNo);

	int insertStoneByExcel(Stone stone);
	int insertStoneByExcel(StoneCopy stone);//把excel文件里的数据导入数据库
	int insertStone(StoneCopy s);
	int batchInsertStoneCopy(List<StoneCopy> list);//批量插入
	
	int updateStoneCopy(StoneCopy stone);
	
	int deleteStoneCopy(String stone_ID);
	
	int downloadExcel(List<Stone> list,HttpServletResponse response);
	int downloadExcelSupplier(List<SupplierStone> list,HttpServletResponse response);
	Stone findStoneByStoneID(String stone_ID);

	Stone findStoneByStoneIDAJAX(String stone_ID);

	List<Stone> findStoneByMainNo(String stone_mainNo);

	List<Stone> findStoneBySubNo(String stone_substoNo);
	
	List<Stone> findStoneByMainNoForManage(String stone_mainNo);//根据主石编获取石头信息(石库管理)

	List<Stone> findStoneBySubNoForManage(String stone_substoNo);//根据副石编获取石头信息(石库管理)

	List<Stone> findStoneBySupplierName(String stone_supplierName);
	
	List<Stone> findStoneByLoosdofty(String stone_loosdofty);//根据裸石厂获取石头信息

	List<Stone> findStoneByPurchdate(Map<String, Object> map);
	
	int updateMainStoneNumber(Stone stone);
	
	int updateSubStoneNumber(Stone stone);
	
	int updateStone(Stone stone);
	
	
	List<StoneCopy> findAllStoneCopy();
	
	List<String> excel2sql(String fileName, MultipartFile mfile, String stone_channelNo);
	
	StoneCopy findStoneCopyByStoneIDAJAX(String stone_ID);
	int copyToStone();
	int clearCopy();
	
	int deleteAllData();

	int deleteStoneByStoneIDAJAX(Stone stone);
	List<Stone> findAllStoneNumWithTime(Map<String, Object> params);
	int downloadExcelAllSupplier(List<Stoninproc> list_supplier, HttpServletResponse response);
	List<Stone> findStoneByTableInfo(Map<String, Object> params);
	List<Stone> findStoneForTableInfo(Map<String, Object> params);
	List<StoneCopy> findStoneCopyByTableInfo(Map<String, Object> params);

	List<StoneCopy> findMainStoneInfoCopy();
	List<StoneCopy> findSubStoneInfoCopy();

    List<Stone> findDistinctMainStoneNo();
    List<Stone> findDistinctSubStoneNo();
    List<Stone> findDistinctLoosdofty();
    List<Stone> findDistinctPurchdate();
    List<Stone> findMainStoneInfo();
    List<Stone> findSubStoneInfo();

}
