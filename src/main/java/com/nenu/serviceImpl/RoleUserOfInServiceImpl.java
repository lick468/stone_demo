package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.RoleUserOfInDao;
import com.nenu.domain.RoleUserOfIn;
import com.nenu.service.RoleUserOfInService;

@Service

public class RoleUserOfInServiceImpl implements RoleUserOfInService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleUserOfInServiceImpl.class);
	@Autowired
	private RoleUserOfInDao roleUserOfInDao;

	@Override
	public List<RoleUserOfIn> findByRoleUser(int role_user) {
		List<RoleUserOfIn> list = new ArrayList<RoleUserOfIn>();
		list = roleUserOfInDao.findByRoleUser(role_user);
		LOGGER.info("开始查询权限字段");
		if (list == null)
			return null;
		else {
			LOGGER.info("权限" + role_user + "可以操作的字段：" + list);
			return list;
		}
	}

	public int insertRoleUserOfIn(RoleUserOfIn roleUserOfIn) {
		LOGGER.info("插入权限>>级别"+roleUserOfIn.getRole_user()+"，字段"+roleUserOfIn.getRole_type());
		return roleUserOfInDao.insertRoleUserOfIn(roleUserOfIn);
	}

	@Override
	public int deleteRoleUserOfIn(int role_user) {
		// TODO Auto-generated method stub
		LOGGER.info("删除权限"+role_user+"的操作字段");
		return roleUserOfInDao.deleteRoleUserOfIn(role_user);
	}

}
