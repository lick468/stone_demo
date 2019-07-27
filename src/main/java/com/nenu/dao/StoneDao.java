package com.nenu.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Stone;
import com.nenu.domain.Stoninproc;
import com.nenu.domain.SupplierStone;

public interface StoneDao {
	
	List<Stone> findAllStone();//获取所有的石头信息
	List<Stone> findAllStoneForPath();//获取所有的石头信息
	

	List<Stone> findAllStoneNum();//获取石头库中石数不小于0的石头信息
	List<Stone> findAllStoneNumWithTime(Map<String, Object> params);//获取总部库存

	List<Stone> findStoneID();//获取石头信息

	List<Stone> findAllNo();//获取所有的石头编号

	List<Stone> excel2sql(String fileName, MultipartFile mfile, String stone_channelNo);//excel文件数据导入数据库（着重读取解析）

	int insertStoneByExcel(Stone stone);//把excel文件里的数据导入数据库
	
	int insertStone(Stone stone);//插入一条石头信息

	int downloadExcel(List<Stone> list,HttpServletResponse response);//生成excel文件
	
	int downloadExcelSupplier(List<SupplierStone> list,HttpServletResponse response);//生成excel文件

	Stone findStoneByStoneID(String stone_ID);//根据ID获取石头信息

	Stone findStoneByStoneIDAJAX(String stone_ID);//根据ID获取石头信息

	List<Stone> findStoneByMainNo(String stone_mainNo);//根据主石编获取石头信息

	List<Stone> findStoneBySubNo(String stone_substoNo);//根据副石编获取石头信息
	
	List<Stone> findStoneByMainNoForManage(String stone_mainNo);//根据主石编获取石头信息(石库管理)

	List<Stone> findStoneBySubNoForManage(String stone_substoNo);//根据副石编获取石头信息(石库管理)

	List<Stone> findStoneBySupplierName(String stone_supplierName);//根据供应商获取石头信息
	
	List<Stone> findStoneByLoosdofty(String stone_loosdofty);//根据裸石厂获取石头信息

	List<Stone> findStoneByPurchdate(Map<String, Object> map);//根据采购日期获取石头信息
	
	int updateMainStoneNumber(Stone stone);//更新主石数量
	
	int updateSubStoneNumber(Stone stone);//更新副石数量
	
	int updateStone(Stone stone);//更新石头信息
	
	int deleteStoneByStoneIDAJAX(Stone stone);//总部退石
	
	int downloadExcelAllSupplier(List<Stoninproc> list_supplier, HttpServletResponse response);//生成供应商库存汇总记录
	
	List<Stone> findStoneByTableInfo(Map<String, Object> params);

	List<Stone> findDistinctMainStoneNo();
    List<Stone> findDistinctSubStoneNo();
    List<Stone> findDistinctLoosdofty();
    List<Stone> findDistinctPurchdate();
    List<Stone> findMainStoneInfo();
    List<Stone> findSubStoneInfo();


}
