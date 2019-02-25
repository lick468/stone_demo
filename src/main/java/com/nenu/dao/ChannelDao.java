package com.nenu.dao;

import java.util.List;

import com.nenu.domain.Channel;
import com.nenu.domain.Supplier;

public interface ChannelDao {
	int insertChannel(Channel channel);

	int updateChannel(Channel channel);

	int deleteChannel(int channel_ID);

	List<Channel> findAllChannel();

	Channel findChannelById(int channel_ID);

}
