package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Employee;

public interface EmployeeDao {
	
	int createEmployee(Employee employee);
	int updateEmployee(Employee employee);
	int deleteEmployee(int id);
	List<Employee> findAllEmployee();
	Employee findEmployeeByName(String emp_name);
	Employee findEmployeeByID(int id);
	
	
	
}
