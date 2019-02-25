package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Staff;

public interface StaffDao {
	int insertStaff(Staff staff);

	int updateStaff(Staff staff);

	int deleteStaff(int staff_ID);

	List<Staff> findAllStaff();

	Staff findStaffById(int staff_ID);
}
