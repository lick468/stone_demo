package com.nenu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nenu.domain.EmployeeRole;


public interface EmployeeRoleService {
	List<EmployeeRole> findEmployeeRoleByEmpID(int emp_id);
	int insertEmployeeRole(EmployeeRole employeeRole);
	int deleteEmployeeRoleByEmpID(int emp_id);
}
