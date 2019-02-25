package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Belong implements Serializable {

	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int belong_ID;//统计对象ID

	private String belong_name;//统计对象名称
	private String counter_name;//柜台名称
	

	public String getCounter_name() {
		return counter_name;
	}

	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}

	public int getBelong_ID() {
		return belong_ID;
	}

	public void setBelong_ID(int belong_ID) {
		this.belong_ID = belong_ID;
	}

	public String getBelong_name() {
		return belong_name;
	}

	public void setBelong_name(String belong_name) {
		this.belong_name = belong_name;
	}

	

}
