package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Supplier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9047526949298952908L;

	@Id
	@GeneratedValue
	private int supplier_ID;//供应商ID
	private String supplier_name;//供应商名称

	public int getSupplier_ID() {
		return supplier_ID;
	}

	public void setSupplier_ID(int supplier_ID) {
		this.supplier_ID = supplier_ID;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
