package com.nenu.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.GroupDao;
import com.nenu.domain.Group;
import com.nenu.service.GroupService;
@Service

public class GroupServiceImpl implements GroupService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
	@Autowired
	private GroupDao groupDao;

	@Override
	public List<Group> findAllGroup() {
		// TODO Auto-generated method stub
		LOGGER.info("查找所有：>>" );
		return groupDao.findAllGroup();
	}

	@Override
	public int insertGroup(Group group) {
		// TODO Auto-generated method stub
		LOGGER.info("插入一条记录：>>" );
		return groupDao.insertGroup(group);
	}

	@Override
	public int deleteAllGroup() {
		// TODO Auto-generated method stub
		LOGGER.info("清空：>>" );
		return groupDao.deleteAllGroup();
	}

}
