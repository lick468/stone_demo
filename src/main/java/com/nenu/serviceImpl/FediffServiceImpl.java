package com.nenu.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import com.nenu.dao.FediffDao;
import com.nenu.domain.Fediff;
import com.nenu.domain.Stone;
import com.nenu.service.FediffService;
import com.nenu.util.ExcelUtils;

@Service

public class FediffServiceImpl implements FediffService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FediffServiceImpl.class);
	@Autowired
	private FediffDao fediffDao;

	@Override
	public List<Fediff> findAllFediff() {
		// TODO Auto-generated method stub
		List<Fediff> list = new ArrayList<Fediff>();
		list = fediffDao.findAllFediff();
	   LOGGER.info("查询所有入料差:>>"+list.size());
		return list;
	}

	@Override
	public int insertFediff(Fediff fediff) {
		// TODO Auto-generated method stub
		LOGGER.info("插入入料差:>>");
		return fediffDao.insertFediff(fediff);
	}

	@Override
	public Fediff findFediffByTwoParam(long fediff_procordNo, String fediff_stoneNo) {
		// TODO Auto-generated method stub
		LOGGER.info("查看订单和石编是否已经在库里:>>");
		return fediffDao.findFediffByTwoParam(fediff_procordNo, fediff_stoneNo);
	}

	@Override
	public int downloadFediff(List<Fediff> list,HttpServletResponse response) {
		String sheetName = "入料差表单";
		String titleName = "入料差数据统计表";
		String fileName = "入料差统计表单";
		int columnNumber = 5;
		int[] columnWidth = { 20, 20, 20, 20,20};

		
		String[][] dataList = new String[list.size()][5];

		for (int i = 0; i < dataList.length; i++) {
			Fediff f = (Fediff) list.get(i);
			dataList[i][0] = String.valueOf(f.getFediff_ID());
			dataList[i][1] = String.valueOf(f.getFediff_procordNo());
			dataList[i][2] = String.valueOf(f.getFinpord_procordNo());
			dataList[i][3] =f.getFediff_stoneNo();
			dataList[i][4] = String.valueOf(f.getFediff_weightDiff());

		}
		String path="D:\\FediffFile";
		String[] columnName = {"序号", "订单号","成品单号", "石编", "入料差"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 2;
	}

	@Override
	public List<Fediff> findFediffByTime(Date fediff_time) {
		// TODO Auto-generated method stub
		LOGGER.info("根据时间查询入料差:>>"+fediff_time);
		return fediffDao.findFediffByTime(fediff_time);
	}

	@Override
	public List<Fediff> findFediffByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return fediffDao.findFediffByTableInfo(params);
	}

	

}
