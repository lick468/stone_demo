package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class ListPrice  implements Serializable {
	
	private static final long serialVersionUID = -4658408938260880691L;
	@Id
	@GeneratedValue
	private int listPrice_ID;//id
	private double listPrice_start;//标价区间上限
	private double listPrice_end;//标价区间下限
	public int getListPrice_ID() {
		return listPrice_ID;
	}
	public void setListPrice_ID(int listPrice_ID) {
		this.listPrice_ID = listPrice_ID;
	}
	public double getListPrice_start() {
		return listPrice_start;
	}
	public void setListPrice_start(double listPrice_start) {
		this.listPrice_start = listPrice_start;
	}
	public double getListPrice_end() {
		return listPrice_end;
	}
	public void setListPrice_end(double listPrice_end) {
		this.listPrice_end = listPrice_end;
	}
	
}
