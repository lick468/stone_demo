package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Employee  implements Serializable {
	
	private static final long serialVersionUID = 7070281488316196093L;
	@Id
	@GeneratedValue
	private int id;//数据分析角色权限ID
	private String emp_name;//数据分析角色名称
	private String emp_password;//数据分析角色密码
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getEmp_password() {
		return emp_password;
	}
	public void setEmp_password(String emp_password) {
		this.emp_password = emp_password;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{id="+id+",emp_name"+emp_name+",emp_password"+emp_password+"}";
	}
	
	

}
