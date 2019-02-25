package com.nenu.service;

import java.util.List;

import com.nenu.domain.User;

public interface LoginService {

	User findUserByUser(User ur);

	User findUserByUserID(int user_ID);
	
	User findUserByUserName(String user_name);

	List<User> findRoleList();

	int createUser(User user);

	int updateUser(User user);
}
