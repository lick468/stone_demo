package com.nenu.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nenu.domain.Fediff;
import com.nenu.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import com.nenu.dao.ProcordDao;
import com.nenu.domain.Procord;


import com.nenu.service.ProcordService;

import javax.servlet.http.HttpServletResponse;

@Service

public class ProcordServiceImpl implements ProcordService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcordServiceImpl.class);
	@Autowired
	private ProcordDao procordDao;

	@Override
	public List<Procord> findAllProcord() {
		// TODO Auto-generated method stub
		List<Procord> list = new ArrayList<Procord>();
		list = procordDao.findAllProcord();
		LOGGER.info("查询所有订单:>>");
		return list;
	}

	@Override
	public int insertProcord(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("送石出库:>>");
		return procordDao.insertProcord(procord);
	}

	@Override
	public Procord findProcordByProcordNo(long procord_no) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号查询:>>"+procord_no);
		return procordDao.findProcordByProcordNo(procord_no);
	}

	@Override
	public int updateProcord(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("更新订单信息:>>");
		return procordDao.updateProcord(procord);
	}

	@Override
	public int updateProcordState(Procord procord ) {
		// TODO Auto-generated method stub
		LOGGER.info("根据订单号更新订单状态:>>");
		return procordDao.updateProcordState(procord);
	}

	@Override
	public int updateProcordAccounts(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("退石后修改订单中石数:>>");
		return procordDao.updateProcordAccounts(procord);
	}

	@Override
	public int updateProcordPay(Procord procord) {
		// TODO Auto-generated method stub
		LOGGER.info("退石后修改货款金额:>>");
		return procordDao.updateProcordPay(procord);
	}

	@Override
	public List<Procord> findProcordBySupplier(String procord_supplier) {
		// TODO Auto-generated method stub
		LOGGER.info("根据供应商查询:>>"+procord_supplier);
		return procordDao.findProcordBySupplier(procord_supplier);
	}

	@Override
	public List<Procord> findProcordByTableInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return procordDao.findProcordByTableInfo(params);
	}

	@Override
	public List<Procord> findProcordForTableInfo(Map<String, Object> params) {
		return procordDao.findProcordForTableInfo(params);
	}

	@Override
	public int downloadExcel(List<Procord> list, HttpServletResponse response) {
		LOGGER.info("导出加工订单表");
        String sheetName = "加工订单表表单";
        String titleName = "加工订单表数据统计表";
        String fileName = "加工订单表表单";
        int columnNumber = 12;
        int[] columnWidth = { 20, 20, 20, 20,20,20, 20, 20, 20,20,20,20};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[][] dataList = new String[list.size()][12];

        for (int i = 0; i < dataList.length; i++) {
            Procord f = (Procord) list.get(i);
            dataList[i][0] = String.valueOf(f.getProcord_ID());
            dataList[i][1] = String.valueOf(f.getProcord_no());
            dataList[i][2] = f.getProcord_supplier();
            dataList[i][3] = String.valueOf(f.getProcord_batch());
            dataList[i][4] =sdf.format(f.getProcord_date());
            dataList[i][5] =sdf.format(f.getProcord_delydate());
            dataList[i][6] = String.valueOf(f.getProcord_goodscount());
            dataList[i][7] = String.valueOf(f.getProcord_weight());
            dataList[i][8] = String.valueOf(f.getProcord_pay());
            dataList[i][9] = f.getProcord_delyman();
            dataList[i][10] = f.getProcord_preparer();
            dataList[i][11] = f.getProcord_porter();
        }
        String path="D:\\FediffFile";
        String[] columnName = {"序号", "订单号","加工厂", "批次", "订单日期", "出货日期", "石数", "石重", "货款金额", "送货人", "经手人", "接货人"};

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

}
