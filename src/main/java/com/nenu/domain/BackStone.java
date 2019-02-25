package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class BackStone implements Serializable {
	
	private static final long serialVersionUID = 8808571458583200325L;
	@Id
	@GeneratedValue
	private int back_ID;//ID
	private Date back_time;//退石时间
	private long back_procodeNo;//退石订单号
	private String back_content;//退石记录
	private String back_process;//生成订单的记录
	private String back_stoneNo;//石编
	public int getBack_ID() {
		return back_ID;
	}
	public void setBack_ID(int back_ID) {
		this.back_ID = back_ID;
	}
	public Date getBack_time() {
		return back_time;
	}
	public void setBack_time(Date back_time) {
		this.back_time = back_time;
	}
	
	public long getBack_procodeNo() {
		return back_procodeNo;
	}
	public void setBack_procodeNo(long back_procodeNo) {
		this.back_procodeNo = back_procodeNo;
	}
	public String getBack_content() {
		return back_content;
	}
	public void setBack_content(String back_content) {
		this.back_content = back_content;
	}
	public String getBack_process() {
		return back_process;
	}
	public void setBack_process(String back_process) {
		this.back_process = back_process;
	}
	public String getBack_stoneNo() {
		return back_stoneNo;
	}
	public void setBack_stoneNo(String back_stoneNo) {
		this.back_stoneNo = back_stoneNo;
	}
	

}
