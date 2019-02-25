package com.nenu.service;

import java.util.List;

import com.nenu.domain.Group;

public interface GroupService {
	List<Group> findAllGroup();
	int insertGroup(Group group);
	int deleteAllGroup();
}
