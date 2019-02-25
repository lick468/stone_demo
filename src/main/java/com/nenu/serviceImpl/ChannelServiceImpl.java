package com.nenu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nenu.dao.ChannelDao;
import com.nenu.domain.Channel;
import com.nenu.service.ChannelService;
@Service

public class ChannelServiceImpl implements ChannelService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelServiceImpl.class);
	@Autowired
	private ChannelDao channelDao;

	@Override
	public int insertChannel(Channel channel) {
		LOGGER.info("插入来源：" + channel);
		return channelDao.insertChannel(channel);
	}

	@Override
	public int updateChannel(Channel channel) {
		LOGGER.info("更新来源信息：" + channel);
		return channelDao.updateChannel(channel);
	}

	@Override
	public int deleteChannel(int channel_ID) {
		LOGGER.info("删除来源信息（ID）：" + channel_ID);
		return channelDao.deleteChannel(channel_ID);
	}

	@Override
	public List<Channel> findAllChannel() {
		List<Channel> list = new ArrayList<Channel>();
		list = channelDao.findAllChannel();
		LOGGER.info("查询所有来源信息：" + list.size());
		return list;
	}

	@Override
	public Channel findChannelById(int channel_ID) {
		LOGGER.info("根据ID查询来源信息：" + channel_ID);
		return channelDao.findChannelById(channel_ID);
	}

}
