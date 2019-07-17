package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class FinpordCopy implements Serializable {

	private static final long serialVersionUID = 732320922769446413L;
	@Id
	private String finpord_ID;//ID
	@GeneratedValue
	private int    finpord_seqno;//序号
	private long   finpord_procordNo;//单号
	private String finpord_barcode;//条码
	private Date   finpord_inboundate;//入库日期
	private String finpord_supplier; //供应商
	private String finpord_styno;//款号
	private String finpord_quality; //成色
	private String finpord_name;//名称
	private int    finpord_ringsz;//圈口
	private float    finpord_multi;//倍率
	private double finpord_tagedpr;//标价

	private double finpord_goldwgt;//金重
	private double finpord_merchwgt;//货重
	private String finpord_counter;//柜台
	private String finpord_catagory;//品类
	private String finpord_goldcolor;//金料颜色
	private double finpord_baslabcost;//基本工费
	private double finpord_othrcost;//其它费用
	private double finpord_salabcost;//销售工费
	private double finpord_goldpr;//金价
	private double finpord_goldlos;//金损耗
	private double finpord_cost;//成本
	private String finpord_armsty;//戒指壁样式

	private String finpord_storname;//库房（门店）
	private String finpord_entsdno;//企业标准号
	private String finpord_comments;//备注
	private String finpord_mainstono;//主石编
	private String finpord_mainstoname;//主石名
	private double finpord_mainstowgt;//主石
	private int    finpord_mainstoqty;//主石数
	private double finpord_mainstopr;//主石价
	private String finpord_cert1;//证书号
	private String finpord_cert2;//证书2
	private String finpord_gia1;//GIA证书
	private String finpord_gia2;//GIA证书2

	private String finpord_mainstoshape;//形状
	private String finpord_series;//系列
	private String finpord_cut1;//切工
	private String finpord_symmetry1;//对称性
	private String finpord_polish1;//抛光
	private String finpord_fluorescence1;//荧光
	private String finpord_clarity1;//级别
	private String finpord_color1;//颜色
	private String finpord_cut2;//切工2
	private String finpord_symmetry2;//对称性2
	private String finpord_polish2;//抛光2
	private String finpord_fluorescence2;//荧光2

	private String finpord_clarity2;//级别2
	private String finpord_color2;//颜色2
	private String finpord_substo1no;//副石编
	private String finpord_substo1name;//副石名
	private double finpord_substo1wgt;//副石
	private int    finpord_substo1qty;//副石数
	private double finpord_substo1pr;//副石价
	private String finpord_substo2no;//副石2编
	private String finpord_substo2name;//副石2名
	private double finpord_substo2wgt;//副石2重
	private int    finpord_substo2qty;//副石2数
	private double finpord_substo2pr;//副石2价

	private String finpord_substo3no;//副石3编
	private String finpord_substo3name;//副石3名
	private double finpord_substo3wgt;//副石3重
	private int    finpord_substo3qty;//副石3数
	private double finpord_substo3pr;//副石3价
	private int    finpord_reserved1;//备注1
	private String finpord_reserved2;//备注2
	
	private double profit;//利润
	private double mainStoneCost;//主石成本
	private double subStoneCost;//副石成本
	private double materialCost;//物料成本
	
	
	
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public double getMainStoneCost() {
		return mainStoneCost;
	}
	public void setMainStoneCost(double mainStoneCost) {
		this.mainStoneCost = mainStoneCost;
	}
	public double getSubStoneCost() {
		return subStoneCost;
	}
	public void setSubStoneCost(double subStoneCost) {
		this.subStoneCost = subStoneCost;
	}
	public double getMaterialCost() {
		return materialCost;
	}
	public void setMaterialCost(double materialCost) {
		this.materialCost = materialCost;
	}
	public FinpordCopy() {
		
	} 
	public FinpordCopy( String finpord_ID,int finpord_seqno, long finpord_procordNo, String finpord_barcode, Date finpord_inboundate, String finpord_supplier, String finpord_styno, String finpord_quality, String finpord_name, int finpord_ringsz, float finpord_multi, double finpord_tagedpr, double finpord_goldwgt, double finpord_merchwgt, String finpord_counter, String finpord_catagory,
			String finpord_goldcolor, double finpord_baslabcost, double finpord_othrcost, double finpord_salabcost,double finpord_goldpr, double finpord_goldlos, double finpord_cost, String finpord_armsty, String finpord_storname, String finpord_entsdno, String finpord_comments, String finpord_mainstono, String finpord_mainstoname, double finpord_mainstowgt, int finpord_mainstoqty, 
			double finpord_mainstopr, String finpord_cert1, String finpord_cert2, String finpord_gia1, String finpord_gia2, String finpord_mainstoshape, String finpord_series, String finpord_cut1, String finpord_symmetry1, String finpord_polish1, String finpord_fluorescence1, String finpord_clarity1, String finpord_color1, String finpord_cut2, String finpord_symmetry2, String finpord_polish2,
			String finpord_fluorescence2, String finpord_clarity2, String finpord_color2, String finpord_substo1no, String finpord_substo1name, double finpord_substo1wgt, int finpord_substo1qty, double finpord_substo1pr, String finpord_substo2no, String finpord_substo2name, double finpord_substo2wgt, int finpord_substo2qty, double finpord_substo2pr,String finpord_substo3no, String finpord_substo3name,
			double finpord_substo3wgt, int finpord_substo3qty, double finpord_substo3pr,int finpord_reserved1,String finpord_reserved2) {
		this.finpord_ID = finpord_ID;

		this.finpord_seqno = finpord_seqno;

		this.finpord_procordNo = finpord_procordNo;

		this.finpord_barcode = finpord_barcode;

		this.finpord_inboundate = finpord_inboundate;

		this.finpord_supplier = finpord_supplier;

		this.finpord_styno = finpord_styno;

		this.finpord_quality = finpord_quality;

		this.finpord_name = finpord_name;

		this.finpord_ringsz = finpord_ringsz;

		this.finpord_multi = finpord_multi;

		this.finpord_tagedpr = finpord_tagedpr;

		this.finpord_goldwgt = finpord_goldwgt;

		this.finpord_merchwgt = finpord_merchwgt;

		this.finpord_mainstono = finpord_mainstono;

		this.finpord_substo1no = finpord_substo1no;

		this.finpord_substo2no = finpord_substo2no;

		this.finpord_substo3no = finpord_substo3no;

		this.finpord_reserved1 = finpord_reserved1;

		this.finpord_counter = finpord_counter;

		this.finpord_catagory = finpord_catagory;

		this.finpord_goldcolor = finpord_goldcolor;

		this.finpord_baslabcost = finpord_baslabcost;

		this.finpord_othrcost = finpord_othrcost;

		this.finpord_salabcost = finpord_salabcost;

		this.finpord_goldpr = finpord_goldpr;

		this.finpord_goldlos = finpord_goldlos;

		this.finpord_cost = finpord_cost;

		this.finpord_armsty = finpord_armsty;

		this.finpord_storname = finpord_storname;

		this.finpord_entsdno = finpord_entsdno;

		this.finpord_comments = finpord_comments;

		this.finpord_mainstoname = finpord_mainstoname;

		this.finpord_mainstowgt = finpord_mainstowgt;

		this.finpord_mainstoqty = finpord_mainstoqty;

		this.finpord_mainstopr = finpord_mainstopr;

		this.finpord_cert1 = finpord_cert1;

		this.finpord_cert2 = finpord_cert2;

		this.finpord_gia1 = finpord_gia1;

		this.finpord_gia2 = finpord_gia2;

		this.finpord_mainstoshape = finpord_mainstoshape;

		this.finpord_series = finpord_series;

		this.finpord_cut1 = finpord_cut1;

		this.finpord_symmetry1 = finpord_symmetry1;

		this.finpord_polish1 = finpord_polish1;

		this.finpord_fluorescence1 = finpord_fluorescence1;

		this.finpord_clarity1 = finpord_clarity1;

		this.finpord_color1 = finpord_color1;

		this.finpord_cut2 = finpord_cut2;

		this.finpord_symmetry2 = finpord_symmetry2;

		this.finpord_polish2 = finpord_polish2;

		this.finpord_fluorescence2 = finpord_fluorescence2;

		this.finpord_clarity2 = finpord_clarity2;

		this.finpord_color2 = finpord_color2;
	
		this.finpord_substo1name = finpord_substo1name;
	
		this.finpord_substo1wgt = finpord_substo1wgt;

		this.finpord_substo1qty = finpord_substo1qty;

		this.finpord_substo1pr = finpord_substo1pr;
	
		this.finpord_substo2name = finpord_substo2name;
	
		this.finpord_substo2wgt = finpord_substo2wgt;

		this.finpord_substo2qty = finpord_substo2qty;

		this.finpord_substo2pr = finpord_substo2pr;
	
		this.finpord_substo3name = finpord_substo3name;

		this.finpord_substo3wgt = finpord_substo3wgt;

		this.finpord_substo3qty = finpord_substo3qty;

		this.finpord_substo3pr = finpord_substo3pr;
	
		this.finpord_reserved2 = finpord_reserved2;
		
	}
	
	
	
	
	
	
	 
	
	public String getFinpord_ID() {
		return finpord_ID;
	}
	public void setFinpord_ID(String finpord_ID) {
		this.finpord_ID = finpord_ID;
	}
	public int getFinpord_seqno() {
		return finpord_seqno;
	}
	public void setFinpord_seqno(int finpord_seqno) {
		this.finpord_seqno = finpord_seqno;
	}
	public long getFinpord_procordNo() {
		return finpord_procordNo;
	}
	public void setFinpord_procordNo(long finpord_procordNo) {
		this.finpord_procordNo = finpord_procordNo;
	}
	public String getFinpord_barcode() {
		return finpord_barcode;
	}
	public void setFinpord_barcode(String finpord_barcode) {
		this.finpord_barcode = finpord_barcode;
	}
	public Date getFinpord_inboundate() {
		return finpord_inboundate;
	}
	public void setFinpord_inboundate(Date finpord_inboundate) {
		this.finpord_inboundate = finpord_inboundate;
	}
	public String getFinpord_supplier() {
		return finpord_supplier;
	}
	public void setFinpord_supplier(String finpord_supplier) {
		this.finpord_supplier = finpord_supplier;
	}
	public String getFinpord_styno() {
		return finpord_styno;
	}
	public void setFinpord_styno(String finpord_styno) {
		this.finpord_styno = finpord_styno;
	}
	public String getFinpord_quality() {
		return finpord_quality;
	}
	public void setFinpord_quality(String finpord_quality) {
		this.finpord_quality = finpord_quality;
	}
	public String getFinpord_name() {
		return finpord_name;
	}
	public void setFinpord_name(String finpord_name) {
		this.finpord_name = finpord_name;
	}
	public int getFinpord_ringsz() {
		return finpord_ringsz;
	}
	public void setFinpord_ringsz(int finpord_ringsz) {
		this.finpord_ringsz = finpord_ringsz;
	}
	public float getFinpord_multi() {
		return finpord_multi;
	}
	public void setFinpord_multi(float finpord_multi) {
		this.finpord_multi = finpord_multi;
	}
	public double getFinpord_tagedpr() {
		return finpord_tagedpr;
	}
	public void setFinpord_tagedpr(double finpord_tagedpr) {
		this.finpord_tagedpr = finpord_tagedpr;
	}
	public double getFinpord_goldwgt() {
		return finpord_goldwgt;
	}
	public void setFinpord_goldwgt(double finpord_goldwgt) {
		this.finpord_goldwgt = finpord_goldwgt;
	}
	public double getFinpord_merchwgt() {
		return finpord_merchwgt;
	}
	public void setFinpord_merchwgt(double finpord_merchwgt) {
		this.finpord_merchwgt = finpord_merchwgt;
	}
	
	public String getFinpord_mainstono() {
		return finpord_mainstono;
	}
	public void setFinpord_mainstono(String finpord_mainstono) {
		this.finpord_mainstono = finpord_mainstono;
	}
	public String getFinpord_substo1no() {
		return finpord_substo1no;
	}
	public void setFinpord_substo1no(String finpord_substo1no) {
		this.finpord_substo1no = finpord_substo1no;
	}
	public String getFinpord_substo2no() {
		return finpord_substo2no;
	}
	public void setFinpord_substo2no(String finpord_substo2no) {
		this.finpord_substo2no = finpord_substo2no;
	}
	public String getFinpord_substo3no() {
		return finpord_substo3no;
	}
	public void setFinpord_substo3no(String finpord_substo3no) {
		this.finpord_substo3no = finpord_substo3no;
	}
	public int getFinpord_reserved1() {
		return finpord_reserved1;
	}
	public void setFinpord_reserved1(int finpord_reserved1) {
		this.finpord_reserved1 = finpord_reserved1;
	}
	public String getFinpord_counter() {
		return finpord_counter;
	}
	public void setFinpord_counter(String finpord_counter) {
		this.finpord_counter = finpord_counter;
	}
	public String getFinpord_catagory() {
		return finpord_catagory;
	}
	public void setFinpord_catagory(String finpord_catagory) {
		this.finpord_catagory = finpord_catagory;
	}
	public String getFinpord_goldcolor() {
		return finpord_goldcolor;
	}
	public void setFinpord_goldcolor(String finpord_goldcolor) {
		this.finpord_goldcolor = finpord_goldcolor;
	}
	public double getFinpord_baslabcost() {
		return finpord_baslabcost;
	}
	public void setFinpord_baslabcost(double finpord_baslabcost) {
		this.finpord_baslabcost = finpord_baslabcost;
	}
	public double getFinpord_othrcost() {
		return finpord_othrcost;
	}
	public void setFinpord_othrcost(double finpord_othrcost) {
		this.finpord_othrcost = finpord_othrcost;
	}
	public double getFinpord_salabcost() {
		return finpord_salabcost;
	}
	public void setFinpord_salabcost(double finpord_salabcost) {
		this.finpord_salabcost = finpord_salabcost;
	}
	public double getFinpord_goldpr() {
		return finpord_goldpr;
	}
	public void setFinpord_goldpr(double finpord_goldpr) {
		this.finpord_goldpr = finpord_goldpr;
	}
	public double getFinpord_goldlos() {
		return finpord_goldlos;
	}
	public void setFinpord_goldlos(double finpord_goldlos) {
		this.finpord_goldlos = finpord_goldlos;
	}
	public double getFinpord_cost() {
		return finpord_cost;
	}
	public void setFinpord_cost(double finpord_cost) {
		this.finpord_cost = finpord_cost;
	}
	public String getFinpord_armsty() {
		return finpord_armsty;
	}
	public void setFinpord_armsty(String finpord_armsty) {
		this.finpord_armsty = finpord_armsty;
	}
	public String getFinpord_storname() {
		return finpord_storname;
	}
	public void setFinpord_storname(String finpord_storname) {
		this.finpord_storname = finpord_storname;
	}
	public String getFinpord_entsdno() {
		return finpord_entsdno;
	}
	public void setFinpord_entsdno(String finpord_entsdno) {
		this.finpord_entsdno = finpord_entsdno;
	}
	public String getFinpord_comments() {
		return finpord_comments;
	}
	public void setFinpord_comments(String finpord_comments) {
		this.finpord_comments = finpord_comments;
	}
	
	public String getFinpord_mainstoname() {
		return finpord_mainstoname;
	}
	public void setFinpord_mainstoname(String finpord_mainstoname) {
		this.finpord_mainstoname = finpord_mainstoname;
	}
	public double getFinpord_mainstowgt() {
		return finpord_mainstowgt;
	}
	public void setFinpord_mainstowgt(double finpord_mainstowgt) {
		this.finpord_mainstowgt = finpord_mainstowgt;
	}
	public int getFinpord_mainstoqty() {
		return finpord_mainstoqty;
	}
	public void setFinpord_mainstoqty(int finpord_mainstoqty) {
		this.finpord_mainstoqty = finpord_mainstoqty;
	}
	public double getFinpord_mainstopr() {
		return finpord_mainstopr;
	}
	public void setFinpord_mainstopr(double finpord_mainstopr) {
		this.finpord_mainstopr = finpord_mainstopr;
	}
	public String getFinpord_cert1() {
		return finpord_cert1;
	}
	public void setFinpord_cert1(String finpord_cert1) {
		this.finpord_cert1 = finpord_cert1;
	}
	public String getFinpord_cert2() {
		return finpord_cert2;
	}
	public void setFinpord_cert2(String finpord_cert2) {
		this.finpord_cert2 = finpord_cert2;
	}
	public String getFinpord_gia1() {
		return finpord_gia1;
	}
	public void setFinpord_gia1(String finpord_gia1) {
		this.finpord_gia1 = finpord_gia1;
	}
	public String getFinpord_gia2() {
		return finpord_gia2;
	}
	public void setFinpord_gia2(String finpord_gia2) {
		this.finpord_gia2 = finpord_gia2;
	}
	public String getFinpord_mainstoshape() {
		return finpord_mainstoshape;
	}
	public void setFinpord_mainstoshape(String finpord_mainstoshape) {
		this.finpord_mainstoshape = finpord_mainstoshape;
	}
	public String getFinpord_series() {
		return finpord_series;
	}
	public void setFinpord_series(String finpord_series) {
		this.finpord_series = finpord_series;
	}
	public String getFinpord_cut1() {
		return finpord_cut1;
	}
	public void setFinpord_cut1(String finpord_cut1) {
		this.finpord_cut1 = finpord_cut1;
	}
	public String getFinpord_symmetry1() {
		return finpord_symmetry1;
	}
	public void setFinpord_symmetry1(String finpord_symmetry1) {
		this.finpord_symmetry1 = finpord_symmetry1;
	}
	public String getFinpord_polish1() {
		return finpord_polish1;
	}
	public void setFinpord_polish1(String finpord_polish1) {
		this.finpord_polish1 = finpord_polish1;
	}
	public String getFinpord_fluorescence1() {
		return finpord_fluorescence1;
	}
	public void setFinpord_fluorescence1(String finpord_fluorescence1) {
		this.finpord_fluorescence1 = finpord_fluorescence1;
	}
	public String getFinpord_clarity1() {
		return finpord_clarity1;
	}
	public void setFinpord_clarity1(String finpord_clarity1) {
		this.finpord_clarity1 = finpord_clarity1;
	}
	public String getFinpord_color1() {
		return finpord_color1;
	}
	public void setFinpord_color1(String finpord_color1) {
		this.finpord_color1 = finpord_color1;
	}
	public String getFinpord_cut2() {
		return finpord_cut2;
	}
	public void setFinpord_cut2(String finpord_cut2) {
		this.finpord_cut2 = finpord_cut2;
	}
	public String getFinpord_symmetry2() {
		return finpord_symmetry2;
	}
	public void setFinpord_symmetry2(String finpord_symmetry2) {
		this.finpord_symmetry2 = finpord_symmetry2;
	}
	public String getFinpord_polish2() {
		return finpord_polish2;
	}
	public void setFinpord_polish2(String finpord_polish2) {
		this.finpord_polish2 = finpord_polish2;
	}
	public String getFinpord_fluorescence2() {
		return finpord_fluorescence2;
	}
	public void setFinpord_fluorescence2(String finpord_fluorescence2) {
		this.finpord_fluorescence2 = finpord_fluorescence2;
	}
	public String getFinpord_clarity2() {
		return finpord_clarity2;
	}
	public void setFinpord_clarity2(String finpord_clarity2) {
		this.finpord_clarity2 = finpord_clarity2;
	}
	public String getFinpord_color2() {
		return finpord_color2;
	}
	public void setFinpord_color2(String finpord_color2) {
		this.finpord_color2 = finpord_color2;
	}
	
	
	public String getFinpord_substo1name() {
		return finpord_substo1name;
	}
	public void setFinpord_substo1name(String finpord_substo1name) {
		this.finpord_substo1name = finpord_substo1name;
	}
	public double getFinpord_substo1wgt() {
		return finpord_substo1wgt;
	}
	public void setFinpord_substo1wgt(double finpord_substo1wgt) {
		this.finpord_substo1wgt = finpord_substo1wgt;
	}
	public int getFinpord_substo1qty() {
		return finpord_substo1qty;
	}
	public void setFinpord_substo1qty(int finpord_substo1qty) {
		this.finpord_substo1qty = finpord_substo1qty;
	}
	public double getFinpord_substo1pr() {
		return finpord_substo1pr;
	}
	public void setFinpord_substo1pr(double finpord_substo1pr) {
		this.finpord_substo1pr = finpord_substo1pr;
	}
	
	
	public String getFinpord_substo2name() {
		return finpord_substo2name;
	}
	public void setFinpord_substo2name(String finpord_substo2name) {
		this.finpord_substo2name = finpord_substo2name;
	}
	public double getFinpord_substo2wgt() {
		return finpord_substo2wgt;
	}
	public void setFinpord_substo2wgt(double finpord_substo2wgt) {
		this.finpord_substo2wgt = finpord_substo2wgt;
	}
	public int getFinpord_substo2qty() {
		return finpord_substo2qty;
	}
	public void setFinpord_substo2qty(int finpord_substo2qty) {
		this.finpord_substo2qty = finpord_substo2qty;
	}
	public double getFinpord_substo2pr() {
		return finpord_substo2pr;
	}
	public void setFinpord_substo2pr(double finpord_substo2pr) {
		this.finpord_substo2pr = finpord_substo2pr;
	}
	
	
	public String getFinpord_substo3name() {
		return finpord_substo3name;
	}
	public void setFinpord_substo3name(String finpord_substo3name) {
		this.finpord_substo3name = finpord_substo3name;
	}
	public double getFinpord_substo3wgt() {
		return finpord_substo3wgt;
	}
	public void setFinpord_substo3wgt(double finpord_substo3wgt) {
		this.finpord_substo3wgt = finpord_substo3wgt;
	}
	public int getFinpord_substo3qty() {
		return finpord_substo3qty;
	}
	public void setFinpord_substo3qty(int finpord_substo3qty) {
		this.finpord_substo3qty = finpord_substo3qty;
	}
	public double getFinpord_substo3pr() {
		return finpord_substo3pr;
	}
	public void setFinpord_substo3pr(double finpord_substo3pr) {
		this.finpord_substo3pr = finpord_substo3pr;
	}
	
	public String getFinpord_reserved2() {
		return finpord_reserved2;
	}
	public void setFinpord_reserved2(String finpord_reserved2) {
		this.finpord_reserved2 = finpord_reserved2;
	}
	
	

	
}
