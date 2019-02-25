package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Group;

public interface GroupDao {
	List<Group> findAllGroup();
	int insertGroup(Group group);
	int deleteAllGroup();
}
