package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class SupplierStone implements Serializable {

	private static final long serialVersionUID = 4625931196965725775L;
	
	@Id
	@GeneratedValue
	private int id;
	private String supplier_name;//供应商
	private Date supplier_purchdate;//采购日期
	private long supplier_procorNo;//订单号
	
	private String supplier_mainStoneNo;//主石编
	private int supplier_mainNumber;//主石数
	private double supplier_mainWeight;//主石

	private String supplier_subStoneNo;//副石编
	private double supplier_subWeight;//副石
	private int supplier_subNumber;//副石数
	
	
	private String supplier_delyman;//送货人
	private String supplier_preparer;//经手人
	private String supplier_porter;//接货人
	
	private int mainNumber;//供应商主石总数
	private int subNumber;//供应商副石总数
	private double mainWeight;//供应商主石总重
	private double subWeight;//供应商副石总重
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public Date getSupplier_purchdate() {
		return supplier_purchdate;
	}
	public void setSupplier_purchdate(Date supplier_purchdate) {
		this.supplier_purchdate = supplier_purchdate;
	}
	public long getSupplier_procorNo() {
		return supplier_procorNo;
	}
	public void setSupplier_procorNo(long supplier_procorNo) {
		this.supplier_procorNo = supplier_procorNo;
	}
	public String getSupplier_mainStoneNo() {
		return supplier_mainStoneNo;
	}
	public void setSupplier_mainStoneNo(String supplier_mainStoneNo) {
		this.supplier_mainStoneNo = supplier_mainStoneNo;
	}
	public int getSupplier_mainNumber() {
		return supplier_mainNumber;
	}
	public void setSupplier_mainNumber(int supplier_mainNumber) {
		this.supplier_mainNumber = supplier_mainNumber;
	}
	public double getSupplier_mainWeight() {
		return supplier_mainWeight;
	}
	public void setSupplier_mainWeight(double supplier_mainWeight) {
		this.supplier_mainWeight = supplier_mainWeight;
	}
	public String getSupplier_subStoneNo() {
		return supplier_subStoneNo;
	}
	public void setSupplier_subStoneNo(String supplier_subStoneNo) {
		this.supplier_subStoneNo = supplier_subStoneNo;
	}
	public double getSupplier_subWeight() {
		return supplier_subWeight;
	}
	public void setSupplier_subWeight(double supplier_subWeight) {
		this.supplier_subWeight = supplier_subWeight;
	}
	public int getSupplier_subNumber() {
		return supplier_subNumber;
	}
	public void setSupplier_subNumber(int supplier_subNumber) {
		this.supplier_subNumber = supplier_subNumber;
	}
	public String getSupplier_delyman() {
		return supplier_delyman;
	}
	public void setSupplier_delyman(String supplier_delyman) {
		this.supplier_delyman = supplier_delyman;
	}
	public String getSupplier_preparer() {
		return supplier_preparer;
	}
	public void setSupplier_preparer(String supplier_preparer) {
		this.supplier_preparer = supplier_preparer;
	}
	public String getSupplier_porter() {
		return supplier_porter;
	}
	public void setSupplier_porter(String supplier_porter) {
		this.supplier_porter = supplier_porter;
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
	
	

}
