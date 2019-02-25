package com.nenu.dao;

import java.util.List;

import com.nenu.domain.EmployeeRole;

public interface EmployeeRoleDao {
	List<EmployeeRole> findEmployeeRoleByEmpID(int emp_id);
	int insertEmployeeRole(EmployeeRole employeeRole);
	int deleteEmployeeRoleByEmpID(int emp_id);
}
