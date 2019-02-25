package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class SettlementPrice  implements Serializable {

	private static final long serialVersionUID = 9200229802256098984L;
	@Id
	@GeneratedValue
	private int settlementPrice_ID;//id
	private double settlementPrice_start;//结算价区间上限
	private double settlementPrice_end;//结算价区间下限
	public int getSettlementPrice_ID() {
		return settlementPrice_ID;
	}
	public void setSettlementPrice_ID(int settlementPrice_ID) {
		this.settlementPrice_ID = settlementPrice_ID;
	}
	public double getSettlementPrice_start() {
		return settlementPrice_start;
	}
	public void setSettlementPrice_start(double settlementPrice_start) {
		this.settlementPrice_start = settlementPrice_start;
	}
	public double getSettlementPrice_end() {
		return settlementPrice_end;
	}
	public void setSettlementPrice_end(double settlementPrice_end) {
		this.settlementPrice_end = settlementPrice_end;
	}
	
	

}
