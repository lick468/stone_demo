package com.nenu.serviceImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.TBackStoneDao;
import com.nenu.domain.TBackStone;
import com.nenu.service.TBackStoneService;
import com.nenu.util.ExcelUtils;
@Service

public class TBackStoneServiceImpl implements TBackStoneService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TBackStoneServiceImpl.class);
	@Autowired
	private TBackStoneDao tbackStoneDao;
	@Override
	public int insertTBackStone(TBackStone tbackStone) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条退石记录：>>" );
		return tbackStoneDao.insertTBackStone(tbackStone);
	}

	@Override
	public List<TBackStone> findAllTBackStone() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有退石记录：>>" );
		return tbackStoneDao.findAllTBackStone();
	}

	@Override
	public List<TBackStone> findTBackStoneByStoneNo(String stoneNo) {
		// TODO Auto-generated method stub
		LOGGER.info("根据石编查找退石记录：>>"+ stoneNo);
		return tbackStoneDao.findTBackStoneByStoneNo(stoneNo);
	}

	@Override
	public List<TBackStone> findTBackStoneByTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		LOGGER.info("根据退石时间查找退石记录：>>"+ map.get("tback_time"));
		return tbackStoneDao.findTBackStoneByTime(map);
	}

	@Override
	public int downloadExcel(List<TBackStone> list, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String sheetName = "总部退石表单";
		String titleName = "总部退石统计表";
		String fileName = "总部退石统计表单";
		int columnNumber = 4;
		int[] columnWidth = {  10, 20, 20, 80};
		
		String[][] dataList = new String[list.size()][5];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			dataList[i][0]=""+i;
			dataList[i][1]=sdf.format(list.get(i).getTback_time());
			dataList[i][2]=list.get(i).getTback_stoneNo();
			dataList[i][3]=list.get(i).getTback_content();
		}
		String path="D:\\stoneFile";

		String[] columnName = { "序号","时间","石编", "内容"};

		try {
			new ExcelUtils().ExportNoResponse(sheetName, titleName, fileName, columnNumber, columnWidth,
					columnName, dataList,path,response);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

}
