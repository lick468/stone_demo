package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Channel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int channel_ID; //来源ID

	private String channel_name;//来源名称

	public int getChannel_ID() {
		return channel_ID;
	}

	public void setChannel_ID(int channel_ID) {
		this.channel_ID = channel_ID;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

}
