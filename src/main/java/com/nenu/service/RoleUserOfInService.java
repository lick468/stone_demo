package com.nenu.service;

import java.util.List;

import com.nenu.domain.RoleUserOfIn;

public interface RoleUserOfInService {
	List<RoleUserOfIn> findByRoleUser(int role_user);

	int insertRoleUserOfIn(RoleUserOfIn roleUserOfIn);
	int deleteRoleUserOfIn(int role_user);
}
