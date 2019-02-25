package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.StaffDao;

import com.nenu.domain.Staff;

import com.nenu.service.StaffService;
@Service

public class StaffServiceImpl implements StaffService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StaffServiceImpl.class);
	@Autowired
	private StaffDao staffDao;

	@Override
	public int insertStaff(Staff staff) {
		LOGGER.info("插入供应商：" + staff);
		return staffDao.insertStaff(staff);
	}

	@Override
	public int updateStaff(Staff staff) {
		LOGGER.info("更新供应商信息：" + staff);
		return staffDao.updateStaff(staff);
	}

	@Override
	public int deleteStaff(int staff_ID) {
		LOGGER.info("删除供应商信息（ID）：" + staff_ID);
		return staffDao.deleteStaff(staff_ID);
	}

	@Override
	public List<Staff> findAllStaff() {
		List<Staff> list = new ArrayList<Staff>();
		list = staffDao.findAllStaff();
		LOGGER.info("查询所有供应商信息：" + list.size());
		return list;
	}

	@Override
	public Staff findStaffById(int staff_ID) {
		LOGGER.info("根据ID查询供应商信息：" + staff_ID);
		return staffDao.findStaffById(staff_ID);
	}

}
