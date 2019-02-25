package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Counter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int counter_ID; //柜台ID

	private String counter_name;//柜台名称

	public int getCounter_ID() {
		return counter_ID;
	}

	public void setCounter_ID(int counter_ID) {
		this.counter_ID = counter_ID;
	}

	public String getCounter_name() {
		return counter_name;
	}

	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}

}
