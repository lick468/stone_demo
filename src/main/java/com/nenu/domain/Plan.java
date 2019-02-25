package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Plan implements Serializable{
	private static final long serialVersionUID = 8808571458583200325L;
	@Id
	@GeneratedValue
	private int plan_ID;//ID
	private Date plan_start;//开始时间
	private Date plan_end;//结束时间
	private String plan_room;//门店,库房
	private String plan_counter;//柜台
	private String plan_area;//区域
	private String plan_belong;//统计对象
	private float plan_num;//计划任务
	private String plan_index;//指标
	private String plan_content;//备注
	private String plan_success;//完成情况
	private String plan_text1;//完成情况
	private String plan_text2;//完成情况
	private String plan_text3;//完成情况
	private float plan_diff;//差额
	private float plan_do;//完成额度
	
	public Plan(Date plan_start,Date plan_end,String plan_room,String plan_belong,float plan_num,String plan_index) {
		
		this.plan_start = plan_start;
		this.plan_end = plan_end;
		this.plan_room = plan_room;
		this.plan_belong = plan_belong;
		this.plan_num = plan_num;
		this.plan_index = plan_index;			
	}
	public Plan() {
		
	}
	
	
	public float getPlan_do() {
		return plan_do;
	}
	public void setPlan_do(float plan_do) {
		this.plan_do = plan_do;
	}
	public float getPlan_diff() {
		return plan_diff;
	}
	public void setPlan_diff(float plan_diff) {
		this.plan_diff = plan_diff;
	}
	public int getPlan_ID() {
		return plan_ID;
	}
	public void setPlan_ID(int plan_ID) {
		this.plan_ID = plan_ID;
	}
	public Date getPlan_start() {
		return plan_start;
	}
	public void setPlan_start(Date plan_start) {
		this.plan_start = plan_start;
	}
	public Date getPlan_end() {
		return plan_end;
	}
	public void setPlan_end(Date plan_end) {
		this.plan_end = plan_end;
	}
	public String getPlan_room() {
		return plan_room;
	}
	public void setPlan_room(String plan_room) {
		this.plan_room = plan_room;
	}
	public String getPlan_counter() {
		return plan_counter;
	}
	public void setPlan_counter(String plan_counter) {
		this.plan_counter = plan_counter;
	}
	public String getPlan_area() {
		return plan_area;
	}
	public void setPlan_area(String plan_area) {
		this.plan_area = plan_area;
	}
	public float getPlan_num() {
		return plan_num;
	}
	public void setPlan_num(float plan_num) {
		this.plan_num = plan_num;
	}
	public String getPlan_content() {
		return plan_content;
	}
	public void setPlan_content(String plan_content) {
		this.plan_content = plan_content;
	}
	public String getPlan_success() {
		return plan_success;
	}
	public void setPlan_success(String plan_success) {
		this.plan_success = plan_success;
	}
	public String getPlan_text1() {
		return plan_text1;
	}
	public void setPlan_text1(String plan_text1) {
		this.plan_text1 = plan_text1;
	}
	public String getPlan_text2() {
		return plan_text2;
	}
	public void setPlan_text2(String plan_text2) {
		this.plan_text2 = plan_text2;
	}
	public String getPlan_text3() {
		return plan_text3;
	}
	public void setPlan_text3(String plan_text3) {
		this.plan_text3 = plan_text3;
	}
	


	public String getPlan_index() {
		return plan_index;
	}


	public void setPlan_index(String plan_index) {
		this.plan_index = plan_index;
	}


	public String getPlan_belong() {
		return plan_belong;
	}


	public void setPlan_belong(String plan_belong) {
		this.plan_belong = plan_belong;
	}
	
	
	
}
