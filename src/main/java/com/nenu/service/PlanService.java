package com.nenu.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.nenu.domain.Plan;

public interface PlanService {
	List<Plan> planexcel2sql(String fileName, MultipartFile mfile);//excel文件数据导入数据库（着重读取解析）
	int insertPlanByExcel(Plan plan);//把excel文件里的数据导入数据库
	int batchInsertPlan(List<Plan> planList);//批量插入
	List<Plan> findPlanByTableInfo(Map<String, Object> params);//查找数据库里数据
	int deleteOneData(int id); //删除一条
	int deleteAllData();//清空
	List<Plan> findPlanByParams(Map<String, Object> params);//根据参数查找
	Plan findPlanByPlan(Plan plan);//查重
	List<Plan> findAllPlan();//查找所有计划销量
}
