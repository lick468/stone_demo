package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.EmployeeRoleDao;
import com.nenu.domain.EmployeeRole;
import com.nenu.service.EmployeeRoleService;
@Service

public class EmployeeRoleServiceImpl implements EmployeeRoleService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRoleServiceImpl.class);
	@Autowired
	private EmployeeRoleDao employeeRoleDao;
	@Override
	public List<EmployeeRole> findEmployeeRoleByEmpID(int emp_id) {
		// TODO Auto-generated method stub
		LOGGER.info("根据用户ID查询可操作权限>>"+emp_id);
		return employeeRoleDao.findEmployeeRoleByEmpID(emp_id);
	}

	@Override
	public int insertEmployeeRole(EmployeeRole employeeRole) {
		// TODO Auto-generated method stub
		LOGGER.info("添加用户可操作权限>>"+employeeRole.toString());
		return employeeRoleDao.insertEmployeeRole(employeeRole);
	}

	@Override
	public int deleteEmployeeRoleByEmpID(int emp_id) {
		// TODO Auto-generated method stub
		LOGGER.info("根据用户ID查询删除操作权限>>"+emp_id);
		return employeeRoleDao.deleteEmployeeRoleByEmpID(emp_id);
	}

}
