package com.nenu.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Plan;
import com.nenu.domain.StoneAnalysis;

public interface StoneAnalysisDao {
	List<StoneAnalysis> findAllStone();
	List<StoneAnalysis> findStoneByTableInfo(Map<String, Object> params);
	
	List<StoneAnalysis> findSell();
	List<StoneAnalysis> findRoom();
	List<StoneAnalysis> findArea();
	List<StoneAnalysis> findCounter();
	List<StoneAnalysis> findRoomSell();
	List<StoneAnalysis> findAreaSell();
	List<StoneAnalysis> findCounterSell();
	List<StoneAnalysis> findStoneBySupplier(Map<String, Object> params);//711页面
	List<StoneAnalysis> findStoneBySupplierFor711(Map<String, Object> params);//711页面

	
	List<StoneAnalysis> findStoneBySupplierForProductSum(Map<String, Object> params);
	List<StoneAnalysis> findStoneBySupplierForProductSettlementpriceSum(Map<String, Object> params);
	
	List<StoneAnalysis> findProductBySupplierAndCounter(Map<String, Object> params);

	List<StoneAnalysis> findStoneByCounter(Map<String, Object> params);//743页面
	List<StoneAnalysis> findStoneByCounterFor743(Map<String, Object> params);//743页面
	
	
	List<StoneAnalysis> findStoneByArea(Map<String, Object> params);//744页面
	List<StoneAnalysis> findStoneFor744(Map<String, Object> params);//744页面

		
	List<StoneAnalysis> findStoneSellNumber(Map<String, Object> params);
	
	
	List<StoneAnalysis> findAreaAccountByProductAndTime(Map<String, Object> params);
	List<StoneAnalysis> findRoomAccountByProductAndTime(Map<String, Object> params);
	List<StoneAnalysis> findCounterAccountByProductAndTime(Map<String, Object> params);
	
	
	
	
	List<StoneAnalysis> findStoneByAreaAndTimeForNumberSumDemo(Map<String, Object> params);
	List<StoneAnalysis> findStoneByAreaAndTimeForSettlementpriceSum(Map<String, Object> params); //销售趋势分析（结算价）
	List<StoneAnalysis> findStoneByAreaAndTimeForNumberSum(Map<String, Object> params);//销售趋势分析（数量）
	
	
	List<StoneAnalysis> findExchangeByParams(Map<String, Object> params);//根据条件查看兑换情况
	
	
	List<StoneAnalysis> excel2sql(String fileName, MultipartFile mfile);//excel文件数据导入数据库（着重读取解析）
	int insertStone(StoneAnalysis stone);//单条插入
	int batchInsertStone(List<StoneAnalysis> list);//批量插入
	List<StoneAnalysis> findExchangeByParamsFor725(Map<String, Object> params);//查看兑换数量情况
	
	List<StoneAnalysis> findCompareDataSetByParams(Map<String, Object> params);//查看地区销售对比数据
	List<StoneAnalysis> findTwoYearsDataSetForCompare(Map<String, Object> params);//查看地区两年内销售对比
	int downloadExcelForIndex10(List<StoneAnalysis> list);//下载index10页面的表格
	int downloadExcelForIndex3(List<StoneAnalysis> list,HttpServletResponse response);//下载index3页面的表格
	int downloadExcelForIndex(List<StoneAnalysis> list,HttpServletResponse response);//下载index3页面的表格
	int downloadExcelForProductNum(List<StoneAnalysis> list,HttpServletResponse response);//下载ProductNum页面的表格
	int downloadExcelFor743(List<StoneAnalysis> list,HttpServletResponse response);//下载743页面的表格
	int downloadExcelFor744(List<StoneAnalysis> list,HttpServletResponse response);//下载744页面的表格
	List<StoneAnalysis> findAreaFor742(Map<String, Object> params);//根据销售量找出销量排行的区域
	List<StoneAnalysis> findRoomFor742(Map<String, Object> params);//根据销售量找出销量排行的区域
	List<StoneAnalysis> findCounterFor742(Map<String, Object> params);//根据销售量找出销量排行的区域
	
	List<StoneAnalysis> findStoneByCounterAndTimeOfIndex9(Map<String, Object> params);//根据柜台比对今年和去年的销量
	List<StoneAnalysis> findStoneByProductAndTimeOfIndex9(Map<String, Object> params);//根据商品比对今年和去年的销量
	List<StoneAnalysis> findSupplierForIndex11(Map<String, Object> params);//732 供应商排名
	List<StoneAnalysis> findOneSupplierForIndex11(Map<String, Object> params);//732供应商排名


	
	List<StoneAnalysis> findProductOfIndex811(Map<String, Object> params);//8系列
	List<StoneAnalysis> findProductOfIndex812(Map<String, Object> params);//8系列
	List<StoneAnalysis> findProductOfIndex813(Map<String, Object> params);//8系列
	List<StoneAnalysis> findProductOfIndex814(Map<String, Object> params);//8系列
	List<StoneAnalysis> findDateForIndex8888(Map<String, Object> params);//8系列
	
	List<StoneAnalysis> findCompareDateCounterOfIndex822(Map<String, Object> params);//8系列
	List<StoneAnalysis> findCompareDateProductOfIndex822(Map<String, Object> params);//8系列
	List<StoneAnalysis> findCompareDateAreaOfIndex822(Map<String, Object> params);//8系列
	List<StoneAnalysis> findCompareDateRoomOfIndex822(Map<String, Object> params);//8系列
	
	
	
	int downloadExcelForIndex811(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex812(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex813(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex814(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex821(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex822(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex831(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	int downloadExcelForIndex832(List<StoneAnalysis> list,HttpServletResponse response);//8系列
	
	List<StoneAnalysis> findProductByTime(Map<String, Object> params);
	List<StoneAnalysis> findStoneBySource(Map<String, Object> params);//source
	List<StoneAnalysis> findStoneForSource(Map<String, Object> params);//source
	int downloadExcelForIndexSource(List<StoneAnalysis> list,HttpServletResponse response);//source
	List<StoneAnalysis> findStoneFor722And723(Map<String, Object> params);//index722  index723
	List<StoneAnalysis> findStoneFor722(Map<String, Object> params);//index722
	List<StoneAnalysis> findStoneFor723(Map<String, Object> params);//index723
	int downloadExcelForIndex722(List<StoneAnalysis> list,HttpServletResponse response);//index722
	int downloadExcelForIndex723(List<StoneAnalysis> list,HttpServletResponse response);//index723
	int downloadExcelForIndex724(List<StoneAnalysis> list,HttpServletResponse response);//index724
	List<StoneAnalysis> findStoneForIndex724MainStone(Map<String, Object> params);//index724
	List<StoneAnalysis> findStoneForIndex724ListPrice(Map<String, Object> params);//index724
	List<StoneAnalysis> findStoneForIndex724SettlePrice(Map<String, Object> params);//index724
	List<StoneAnalysis> findStoneForIndex724GoldWeight(Map<String, Object> params);//index724
	List<StoneAnalysis> findStoneFor726(Map<String, Object> params);//index726
	List<StoneAnalysis> findStoneForIndex726(Map<String, Object> params);//index726
	int downloadExcelForIndex726(List<StoneAnalysis> list,HttpServletResponse response);//index726
	
	List<StoneAnalysis> findStoneForIndex741(Map<String, Object> params);//index741
	int downloadExcelForIndex741(List<Plan> list, HttpServletResponse response);
	
	int deleteOneData(int id);
	int deleteAllData();
	//下载图表数据
	int downloadGraphExcelFor711(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor712(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForSource(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor722(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor723(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor724(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor726(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex7(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor743(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor744(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForS75(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor7311(String conGraph, HttpServletResponse response);
	int downloadGraphExcelFor7312(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex3(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex11(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForS752(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex811(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex812(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex813(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex814(String conGraph, HttpServletResponse response);
	int downloadGraphExcelForIndex5(String conGraph, HttpServletResponse response);


}
