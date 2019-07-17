package com.nenu.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Stone;
import com.nenu.domain.StoneCopy;

public interface StoneCopyDao {
	
	List<StoneCopy> findAllStone();//获取所有的石头信息

	List<StoneCopy> findAllStoneNum();//获取石头库中石数不小于0的石头信息

	List<StoneCopy> findStoneID();//获取石头信息

	List<StoneCopy> findAllCopyNo();//获取所有的石头编号

	List<StoneCopy> excel2sql(String fileName, MultipartFile mfile, String stone_channelNo);//excel文件数据导入数据库（着重读取解析）

	int insertStoneByExcel(StoneCopy stone);//把excel文件里的数据导入数据库
	
	int insertStoneCopy(StoneCopy stoneCopy);//插入一条石头信息
	
	int batchInsertStoneCopy(List<StoneCopy> list);//批量插入

	int downloadExcel(List<StoneCopy> list);//生成excel文件

	StoneCopy findStoneByStoneID(String stone_ID);//根据ID获取石头信息

	StoneCopy findStoneByStoneIDAJAX(String stone_ID);//根据ID获取石头信息

	List<StoneCopy> findStoneByMainNo(String stone_mainNo);//根据主石编获取石头信息

	List<StoneCopy> findStoneBySubNo(String stone_substoNo);//根据副石编获取石头信息

	List<StoneCopy> findStoneBySupplierName(String stone_supplierName);//根据供应商获取石头信息

	List<StoneCopy> findStoneByPurchdate(Map<String, Object> map);//根据采购日期获取石头信息
	
	int updateMainStoneNumber(StoneCopy stone);//更新主石数量
	
	int updateSubStoneNumber(StoneCopy stone);//更新副石数量
	
	int updateStoneCopy(StoneCopy stone);//更新石头信息
	int copyToStone();
	int clearCopy();
	
	int deleteStoneCopy(String stone_ID);//从临时库中删除一条石头记录
	int deleteAllData();//删除临时库中全部数据
	List<StoneCopy> findStoneCopyByTableInfo(Map<String, Object> params);

}
