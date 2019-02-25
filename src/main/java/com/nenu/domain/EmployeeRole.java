package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class EmployeeRole implements Serializable {


	private static final long serialVersionUID = 3817778797842807925L;
	@Id
	@GeneratedValue
	private int id; //id
	private int emp_id;//数据分析角色权限ID
	private String emp_shownum;//数据分析角色权限序号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	
	public String getEmp_shownum() {
		return emp_shownum;
	}
	public void setEmp_shownum(String emp_shownum) {
		this.emp_shownum = emp_shownum;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{id="+id+",emp_id="+emp_id+",emp_shownum="+emp_shownum+"}";
	}
	
	
}
