package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.LoginDao;
import com.nenu.domain.User;
import com.nenu.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private LoginDao loginDao;

	@Override
	public User findUserByUser(User ur) {
		// TODO Auto-generated method stub
		User userInfo = new User();
		LOGGER.info("开始查找用户");
		userInfo = loginDao.findUserByUser(ur);
		if (userInfo == null) {
			return null;
		} else {
			LOGGER.info("查找用户》》：" + userInfo.toString());
			return userInfo;
		}

	}

	@Override
	public List<User> findRoleList() {
		List<User> list = new ArrayList<User>();
		list = loginDao.findRoleList();
		LOGGER.info("查找权限用户》》：" + list.size());
		return list;
	}

	@Override
	public int createUser(User user) {
		LOGGER.info("创建权限用户》》：" + user);
		return loginDao.createUser(user);
	}

	@Override
	public User findUserByUserID(int user_ID) {
		LOGGER.info("查询员工（user_ID）》》："+user_ID);
		return loginDao.findUserByUserID(user_ID);
	}

	@Override
	public int updateUser(User user) {
		LOGGER.info("更新员工");
		return loginDao.updateUser(user);
	}

	@Override
	public User findUserByUserName(String user_name) {
		// TODO Auto-generated method stub
		LOGGER.info("查询员工（user_name）》》："+user_name);
		return loginDao.findUserByUserName(user_name);
	}

}
