package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class BelongAndCounter implements Serializable {

	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int bc_ID; //统计对象和对象ID
	private String bc_belong_name;//统计对象名称
	private String bc_counter_name;//柜台名称
	public int getBc_ID() {
		return bc_ID;
	}
	public void setBc_ID(int bc_ID) {
		this.bc_ID = bc_ID;
	}
	public String getBc_belong_name() {
		return bc_belong_name;
	}
	public void setBc_belong_name(String bc_belong_name) {
		this.bc_belong_name = bc_belong_name;
	}
	public String getBc_counter_name() {
		return bc_counter_name;
	}
	public void setBc_counter_name(String bc_counter_name) {
		this.bc_counter_name = bc_counter_name;
	}

	

	

}
