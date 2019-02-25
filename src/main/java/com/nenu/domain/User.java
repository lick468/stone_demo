package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882215366071482579L;

	@Id
	@GeneratedValue
	private int user_ID;//角色ID

	private String user_name;//角色姓名
	private String user_password;//角色密码
	private int user_role;

	public int getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(int user_ID) {
		this.user_ID = user_ID;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public int getUser_role() {
		return user_role;
	}

	public void setUser_role(int user_role) {
		this.user_role = user_role;
	}

	public String toString() {
		return "User{user_ID:" + user_ID + ",user_name:" + user_name + ",user_password:" + user_password + ",user_role:"
				+ user_role + "}";
	}

}
