package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class TBackStone implements Serializable{

	private static final long serialVersionUID = 1568280959857419564L;
	@Id
	@GeneratedValue
	private int tback_ID;//ID
	private Date tback_time;//退石时间
	private String tback_content;//退石记录
	private String tback_stoneNo;//石编
	public int getTback_ID() {
		return tback_ID;
	}
	public void setTback_ID(int tback_ID) {
		this.tback_ID = tback_ID;
	}
	public Date getTback_time() {
		return tback_time;
	}
	public void setTback_time(Date tback_time) {
		this.tback_time = tback_time;
	}
	public String getTback_content() {
		return tback_content;
	}
	public void setTback_content(String tback_content) {
		this.tback_content = tback_content;
	}
	public String getTback_stoneNo() {
		return tback_stoneNo;
	}
	public void setTback_stoneNo(String tback_stoneNo) {
		this.tback_stoneNo = tback_stoneNo;
	}
	
}
