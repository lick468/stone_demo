package com.nenu.service;

import java.util.List;

import com.nenu.domain.Employee;

public interface EmployeeService {
	int createEmployee(Employee employee);
	int updateEmployee(Employee employee);
	int deleteEmployee(int id);
	List<Employee> findAllEmployee();
	Employee findEmployeeByName(String emp_name);
	Employee findEmployeeByID(int id);

}
