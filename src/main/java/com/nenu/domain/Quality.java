package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Quality implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int quality_ID; //成色ID

	private String quality_name;//成色名称

	public int getQuality_ID() {
		return quality_ID;
	}

	public void setQuality_ID(int quality_ID) {
		this.quality_ID = quality_ID;
	}

	public String getQuality_name() {
		return quality_name;
	}

	public void setQuality_name(String quality_name) {
		this.quality_name = quality_name;
	}

}
