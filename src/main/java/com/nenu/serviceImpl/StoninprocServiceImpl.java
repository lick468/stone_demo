package com.nenu.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import com.nenu.dao.StoninprocDao;
import com.nenu.domain.Stoninproc;


import com.nenu.service.StoninprocService;
import com.nenu.util.ExcelUtils;

@Service

public class StoninprocServiceImpl implements StoninprocService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoninprocServiceImpl.class);
	@Autowired
	private StoninprocDao stoninprocDao;

	@Override
	public List<Stoninproc> findAllStoninproc() {
		// TODO Auto-generated method stub
		List<Stoninproc> list = new ArrayList<Stoninproc>();
		list = stoninprocDao.findAllStoninproc();
	/*	LOGGER.info("查询所有employee:>>"+list);*/
		return list;
	}

	@Override
	public Stoninproc findLastStoninproc() {
		// TODO Auto-generated method stub
		LOGGER.info("查询订单库里最后一条记录:>>");
		return stoninprocDao.findLastStoninproc();
	}

	@Override
	public int insertStoninproc(Stoninproc stoninproc) {
		// TODO Auto-generated method stub
		LOGGER.info("插入送石表:>>");
		return stoninprocDao.insertStoninproc(stoninproc);
	}
	@Override
	public Stoninproc findStoninprocByStoneNo(String stoninproc_stoneNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据主石编查找订单号:>>");
		return stoninprocDao.findStoninprocByStoneNo(stoninproc_stoneNo);
	}

	@Override
	public List<Stoninproc> findStoninprocBySubStoneNo(String stoninproc_stoneNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据副石编查找订单号:>>");
		return stoninprocDao.findStoninprocBySubStoneNo(stoninproc_stoneNo);
	}

	@Override
	public List<Stoninproc> findAllStoninprocByProcordNo(long stoninproc_procordNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号查找石编:>>");
		return stoninprocDao.findAllStoninprocByProcordNo(stoninproc_procordNo);
	}

	@Override
	public int deleteStoneFromProcord(Map<String, Object> params) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号和石编从表中删除一条记录:>>"+params.get("stoneNo")+">>"+params.get("procordNo"));
		return stoninprocDao.deleteStoneFromProcord(params);
	}

	@Override
	public Stoninproc findStoneNumberFromStoninproc(Map<String, Object> params) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号和石编查询石数:>>"+params.get("stoneNo")+">>"+params.get("procordNo"));
		return stoninprocDao.findStoneNumberFromStoninproc(params);
	}

	@Override
	public int updateStoninprocState(Stoninproc stoninproc) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号和石编修改对应石头的状态>>"+stoninproc.getStoninproc_procordNo()+":"+stoninproc.getStoninproc_stoneNo());
		return stoninprocDao.updateStoninprocState(stoninproc);
	}

	@Override
	public int downloadProcordDetails(String context, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "订单详情表表单";
		String titleName = "订单详情表统计表";
		String fileName = "订单详情表表单";
		String[] glist = context.split("@");
		int columnNumber = 11;
		int[] columnWidth = { 15,15, 15,15,15, 15,15 ,15, 15,15 ,15 };
		String[][] dataList = new String[glist.length/11][11];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < glist.length; i++) {
			if(i%11==10) {
				int j = (i+1)/11-1;
				dataList[j][0] =glist[i-10] ;
				if(glist[i-9].length() == 1) {
                    dataList[j][1] ="";
                }else {
                    dataList[j][1] =glist[i-9] ;
                }
				dataList[j][2] =glist[i-8] ;
				dataList[j][3] =glist[i-7] ;
				dataList[j][4] =glist[i-6] ;
				dataList[j][5] =glist[i-5] ;
				dataList[j][6] =glist[i-4] ;
				dataList[j][7] =glist[i-3] ;
				dataList[j][8] =glist[i-2] ;
				dataList[j][9] =glist[i-1] ;
				dataList[j][10] =glist[i-0] ;
			}					
		}
		String path = "D:\\analysisFile";
		String[] columnName = {"订单","成品单号", "石编","石重", "石数","货款金额", "批次","颜色", "净度","备注","状态"};
		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Stoninproc> findAllStoninprocWithStateEqualZero() {
		// TODO Auto-generated method stub
		return stoninprocDao.findAllStoninprocWithStateEqualZero();
	}

	

}
