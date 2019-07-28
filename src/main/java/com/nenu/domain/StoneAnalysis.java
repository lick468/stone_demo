package com.nenu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class StoneAnalysis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4262926201724735358L;

	@Id
	@GeneratedValue
	private int id;
	private int sid;
	private String source;//来源
	private String room;	//库房
	private int bouns;//本次积分
	private String cut;//切工
	private String cardNo;//卡号
	private String time;//时间
	private float cost;//成本
	private String circle;//圈口
	private String supplier;//供应商
	private String series;//系列
	private String style;//戒臂样式
	private String remark;//备注
	private String area;//区域
	private String orderNo;//单号
	private String barcode;//条码
	private String quality;//成色
	private String centerstonename;//主石名
	private String product;//名称
	private float settlementprice;//结算价
	private float goldweight;//金重
	private float goodweight;//货重
	private float centerstone;//主石
	private float goldprice;//金价
	private float listprice;//标价
	private float discount;//折扣
	private float wage;//销售工费
	private String counter;//柜台
	private float exchangegoldweight;//兑换金重
	private float deprecitiongoldweight;//折足金重
	private float exchangemoney;//兑换金额
	private String sort;//品类
	private int centerstoneNo;//主石编
	private int count;//数量
	private Date date;//日期
	private int sidestoneNo;//副石编
	private float sidestoneweight;//副石重
	private String certificateNo;//证书号
	private String salesman;//销售人
	private String color;//颜色
	private String level;//级别
	private String priceNo;//款号
	private int areacount;
	private int roomcount;
	private int countercount;
	private int areamoney;
	private int roommoney;
	private int countermoney;
	private String index8Attributes;//属性
	private String index8Select;//选择年
	private String index8Compare;//对比年
	private String index8Diff;//增长率
	private int productsum;
	private int sEcho;//下标
	private int len;//步长
	
	
	
	public int getsEcho() {
		return sEcho;
	}
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public int getProductsum() {
		return productsum;
	}
	public void setProductsum(int productsum) {
		this.productsum = productsum;
	}
	public int getProductmoney() {
		return productmoney;
	}
	public void setProductmoney(int productmoney) {
		this.productmoney = productmoney;
	}

	private int productmoney;
	
	
	
	public StoneAnalysis(String index8Attributes,String index8Select,String index8Compare,String index8Diff) {
		this.index8Attributes = index8Attributes;
		this.index8Select = index8Select;
		this.index8Compare = index8Compare;
		this.index8Diff = index8Diff;
	}
	
	
	
	
	
	
public String getIndex8Attributes() {
		return index8Attributes;
	}






	public void setIndex8Attributes(String index8Attributes) {
		this.index8Attributes = index8Attributes;
	}






	public String getIndex8Select() {
		return index8Select;
	}






	public void setIndex8Select(String index8Select) {
		this.index8Select = index8Select;
	}






	public String getIndex8Compare() {
		return index8Compare;
	}






	public void setIndex8Compare(String index8Compare) {
		this.index8Compare = index8Compare;
	}






	public String getIndex8Diff() {
		return index8Diff;
	}






	public void setIndex8Diff(String index8Diff) {
		this.index8Diff = index8Diff;
	}






public StoneAnalysis() {
		
	}
	public StoneAnalysis(int sid,String source,String room,int bouns,String cut,String cardNo,String time,float cost,String circle,
			String supplier, String series,String style, String remark,String area, String orderNo,String barcode, 
			String quality, String centerstonename, String product,float settlementprice,float goldweight,float goodweight,
			float centerstone, float goldprice,float listprice,float discount,float wage,String counter,float exchangegoldweight,
			float deprecitiongoldweight,float exchangemoney,String sort,int centerstoneNo,int count, Date date,int sidestoneNo,
			float sidestoneweight,String certificateNo,String salesman,String color,String level,String priceNo) {
		this.sid = sid;
		this.source =  source;
		this.room =  room;
		this.bouns =  bouns;
		this.cut =  cut;
		this.cardNo =  cardNo;
		this.time =  time;
		this.cost =  cost;
		this.circle =  circle;
		this.supplier =  supplier;
		this.series =  series;
		this.style =  style;
		this.remark =  remark;
		this.area =  area;
		this.orderNo =  orderNo;
		this.barcode =  barcode;
		this.quality =  quality;
		this.centerstonename =  centerstonename;
		this.product =  product;
		this.settlementprice =  settlementprice;
		this.goldweight =  goldweight;
		this.goodweight =  goodweight;
		this.centerstone =  centerstone;
		this.goldprice =  goldprice;
		this.listprice =  listprice;
		this.discount =  discount;
		this.wage =  wage;
		this.counter =  counter;
		this.exchangegoldweight =  exchangegoldweight;
		this.deprecitiongoldweight =  deprecitiongoldweight;
		this.exchangemoney =  exchangemoney;
		this.sort =  sort;
		this.centerstoneNo =  centerstoneNo;
		this.count =  count;
		this.date =  date;
		this.sidestoneNo =  sidestoneNo;
		this.sidestoneweight =  sidestoneweight;
		this.certificateNo =  certificateNo;
		this.salesman =  salesman;
		this.color =  color;
		this.level =  level;
		this.priceNo =  priceNo;
		
	}

	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getAreamoney() {
		return areamoney;
	}

	public void setAreamoney(int areamoney) {
		this.areamoney = areamoney;
	}

	public int getRoommoney() {
		return roommoney;
	}

	public void setRoommoney(int roommoney) {
		this.roommoney = roommoney;
	}

	public int getCountermoney() {
		return countermoney;
	}

	public void setCountermoney(int countermoney) {
		this.countermoney = countermoney;
	}

	private int roomsettlementprice;
	private int areasettlementprice;
	private int countersettlementprice;
	
	private float settlementpriceSum;//结算价求和
	private int numberSum;//数量求和
	private float listpriceSum;//标价求和
	private float goldweightSum;//金重求和
	private float centerstoneSum;//主石重求和
	
	
	
	
	public float getListpriceSum() {
		return listpriceSum;
	}
	public void setListpriceSum(float listpriceSum) {
		this.listpriceSum = listpriceSum;
	}
	public float getGoldweightSum() {
		return goldweightSum;
	}
	public void setGoldweightSum(float goldweightSum) {
		this.goldweightSum = goldweightSum;
	}
	public float getCenterstoneSum() {
		return centerstoneSum;
	}
	public void setCenterstoneSum(float centerstoneSum) {
		this.centerstoneSum = centerstoneSum;
	}

	private int settlementpriceSum2;
	private int numberSum2;
	private int settlementpriceSum3;
	private int numberSum3;
	
	public int getSettlementpriceSum3() {
		return settlementpriceSum3;
	}

	public void setSettlementpriceSum3(int settlementpriceSum3) {
		this.settlementpriceSum3 = settlementpriceSum3;
	}

	public int getNumberSum3() {
		return numberSum3;
	}

	public void setNumberSum3(int numberSum3) {
		this.numberSum3 = numberSum3;
	}

	public int getSettlementpriceSum2() {
		return settlementpriceSum2;
	}

	public void setSettlementpriceSum2(int settlementpriceSum2) {
		this.settlementpriceSum2 = settlementpriceSum2;
	}

	public int getNumberSum2() {
		return numberSum2;
	}

	public void setNumberSum2(int numberSum2) {
		this.numberSum2 = numberSum2;
	}

	public int getNumberSum() {
		return numberSum;
	}

	public void setNumberSum(int numberSum) {
		this.numberSum = numberSum;
	}

	public float getSettlementpriceSum() {
		return settlementpriceSum;
	}

	public void setSettlementpriceSum(float settlementpriceSum) {
		this.settlementpriceSum = settlementpriceSum;
	}

	public int getRoomsettlementprice() {
		return roomsettlementprice;
	}

	public void setRoomsettlementprice(int roomsettlementprice) {
		this.roomsettlementprice = roomsettlementprice;
	}

	public int getAreasettlementprice() {
		return areasettlementprice;
	}

	public void setAreasettlementprice(int areasettlementprice) {
		this.areasettlementprice = areasettlementprice;
	}

	public int getCountersettlementprice() {
		return countersettlementprice;
	}

	public void setCountersettlementprice(int countersettlementprice) {
		this.countersettlementprice = countersettlementprice;
	}

	public int getRoomcount() {
		return roomcount;
	}

	public void setRoomcount(int roomcount) {
		this.roomcount = roomcount;
	}

	public int getCountercount() {
		return countercount;
	}

	public void setCountercount(int countercount) {
		this.countercount = countercount;
	}

	public int getAreacount() {
		return areacount;
	}

	public void setAreacount(int areacount) {
		this.areacount = areacount;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getCenterstonename() {
		return centerstonename;
	}

	public void setCenterstonename(String centerstonename) {
		this.centerstonename = centerstonename;
	}


	public void setSettlementprice(int settlementprice) {
		this.settlementprice = settlementprice;
	}

	public float getGoldweight() {
		return goldweight;
	}

	public void setGoldweight(float goldweight) {
		this.goldweight = goldweight;
	}

	public float getGoodweight() {
		return goodweight;
	}

	public void setGoodweight(float goodweight) {
		this.goodweight = goodweight;
	}

	public float getCenterstone() {
		return centerstone;
	}

	public void setCenterstone(float centerstone) {
		this.centerstone = centerstone;
	}

	public float getGoldprice() {
		return goldprice;
	}

	public void setGoldprice(float goldprice) {
		this.goldprice = goldprice;
	}

	public float getListprice() {
		return listprice;
	}

	public void setListprice(float listprice) {
		this.listprice = listprice;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getWage() {
		return wage;
	}

	public void setWage(float wage) {
		this.wage = wage;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public float getExchangegoldweight() {
		return exchangegoldweight;
	}

	public void setExchangegoldweight(float exchangegoldweight) {
		this.exchangegoldweight = exchangegoldweight;
	}

	public float getDeprecitiongoldweight() {
		return deprecitiongoldweight;
	}

	public void setDeprecitiongoldweight(float deprecitiongoldweight) {
		this.deprecitiongoldweight = deprecitiongoldweight;
	}

	public float getExchangemoney() {
		return exchangemoney;
	}

	public void setExchangemoney(float exchangemoney) {
		this.exchangemoney = exchangemoney;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getCenterstoneNo() {
		return centerstoneNo;
	}

	public void setCenterstoneNo(int centerstoneNo) {
		this.centerstoneNo = centerstoneNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSidestoneNo() {
		return sidestoneNo;
	}

	public void setSidestoneNo(int sidestoneNo) {
		this.sidestoneNo = sidestoneNo;
	}

	public float getSidestoneweight() {
		return sidestoneweight;
	}

	public void setSidestoneweight(float sidestoneweight) {
		this.sidestoneweight = sidestoneweight;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPriceNo() {
		return priceNo;
	}

	public void setPriceNo(String priceNo) {
		this.priceNo = priceNo;
	}

	public int getBouns() {
		return bouns;
	}

	public void setBouns(int bouns) {
		this.bouns = bouns;
	}

	public String getCut() {
		return cut;
	}

	public void setCut(String cut) {
		this.cut = cut;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return source;
	}

	public void setName(String name) {
		this.source = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	

	public float getSettlementprice() {
		return settlementprice;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
