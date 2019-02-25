package com.nenu.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Group {
	@Id
	@GeneratedValue
	private int id;//ID 数据分析ID
	private String attribute;//属性
	private double newData;//新值
	private double oldData;//对比值
	private String groupper;//增长率
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public double getNewData() {
		return newData;
	}
	public void setNewData(double newData) {
		this.newData = newData;
	}
	public double getOldData() {
		return oldData;
	}
	public void setOldData(double oldData) {
		this.oldData = oldData;
	}
	public String getGroupper() {
		return groupper;
	}
	public void setGroupper(String groupper) {
		this.groupper = groupper;
	}
	
	
}
