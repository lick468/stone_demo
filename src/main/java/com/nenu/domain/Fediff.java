package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Fediff implements Serializable {

	
	private static final long serialVersionUID = -2882215366071482579L;
	
	
	@Id
	@GeneratedValue
	private long fediff_ID;//ID
	private String fediff_stoneNo;//石编
	private String fediff_batch;//批次
	private long fediff_procordNo;//订单号
	private double fediff_weightDiff;//入料差
	
	private Date fediff_time;//时间
	
	public Date getFediff_time() {
		return fediff_time;
	}
	public void setFediff_time(Date fediff_time) {
		this.fediff_time = fediff_time;
	}
	public long getFediff_ID() {
		return fediff_ID;
	}
	public void setFediff_ID(long fediff_ID) {
		this.fediff_ID = fediff_ID;
	}
	public String getFediff_stoneNo() {
		return fediff_stoneNo;
	}
	public void setFediff_stoneNo(String fediff_stoneNo) {
		this.fediff_stoneNo = fediff_stoneNo;
	}
	public long getFediff_procordNo() {
		return fediff_procordNo;
	}
	public void setFediff_procordNo(long fediff_procordNo) {
		this.fediff_procordNo = fediff_procordNo;
	}
	public double getFediff_weightDiff() {
		return fediff_weightDiff;
	}
	public void setFediff_weightDiff(double fediff_weightDiff) {
		this.fediff_weightDiff = fediff_weightDiff;
	}
	public String getFediff_batch() {
		return fediff_batch;
	}
	public void setFediff_batch(String fediff_batch) {
		this.fediff_batch = fediff_batch;
	}
	
	
	
}
