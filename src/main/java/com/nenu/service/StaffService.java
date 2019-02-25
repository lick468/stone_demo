package com.nenu.service;

import java.util.List;

import com.nenu.domain.Staff;

public interface StaffService {
	List<Staff> findAllStaff();

	int insertStaff(Staff staff);

	int updateStaff(Staff staff);

	int deleteStaff(int staff_ID);

	Staff findStaffById(int staff_ID);

}
