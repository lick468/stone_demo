package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class StoneCopy implements Serializable {

	private static final long serialVersionUID = 3210451543616240966L;
	@Id
	private String stone_ID; // ID
	private Date stone_purchdate;//
	private int stone_seqno;//
	private String stone_loosdofty;//
	private String stone_supplierName;
	private String stone_mainName;//
	private String stone_mainStoneNo;//
	private String stone_spec;//
	private int stone_mainNumber;
	private double stone_mainWeight;//
	private double stone_mainPrperct;//
	private double stone_mainPramt;//

	private String stone_substoNo;//
	private String stone_substoName;//
	private double stone_substoWgt;//
	private int stone_substoNumber;
	private double stone_substoPrperct;//
	private double stone_substoPramt;//

	private String stone_shape;//
	private String stone_color;//
	private String stone_clarity;//
	private String stone_color2;//
	private String stone_clarity2;//
	private String stone_cut;//
	private String stone_fluorescence;//
	private String stone_symmetry;//
	private String stone_polish;//
	private double stone_diameter;//
	private double stone_height;//
	private double stone_tablewidth;//
	private String stone_preparer;
	private String stone_porter;
	private String stone_comments;//
	private String stone_channelNo;
	private int stone_reserved1;//是否退石
	private String stone_reserved2;//
	private String stone_GIA;//
	

	public StoneCopy() {

	}

	public StoneCopy(String stone_ID, Date stone_purchdate, int stone_seqno, String stone_loosdofty,
			String stone_mainName, String stone_mainStoneNo, String stone_spec,
			int stone_mainNumber, double stone_mainWeight, double stone_mainPrperct, double stone_mainPramt,
			String stone_substoNo, String stone_substoName, double stone_substoWgt, int stone_substoNumber,
			double stone_substoPrperct, double stone_substoPramt, String stone_shape, String stone_color,
			String stone_clarity, String stone_color2, String stone_clarity2, String stone_cut,
			String stone_fluorescence, String stone_symmetry, String stone_polish, double stone_diameter,
			double stone_height, double stone_tablewidth, String stone_preparer, String stone_porter,
			String stone_comments, String stone_channelNo, int stone_reserved1,String stone_GIA) {
		this.stone_ID = stone_ID;
		this.stone_purchdate = stone_purchdate;
		this.stone_seqno = stone_seqno;
		this.stone_loosdofty = stone_loosdofty;
		
		this.stone_mainName = stone_mainName;
		this.stone_mainStoneNo = stone_mainStoneNo;
		this.stone_spec = stone_spec;
		this.stone_mainNumber = stone_mainNumber;
		this.stone_mainWeight = stone_mainWeight;
		this.stone_mainPrperct = stone_mainPrperct;
		this.stone_mainPramt = stone_mainPramt;
		this.stone_substoNo = stone_substoNo;
		this.stone_substoName = stone_substoName;
		this.stone_substoWgt = stone_substoWgt;
		this.stone_substoNumber = stone_substoNumber;
		this.stone_substoPrperct = stone_substoPrperct;
		this.stone_substoPramt = stone_substoPramt;
		this.stone_shape = stone_shape;
		this.stone_color = stone_color;
		this.stone_clarity = stone_clarity;
		this.stone_color2 = stone_color2;
		this.stone_clarity2 = stone_clarity2;
		this.stone_cut = stone_cut;
		this.stone_fluorescence = stone_fluorescence;
		this.stone_symmetry = stone_symmetry;
		this.stone_polish = stone_polish;
		this.stone_diameter = stone_diameter;
		this.stone_height = stone_height;
		this.stone_comments = stone_comments;
		this.stone_preparer = stone_preparer;
		this.stone_porter = stone_porter;
		this.stone_tablewidth = stone_tablewidth;
		this.stone_channelNo = stone_channelNo;
		this.stone_reserved1 = stone_reserved1;
		this.stone_GIA = stone_GIA;

	}

	public String getStone_GIA() {
		return stone_GIA;
	}

	public void setStone_GIA(String stone_GIA) {
		this.stone_GIA = stone_GIA;
	}

	public String getStone_ID() {
		return stone_ID;
	}

	public void setStone_ID(String stone_ID) {
		this.stone_ID = stone_ID;
	}

	public Date getStone_purchdate() {
		return stone_purchdate;
	}

	public void setStone_purchdate(Date stone_purchdate) {
		this.stone_purchdate = stone_purchdate;
	}

	public int getStone_seqno() {
		return stone_seqno;
	}

	public void setStone_seqno(int stone_seqno) {
		this.stone_seqno = stone_seqno;
	}

	public String getStone_loosdofty() {
		return stone_loosdofty;
	}

	public void setStone_loosdofty(String stone_loosdofty) {
		this.stone_loosdofty = stone_loosdofty;
	}

	public String getStone_supplierName() {
		return stone_supplierName;
	}

	public void setStone_supplierName(String stone_supplierName) {
		this.stone_supplierName = stone_supplierName;
	}

	public String getStone_mainName() {
		return stone_mainName;
	}

	public void setStone_mainName(String stone_mainName) {
		this.stone_mainName = stone_mainName;
	}

	public String getStone_spec() {
		return stone_spec;
	}

	public void setStone_spec(String stone_spec) {
		this.stone_spec = stone_spec;
	}

	public int getStone_mainNumber() {
		return stone_mainNumber;
	}

	public void setStone_mainNumber(int stone_mainNumber) {
		this.stone_mainNumber = stone_mainNumber;
	}

	public double getStone_mainWeight() {
		return stone_mainWeight;
	}

	public void setStone_mainWeight(double stone_mainWeight) {
		this.stone_mainWeight = stone_mainWeight;
	}

	public double getStone_mainPrperct() {
		return stone_mainPrperct;
	}

	public void setStone_mainPrperct(double stone_mainPrperct) {
		this.stone_mainPrperct = stone_mainPrperct;
	}

	public double getStone_mainPramt() {
		return stone_mainPramt;
	}

	public void setStone_mainPramt(double stone_mainPramt) {
		this.stone_mainPramt = stone_mainPramt;
	}

	public String getStone_substoName() {
		return stone_substoName;
	}

	public void setStone_substoName(String stone_substoName) {
		this.stone_substoName = stone_substoName;
	}

	public double getStone_substoWgt() {
		return stone_substoWgt;
	}

	public void setStone_substoWgt(double stone_substoWgt) {
		this.stone_substoWgt = stone_substoWgt;
	}

	public int getStone_substoNumber() {
		return stone_substoNumber;
	}

	public void setStone_substoNumber(int stone_substoNumber) {
		this.stone_substoNumber = stone_substoNumber;
	}

	public double getStone_substoPrperct() {
		return stone_substoPrperct;
	}

	public void setStone_substoPrperct(double stone_substoPrperct) {
		this.stone_substoPrperct = stone_substoPrperct;
	}

	public double getStone_substoPramt() {
		return stone_substoPramt;
	}

	public void setStone_substoPramt(double stone_substoPramt) {
		this.stone_substoPramt = stone_substoPramt;
	}

	public String getStone_shape() {
		return stone_shape;
	}

	public void setStone_shape(String stone_shape) {
		this.stone_shape = stone_shape;
	}

	public String getStone_color() {
		return stone_color;
	}

	public void setStone_color(String stone_color) {
		this.stone_color = stone_color;
	}

	public String getStone_clarity() {
		return stone_clarity;
	}

	public void setStone_clarity(String stone_clarity) {
		this.stone_clarity = stone_clarity;
	}

	public String getStone_color2() {
		return stone_color2;
	}

	public void setStone_color2(String stone_color2) {
		this.stone_color2 = stone_color2;
	}

	public String getStone_clarity2() {
		return stone_clarity2;
	}

	public void setStone_clarity2(String stone_clarity2) {
		this.stone_clarity2 = stone_clarity2;
	}

	public String getStone_cut() {
		return stone_cut;
	}

	public void setStone_cut(String stone_cut) {
		this.stone_cut = stone_cut;
	}

	public String getStone_fluorescence() {
		return stone_fluorescence;
	}

	public void setStone_fluorescence(String stone_fluorescence) {
		this.stone_fluorescence = stone_fluorescence;
	}

	public String getStone_symmetry() {
		return stone_symmetry;
	}

	public void setStone_symmetry(String stone_symmetry) {
		this.stone_symmetry = stone_symmetry;
	}

	public String getStone_polish() {
		return stone_polish;
	}

	public void setStone_polish(String stone_polish) {
		this.stone_polish = stone_polish;
	}

	public double getStone_diameter() {
		return stone_diameter;
	}

	public void setStone_diameter(double stone_diameter) {
		this.stone_diameter = stone_diameter;
	}

	public double getStone_height() {
		return stone_height;
	}

	public void setStone_height(double stone_height) {
		this.stone_height = stone_height;
	}

	public double getStone_tablewidth() {
		return stone_tablewidth;
	}

	public void setStone_tablewidth(double stone_tablewidth) {
		this.stone_tablewidth = stone_tablewidth;
	}

	public String getStone_preparer() {
		return stone_preparer;
	}

	public void setStone_preparer(String stone_preparer) {
		this.stone_preparer = stone_preparer;
	}

	public String getStone_porter() {
		return stone_porter;
	}

	public void setStone_porter(String stone_porter) {
		this.stone_porter = stone_porter;
	}

	public String getStone_comments() {
		return stone_comments;
	}

	public void setStone_comments(String stone_comments) {
		this.stone_comments = stone_comments;
	}

	public String getStone_channelNo() {
		return stone_channelNo;
	}

	public void setStone_channelNo(String stone_channelNo) {
		this.stone_channelNo = stone_channelNo;
	}

	public int getStone_reserved1() {
		return stone_reserved1;
	}

	public void setStone_reserved1(int stone_reserved1) {
		this.stone_reserved1 = stone_reserved1;
	}

	public String getStone_reserved2() {
		return stone_reserved2;
	}

	public void setStone_reserved2(String stone_reserved2) {
		this.stone_reserved2 = stone_reserved2;
	}

	public String getStone_mainStoneNo() {
		return stone_mainStoneNo;
	}

	public void setStone_mainStoneNo(String stone_mainStoneNo) {
		this.stone_mainStoneNo = stone_mainStoneNo;
	}

	public String getStone_substoNo() {
		return stone_substoNo;
	}

	public void setStone_substoNo(String stone_substoNo) {
		this.stone_substoNo = stone_substoNo;
	}
	

}
