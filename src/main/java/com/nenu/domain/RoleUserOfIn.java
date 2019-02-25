package com.nenu.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class RoleUserOfIn implements Serializable {


	private static final long serialVersionUID = -7141593933080554713L;

	@Id
	@GeneratedValue
	private int id;//ID

	private int role_user;//钻石流转角色I
	private String role_type;//钻石流转角色等级

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRole_user() {
		return role_user;
	}

	public void setRole_user(int role_user) {
		this.role_user = role_user;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	

	

}
