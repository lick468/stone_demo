package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Procord implements Serializable {

	private static final long serialVersionUID = -2882215366071482579L;
	
	
	@Id
	@GeneratedValue
	private int procord_ID;//ID
	private long procord_no;//订单号
	private int procord_status;//状态
	private String procord_supplier;//供应商
	private String procord_batch;//批次
	private Date procord_date;//订单日期
	private Date procord_delydate;//出货日期
	private int procord_goodscount;//石头数量 ,货品数量，以石头编号计数
	private int procord_backcount;//回库的数量
	private double procord_pay;//货款金额
	private double procord_weight;//石重
	private String procord_delyman;//送货人
	private String procord_preparer;//经手人
	private String procord_porter;//接货人
	public int getProcord_ID() {
		return procord_ID;
	}
	public void setProcord_ID(int procord_ID) {
		this.procord_ID = procord_ID;
	}
	public long getProcord_no() {
		return procord_no;
	}
	public void setProcord_no(long procord_no) {
		this.procord_no = procord_no;
	}
	
	public double getProcord_weight() {
		return procord_weight;
	}
	public void setProcord_weight(double procord_weight) {
		this.procord_weight = procord_weight;
	}
	public int getProcord_status() {
		return procord_status;
	}
	public void setProcord_status(int procord_status) {
		this.procord_status = procord_status;
	}
	public String getProcord_supplier() {
		return procord_supplier;
	}
	public void setProcord_supplier(String procord_supplier) {
		this.procord_supplier = procord_supplier;
	}
	public String getProcord_batch() {
		return procord_batch;
	}
	public void setProcord_batch(String procord_batch) {
		this.procord_batch = procord_batch;
	}
	public Date getProcord_date() {
		return procord_date;
	}
	public void setProcord_date(Date procord_date) {
		this.procord_date = procord_date;
	}
	public Date getProcord_delydate() {
		return procord_delydate;
	}
	public void setProcord_delydate(Date procord_delydate) {
		this.procord_delydate = procord_delydate;
	}
	public int getProcord_goodscount() {
		return procord_goodscount;
	}
	public void setProcord_goodscount(int procord_goodscount) {
		this.procord_goodscount = procord_goodscount;
	}
	public double getProcord_pay() {
		return procord_pay;
	}
	public void setProcord_pay(double procord_pay) {
		this.procord_pay = procord_pay;
	}
	public String getProcord_delyman() {
		return procord_delyman;
	}
	public void setProcord_delyman(String procord_delyman) {
		this.procord_delyman = procord_delyman;
	}
	public String getProcord_preparer() {
		return procord_preparer;
	}
	public void setProcord_preparer(String procord_preparer) {
		this.procord_preparer = procord_preparer;
	}
	public String getProcord_porter() {
		return procord_porter;
	}
	public void setProcord_porter(String procord_porter) {
		this.procord_porter = procord_porter;
	}
	public int getProcord_backcount() {
		return procord_backcount;
	}
	public void setProcord_backcount(int procord_backcount) {
		this.procord_backcount = procord_backcount;
	}
	

	
}
