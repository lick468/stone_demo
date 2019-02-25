package com.nenu.service;

import java.util.List;

import com.nenu.domain.Channel;

public interface ChannelService {
	List<Channel> findAllChannel();

	int updateChannel(Channel channel);

	int deleteChannel(int channel_ID);

	Channel findChannelById(int channel_ID);

	int insertChannel(Channel channel);

}
