package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Staff implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int staff_ID;//人员ID
	private String staff_name;//人员名称

	public int getStaff_ID() {
		return staff_ID;
	}

	public void setStaff_ID(int staff_ID) {
		this.staff_ID = staff_ID;
	}

	public String getStaff_name() {
		return staff_name;
	}

	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}

}
