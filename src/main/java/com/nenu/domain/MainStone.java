package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class MainStone implements Serializable {
	
	private static final long serialVersionUID = -4078528714171127471L;
	@Id
	@GeneratedValue
	private int mainStone_ID;//id
	private double mainStone_start;//主石区间上限
	private double mainStone_end;//主石区间下限
	public int getMainStone_ID() {
		return mainStone_ID;
	}
	public void setMainStone_ID(int mainStone_ID) {
		this.mainStone_ID = mainStone_ID;
	}
	public double getMainStone_start() {
		return mainStone_start;
	}
	public void setMainStone_start(double mainStone_start) {
		this.mainStone_start = mainStone_start;
	}
	public double getMainStone_end() {
		return mainStone_end;
	}
	public void setMainStone_end(double mainStone_end) {
		this.mainStone_end = mainStone_end;
	}
	
	

}
