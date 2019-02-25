package com.nenu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.nenu.domain.TBackStone;

public interface TBackStoneService {
	int insertTBackStone(TBackStone tbackStone);//插入一条退石记录
	List<TBackStone> findAllTBackStone();//获取所有退石记录
	List<TBackStone> findTBackStoneByStoneNo(String stoneNo);//根据石编获取退石记录
	List<TBackStone> findTBackStoneByTime(Map<String, Object> map);
	int downloadExcel(List<TBackStone> list, HttpServletResponse response);//下载excel文件
}
