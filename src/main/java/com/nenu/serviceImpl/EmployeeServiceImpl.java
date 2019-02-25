package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.EmployeeDao;
import com.nenu.domain.Employee;
import com.nenu.service.EmployeeService;
@Service

public class EmployeeServiceImpl  implements EmployeeService{
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	@Autowired
	private EmployeeDao employeeDao;
	@Override
	public int createEmployee(Employee employee) {
		// TODO Auto-generated method stub
		LOGGER.info("创建新用户>>"+employee.toString());
		return employeeDao.createEmployee(employee);
	}

	@Override
	public int updateEmployee(Employee employee) {
		// TODO Auto-generated method stub
		LOGGER.info("更新用户>>"+employee.toString());
		return employeeDao.updateEmployee(employee);
	}

	@Override
	public List<Employee> findAllEmployee() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有用户>>");
		return employeeDao.findAllEmployee();
	}

	@Override
	public Employee findEmployeeByName(String emp_name) {
		// TODO Auto-generated method stub
		LOGGER.info("根据用户名查找>>"+emp_name);
		return employeeDao.findEmployeeByName(emp_name);
	}

	@Override
	public Employee findEmployeeByID(int id) {
		// TODO Auto-generated method stub
		LOGGER.info("根据用户ID查找>>"+id);
		return employeeDao.findEmployeeByID(id);
	}

	@Override
	public int deleteEmployee(int id) {
		// TODO Auto-generated method stub
		LOGGER.info("根据用户ID删除>>"+id);
		return employeeDao.deleteEmployee(id);
	}

}
