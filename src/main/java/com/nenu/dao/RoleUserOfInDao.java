package com.nenu.dao;

import java.util.List;

import com.nenu.domain.RoleUserOfIn;

public interface RoleUserOfInDao {
	List<RoleUserOfIn> findByRoleUser(int role_user);
	int insertRoleUserOfIn(RoleUserOfIn roleUserOfIn);
	int deleteRoleUserOfIn(int role_user);
}
