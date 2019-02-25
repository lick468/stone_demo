package com.nenu.dao;

import java.util.List;

import com.nenu.domain.User;

public interface LoginDao {

	User findUserByUser(User ur);
	User findUserByUserName(String user_name);

	User findUserByUserID(int user_ID);

	List<User> findRoleList();

	int createUser(User user);

	int updateUser(User user);

}
