package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Stoninproc implements Serializable {

	private static final long serialVersionUID = -2882215366071482579L;
	
	
	@Id
	private String stoninproc_ID;//主键
	private int stoninproc_seqno;//序号
	private String stoninproc_batch;//批次
	private String stoninproc_stoneNo;//石头编号
	private double stoninproc_stoneWeight;//石头重量
	private String stoninproc_stoneColor;//石头颜色
	private String stoninproc_stoneClarity;//石头净度
	private String stoninproc_stoneShape;//石头形状
	private String stoninproc_stoneContext;//备注信息
	private String stoninproc_supplier;//供应商
	
	private long stoninproc_procordNo;//订单号
	private int stoninproc_number;//石头数量
	private double stoninproc_subpay;//石头单个金额(货款金额)
	private int stoninproc_stoneState;//石头状态    1正在加工   0 加工完毕成品返回  2退石
	
	private Date orderDate;//订单表生成日期
	private Date chukuDate;//订单表出库日期
	
	private int mainNumber;//供应商主石总数
	private int subNumber;//供应商副石总数
	private double mainWeight;//供应商主石总重
	private double subWeight;//供应商副石总重
	
	
	
	
	
	
	public String getStoninproc_supplier() {
		return stoninproc_supplier;
	}
	public void setStoninproc_supplier(String stoninproc_supplier) {
		this.stoninproc_supplier = stoninproc_supplier;
	}
	public String getStoninproc_stoneContext() {
		return stoninproc_stoneContext;
	}
	public void setStoninproc_stoneContext(String stoninproc_stoneContext) {
		this.stoninproc_stoneContext = stoninproc_stoneContext;
	}
	public int getStoninproc_stoneState() {
		return stoninproc_stoneState;
	}
	public void setStoninproc_stoneState(int stoninproc_stoneState) {
		this.stoninproc_stoneState = stoninproc_stoneState;
	}
	public double getStoninproc_stoneWeight() {
		return stoninproc_stoneWeight;
	}
	public void setStoninproc_stoneWeight(double stoninproc_stoneWeight) {
		this.stoninproc_stoneWeight = stoninproc_stoneWeight;
	}
	public String getStoninproc_stoneColor() {
		return stoninproc_stoneColor;
	}
	public void setStoninproc_stoneColor(String stoninproc_stoneColor) {
		this.stoninproc_stoneColor = stoninproc_stoneColor;
	}
	public String getStoninproc_stoneClarity() {
		return stoninproc_stoneClarity;
	}
	public void setStoninproc_stoneClarity(String stoninproc_stoneClarity) {
		this.stoninproc_stoneClarity = stoninproc_stoneClarity;
	}
	public String getStoninproc_stoneShape() {
		return stoninproc_stoneShape;
	}
	public void setStoninproc_stoneShape(String stoninproc_stoneShape) {
		this.stoninproc_stoneShape = stoninproc_stoneShape;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getChukuDate() {
		return chukuDate;
	}
	public void setChukuDate(Date chukuDate) {
		this.chukuDate = chukuDate;
	}
	
	public String getStoninproc_ID() {
		return stoninproc_ID;
	}
	public void setStoninproc_ID(String stoninproc_ID) {
		this.stoninproc_ID = stoninproc_ID;
	}
	public int getStoninproc_seqno() {
		return stoninproc_seqno;
	}
	public void setStoninproc_seqno(int stoninproc_seqno) {
		this.stoninproc_seqno = stoninproc_seqno;
	}
	public String getStoninproc_stoneNo() {
		return stoninproc_stoneNo;
	}
	public void setStoninproc_stoneNo(String stoninproc_stoneNo) {
		this.stoninproc_stoneNo = stoninproc_stoneNo;
	}
	public long getStoninproc_procordNo() {
		return stoninproc_procordNo;
	}
	public void setStoninproc_procordNo(long stoninproc_procordNo) {
		this.stoninproc_procordNo = stoninproc_procordNo;
	}
	public int getStoninproc_number() {
		return stoninproc_number;
	}
	public void setStoninproc_number(int stoninproc_number) {
		this.stoninproc_number = stoninproc_number;
	}
	public double getStoninproc_subpay() {
		return stoninproc_subpay;
	}
	public void setStoninproc_subpay(double stoninproc_subpay) {
		this.stoninproc_subpay = stoninproc_subpay;
	}
	public int getMainNumber() {
		return mainNumber;
	}
	public void setMainNumber(int mainNumber) {
		this.mainNumber = mainNumber;
	}
	public int getSubNumber() {
		return subNumber;
	}
	public void setSubNumber(int subNumber) {
		this.subNumber = subNumber;
	}
	public double getMainWeight() {
		return mainWeight;
	}
	public void setMainWeight(double mainWeight) {
		this.mainWeight = mainWeight;
	}
	public double getSubWeight() {
		return subWeight;
	}
	public void setSubWeight(double subWeight) {
		this.subWeight = subWeight;
	}
	public String getStoninproc_batch() {
		return stoninproc_batch;
	}
	public void setStoninproc_batch(String stoninproc_batch) {
		this.stoninproc_batch = stoninproc_batch;
	}
	

	
}
