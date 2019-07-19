package com.nenu.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nenu.domain.*;
import com.nenu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="StoneAnalysisController",description="数据分析系统数据分析接口")
@Controller
@RequestMapping(value = "/analysis/")
public class StoneAnalysisController {
	@ApiOperation(value="跳转主页",notes="显示主页")
	@RequestMapping(value = "main")
	public String main(ModelMap map) {
		return "main";
	}

	@Autowired
	private StoneAnalysisService stoneService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private MainStoneService mainStoneService;

	@Autowired
    private GoldWeightService goldWeightService;
	
	@Autowired
	private ListPriceService listPriceService;
	@Autowired
	private SettlementPriceService settlementPriceService;
	@Autowired
	private PlanService planService;
	@Autowired
	private BelongService belongService;
	@Autowired
	private BelongAndCounterService belongAndCounterService;

	/**
	 * index 711 获取供应商
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:03:29
	 */
	@ApiOperation(value="跳转index页面",notes="显示index页面")
	@GetMapping(value = "index")
	public String index(ModelMap map, HttpSession session) throws ParseException {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listSupplier = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			
			if (list.get(i).getSupplier() != null) {
				if (!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length() > 0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
			if (list.get(i).getCounter() != null) {
				if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if (list.get(i).getProduct() != null) {
				if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
					listProduct.add(list.get(i).getProduct());
				}
			}
		}
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listsupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);
		return "index";
	}

	

	/**
	 * 新版index页面 供应商查询
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created at 2018年6月27日
	 */
	@ApiOperation(value="供应商查询",notes="index页面 供应商查询")
	@RequestMapping(value = "supplierFind", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFind(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String supplierName = request.getParameter("supplier");
		String productName = request.getParameter("product");
		String counterName = request.getParameter("counter");
		String selectType = request.getParameter("selectType");

		System.out.println("名称=============" + productName);
		System.out.println("供应商=============" + supplierName);
		System.out.println("类别=============" + selectType);
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("st===" + st + ",ed======" + ed);
		params.put("start", st);
		params.put("end", ed);
		
		if(supplierName.contains("所有") ||supplierName.contains("供应商"))  {
		}else {
			params.put("supplier", supplierName);
		}
		if(productName.contains("所有") ||productName.contains("名称"))  {
		}else {
			params.put("product", productName);
		}
		if(counterName.contains("所有") ||counterName.contains("柜台"))  {
		}else {
			params.put("counter", counterName);
		}
	
		

		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据
		
		listAll = stoneService.findStoneBySupplier(params);
		list = stoneService.findStoneBySupplierFor711(params);
		

		List listDate = new ArrayList<>();
		List listNum = new ArrayList<>();
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		List lisAlltCenterstone = new ArrayList<>();

		String result = "";
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
				lisAlltCenterstone.add(listAll.get(i).getCenterstone());
				listAllCounter.add(listAll.get(i).getCounter());
			}
		}

		if (list != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < list.size(); i++) {
					listDate.add(sdf.format(list.get(i).getDate()));
					listNum.add(list.get(i).getNumberSum());
				}
			} else if (selectType.contains("结算价")){
				for (int i = 0; i < list.size(); i++) {
					listDate.add(sdf.format(list.get(i).getDate()));
					listNum.add(list.get(i).getSettlementpriceSum());
				}
			} else if (selectType.contains("标价")){
				for (int i = 0; i < list.size(); i++) {
					listDate.add(sdf.format(list.get(i).getDate()));
					listNum.add(list.get(i).getListpriceSum());
				}
			} else if (selectType.contains("金重")){
				for (int i = 0; i < list.size(); i++) {
					listDate.add(sdf.format(list.get(i).getDate()));
					listNum.add(list.get(i).getGoldweightSum());
				}
			} else if (selectType.contains("主石")){
				for (int i = 0; i < list.size(); i++) {
					listDate.add(sdf.format(list.get(i).getDate()));
					listNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}

		result = "" + listNum + "@" + listDate + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct 
				+ "@" + listAllSettlementprice+ "@" + listAllListprice+ "@" 
				+ listAllGoldweight+ "@" + lisAlltCenterstone+ "@" + listAllCounter;
		return result;
	}

	/**
	 * index页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index页面 导出excel表格",notes="index页面 导出excel表格")
	@RequestMapping(value = "downloadExcelFor711", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[]=con.split("&");
		String supplier = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String start = conList[3];
		String end = conList[4];

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("start", start);
		params.put("end", end);

		if(supplier.contains("所有") ||supplier.contains("供应商"))  {
		}else {
			params.put("supplier", supplier);
		}
		if(product.contains("所有") ||product.contains("名称"))  {
		}else {
			params.put("product", product);
		}
		if(counter.contains("所有") ||counter.contains("柜台"))  {
		}else {
			params.put("counter", counter);
		}
		List<StoneAnalysis> listAll = new ArrayList<>();
		listAll = stoneService.findStoneBySupplier(params);

		stoneService.downloadExcelForIndex(listAll,response);
		

		return result;
	}

	/**
	 * 
	 * main跳转到productnum页面 获取供应商
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:26:57
	 */
	@ApiOperation(value="跳转到productnum页面",notes="跳转到productnum页面")
	@GetMapping(value = "productnum")
	public String productnum(ModelMap map, HttpSession session) throws ParseException {

		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();

		List listSupplier = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listQuality = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getQuality()!=null) {
				if(!listQuality.contains(list.get(i).getQuality()) && list.get(i).getQuality().length()>0) {
					listQuality.add(list.get(i).getQuality());
				}
			}
		}
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listQuality, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listsupplier", listSupplier);
		map.addAttribute("listQuality", listQuality);
		map.addAttribute("listCounter", listCounter);
		return "productnum";
	}

	/**
	 * productnum页面 查询
	 * 
	 * @param request
	 * @return List created by lick on 2018年7月1日
	 */
	@ApiOperation(value="productnum页面 查询",notes="productnum页面 查询")
	@RequestMapping(value = "supplierFind2", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFind2(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String supplier = request.getParameter("supplier");
		String counter = request.getParameter("counter");
		String quality = request.getParameter("quality");
		String selectType = request.getParameter("selectType");

		System.out.println("供应商=============" + supplier);
		System.out.println("柜台=============" + counter);
		System.out.println("成色=============" + quality);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();

		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			params.put("counter", counter);
		}
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			params.put("supplier", supplier);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			params.put("quality", quality);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("st===" + st + ",ed======" + ed);
		params.put("start", st);
		params.put("end", ed);

		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneBySupplier(params);
		list = stoneService.findStoneBySupplierForProductSum(params);
		

		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
		List listAllDate = new ArrayList<>();
		List listAllQuality = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllCounter = new ArrayList<>();

		String result = "";
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllCounter.add(listAll.get(i).getCounter());
				listAllQuality.add(listAll.get(i).getQuality());
			}
		}

		if (list != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < list.size(); i++) {
					listProduct.add(list.get(i).getProduct());
					listProductNum.add(list.get(i).getNumberSum());
				}
			} else if(selectType.contains("结算价")) {
				for (int i = 0; i < list.size(); i++) {
					listProduct.add(list.get(i).getProduct());
					listProductNum.add(list.get(i).getSettlementpriceSum());
				}
			} else if(selectType.contains("标价")) {
				for (int i = 0; i < list.size(); i++) {
					listProduct.add(list.get(i).getProduct());
					listProductNum.add(list.get(i).getListpriceSum());
				}
			} else if(selectType.contains("金重")) {
				for (int i = 0; i < list.size(); i++) {
					listProduct.add(list.get(i).getProduct());
					listProductNum.add(list.get(i).getGoldweightSum());
				}
			} else if(selectType.contains("主石")) {
				for (int i = 0; i < list.size(); i++) {
					listProduct.add(list.get(i).getProduct());
					listProductNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}

		//System.out.println(list);
		//System.out.println(listAll);
		result = "" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct 
				+ "@" + listAllSettlementprice+ "@" + listAllListprice+ "@" + listAllGoldweight+ "@" + listAllCenterstone
				+ "@" + listAllCounter+ "@" + listAllQuality;
		return result;
	}

	/**
	 * index2 productnum页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="productnum页面  导出excel表格",notes="productnum页面  导出excel表格")
	@RequestMapping(value = "downloadExcelFor712", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForProductNum(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[]=con.split("&");
		String supplier = conList[0];
		String counter = conList[1];
		String quality = conList[2];
		String start = conList[3];
		String end = conList[4];
		

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			params.put("counter", counter);
		}
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			params.put("supplier", supplier);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			params.put("quality", quality);
		}
		
		params.put("start", start);
		params.put("end", end);

		List<StoneAnalysis> listAll = new ArrayList<>();
		listAll = stoneService.findStoneBySupplier(params);
		stoneService.downloadExcelForProductNum(listAll,response);
		

		return result;
	}

	/**
	 * main跳转到index3页面 725 获取供应商 柜台  名称
	 * 
	 * @param map
	 * @return String created by lick on 2018年7月1日
	 */
	@ApiOperation(value="跳转到index3页面",notes="兑换销售排名分析页面")
	@SuppressWarnings("rawtypes")
	@GetMapping(value = "index3")
	public String index3(ModelMap map) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listSupplier = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getSupplier() != null) {
					if (!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length() > 0) {
						listSupplier.add(list.get(i).getSupplier());
					}
				}
				if (list.get(i).getCounter() != null) {
					if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listCounter.add(list.get(i).getCounter());
					}
				}
				if (list.get(i).getProduct() != null) {
					if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
						listProduct.add(list.get(i).getProduct());
					}
				}
			}
		}
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);
		return "index3";
	}

	

	

	/**
	 * index3页面 725 兑换销售排名分析
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年6月28日
	 */
	@ApiOperation(value="index3页面 兑换销售排名分析",notes="index3页面 兑换销售排名分析")
	@RequestMapping(value = "exchangeFind", method = RequestMethod.POST)
	@ResponseBody
	public String exchangeFind(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String supplier = request.getParameter("supplier");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String selectType = request.getParameter("selectType");

		System.out.println("supplier" + supplier + "\ncounter" + counter + "\nproduct" + product + "\nstart" + start + "\nend" + end + "\nselectType" + selectType);
		/*
		 * supplier供应商 counter柜台 product名称 start2018-06-13 end2018-06-04 selectType数量
		 */
		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>();
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();
		
		
		if(counter.contains("柜台") || counter.contains("所有")) {
		}else {
			params.put("counter", counter);
		}
		if(product.contains("名称") || product.contains("所有")) {
		}else {
			params.put("product", product);
		}
		if(supplier.contains("供应商") || supplier.contains("所有")) {
		}else {
			params.put("supplier", supplier);
		}
		listAll = stoneService.findExchangeByParams(params);
		list = stoneService.findExchangeByParamsFor725(params);
		
		
		 
		// 图标数据
		List listY_t = new ArrayList<>();
		List listXHave_t = new ArrayList<>();
		List listXNotHave_t = new ArrayList<>();
		List listY = new ArrayList<>();
		List listXHave = new ArrayList<>();
		List listXNotHave = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (selectType.contains("数量")) {
					if (!listY.contains(list.get(i).getProduct())) {
						listY.add(list.get(i).getProduct());
					}
					listY_t.add(list.get(i).getProduct());

					if (list.get(i).getSource().contains("兑换")) {
						listXHave_t.add(list.get(i).getNumberSum());
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(list.get(i).getNumberSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("结算价")) {
					if (!listY.contains(list.get(i).getProduct())) {
						listY.add(list.get(i).getProduct());
					}
					listY_t.add(list.get(i).getProduct());

					if (list.get(i).getSource().contains("兑换")) {
						listXHave_t.add(Math.abs(list.get(i).getSettlementpriceSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(list.get(i).getSettlementpriceSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("标价")) {
					if (!listY.contains(list.get(i).getProduct())) {
						listY.add(list.get(i).getProduct());
					}
					listY_t.add(list.get(i).getProduct());

					if (list.get(i).getSource().contains("兑换")) {
						listXHave_t.add(Math.abs(list.get(i).getListpriceSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(list.get(i).getListpriceSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("金重")) {
					if (!listY.contains(list.get(i).getProduct())) {
						listY.add(list.get(i).getProduct());
					}
					listY_t.add(list.get(i).getProduct());

					if (list.get(i).getSource().contains("兑换")) {
						listXHave_t.add(Math.abs(list.get(i).getGoldweightSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(list.get(i).getGoldweightSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("主石")) {
					if (!listY.contains(list.get(i).getProduct())) {
						listY.add(list.get(i).getProduct());
					}
					listY_t.add(list.get(i).getProduct());

					if (list.get(i).getSource().contains("兑换")) {
						listXHave_t.add(Math.abs(list.get(i).getCenterstoneSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(list.get(i).getCenterstoneSum());
						listXHave_t.add(0);
					}
				}

			}
		}

		if (listY != null) {
			for (int i = 0; i < listY.size(); i++) {
				listXHave.add(0);
				listXNotHave.add(0);
			}
		}
		float haveSum = 0;
		float notHaveSum = 0;
		if (listY != null) {
			for (int i = 0; i < listY.size(); i++) {
				for (int j = 0; j < listY_t.size(); j++) {
					if (listY.get(i).toString() == listY_t.get(j).toString()) {
						float have = 0;
						float notHave = 0;
						have = Float.parseFloat(listXHave.get(i).toString()) + Float.parseFloat(listXHave_t.get(j).toString());
						notHave = Float.parseFloat(listXNotHave.get(i).toString()) + Float.parseFloat(listXNotHave_t.get(j).toString());
						listXHave.set(i, have);
						listXNotHave.set(i, notHave);
						haveSum += Float.parseFloat(listXHave_t.get(j).toString());
						notHaveSum += Float.parseFloat(listXNotHave_t.get(j).toString());
					}
				}
			}
		}
		listY.add("总量");
		listXHave.add(haveSum);
		listXNotHave.add(notHaveSum);

		// 表格数据
		List listSupplier = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSource = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listListprice = new ArrayList<>();
		List listGoldweight = new ArrayList<>();
		List listCenterstone = new ArrayList<>();
		List listExchangegoldweight = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listSupplier.add(listAll.get(i).getSupplier());
				listCounter.add(listAll.get(i).getCounter());
				listProduct.add(listAll.get(i).getProduct());
				listSource.add(listAll.get(i).getSource());
				listSettlementprice.add(listAll.get(i).getSettlementprice());
				listListprice.add(listAll.get(i).getListprice());
				listGoldweight.add(listAll.get(i).getGoldweight());
				listCenterstone.add(listAll.get(i).getCenterstone());
                listExchangegoldweight.add(listAll.get(i).getExchangegoldweight());
				listDate.add(sdf.format(listAll.get(i).getDate()));
			}
		}

		result = "" + listY + "@" + listXHave + "@" + listXNotHave + "@" + listSupplier + "@" + listCounter + "@" + listProduct + "@" 
		+ listSource + "@" + listSettlementprice + "@" + listDate + "@" + listAll.size()
		+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone+"@"+listExchangegoldweight;

		return result;
	}

	/**
	 * index3页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index3页面  导出excel表格",notes="index3页面  导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex3", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex3(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[]=con.split("&");
		String supplier = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String start = conList[3];
		String end = conList[4];
		

		System.out.println("supplier" + supplier + "\ncounter" + counter + "\nproduct" + product + "\nstart" + start + "\nend" + end);

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("柜台") || counter.contains("所有")) {
		}else {
			params.put("counter", counter);
		}
		if(product.contains("名称") || product.contains("所有")) {
		}else {
			params.put("product", product);
		}
		if(supplier.contains("供应商") || supplier.contains("所有")) {
		}else {
			params.put("supplier", supplier);
		}
		params.put("start", start);
		params.put("end", end);

		List<StoneAnalysis> listAll = new ArrayList<>();
		listAll = stoneService.findExchangeByParams(params);
		stoneService.downloadExcelForIndex3(listAll,response);
		
		return result;
	}

	/**
	 * 跳转到7.3.1页面  系列商品走势
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:41:02
	 */
	@ApiOperation(value="跳转到7.3.1页面  ",notes="系列商品走势页面  ")
	@GetMapping(value = "seriesproduct")
	public String seriesproduct(ModelMap map, HttpSession session) throws ParseException {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listSupplier = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listCounter = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSupplier() != null) {
				if (!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length() > 0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
			if (list.get(i).getProduct() != null) {
				if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
					listProduct.add(list.get(i).getProduct());
				}
			}
			if (list.get(i).getProduct() != null) {
				if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
		}
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listsupplier",  listSupplier);
		map.addAttribute("listProduct", listProduct);
		map.addAttribute("listCounter", listCounter);
		return "7.3.1";
	}
	/**
	 * 7.3.1 页面 查询
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="7.3.1页面  查询 ",notes="系列商品走势页面  ")
	@RequestMapping(value = "supplierFind731", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFind731(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String supplier = request.getParameter("supplier");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		String selectType = request.getParameter("selectType");
        String selectYear = request.getParameter("selectYear");
		System.out.println("名称=============" + product);
		System.out.println("供应商=============" + supplier);
		System.out.println("类别=============" + selectType);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 年走势
		Map<String, Object> params = new HashMap<String, Object>();
		if(product.contains("名称") || product.contains("所有")) {
		}else {
			params.put("product", product);
		}
		if(supplier.contains("供应商") || supplier.contains("所有")) {
		}else {
			params.put("supplier", supplier);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {
		}else {
			params.put("counter", counter);
		}
		Date d = new Date();
		String start =selectYear + "-01-01";
		String end = selectYear + "-12-31";
		System.out.println("st===" + start + ",ed======" + end);
		params.put("start", start);
		params.put("end", end);
		
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		list = stoneService.findStoneBySupplierFor711(params);
		

		List listMonth = new ArrayList<>();// 年走势月份
		List listNum = new ArrayList<>();// 年走势数量

		String result = "";
		for (int i = 0; i < 12; i++) {
			listNum.add(0);
			String Mon = "" + (i + 1) + "月";
			listMonth.add(Mon);
		}
		if (list != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								int num = 0;
								num = listNum.get(j).hashCode() + list.get(i).getNumberSum();
								listNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("结算价")){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								int num = 0;
								num = listNum.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("标价")){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								float num = 0;
								num = Float.parseFloat(listNum.get(j).toString()) + list.get(i).getListpriceSum();
								listNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("金重")){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								float num = 0;
								num = Float.parseFloat(listNum.get(j).toString()) + list.get(i).getGoldweightSum();
								listNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("主石")){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								float num = 0;
								num = Float.parseFloat(listNum.get(j).toString()) + list.get(i).getCenterstoneSum();
								listNum.set(j, num);
							}
						}
					}
				}
			}
		}
		// 月走势
		String selectMonth = request.getParameter("selectMonth");

		List listDate = new ArrayList<>();// 月走势日期
		List listMonthNum = new ArrayList<>();// 月走势数量
		int year = Integer.parseInt(sdf.format(d).substring(0, 4));
		String strat_day = selectYear + "-"+selectMonth+"-01";
		String strat_end = selectYear;
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {// 闰年
			if (selectMonth.equals("2")) {
				strat_end += "-02-29";
				for (int i = 0; i < 29; i++) {
					listDate.add(i + 1);
					listMonthNum.add(0);
				}
			}
		} else {
			if (selectMonth.equals("2")) {
				strat_end += "-02-28";
				for (int i = 0; i < 28; i++) {
					listDate.add(i + 1);
					listMonthNum.add(0);
				}
			}
		}
		if (selectMonth.equals("1") || selectMonth.equals("3") || selectMonth.equals("5") || selectMonth.equals("7") || selectMonth.equals("8") || selectMonth.equals("10") || selectMonth.equals("12")) {
			strat_end = strat_end +"-"+selectMonth + "-31";
			for (int i = 0; i < 31; i++) {
				listDate.add(i + 1);
				listMonthNum.add(0);
			}
		}
		if (selectMonth.equals("4") || selectMonth.equals("6") || selectMonth.equals("9") || selectMonth.equals("11")) {
			strat_end = strat_end +"-"+ selectMonth + "-30";
			for (int i = 0; i < 30; i++) {
				listDate.add(i + 1);
				listMonthNum.add(0);
			}
		}
		Map<String, Object> params_day = new HashMap<String, Object>();
		if(product.contains("名称") || product.contains("所有")) {
		}else {
			params_day.put("product", product);
		}
		if(supplier.contains("供应商") || supplier.contains("所有")) {
		}else {
			params_day.put("supplier", supplier);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {
		}else {
			params_day.put("counter", counter);
		}
		params_day.put("end", strat_end);
		params_day.put("start", strat_day);
		System.out.println(strat_day+"------->"+strat_end);
		List<StoneAnalysis> ListOneMonth = new ArrayList<>();
		ListOneMonth = stoneService.findStoneBySupplierFor711(params_day);
		if (ListOneMonth != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						System.out.println("m=="+m);
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								int num = 0;
								num = listMonthNum.get(j).hashCode() + ListOneMonth.get(i).getNumberSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("结算价")){
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								int num = 0;
								num = listMonthNum.get(j).hashCode() + ListOneMonth.get(i).getSettlementpriceSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("标价")){
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								float num = 0;
								num = listMonthNum.get(j).hashCode() + ListOneMonth.get(i).getListpriceSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("金重")){
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								float num = 0;
								num = listMonthNum.get(j).hashCode() + ListOneMonth.get(i).getGoldweightSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			} else if (selectType.contains("主石")){
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								float num = 0;
								num = listMonthNum.get(j).hashCode() + ListOneMonth.get(i).getCenterstoneSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			}
		}

		result = "" + listNum + "@" + listMonth + "@" + listMonthNum + "@" + listDate;
		return result;
	}

	/**
	 * index5页面 跳转到index5页面 分析销售增减趋势
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年6月27日
	 */
	@ApiOperation(value="跳转到index5页面 ",notes="分析销售增减趋势页面  ")
	@GetMapping(value = "index5")
	public String index5(ModelMap map, HttpSession session) throws ParseException {
		List<StoneAnalysis> list = stoneService.findAllStone();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List listArea = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getArea() != null) {
				if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
					listArea.add(list.get(i).getArea());
				}
			}

		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		//System.out.println(listArea);
		Date da = new Date();
		Date date = null;
		String end = sdf.format(da);
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		try {
			date = sdf.parse(sdf.format(da));
		} catch (Exception e) {
		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, -6);// 在日历的月份上减去6个月
		// System.out.println(sdf.format(c.getTime()));//得到6个月后的日期
		String start = sdf.format(c.getTime());

		int year_s = Integer.parseInt(start.substring(0, 4));
		int year_e = Integer.parseInt(end.substring(0, 4));
		int month_s = Integer.parseInt(start.substring(5, 7));
		int month_e = Integer.parseInt(end.substring(5, 7));
		int year_diff = year_e - year_s;
		int month_diff = month_e - month_s;
		int diff = year_diff * 12 + month_diff + 2;
		System.out.println(diff + "cccccc");
		String[][] str = new String[listArea.size()][diff];
		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < diff; j++) {
				str[i][j] = "0";
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		List<StoneAnalysis> listAllArea = stoneService.findStoneByAreaAndTimeForNumberSumDemo(params);
		for (int i = 0; i < listArea.size(); i++) {
			str[i][0] = (String) listArea.get(i);
		}

		List titleList = new ArrayList<>();// 表头
		titleList.add("area");
		String st = start.substring(0, 7);
		titleList.add(st);
		for (int i = 2; i < diff; i++) {
			start = StoneAnalysisController.monthAddFrist(start);
			titleList.add(start);
		}

		//System.out.println(titleList);
//		for (int i = 0; i < str.length; i++) {
//			for (int j = 0; j < diff; j++) {
//				System.out.print(str[i][j] + "\t");
//			}
//			System.out.println();
//		}
		for (int i = 0; i < listAllArea.size(); i++) {
			for (int j = 0; j < str.length; j++) {
				if (listAllArea.get(i).getArea() != null) {
					if (listAllArea.get(i).getArea().contains(str[j][0])) {
						int year_st = Integer.parseInt(sdf.format(listAllArea.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(listAllArea.get(i).getDate()).substring(5, 7));
						if (year_s == year_st) {
							int month_ss = month_st - month_s + 1;
							str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllArea.get(i).getNumberSum()));
						} else {
							int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
							str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllArea.get(i).getNumberSum()));
						}
					}
				}
			}
		}

//		for (int i = 0; i < str.length; i++) {
//			for (int j = 0; j < diff; j++) {
//				System.out.print(str[i][j] + "\t");
//			}
//			System.out.println();
//		}

		String result = "";

		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < diff; j++) {
				// title.add(str[i][j]);
				if (j == 0) {
					result += str[i][j] + ",";
				} else if (j == diff - 1) {
					result += str[i][j] + "@";
				} else {
					result += str[i][j] + ",";
				}
			}
		}

		map.addAttribute("result", result);
		map.addAttribute("titleList", titleList);
		return "index5";

	}

	/**
	 * 月份加一
	 * 
	 * @param date
	 * @return
	 */
	@ApiOperation(value=" 月份加一",notes="月份加一")
	public static String monthAddFrist(String date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM");
		try {
			Calendar ct = Calendar.getInstance();
			ct.setTime(df.parse(date));
			ct.add(Calendar.MONTH, +1);
			return df.format(ct.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	/**
	 * 输入年月，返回这个月有多少天
	 *
	 * com.nenu.controller
	 * @param year
	 * @param month
	 * @return int
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="输入年月，返回这个月有多少天",notes="输入年月，返回这个月有多少天")
	public static int calculateDays(int year,int month) {
		int days=0;
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {// 闰年
			if(month==2) {
				days=29;
			}else if(month==4 || month==6 || month==9 || month==11) {
				days=30;
			}else {
				days=31;
			}
		}else {
			if(month==2) {
				days=28;
			}else if(month==4 || month==6 || month==9 || month==11) {
				days=30;
			}else {
				days=31;
			}
		}
		return days;
	}

	/**
	 * index5页面  742 销售增减分析，回显表格和图标
	 * 
	 * @param request
	 * @param map
	 * @return String created on 2018年6月28日
	 */
	@ApiOperation(value="index5页面  销售增减分析，回显表格和图标",notes="index5页面  742 销售增减分析，回显表格和图标")
	@RequestMapping(value = "saleTrendFind", method = RequestMethod.POST)
	@ResponseBody
	public String saleTrendFind(HttpServletRequest request, ModelMap map) {
		String selectType = request.getParameter("selectType");
		String type = request.getParameter("type");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		List<StoneAnalysis> list  = stoneService.findAllStone();
		List listTitle = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			if(type.contains("地区")) {
				if (list.get(i).getArea() != null) {
					if (!listTitle.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listTitle.add(list.get(i).getArea());
					}
				}
			}else if(type.contains("柜台")) {
				if (list.get(i).getCounter() != null) {
					if (!listTitle.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listTitle.add(list.get(i).getCounter());
					}
				}
			}else if(type.contains("门店")) {
				if (list.get(i).getRoom() != null) {
					if (!listTitle.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
						listTitle.add(list.get(i).getRoom());
					}
				}
			}
		}
		int year_s = Integer.parseInt(start.substring(0, 4));
		int year_e = Integer.parseInt(end.substring(0, 4));
		int month_s = Integer.parseInt(start.substring(5, 7));
		int month_e = Integer.parseInt(end.substring(5, 7));
		int year_diff = year_e - year_s;
		int month_diff = month_e - month_s;
		int diff = year_diff * 12 + month_diff + 2;
		System.out.println("\nstart=" + start + "\nend=" + end + "\nselectType=" + selectType + "\ndiff=" + diff);
		String result = "";
		List<StoneAnalysis> listAllContent = new ArrayList<>();
		String[][] str = new String[listTitle.size()][diff];
		// 初始化
		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < diff; j++) {
				str[i][j] = "0";
			}
		}
		List titleList = new ArrayList<>();// 表头
		titleList.add("area");
		String st = start.substring(0, 7);
		titleList.add(st);
		for (int i = 2; i < diff; i++) {
			start = StoneAnalysisController.monthAddFrist(start);
			titleList.add(start);
		}
		for (int i = 0; i < listTitle.size(); i++) {
			str[i][0] = (String) listTitle.get(i);
		}
		if (type.contains("地区")) {
			listAllContent = stoneService.findAreaFor742(params);
			for (int i = 0; i < listAllContent.size(); i++) {
				for (int j = 0; j < str.length; j++) {
					if (listAllContent.get(i).getArea() != null) {
						if (listAllContent.get(i).getArea().contains(str[j][0])) {
							int year_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(0, 4));
							int month_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(5, 7));
							if(selectType.contains("数量")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								}
							}else if(selectType.contains("结算价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								}
							}else if(selectType.contains("标价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								}
							}
						}
					}
				}
			}
		} else if (type.contains("柜台")) {
			listAllContent = stoneService.findCounterFor742(params);
			for (int i = 0; i < listAllContent.size(); i++) {
				for (int j = 0; j < str.length; j++) {
					if (listAllContent.get(i).getCounter() != null) {
						if (listAllContent.get(i).getCounter().contains(str[j][0])) {
							int year_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(0, 4));
							int month_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(5, 7));
							if(selectType.contains("数量")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								}
							}else if(selectType.contains("结算价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								}
							}else if(selectType.contains("标价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								}
							}
						}
					}
				}
			}
		}else if (type.contains("门店")) {
			listAllContent = stoneService.findRoomFor742(params);
			for (int i = 0; i < listAllContent.size(); i++) {
				for (int j = 0; j < str.length; j++) {
					if (listAllContent.get(i).getRoom() != null) {
						if (listAllContent.get(i).getRoom().contains(str[j][0])) {
							int year_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(0, 4));
							int month_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(5, 7));
							if(selectType.contains("数量")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
								}
							}else if(selectType.contains("结算价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								}
							}else if(selectType.contains("标价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								}
							}
						}
					}
				}
			}
		}
		
		
		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < diff; j++) {
				if (j == 0) {
					result += str[i][j] + ",";
				} else if (j == diff - 1) {
					result += str[i][j] + "@";
				} else {
					result += str[i][j] + ",";
				}
			}
		}
		
		
		
		result += "#" + titleList;
		//System.out.println(result);
		return result;
	}

	/**
	 * index6 S743 销售结构分析 获取柜台
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:55:11
	 */
	@ApiOperation(value="跳转到s743页面",notes=" 销售结构分析")
	@GetMapping(value = "s743")
	public String s743(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listCounter = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listcounter", listCounter);
		return "s743";
	}

	/**
	 * index6 s743页面  查询
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:56:15
	 */
	@ApiOperation(value="s743页面  查询",notes="销售结构分析")
	@RequestMapping(value = "counterFind", method = RequestMethod.POST)
	@ResponseBody
	public String counterFind(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String counterName = request.getParameter("counter");
		String selectType = request.getParameter("selectType");

		System.out.println("柜台=============" + counterName);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		if(counterName.contains("所有") || counterName.contains("柜台")) {	
		}else {
			System.out.println("有柜台");
			params.put("counter", counterName);
		}
		
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		params.put("start", st);
		params.put("end", ed);

		System.out.println("start===" + st + "======end" + ed);

		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneByCounter(params);
		list = stoneService.findStoneByCounterFor743(params);
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				listProduct.add(list.get(i).getProduct());
				if(selectType.contains("数量")) {
					listProductNum.add(list.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(list.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(list.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(list.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}	
		//System.out.println(list);
		//System.out.println(listAll);
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAlllistprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();

		String result = "";
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllCounter.add(listAll.get(i).getCounter());
				listAlllistprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldprice());
			}
		}


		result = "" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + listAllSettlementprice 
				+ "@" + listAllCounter+ "@" + listAlllistprice+ "@" + listAllCenterstone+ "@" + listAllGoldweight;
		return result;
	}

	/**
	 * s743页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="s743页面 导出excel表格",notes="s743页面 导出excel表格")
	@RequestMapping(value = "downloadExcelFor743", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex6(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[]=con.split("&");
		String counterName = conList[0];
		String start = conList[1];
		String end = conList[2];
		

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(counterName.contains("所有") || counterName.contains("柜台")) {	
		}else {
			System.out.println("有柜台");
			params.put("counter", counterName);
		}
		params.put("start", start);
		params.put("end", end);

		List<StoneAnalysis> listAll = new ArrayList<>();
		listAll = stoneService.findStoneByCounter(params);
		stoneService.downloadExcelFor743(listAll,response);
		
		return result;
	}

	/**
	 * main 转s744 // s744 区域经营分析 获取地区
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午8:35:49
	 */
	@ApiOperation(value="跳转到s744页面，区域经营分析",notes="区域经营分析")
	@GetMapping(value = "s744")
	public String s744(ModelMap map, HttpSession session) throws ParseException {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listQuality = new ArrayList<>();
		List listCounter = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getArea() != null) {
				if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if (list.get(i).getRoom() != null) {
				if (!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
			if (list.get(i).getCounter() != null) {
				if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if (list.get(i).getQuality() != null) {
				if (!listQuality.contains(list.get(i).getQuality()) && list.get(i).getQuality().length() > 0) {
					listQuality.add(list.get(i).getQuality());
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listQuality, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listQuality", listQuality);
		return "s744";
	}

	/**
	 * s744
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午8:36:42
	 */
	@ApiOperation(value="s744页面，区域经营分析 查询",notes="区域经营分析")
	@RequestMapping(value = "areaFind", method = RequestMethod.POST)
	@ResponseBody
	public String areaFind(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String areaName = request.getParameter("area");
		String room = request.getParameter("room");
		String quality = request.getParameter("quality");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + areaName);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			System.out.println("有成色");
			params.put("quality", quality);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");

		System.out.println("start===" + st + "======end" + ed);

		params.put("start", st);
		params.put("end", ed);
	
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneByArea(params);
		list = stoneService.findStoneFor744(params);
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				listProduct.add(list.get(i).getProduct());
				if(selectType.contains("数量")) {
					listProductNum.add(list.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(list.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(list.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(list.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}	
		String result = "";
		
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllArea = new ArrayList<>();
		List listAllRoom = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllQuality = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllArea.add(listAll.get(i).getArea());
				listAllRoom.add(listAll.get(i).getRoom());
				listAllCounter.add(listAll.get(i).getCounter());
				listAllQuality.add(listAll.get(i).getQuality());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
			}
		}
		//System.out.println(list);
		//System.out.println(listAll);
		//System.out.println("===" + listProductNum);
		result = "" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + 
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllQuality+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
		return result;
	}

	/**
	 * s744页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="s744页面,区域经营分析 导出excel表格",notes="区域经营分析")
	@RequestMapping(value = "downloadExcelFor744", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex7(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[]=con.split("&");
		String areaName = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String quality = conList[3];
		

		String start = conList[4];
		String end = conList[5];

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			System.out.println("有成色");
			params.put("quality", quality);
		}
		
		params.put("start", start);
		params.put("end", end);

		List<StoneAnalysis> listAll = new ArrayList<>();
		listAll = stoneService.findStoneByArea(params);
		stoneService.downloadExcelFor744(listAll,response);
		
		return result;
	}

	/**
	 * index10 main跳转index10页面
	 * 
	 * @param map
	 * @return String created by lick on 2018年7月1日
	 */
	@ApiOperation(value="跳转到index10页面,销售数据统计")
	@GetMapping(value = "index10")
	public String index10(ModelMap map) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getArea() != null) {
					if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listArea.add(list.get(i).getArea());
					}
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);

		return "index10";
	}

	/**
	 * index10页面 通过销售区域获取柜台
	 * 
	 * @param request
	 * @return List created by lick on 2018年7月1日
	 */
	@ApiOperation(value="index10页面,销售数据统计,通过销售区域获取柜台")
	@RequestMapping(value = "findCounterByAreaForIndex10", method = RequestMethod.POST)
	@ResponseBody
	public List findCounterByAreaForIndex10(HttpServletRequest request) {
		String area = request.getParameter("area");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area", area);
		List<StoneAnalysis> list = stoneService.findCompareDataSetByParams(params);
		List listCounter = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
				listCounter.add(list.get(i).getCounter());
			}
		}
		return listCounter;
	}

	/**
	 * index10页面 通过销售区域和柜台获取名称
	 * 
	 * @param request
	 * @return List created by lick on 2018年7月1日
	 */
	@ApiOperation(value="index10页面,销售数据统计, 通过销售区域和柜台获取名称")
	@RequestMapping(value = "findProductByAreaAndCounterForIndex10", method = RequestMethod.POST)
	@ResponseBody
	public List findProductByAreaAndCounterForIndex10(HttpServletRequest request) {
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area", area);
		params.put("counter", counter);
		List<StoneAnalysis> list = stoneService.findCompareDataSetByParams(params);
		List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
				listProduct.add(list.get(i).getProduct());
			}
		}
		return listProduct;
	}

	/**
	 * index10页面
	 * 
	 * 查找数据，回显图标和表格
	 * 
	 * @param request
	 * @return List created by lick on 2018年7月1日 上午1:20:28
	 */
	@ApiOperation(value="index10页面,销售数据统计, 查找数据,回显图标和表格")
	@RequestMapping(value = "compareFindForIndex10", method = RequestMethod.POST)
	@ResponseBody
	public String compareFindForIndex10(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String selectChoice = request.getParameter("selectChoice");//年份
		String selectType = request.getParameter("selectType");
		
		
		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product + "\nselectChoice" + selectChoice + "\nselectType" + selectType);
		/*
		 * area双阳地区 counter柜台 product名称 selectChoice年度对比 selectType数量
		 */
		String result = "";
		List<StoneAnalysis> list = new ArrayList<>();
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area", area);
		if (!counter.contains("柜台")) {
			params.put("counter", counter);
			if (!product.contains("名称")) {
				params.put("product", product);
				list = stoneService.findTwoYearsDataSetForCompare(params);
				listAll = stoneService.findCompareDataSetByParams(params);
			} else {
				list = stoneService.findTwoYearsDataSetForCompare(params);
				listAll = stoneService.findCompareDataSetByParams(params);
			}
		} else {
			list = stoneService.findTwoYearsDataSetForCompare(params);
			listAll = stoneService.findCompareDataSetByParams(params);
		}

		List listY = new ArrayList<>();
		List listThisYearX = new ArrayList<>();
		List listLastYearX = new ArrayList<>();
		if (list != null) {
			Date date = new Date();
			int year = date.getYear();

			if (selectType.contains("销量")) {
				if (selectChoice.contains("年度对比")) {
					listY.add("年度");
					for (int i = 0; i < 1; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						if (y == year) {
							int num = 0;
							num = listThisYearX.get(0).hashCode() + list.get(i).getNumberSum();
							listThisYearX.set(0, num);
						} else {
							int num = 0;
							num = listLastYearX.get(0).hashCode() + list.get(i).getNumberSum();
							listLastYearX.set(0, num);
						}
					}
				} else if (selectChoice.contains("季度对比")) {
					listY.add("第一季度");
					listY.add("第二季度");
					listY.add("第三季度");
					listY.add("第四季度");

					for (int i = 0; i < 4; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						int m = list.get(i).getDate().getMonth() + 1;
						if (y == year) {// 今年
							if (m >= 1 && m <= 3) {
								int num = 0;
								num = listThisYearX.get(0).hashCode() + list.get(i).getNumberSum();
								listThisYearX.set(0, num);
							}
							if (m >= 4 && m <= 6) {
								int num = 0;
								num = listThisYearX.get(1).hashCode() + list.get(i).getNumberSum();
								listThisYearX.set(1, num);
							}
							if (m >= 7 && m <= 9) {
								int num = 0;
								num = listThisYearX.get(2).hashCode() + list.get(i).getNumberSum();
								listThisYearX.set(2, num);
							}
							if (m >= 10 && m <= 12) {
								int num = 0;
								num = listThisYearX.get(3).hashCode() + list.get(i).getNumberSum();
								listThisYearX.set(3, num);
							}

						} else {// 去年
							if (m >= 1 && m <= 3) {
								int num = 0;
								num = listLastYearX.get(0).hashCode() + list.get(i).getNumberSum();
								listLastYearX.set(0, num);
							}
							if (m >= 4 && m <= 6) {
								int num = 0;
								num = listLastYearX.get(1).hashCode() + list.get(i).getNumberSum();
								listLastYearX.set(1, num);
							}
							if (m >= 7 && m <= 9) {
								int num = 0;
								num = listLastYearX.get(2).hashCode() + list.get(i).getNumberSum();
								listLastYearX.set(2, num);
							}
							if (m >= 10 && m <= 12) {
								int num = 0;
								num = listLastYearX.get(3).hashCode() + list.get(i).getNumberSum();
								listLastYearX.set(3, num);
							}
						}
					}

				} else if (selectChoice.contains("月度对比")) {
					listY.add("1月");
					listY.add("2月");
					listY.add("3月");
					listY.add("4月");
					listY.add("5月");
					listY.add("6月");
					listY.add("7月");
					listY.add("8月");
					listY.add("9月");
					listY.add("10月");
					listY.add("11月");
					listY.add("12月");

					for (int i = 0; i < 12; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						int m = list.get(i).getDate().getMonth() + 1;
						if (y == year) {// 今年
							for (int j = 0; j < 12; j++) {
								if (m == j) {
									int num = 0;
									num = listThisYearX.get(j).hashCode() + list.get(i).getNumberSum();
									listThisYearX.set(j, num);
								}
							}

						} else {// 去年
							for (int j = 0; j < 12; j++) {
								if (m == j) {
									int num = 0;
									num = listLastYearX.get(j).hashCode() + list.get(i).getNumberSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}

			} else {// 营业额
				if (selectChoice.contains("年度对比")) {
					listY.add("年度");
					for (int i = 0; i < 1; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						if (y == year) {
							int num = 0;
							num = listThisYearX.get(0).hashCode() + list.get(i).getSettlementpriceSum();
							listThisYearX.set(0, num);
						} else {
							int num = 0;
							num = listLastYearX.get(0).hashCode() + list.get(i).getSettlementpriceSum();
							listLastYearX.set(0, num);
						}
					}
				} else if (selectChoice.contains("季度对比")) {
					listY.add("第一季度");
					listY.add("第二季度");
					listY.add("第三季度");
					listY.add("第四季度");

					for (int i = 0; i < 4; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						int m = list.get(i).getDate().getMonth() + 1;
						if (y == year) {// 今年
							if (m >= 1 && m <= 3) {
								int num = 0;
								num = listThisYearX.get(0).hashCode() + list.get(i).getSettlementpriceSum();
								listThisYearX.set(0, num);
							}
							if (m >= 4 && m <= 6) {
								int num = 0;
								num = listThisYearX.get(1).hashCode() + list.get(i).getSettlementpriceSum();
								listThisYearX.set(1, num);
							}
							if (m >= 7 && m <= 9) {
								int num = 0;
								num = listThisYearX.get(2).hashCode() + list.get(i).getSettlementpriceSum();
								listThisYearX.set(2, num);
							}
							if (m >= 10 && m <= 12) {
								int num = 0;
								num = listThisYearX.get(3).hashCode() + list.get(i).getSettlementpriceSum();
								listThisYearX.set(3, num);
							}

						} else {// 去年
							if (m >= 1 && m <= 3) {
								int num = 0;
								num = listLastYearX.get(0).hashCode() + list.get(i).getSettlementpriceSum();
								listLastYearX.set(0, num);
							}
							if (m >= 4 && m <= 6) {
								int num = 0;
								num = listLastYearX.get(1).hashCode() + list.get(i).getSettlementpriceSum();
								listLastYearX.set(1, num);
							}
							if (m >= 7 && m <= 9) {
								int num = 0;
								num = listLastYearX.get(2).hashCode() + list.get(i).getSettlementpriceSum();
								listLastYearX.set(2, num);
							}
							if (m >= 10 && m <= 12) {
								int num = 0;
								num = listLastYearX.get(3).hashCode() + list.get(i).getSettlementpriceSum();
								listLastYearX.set(3, num);
							}
						}
					}

				} else if (selectChoice.contains("月度对比")) {
					listY.add("1月");
					listY.add("2月");
					listY.add("3月");
					listY.add("4月");
					listY.add("5月");
					listY.add("6月");
					listY.add("7月");
					listY.add("8月");
					listY.add("9月");
					listY.add("10月");
					listY.add("11月");
					listY.add("12月");

					for (int i = 0; i < 12; i++) {
						listThisYearX.add(0);
						listLastYearX.add(0);
					}
					for (int i = 0; i < list.size(); i++) {
						int y = list.get(i).getDate().getYear();
						int m = list.get(i).getDate().getMonth() + 1;
						if (y == year) {// 今年
							for (int j = 0; j < 12; j++) {
								if (m == j) {
									int num = 0;
									num = listThisYearX.get(j).hashCode() + list.get(i).getSettlementpriceSum();
									listThisYearX.set(j, num);
								}
							}

						} else {// 去年
							for (int j = 0; j < 12; j++) {
								if (m == j) {
									int num = 0;
									num = listLastYearX.get(j).hashCode() + list.get(i).getSettlementpriceSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		}
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll != null) {
			for (int j = 0; j < listAll.size(); j++) {
				listArea.add(listAll.get(j).getArea());
				listRoom.add(listAll.get(j).getRoom());
				listCounter.add(listAll.get(j).getCounter());
				listProduct.add(listAll.get(j).getProduct());
				listSettlementprice.add(listAll.get(j).getSettlementprice());
				listDate.add(sdf.format(listAll.get(j).getDate()));
			}
		}
		//System.out.println(listY);
		//System.out.println(listThisYearX);
		//System.out.println(listLastYearX);
		result = "" + listY + "@" + listThisYearX + "@" + listLastYearX + "@" + listArea + "@" + listCounter + "@" + listProduct + "@" + listSettlementprice + "@" + listDate + "@" + listRoom;
		System.out.println(result);
		return result;
	}

	/**
	 * index10页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index10页面,销售数据统计, 导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex10", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex10(HttpServletRequest request) {
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");

		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product);

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area", area);
		if (!counter.contains("柜台")) {
			params.put("counter", counter);
			if (!product.contains("名称")) {
				params.put("product", product);
				listAll = stoneService.findCompareDataSetByParams(params);
			} else {
				listAll = stoneService.findCompareDataSetByParams(params);
			}
		} else {
			listAll = stoneService.findCompareDataSetByParams(params);
		}
		System.out.println("====" + listAll + "====");
		if (listAll.size() != 0) {
			int re = stoneService.downloadExcelForIndex10(listAll);
			if (re == 1) {
				result = "下载成功，下载文件放在D:\\analysisFile下";
			} else {
				result = "下载出错";
			}
		} else {
			result = "没有数据，下载失败";
		}

		return result;
	}

	/**
	 * 跳转到productsum页面 供应商品群分析
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="productsum页面,供应商品群分析")
	@GetMapping(value = "productsum")
	public String productsum(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listSupplier = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			String supplier = list.get(i).getSupplier();
			String product = list.get(i).getProduct();
			int index0 = listSupplier.indexOf(supplier);
			if (index0 == -1) {
				listSupplier.add(supplier);
			}
		}
		map.addAttribute("listsupplier", listSupplier);

		List listdate = (List) session.getAttribute("listdate");
		List listnum = (List) session.getAttribute("listnum");
		String supplierName = (String) session.getAttribute("supplierName");
		List<StoneAnalysis> supplierList = (List<StoneAnalysis>) session.getAttribute("list");
		List productList = (List) session.getAttribute("product");
		List listProduct = (List) session.getAttribute("listProduct");
		List listProductCount = (List) session.getAttribute("listProductCount");

		map.addAttribute("listdate", listdate);
		map.addAttribute("productList", productList);
		map.addAttribute("supplierList", supplierList);
		map.addAttribute("supplierName", supplierName);
		map.addAttribute("listProductCount", listProductCount);
		map.addAttribute("listProduct", listProduct);
		return "productsum";
	}

	/**
	 * 跳转到index9页面 销售结构分析
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="index9页面,销售结构分析")
	@GetMapping(value = "s752")
	public String s752(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCounter() != null) {
				if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if (list.get(i).getProduct() != null) {
				if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
					listProduct.add(list.get(i).getProduct());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);
		
		return "index9";
	}
	/**
	 * index9页面 销售结构分析 查询
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="index9页面,销售结构分析,查询")
	@RequestMapping(value = "compareCounterInTwoYearOfIndex9", method = RequestMethod.POST)
	@ResponseBody
	public String compareCounterInTwoYearOfIndex9(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String selectType = request.getParameter("selectType");

		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String start = request.getParameter("selectTimeStart");
		String end = request.getParameter("selectTimeEnd");

		String endOfY = end.substring(0, 4) + "-01-01";

		int year_s = Integer.parseInt(start.substring(0, 4));
		int year_e = Integer.parseInt(end.substring(0, 4));
		int month_s = Integer.parseInt(start.substring(5, 7));
		int month_e = Integer.parseInt(end.substring(5, 7));
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			params.put("counter", counter);
		}
		if(product.contains("所有") || product.contains("名称")) {
		}else {
			params.put("product", product);
		}
		params.put("start", start);
		params.put("end", end);
		
		List startList = new ArrayList<>(); // 构建今年月列表
		List endList = new ArrayList<>();// 构建去年月列表
		String st = start.substring(0, 7);
		String ed = endOfY.substring(0, 7);
		startList.add(st);
		endList.add(ed);
		for (int i = 1; i < 12; i++) {
			start = StoneAnalysisController.monthAddFrist(start);
			startList.add(start);
		}
		for (int i = 1; i < 12; i++) {
			endOfY = StoneAnalysisController.monthAddFrist(endOfY);
			endList.add(endOfY);
		}
		String result = "";
		List listThisYearX = new ArrayList<>();// 今年每个月销售
		List listLastYearX = new ArrayList<>();// 去年每个月销售
		List<StoneAnalysis> list = new  ArrayList<StoneAnalysis>();
		if((!counter.contains("所有") && !counter.contains("柜台")) && (product.contains("所有") || product.contains("名称")) ) {	
			System.out.println("根据柜台查询");
			 list = stoneService.findStoneByCounterAndTimeOfIndex9(params);
		}else {
			System.out.println("根据商品查询");
			list = stoneService.findStoneByProductAndTimeOfIndex9(params);	
		}
		
		
		if (selectType.contains("数量")) {
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = listThisYearX.get(j).hashCode() + list.get(i).getNumberSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = listLastYearX.get(j).hashCode() + list.get(i).getNumberSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		} else if (selectType.contains("结算价")){// 获取营业额
			
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = listThisYearX.get(j).hashCode() + list.get(i).getSettlementpriceSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = listLastYearX.get(j).hashCode() + list.get(i).getSettlementpriceSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		} else if (selectType.contains("标价")){// 获取营业额
			
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listThisYearX.get(j).toString()) + list.get(i).getListpriceSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listLastYearX.get(j).toString()) + list.get(i).getListpriceSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		} else if (selectType.contains("金重")){// 获取营业额
			
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listThisYearX.get(j).toString()) + list.get(i).getGoldweightSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listLastYearX.get(j).toString()) + list.get(i).getGoldweightSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		} else if (selectType.contains("主石")){// 获取营业额
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listThisYearX.get(j).toString()) + list.get(i).getCenterstoneSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listLastYearX.get(j).toString()) + list.get(i).getCenterstoneSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
		}	
		//System.out.println(startList);
		//System.out.println(endList);
		//System.out.println(listThisYearX);
		//System.out.println(listLastYearX);
		result = "" + startList + "@" + endList + "@" + listThisYearX + "@" + listLastYearX;
		return result;
	}

	/**
	 * 跳转到s75页面   管理分析模型
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月2日 上午9:42:32
	 */
	@ApiOperation(value="跳转到s75页面,管理分析")
	@GetMapping(value = "s75")
	public String s75(ModelMap map, HttpSession session) throws ParseException {
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listProduct = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listArea = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getProduct()!=null) {
				if(!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length()>0) {
					listProduct.add(list.get(i).getProduct());
				}
			}
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
		}
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listproduct", listProduct);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		
		return "s75";
	}
	/**
	 * s75 商品系列 TOPN 分析模型
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年9月20日
	 */
	@ApiOperation(value="s75页面,管理分析,查询")
	@RequestMapping(value = "newA", method = RequestMethod.POST)
	@ResponseBody
	public String newA(HttpServletRequest request) {
		String selectType = request.getParameter("selectType");
		String select = request.getParameter("select");
		String product = request.getParameter("product");
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String room = request.getParameter("room");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		System.out.println("类别=============" + selectType);
		System.out.println("选择=============" + select);
		System.out.println("名称=============" + product);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> params = new HashMap<String, Object>();
		if(product.contains("名称")||product.contains("所有")) {
		}else {
			params.put("product", product);
		}
		if(room.contains("门店")||room.contains("所有")) {
		}else {
			params.put("room", room);
		}
		if(counter.contains("柜台")||counter.contains("所有")) {
		}else {
			params.put("counter", counter);
		}
		if(area.contains("地区")||area.contains("所有")) {
		}else {
			params.put("area", area);
		}
		
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("st===" + st + ",ed======" + ed);
		params.put("start", st);
		params.put("end", ed);

		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据

		List listX = new ArrayList<>();
		List listY = new ArrayList<>();

		
			if (select.contains("地区")) {
				if (selectType.contains("数量")) {
					list = stoneService.findAreaAccountByProductAndTime(params);		
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getNumberSum());
						}
					}
				} else if (selectType.contains("结算价")){
					list = stoneService.findAreaAccountByProductAndTime(params);
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getSettlementpriceSum());
						}
					}
				} else if (selectType.contains("标价")){
					list = stoneService.findAreaAccountByProductAndTime(params);
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getListpriceSum());
						}
					}
				} else if (selectType.contains("金重")){
					list = stoneService.findAreaAccountByProductAndTime(params);
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getGoldweightSum());
						}
					}
				} else if (selectType.contains("主石")){
					list = stoneService.findAreaAccountByProductAndTime(params);
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getCenterstoneSum());
						}
					}
				}
			}
			if (select.contains("门店")) {
				if (selectType.contains("数量")) {
					list = stoneService.findRoomAccountByProductAndTime(params);	
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getRoom());
							listY.add(list.get(i).getNumberSum());
						}
					}
				} else if (selectType.contains("结算价")){
					list = stoneService.findRoomAccountByProductAndTime(params);	
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getRoom());
							listY.add(list.get(i).getSettlementpriceSum());
						}
					}
				} else if (selectType.contains("标价")){
					list = stoneService.findRoomAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getRoom());
							listY.add(list.get(i).getListpriceSum());
						}
					}
				} else if (selectType.contains("金重")){
					list = stoneService.findRoomAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getRoom());
							listY.add(list.get(i).getGoldweightSum());
						}
					}
				} else if (selectType.contains("主石")){
					list = stoneService.findRoomAccountByProductAndTime(params);				
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getRoom());
							listY.add(list.get(i).getCenterstoneSum());
						}
					}
				}
			}
			if (select.contains("柜台")) {
				if (selectType.contains("数量")) {
					list = stoneService.findCounterAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getCounter());
							listY.add(list.get(i).getNumberSum());
						}
					}
				} else if (selectType.contains("结算价")){
					list = stoneService.findCounterAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getCounter());
							listY.add(list.get(i).getSettlementpriceSum());
						}
					}
				} else if (selectType.contains("标价")){
					list = stoneService.findCounterAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getCounter());
							listY.add(list.get(i).getListpriceSum());
						}
					}
				} else if (selectType.contains("金重")){
					list = stoneService.findCounterAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getCounter());
							listY.add(list.get(i).getGoldweightSum());
						}
					}
				} else if (selectType.contains("主石")){
					list = stoneService.findCounterAccountByProductAndTime(params);					
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getCounter());
							listY.add(list.get(i).getCenterstoneSum());
						}
					}
				}
			}
		
		String result = "";
		result = "" + listY + "@" + listX;
		//System.out.println(result);

		return result;
	}
	
	/**
	 * main跳转到index11页面  供应商销售排名分析  获取供应商
	 * 
	 * @param map
	 * @return String created by lick on 2018年7月1日
	 */
	@ApiOperation(value="跳转到index11页面,供应商销售排名分析")
	@GetMapping(value = "index11")
	public String index11(ModelMap map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date da = new Date();
		Date date = null;
		String end_this = sdf.format(da);//2018-07-03
		String start_this = sdf.format(da).substring(0, 4)+"-01-01";//2018-01-01
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		try {
			date = sdf.parse(sdf.format(da));
		} catch (Exception e) {
		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, -12);// 在日历的月份上减去6个月
		// System.out.println(sdf.format(c.getTime()));//得到6个月后的日期
		String end_last = sdf.format(c.getTime());//2017-07-03
		String start_last = sdf.format(c.getTime()).substring(0, 4)+"-01-01";//2017-01-01
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start_this);
		params.put("end", end_this);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", start_last);
		params1.put("end", end_last);
		System.out.println("end_last="+end_last);
		System.out.println("start_last="+start_last);
		System.out.println("end_this="+end_this);
		System.out.println("start_this="+start_this);
		List<StoneAnalysis> list = stoneService.findSupplierForIndex11(params);
		List listSupplier = new ArrayList<>();
		List listThisYear = new ArrayList<>();
		List listLastYear = new ArrayList<>();
		if(list.size()>=10) {
			for(int i=0;i<list.size();i++) {
				listSupplier.add(list.get(i).getSupplier());
				listThisYear.add(list.get(i).getNumberSum());
				params1.put("supplier", list.get(i).getSupplier());
				List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
				if(one.size()>0) {
					listLastYear.add(one.get(0).getNumberSum());
				}else {
					listLastYear.add(0);
				}
			}
		}
		
		List<StoneAnalysis> listAll = stoneService.findAllStone();
		List listCounter = new ArrayList<>();
        List listProduct = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				if (listAll.get(i).getCounter() != null) {
					if (!listCounter.contains(listAll.get(i).getCounter()) && listAll.get(i).getCounter().length() > 0) {
						listCounter.add(listAll.get(i).getCounter());
					}
				}
                if (listAll.get(i).getProduct() != null) {
                    if (!listProduct.contains(listAll.get(i).getProduct()) && listAll.get(i).getProduct().length() > 0) {
                        listProduct.add(listAll.get(i).getProduct());
                    }
                }
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
        Collections.sort(listProduct, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });
		map.addAttribute("listProduct", listProduct);
        map.addAttribute("listCounter", listCounter);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listThisYear", listThisYear);
		map.addAttribute("listLastYear", listLastYear);
		
		return "index11";
	}
	/**
	 * index11页面 
	 * 根据销量或营业额获取供应商
	 *
	 * @return String
	 * created  at 2018年7月3日
	 */
	@ApiOperation(value="index11页面,供应商销售排名分析,根据销量或营业额获取供应商")
	@RequestMapping(value = "findSupplierForindex11",method=RequestMethod.POST)
	@ResponseBody
	public String findSupplierForindex11(HttpServletRequest request) {
		String selectType = request.getParameter("selectType");
		String counter = request.getParameter("counter");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date da = new Date();
		Date date = null;
		String end_this = sdf.format(da);//2018-07-03
		String start_this = sdf.format(da).substring(0, 4)+"-01-01";//2018-01-01
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		try {
			date = sdf.parse(sdf.format(da));
		} catch (Exception e) {
		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, -12);// 在日历的月份上减去12个月
		// System.out.println(sdf.format(c.getTime()));//得到12个月后的日期
		String end_last = sdf.format(c.getTime());//2017-07-03
		String start_last = sdf.format(c.getTime()).substring(0, 4)+"-01-01";//2017-01-01
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start_this);
		params.put("end", end_this);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", start_last);
		params1.put("end", end_last);
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			params.put("counter", counter);
			params1.put("counter", counter);
		}
		String result="";
		List listSupplier = new ArrayList<>();
		List listThisYear = new ArrayList<>();
		List listLastYear = new ArrayList<>();
		List<StoneAnalysis> list = stoneService.findSupplierForIndex11(params);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				listSupplier.add(list.get(i).getSupplier());
				if(selectType.contains("数量")) {
					listThisYear.add(list.get(i).getNumberSum());
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
					if(one.size()>0) {
						listLastYear.add(one.get(0).getNumberSum());
					}else {
						listLastYear.add(0);
					}
				}else if(selectType.contains("结算价")) {
					listThisYear.add(list.get(i).getSettlementpriceSum() );
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
					if(one.size()>0) {
						listLastYear.add(one.get(0).getSettlementpriceSum());
					}else {
						listLastYear.add(0);
					}
				}else if(selectType.contains("标价")) {
					listThisYear.add(list.get(i).getListpriceSum() );
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
					if(one.size()>0) {
						listLastYear.add(one.get(0).getListpriceSum());
					}else {
						listLastYear.add(0);
					}
				}else if(selectType.contains("金重")) {
					listThisYear.add(list.get(i).getGoldweightSum() );
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
					if(one.size()>0) {
						listLastYear.add(one.get(0).getGoldweightSum());
					}else {
						listLastYear.add(0);
					}
				}else if(selectType.contains("主石")) {
					listThisYear.add(list.get(i).getCenterstoneSum() );
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findOneSupplierForIndex11(params1);
					if(one.size()>0) {
						listLastYear.add(one.get(0).getCenterstoneSum());
					}else {
						listLastYear.add(0);
					}
				}
			}
		}
		result = ""+listSupplier+"@"+listThisYear+"@"+listLastYear;
		//System.out.println(listSupplier);
		//System.out.println(listThisYear);
		//System.out.println(listLastYear);
		return result;
	}
	/**
	 * main跳转到index811页面 周销售数据统计
	 * 
	 * @return String
	 * created  on 2018年7月3日 下午10:10:33
	 */
	@ApiOperation(value="跳转到index811页面,周销售数据统计")
	@GetMapping(value="index811")
	public String index811(ModelMap map) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getArea() != null) {
					if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listArea.add(list.get(i).getArea());
					}
				}
				if (list.get(i).getRoom() != null) {
					if (!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
						listRoom.add(list.get(i).getRoom());
					}
				}
				if (list.get(i).getCounter() != null) {
					if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listCounter.add(list.get(i).getCounter());
					}
				}
				if (list.get(i).getProduct() != null) {
					if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
						listProduct.add(list.get(i).getProduct());
					}
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);

		return "index811";
	}
	/**
	 * main跳转到index812页面 月销售数据统计
	 * 
	 * @return String
	 * created  on 2018年7月3日 下午10:10:33
	 */
	@ApiOperation(value="跳转到index812页面,月销售数据统计")
	@GetMapping(value="index812")
	public String index812(ModelMap map) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getArea() != null) {
					if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listArea.add(list.get(i).getArea());
					}
				}
				if (list.get(i).getRoom() != null) {
					if (!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
						listRoom.add(list.get(i).getRoom());
					}
				}
				if (list.get(i).getCounter() != null) {
					if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listCounter.add(list.get(i).getCounter());
					}
				}
				if (list.get(i).getProduct() != null) {
					if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
						listProduct.add(list.get(i).getProduct());
					}
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);

		return "index812";
	}
	/**
	 * main跳转到index813页面  季度销售数据统计
	 * 
	 * @return String
	 * created  on 2018年7月3日 下午10:10:33
	 */
	@ApiOperation(value="跳转到index813页面,季度销售数据统计")
	@GetMapping(value="index813")
	public String index813(ModelMap map) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getArea() != null) {
					if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listArea.add(list.get(i).getArea());
					}
				}
				if (list.get(i).getRoom() != null) {
					if (!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
						listRoom.add(list.get(i).getRoom());
					}
				}
				if (list.get(i).getCounter() != null) {
					if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listCounter.add(list.get(i).getCounter());
					}
				}
				if (list.get(i).getProduct() != null) {
					if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
						listProduct.add(list.get(i).getProduct());
					}
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);

		return "index813";
	}
	/**
	 * main跳转到index814页面 年销售数据统计
	 * 
	 * @return String
	 * created  on 2018年7月3日 下午10:10:33
	 */
	@ApiOperation(value="跳转到index814页面,年销售数据统计")
	@GetMapping(value="index814")
	public String index814(ModelMap map ) {
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getArea() != null) {
					if (!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length() > 0) {
						listArea.add(list.get(i).getArea());
					}
				}
				if (list.get(i).getRoom() != null) {
					if (!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length() > 0) {
						listRoom.add(list.get(i).getRoom());
					}
				}
				if (list.get(i).getCounter() != null) {
					if (!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length() > 0) {
						listCounter.add(list.get(i).getCounter());
					}
				}
				if (list.get(i).getProduct() != null) {
					if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
						listProduct.add(list.get(i).getProduct());
					}
				}
			}
		}
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listArea", listArea);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listProduct", listProduct);

		return "index814";
	}
	/**
	 * index814页面
	 * 查询名称
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午12:44:43
	 */
	@ApiOperation(value="index814页面,年销售数据统计,查询")
	@RequestMapping(value="findProductOnIndex814",method=RequestMethod.POST)
	@ResponseBody
	public String findProductOnIndex814(HttpServletRequest request ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String area = request.getParameter("area");//销售区域
		String counter = request.getParameter("counter");//柜台
		String product = request.getParameter("product");//名称
		String room = request.getParameter("room");//门店
		String selectChoice = request.getParameter("selectChoice");//年份
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		String selectType = request.getParameter("selectType");//指标（数量，标价，结算价，金重，主石）

		System.out.println("area=" + area + "\ncounter=" + counter + "\nproduct=" + product +"\nroom="+room+"\nselectChoice=" + selectChoice + "\nselectType=" + selectType);
		/*
		 * area双阳地区 counter柜台 product名称 selectChoice年度对比 selectType数量
		 */
		String result = "";
		List<StoneAnalysis> list = new ArrayList<>();//图数据
		List<StoneAnalysis> listAll = new ArrayList<>();//表格数据
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		list = stoneService.findProductOfIndex814(params);
		listAll = stoneService.findDateForIndex8888(params);
		
		
		List listX = new ArrayList<>();
		List listY = new ArrayList<>();
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if (!listX.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    String regex="^[0-9].*$";
                    Pattern p = Pattern.compile(regex);
                    //数字开头 加个下划线
                    if(p.matcher(list.get(i).getProduct()).matches()) {
                        listX.add("_"+list.get(i).getProduct());
                    }else {
                        listX.add(list.get(i).getProduct());
                    }
				}
			}
		}
		for (int i = 0; i < listX.size(); i++) {
			listY.add(0);
		}
		
		
		
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				
				if(selectType.contains("销量")) {
					int num = 0;
					for (int j = 0; j < listX.size(); j++) {
						if(listX.get(j).toString().contains(list.get(i).getProduct())) {
							num = listY.get(j).hashCode() + list.get(i).getNumberSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("结算价")) {
					int num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(list.get(i).getProduct())) {
							num = listY.get(j).hashCode() + list.get(i).getSettlementpriceSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("标价")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(list.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + list.get(i).getListpriceSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("金重")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(list.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + list.get(i).getGoldweightSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("主石")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(list.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + list.get(i).getCenterstoneSum();
							listY.set(j, num);
						}
					}
				}
			}
		}

		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listListprice = new ArrayList<>();
		List listCenterstone = new ArrayList<>();
		List listGoldweight = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll.size()>0) {
			for (int j = 0; j < listAll.size(); j++) {
				listArea.add(listAll.get(j).getArea());
				listRoom.add(listAll.get(j).getRoom());
				listCounter.add(listAll.get(j).getCounter());
				listProduct.add(listAll.get(j).getProduct());
				listSettlementprice.add(listAll.get(j).getSettlementprice());
				listListprice.add(listAll.get(j).getListprice());
				listGoldweight.add(listAll.get(j).getGoldweight());
				listCenterstone.add(listAll.get(j).getCenterstone());
				listDate.add(sdf.format(listAll.get(j).getDate()));
			}
		}
		
		
		result=""+listX+"@"+listY+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone;
		
		
		return result;
	}
	/**
	 * index812页面
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午12:45:40
	 */
	@ApiOperation(value="index812页面,月销售数据统计,查询")
	@RequestMapping(value="findProductOnIndex812",method=RequestMethod.POST)
	@ResponseBody
	public String findProductOnIndex812(HttpServletRequest request ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String room = request.getParameter("room");
		String selectChoice = request.getParameter("selectChoice");
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		String selectType = request.getParameter("selectType");

		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product + "\nselectChoice" + selectChoice + "\nselectType" + selectType);
		/*
		 * area双阳地区 counter柜台 product名称 selectChoice年度对比 selectType数量
		 */
		String result = "";
		List<StoneAnalysis> list = new ArrayList<>();//图数据
		List<StoneAnalysis> listAll = new ArrayList<>();//表格数据
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		list = stoneService.findProductOfIndex812(params);
		listAll = stoneService.findDateForIndex8888(params);
		
		
		List listX = new ArrayList<>();
		List listY1 = new ArrayList<>();
		List listY2 = new ArrayList<>();
		List listY3 = new ArrayList<>();
		List listY4 = new ArrayList<>();
		List listY5 = new ArrayList<>();
		List listY6 = new ArrayList<>();
		List listY7 = new ArrayList<>();
		List listY8 = new ArrayList<>();
		List listY9 = new ArrayList<>();
		List listY10 = new ArrayList<>();
		List listY11 = new ArrayList<>();
		List listY12 = new ArrayList<>();
		
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					if (!listX.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                        String regex="^[0-9].*$";
                        Pattern p = Pattern.compile(regex);
                        //数字开头 加个下划线
                        if(p.matcher(list.get(i).getProduct()).matches()) {
                            listX.add("_"+list.get(i).getProduct());
                        }else {
                            listX.add(list.get(i).getProduct());
                        }
					}
				}
			}
		
		for (int i = 0; i <listX.size(); i++) {
			listY1.add(0);
			listY2.add(0);
			listY3.add(0);
			listY4.add(0);
			listY5.add(0);
			listY6.add(0);
			listY7.add(0);
			listY8.add(0);
			listY9.add(0);
			listY10.add(0);
			listY11.add(0);
			listY12.add(0);
			
		}
		
		if(selectType.contains("销量")) {
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
		
					if (m == 1) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY5.get(j).hashCode() + list.get(i).getNumberSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY6.get(j).hashCode() + list.get(i).getNumberSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY7.get(j).hashCode() + list.get(i).getNumberSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY8.get(j).hashCode() + list.get(i).getNumberSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY9.get(j).hashCode() + list.get(i).getNumberSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY10.get(j).hashCode() + list.get(i).getNumberSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY11.get(j).hashCode() + list.get(i).getNumberSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY12.get(j).hashCode() + list.get(i).getNumberSum();
								listY12.set(j, num);
							}
						}
					}
					
					
				}
			}
		}else if(selectType.contains("结算价")){//标价
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY5.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY6.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY7.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY8.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY9.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY10.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY11.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY12.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("标价")){//标价
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getListpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getListpriceSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getListpriceSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + list.get(i).getListpriceSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + list.get(i).getListpriceSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + list.get(i).getListpriceSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + list.get(i).getListpriceSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + list.get(i).getListpriceSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + list.get(i).getListpriceSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("金重")){
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getGoldweightSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getGoldweightSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + list.get(i).getGoldweightSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + list.get(i).getGoldweightSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + list.get(i).getGoldweightSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + list.get(i).getGoldweightSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + list.get(i).getGoldweightSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + list.get(i).getGoldweightSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("主石")){
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listListprice = new ArrayList<>();
		List listCenterstone = new ArrayList<>();
		List listGoldweight = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll.size()>0) {
			for (int j = 0; j < listAll.size(); j++) {
				listArea.add(listAll.get(j).getArea());
				listRoom.add(listAll.get(j).getRoom());
				listCounter.add(listAll.get(j).getCounter());
				listProduct.add(listAll.get(j).getProduct());
				listSettlementprice.add(listAll.get(j).getSettlementprice());
				listListprice.add(listAll.get(j).getListprice());
				listGoldweight.add(listAll.get(j).getGoldweight());
				listCenterstone.add(listAll.get(j).getCenterstone());
				listDate.add(sdf.format(listAll.get(j).getDate()));
			}
		}
		//System.out.println("listY1="+listY1);
		//System.out.println("listY2="+listY2);
		//System.out.println("listY3="+listY3);
		//System.out.println("listY4="+listY4);
		
		result=""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4+"@"+
				listY5+"@"+listY6+"@"+listY7+"@"+listY8+"@"+listY9+"@"+listY10+"@"+listY11+"@"+listY12
				+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone;
		
		
		return result;
	}
	/**
	 * index813页面
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午12:45:40
	 */
	@ApiOperation(value="index813页面,季度销售数据统计,查询")
	@RequestMapping(value="findProductOnIndex813",method=RequestMethod.POST)
	@ResponseBody
	public String findProductOnIndex813(HttpServletRequest request ) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String room = request.getParameter("room");
		String selectChoice = request.getParameter("selectChoice");
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		String selectType = request.getParameter("selectType");

		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product + "\nselectChoice" + selectChoice + "\nselectType" + selectType);
		/*
		 * area双阳地区 counter柜台 product名称 selectChoice年度对比 selectType数量
		 */
		String result = "";
		List<StoneAnalysis> list = new ArrayList<>();//图数据
		List<StoneAnalysis> listAll = new ArrayList<>();//表格数据
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		list = stoneService.findProductOfIndex813(params);
		listAll = stoneService.findDateForIndex8888(params);
		
		List listX = new ArrayList<>();
		List listY1 = new ArrayList<>();
		List listY2 = new ArrayList<>();
		List listY3 = new ArrayList<>();
		List listY4 = new ArrayList<>();
		
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if (!listX.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    String regex="^[0-9].*$";
                    Pattern p = Pattern.compile(regex);
                    //数字开头 加个下划线
                    if(p.matcher(list.get(i).getProduct()).matches()) {
                        listX.add("_"+list.get(i).getProduct());
                    }else {
                        listX.add(list.get(i).getProduct());
                    }
				}
			}
		}
		for (int i = 0; i <listX.size(); i++) {
			listY1.add(0);
			listY2.add(0);
			listY3.add(0);
			listY4.add(0);
			
		}
		
		
		
		if(selectType.contains("销量")) {
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					int m = list.get(i).getDate().getMonth() + 1;
					if (m >= 1 && m <= 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if (m >= 4 && m <= 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("结算价")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m >= 4 && m <= 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("标价")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getListpriceSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("金重")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("主石")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int m = list.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listListprice = new ArrayList<>();
		List listCenterstone = new ArrayList<>();
		List listGoldweight = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll.size()>0) {
			for (int j = 0; j < listAll.size(); j++) {
				listArea.add(listAll.get(j).getArea());
				listRoom.add(listAll.get(j).getRoom());
				listCounter.add(listAll.get(j).getCounter());
				listProduct.add(listAll.get(j).getProduct());
				listSettlementprice.add(listAll.get(j).getSettlementprice());
				listListprice.add(listAll.get(j).getListprice());
				listGoldweight.add(listAll.get(j).getGoldweight());
				listCenterstone.add(listAll.get(j).getCenterstone());
				listDate.add(sdf.format(listAll.get(j).getDate()));
			}
		}
		//System.out.println("listX="+listX);
		//System.out.println("listY1="+listY1);
		//System.out.println("listY2="+listY2);
		//System.out.println("listY3="+listY3);
		//System.out.println("listY4="+listY4);
		
		result=""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone;
		
		
		return result;
	}
	/**
	 * index811页面
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午12:45:40
	 * @throws ParseException 
	 */
	@ApiOperation(value="index811页面,周销售数据统计,查询")
	@RequestMapping(value="findProductOnIndex811",method=RequestMethod.POST)
	@ResponseBody
	public String findProductOnIndex811(HttpServletRequest request ) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		String area = request.getParameter("area");
		String counter = request.getParameter("counter");
		String product = request.getParameter("product");
		String room = request.getParameter("room");
		String selectChoiceYear = request.getParameter("selectChoiceYear");
		String selectChoiceMonth = request.getParameter("selectChoiceMonth");
		String start = selectChoiceYear+"-"+selectChoiceMonth+"-01";
		int year = Integer.parseInt(selectChoiceYear);
		String end="";
		int days=0;
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {// 闰年
			if(selectChoiceMonth.contains("2")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-29";
				days=29;
			}else if(selectChoiceMonth.contains("4") || selectChoiceMonth.contains("6") || selectChoiceMonth.contains("9") || selectChoiceMonth.contains("11")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-30";
				days=30;
			}else {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-31";
				days=31;
			}
		}else {
			if(selectChoiceMonth.contains("2")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-28";
				days=28;
			}else if(selectChoiceMonth.contains("4") || selectChoiceMonth.contains("6") || selectChoiceMonth.contains("9") || selectChoiceMonth.contains("11")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-30";
				days=30;
			}else {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-31";
				days=31;
			}
		}
		
		
		String selectType = request.getParameter("selectType");

		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product + "\nselectType" + selectType+"\nyear="+selectChoiceYear+"\nmonth="+selectChoiceMonth);
		/*
		 * area双阳地区 counter柜台 product名称 selectChoice年度对比 selectType数量
		 */
		String result = "";
		List<StoneAnalysis> list = new ArrayList<>();//图数据
		List<StoneAnalysis> listAll = new ArrayList<>();//表格数据
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		System.out.println("start="+start);
		System.out.println("end="+end);
		
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		list = stoneService.findProductOfIndex811(params);
		listAll = stoneService.findDateForIndex8888(params);
		
		List listX = new ArrayList<>();
		List listY1 = new ArrayList<>();
		List listY2 = new ArrayList<>();
		List listY3 = new ArrayList<>();
		List listY4 = new ArrayList<>();
		List listY5 = new ArrayList<>();
		List listY6 = new ArrayList<>();
		
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					if (!listX.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                        String regex="^[0-9].*$";
                        Pattern p = Pattern.compile(regex);
                        //数字开头 加个下划线
                        if(p.matcher(list.get(i).getProduct()).matches()) {
                            listX.add("_"+list.get(i).getProduct());
                        }else {
                            listX.add(list.get(i).getProduct());
                        }
					}
				}
			}
		
		for (int i = 0; i <listX.size(); i++) {
			listY1.add(0);
			listY2.add(0);
			listY3.add(0);
			listY4.add(0);
			listY5.add(0);
			listY6.add(0);
		}
		int sum=0;
		List listMonday=new ArrayList<>();
		for (int i = 1; i <= days; i++) {
			String da = selectChoiceYear+"-"+selectChoiceMonth+"-"+i;
			if(dateFm.format(sdf.parse(da)).equals("星期一")) {
				listMonday.add(i);
				sum++;
			}
			
		}
		//System.out.println("sum="+sum);
		//System.out.println("listMonday="+listMonday);
		if(selectType.contains("销量")) {
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int thisday = Integer.parseInt(list.get(i).getDate().toString().substring(8, 10));//这个月的几号
					//System.out.println(thisday+"---------");
					if(thisday>=1 && thisday <listMonday.get(0).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(0).hashCode() && thisday <listMonday.get(1).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(1).hashCode() && thisday <listMonday.get(2).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(2).hashCode() && thisday <listMonday.get(3).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=listMonday.get(3).hashCode() && thisday <listMonday.get(4).hashCode()) {
							int num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(list.get(i).getProduct())) {
									num = listY5.get(j).hashCode() + list.get(i).getNumberSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=listMonday.get(4).hashCode() && thisday <listMonday.get(5).hashCode()) {
								int num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(list.get(i).getProduct())) {
										num = listY6.get(j).hashCode() + list.get(i).getNumberSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
					
					
				}
			}
		}else if(selectType.contains("结算价")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int thisday = Integer.parseInt(list.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <listMonday.get(0).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY1.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(0).hashCode() && thisday <listMonday.get(1).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY2.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(1).hashCode() && thisday <listMonday.get(2).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY3.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(2).hashCode() && thisday <listMonday.get(3).hashCode()) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = listY4.get(j).hashCode() + list.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=listMonday.get(3).hashCode() && thisday <listMonday.get(4).hashCode()) {
							int num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(list.get(i).getProduct())) {
									num = listY5.get(j).hashCode() + list.get(i).getSettlementpriceSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=listMonday.get(4).hashCode() && thisday <listMonday.get(5).hashCode()) {
								int num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(list.get(i).getProduct())) {
										num = listY6.get(j).hashCode() + list.get(i).getSettlementpriceSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("标价")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int thisday = Integer.parseInt(list.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <listMonday.get(0).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
					
					}
					if(thisday>=listMonday.get(0).hashCode() && thisday <listMonday.get(1).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(1).hashCode() && thisday <listMonday.get(2).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(2).hashCode() && thisday <listMonday.get(3).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getListpriceSum();
								listY1.set(4, num);
							}
						}
					}
					System.out.println("sum="+sum);
					if(sum>4) {
						if(thisday>=listMonday.get(3).hashCode() && thisday <listMonday.get(4).hashCode()) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(list.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getListpriceSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=listMonday.get(4).hashCode() && thisday <listMonday.get(5).hashCode()) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(list.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getListpriceSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("金重")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int thisday = Integer.parseInt(list.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <listMonday.get(0).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(0).hashCode() && thisday <listMonday.get(1).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(1).hashCode() && thisday <listMonday.get(2).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(2).hashCode() && thisday <listMonday.get(3).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					
					if(sum>4) {
						if(thisday>=listMonday.get(3).hashCode() && thisday <listMonday.get(4).hashCode()) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(list.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getGoldweightSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=listMonday.get(4).hashCode() && thisday <listMonday.get(5).hashCode()) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(list.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getGoldweightSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("主石")){//营业额
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					int thisday = Integer.parseInt(list.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <listMonday.get(0).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(0).hashCode() && thisday <listMonday.get(1).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(1).hashCode() && thisday <listMonday.get(2).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=listMonday.get(2).hashCode() && thisday <listMonday.get(3).hashCode()) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(list.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + list.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=listMonday.get(3).hashCode() && thisday <listMonday.get(4).hashCode()) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(list.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + list.get(i).getCenterstoneSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=listMonday.get(4).hashCode() && thisday <listMonday.get(5).hashCode()) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(list.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + list.get(i).getCenterstoneSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listProduct = new ArrayList<>();
		List listSettlementprice = new ArrayList<>();
		List listListprice = new ArrayList<>();
		List listCenterstone = new ArrayList<>();
		List listGoldweight = new ArrayList<>();
		List listDate = new ArrayList<>();
		if (listAll.size()>0) {
			for (int j = 0; j < listAll.size(); j++) {
				listArea.add(listAll.get(j).getArea());
				listRoom.add(listAll.get(j).getRoom());
				listCounter.add(listAll.get(j).getCounter());
				listProduct.add(listAll.get(j).getProduct());
				listSettlementprice.add(listAll.get(j).getSettlementprice());
				listListprice.add(listAll.get(j).getListprice());
				listGoldweight.add(listAll.get(j).getGoldweight());
				listCenterstone.add(listAll.get(j).getCenterstone());
				listDate.add(sdf.format(listAll.get(j).getDate()));
			}
		}
		//System.out.println("listX="+listX);
		//System.out.println("listY1="+listY1);
		//System.out.println("listY2="+listY2);
		//System.out.println("listY3="+listY3);
		//System.out.println("listY4="+listY4);
		//System.out.println("listY5="+listY5);
		//System.out.println("listY6="+listY6);
		
		result=""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4
		+"@"+listY5+"@"+listY6+"@"+sum+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone;
		
		
		return result;
	}
	/**
	 * index811下载Excel表格
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午3:22:06
	 */
	@ApiOperation(value="index811页面,周销售数据统计,下载Excel表格")
	@RequestMapping(value = "downloadExcelForIndex811", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex811(HttpServletRequest request,HttpServletResponse response) {
		String con = request.getParameter("context");
		String conList[] = con.split("&");
		String area = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String room = conList[3];
		String selectChoiceYear = conList[5];
		String selectChoiceMonth = conList[4];
		String start = selectChoiceYear+"-"+selectChoiceMonth+"-01";
		int year = Integer.parseInt(selectChoiceYear);
		String end="";
		int days=0;
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {// 闰年
			if(selectChoiceMonth.contains("2")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-29";
				days=29;
			}else if(selectChoiceMonth.contains("4") || selectChoiceMonth.contains("6") || selectChoiceMonth.contains("9") || selectChoiceMonth.contains("11")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-30";
				days=30;
			}else {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-31";
				days=31;
			}
		}else {
			if(selectChoiceMonth.contains("2")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-28";
				days=28;
			}else if(selectChoiceMonth.contains("4") || selectChoiceMonth.contains("6") || selectChoiceMonth.contains("9") || selectChoiceMonth.contains("11")) {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-30";
				days=30;
			}else {
				end = selectChoiceYear+"-"+selectChoiceMonth+"-31";
				days=31;
			}
		}
		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product);

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		listAll = stoneService.findDateForIndex8888(params);
		stoneService.downloadExcelForIndex811(listAll,response);
		

		return result;
	}
	/**
	 * index812下载Excel表格
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午3:22:06
	 */
	@ApiOperation(value="index812页面,月销售数据统计,下载Excel表格")
	@RequestMapping(value = "downloadExcelForIndex812", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex812(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String room = conList[3];
		String selectChoice = conList[4];
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product);

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		listAll = stoneService.findDateForIndex8888(params);
		stoneService.downloadExcelForIndex812(listAll,response);
		

		return result;
	}
	/**
	 * index813下载Excel表格
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午3:22:06
	 */
	@ApiOperation(value="index813页面,季度销售数据统计,下载Excel表格")
	@RequestMapping(value = "downloadExcelForIndex813", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex813(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String room = conList[3];
		String selectChoice = conList[4];
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product);

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		listAll = stoneService.findDateForIndex8888(params);
		stoneService.downloadExcelForIndex813(listAll,response);
		

		return result;
	}
	/**
	 * index814下载Excel表格
	 * 
	 * @param request
	 * @return String
	 * created by lick on 2018年7月4日 上午3:22:06
	 */
	@ApiOperation(value="index814页面,年销售数据统计,下载Excel表格")
	@RequestMapping(value = "downloadExcelForIndex814", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex814(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String counter = conList[1];
		String product = conList[2];
		String room = conList[3];
		String selectChoice = conList[4];
		String start = selectChoice+"-01-01";
		String end = selectChoice+"-12-31";
		System.out.println("area" + area + "\ncounter" + counter + "\nproduct" + product);

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("销售区域")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(product.contains("名称") || product.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("product", product);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		listAll = stoneService.findDateForIndex8888(params);
		stoneService.downloadExcelForIndex814(listAll,response);


		return result;
	}
	/**
	 * main跳转index822页面 年销售数据同比分析
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="跳转到index822页面,年销售数据同比分析")
	@GetMapping(value="index822")
	public String index822() {
		return "index822";
	}
	/**
	 * main跳转index832页面  年销售数据环比分析
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="跳转到index832页面,年销售数据环比分析")
	@GetMapping(value="index832")
	public String index832() {
		return "index832";
	}
	/**
	 * main跳转index821页面  月销售数据同比分析
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="跳转到index821页面,月销售数据同比分析")
	@GetMapping(value="index821")
	public String index821() {
		return "index821";
	}
	/**
	 * main跳转index831页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="跳转到index831页面,月销售数据环比分析")
	@GetMapping(value="index831")
	public String index831() {
		return "index831";
	}
	
	/**
	 * index822页面 
	 * 
	 * 年销售同比分析
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="index822页面,年销售数据同比分析,年销售同比分析")
	@RequestMapping(value = "findCompareDateOfIndex822", method = RequestMethod.POST)
	@ResponseBody
	public List<Group> findCompareDateOfIndex822(HttpServletRequest request) {
		List<Group> groupList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String selectSerachType = request.getParameter("selectSerachType");
		String selectChoice = request.getParameter("selectChoice");
		String selectType = request.getParameter("selectType");
		Date da = new Date();
		Date date = null;
		String end = sdf.format(da);
		String this_end = selectChoice+sdf.format(da).substring(4);
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		try {
			date = sdf.parse(this_end);
		} catch (Exception e) {
		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, -12);// 在日历的月份上减去12个月
		// System.out.println(sdf.format(c.getTime()));//得到12个月前的日期
		String start = sdf.format(c.getTime());
		String this_start = selectChoice+"-01-01";
		String last_start = (Integer.parseInt(selectChoice)-1)+"-01-01";
		String last_end = sdf.format(c.getTime());
		System.out.println("this_start="+this_start);
		System.out.println("this_end="+this_end);
		System.out.println("last_start="+last_start);
		System.out.println("last_end="+last_end);
		
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();//选择年
		List<StoneAnalysis> listCompare = new ArrayList<>();//对比年
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", this_start);
		params.put("end", this_end);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", last_start);
		params1.put("end", last_end);
		List listY = new ArrayList<>();
		List listThisYearX = new ArrayList<>();
		List listLastYearX = new ArrayList<>();
		List listDiffX = new ArrayList<>();
		if(selectSerachType.contains("销售区域")) {
			listAll = stoneService.findCompareDateAreaOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getArea());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getArea());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("area", listAll.get(i).getArea());
					listCompare = stoneService.findCompareDateAreaOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 
						listLastYearX.add(0);
						group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);	
				}
			}
		}
		if(selectSerachType.contains("柜台")) {
			listAll = stoneService.findCompareDateCounterOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getCounter());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getCounter());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("counter", listAll.get(i).getCounter());
					listCompare = stoneService.findCompareDateCounterOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 
						listLastYearX.add(0);
						group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("名称")) {
			listAll = stoneService.findCompareDateProductOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getProduct());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getProduct());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("product", listAll.get(i).getProduct());
					listCompare = stoneService.findCompareDateProductOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 
						
							listLastYearX.add(0);
							group.setOldData(0);
						
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
					groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("门店")) {
			listAll = stoneService.findCompareDateRoomOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getRoom());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getRoom());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		
		//清空表
		groupService.deleteAllGroup();
		//插入表数据
		for (int i = 0; i < groupList.size(); i++) {
			groupService.insertGroup(groupList.get(i));
			//System.out.println(groupList.get(i).getAttribute()+"==="+groupList.get(i).getGroupper()+"==="+groupList.get(i).getNewData());
		}
		//获取表数据
		List<Group> listGroup = new ArrayList<>();
		listGroup = groupService.findAllGroup();
		
		return listGroup;
	}
	/**
	 * index822页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index822页面,年销售数据同比分析,导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex822", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex822(HttpServletRequest request,HttpServletResponse response) {
		String str = request.getParameter("context");
		String result = "";
		System.out.println("str="+str);
		List<StoneAnalysis> listAll = new ArrayList<>();
		String[] re = str.split("&");
		if(re.length==0) {
			
		}else {
			String[][] strList = new String[4][re.length/4];
			for (int i = 0; i < re.length; i++) {
				if(i%4==3) {
					StoneAnalysis stoneAnalysis = new StoneAnalysis();
					stoneAnalysis.setIndex8Attributes(re[i-3]);
					stoneAnalysis.setIndex8Select(re[i-2]);
					stoneAnalysis.setIndex8Compare(re[i-1]);
					stoneAnalysis.setIndex8Diff(re[i]);
					listAll.add(stoneAnalysis);
				}
			}
			stoneService.downloadExcelForIndex822(listAll,response);
			
		}
		return result;
	}
	/**
	 * index832页面 
	 * 
	 * 年销售环比分析
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="index832页面,年销售数据环比分析,年销售环比分析")
	@RequestMapping(value = "findCompareDateOfIndex832", method = RequestMethod.POST)
	@ResponseBody
	public List<Group> findCompareDateOfIndex832(HttpServletRequest request) {
		List<Group> groupList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String selectSerachType = request.getParameter("selectSerachType");
		String selectChoice = request.getParameter("selectChoice");
		String selectType = request.getParameter("selectType");
		Date da = new Date();
		Date date = null;
		String end = sdf.format(da);
		String this_end = selectChoice+sdf.format(da).substring(4);
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		try {
			date = sdf.parse(this_end);
		} catch (Exception e) {
		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, -12);// 在日历的月份上减去12个月
		// System.out.println(sdf.format(c.getTime()));//得到12个月前的日期
		String start = sdf.format(c.getTime());
		String this_start = selectChoice+"-01-01";
		String last_start = (Integer.parseInt(selectChoice)-1)+"-01-01";
		String last_end = sdf.format(c.getTime());
		System.out.println("this_start="+this_start);
		System.out.println("this_end="+this_end);
		System.out.println("last_start="+last_start);
		System.out.println("last_end="+last_end);
		
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();//选择年
		List<StoneAnalysis> listCompare = new ArrayList<>();//对比年
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", this_start);
		params.put("end", this_end);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", last_start);
		params1.put("end", last_end);
		List listY = new ArrayList<>();
		List listThisYearX = new ArrayList<>();
		List listLastYearX = new ArrayList<>();
		List listDiffX = new ArrayList<>();
		if(selectSerachType.contains("销售区域")) {
			listAll = stoneService.findCompareDateAreaOfIndex822(params);
			if (listAll.size()>0) {//有销售数据 
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getArea());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getArea());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("柜台")) {
			listAll = stoneService.findCompareDateCounterOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getCounter());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getCounter());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("名称")) {
			listAll = stoneService.findCompareDateProductOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getProduct());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getProduct());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("门店")) {
			listAll = stoneService.findCompareDateRoomOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getRoom());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getRoom());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		//清空表
			groupService.deleteAllGroup();
			//插入表数据
			for (int i = 0; i < groupList.size(); i++) {
				groupService.insertGroup(groupList.get(i));
				//System.out.println(groupList.get(i).getAttribute()+"==="+groupList.get(i).getGroupper()+"==="+groupList.get(i).getNewData());
			}
			//获取表数据
			List<Group> listGroup = new ArrayList<>();
			listGroup = groupService.findAllGroup();
		return listGroup;
	}
	/**
	 * index832页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index832页面,年销售数据环比分析,导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex832", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex832(HttpServletRequest request,HttpServletResponse response) {
		String str = request.getParameter("context");
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		String[] re = str.split("&");
		if(re.length==0) {
			
		}else {
			String[][] strList = new String[4][re.length/4];
			for (int i = 0; i < re.length; i++) {
				if(i%4==3) {
					StoneAnalysis stoneAnalysis = new StoneAnalysis();
					stoneAnalysis.setIndex8Attributes(re[i-3]);
					stoneAnalysis.setIndex8Select(re[i-2]);
					stoneAnalysis.setIndex8Compare(re[i-1]);
					stoneAnalysis.setIndex8Diff(re[i]);
					listAll.add(stoneAnalysis);
				}
			}
			 stoneService.downloadExcelForIndex832(listAll,response);
			
		}
		return result;
	}
	/**
	 * index821页面 
	 * 
	 * 月销售数据同比分析
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="index821页面,月销售数据同比分析,查询")
	@RequestMapping(value = "findCompareDateOfIndex821", method = RequestMethod.POST)
	@ResponseBody
	public List<Group> findCompareDateOfIndex821(HttpServletRequest request) {
		List<Group> groupList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String selectSerachType = request.getParameter("selectSerachType");
		String selectType = request.getParameter("selectType");
		String selectChoiceYear = request.getParameter("selectChoiceYear");
		String selectChoiceMonth = request.getParameter("selectChoiceMonth");
		
		int year = Integer.parseInt(selectChoiceYear);
		int month = Integer.parseInt(selectChoiceMonth);
		int days=calculateDays(year,month);
		int last_days = calculateDays(year-1,month);
		
		
		String this_start = selectChoiceYear+"-"+selectChoiceMonth+"-01";
		String this_end = selectChoiceYear+"-"+selectChoiceMonth+"-"+days;
		String last_start = "";
		String last_end = "";
		
		last_start = (Integer.parseInt(selectChoiceYear)-1)+"-"+selectChoiceMonth+"-01";
		last_end = (Integer.parseInt(selectChoiceYear)-1)+"-"+selectChoiceMonth+"-"+last_days;
		
		
		System.out.println("this_start="+this_start);
		System.out.println("this_end="+this_end);
		System.out.println("last_start="+last_start);
		System.out.println("last_end="+last_end);
		
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();//选择年
		List<StoneAnalysis> listCompare = new ArrayList<>();//对比年
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", this_start);
		params.put("end", this_end);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", last_start);
		params1.put("end", last_end);
		List listY = new ArrayList<>();
		List listThisYearX = new ArrayList<>();
		List listLastYearX = new ArrayList<>();
		List listDiffX = new ArrayList<>();
		if(selectSerachType.contains("销售区域")) {
			listAll = stoneService.findCompareDateAreaOfIndex822(params);
			if (listAll.size()>0) {//有销售数据 
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getArea());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getArea());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("柜台")) {
			listAll = stoneService.findCompareDateCounterOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getCounter());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getCounter());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("名称")) {
			listAll = stoneService.findCompareDateProductOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getProduct());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getProduct());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("门店")) {
			listAll = stoneService.findCompareDateRoomOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getRoom());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getRoom());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				   groupList.add(group);
				}
			}
		}
		//清空表
		groupService.deleteAllGroup();
		//插入表数据
		for (int i = 0; i < groupList.size(); i++) {
			groupService.insertGroup(groupList.get(i));
			//System.out.println(groupList.get(i).getAttribute()+"==="+groupList.get(i).getGroupper()+"==="+groupList.get(i).getNewData());
		}
		//获取表数据
		List<Group> listGroup = new ArrayList<>();
		listGroup = groupService.findAllGroup();
		return listGroup;
	}
	/**
	 * index831页面 
	 * 
	 * 月销售数据环比分析
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年7月4日
	 */
	@ApiOperation(value="index831页面,月销售数据环比分析,查询")
	@RequestMapping(value = "findCompareDateOfIndex831", method = RequestMethod.POST)
	@ResponseBody
	public List<Group> findCompareDateOfIndex831(HttpServletRequest request) {
		List<Group> groupList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String selectSerachType = request.getParameter("selectSerachType");
		String selectType = request.getParameter("selectType");
		String selectChoiceYear = request.getParameter("selectChoiceYear");
		String selectChoiceMonth = request.getParameter("selectChoiceMonth");
		
		int year = Integer.parseInt(selectChoiceYear);
		int month = Integer.parseInt(selectChoiceMonth);
		int days=calculateDays(year,month);
		int last_days = 0;
		if(month==1) {
			last_days = calculateDays(year-1,12);
		} else {
			last_days = calculateDays(year,month-1);
		}
		
		String this_start = selectChoiceYear+"-"+selectChoiceMonth+"-01";
		String this_end = selectChoiceYear+"-"+selectChoiceMonth+"-"+days;
		String last_start = "";
		String last_end = "";
		if(month==1) {
			last_start = (Integer.parseInt(selectChoiceYear)-1)+"-12"+"-01";
			last_end = (Integer.parseInt(selectChoiceYear)-1)+"-12"+"-31";
		}else {
			last_start = selectChoiceYear+"-"+(Integer.parseInt(selectChoiceMonth)-1)+"-01";
			last_start = selectChoiceYear+"-"+(Integer.parseInt(selectChoiceMonth)-1)+"-"+last_days;
		}
		
		System.out.println("this_start="+this_start);
		System.out.println("this_end="+this_end);
		System.out.println("last_start="+last_start);
		System.out.println("last_end="+last_end);
		
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();//选择年
		List<StoneAnalysis> listCompare = new ArrayList<>();//对比年
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", this_start);
		params.put("end", this_end);
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("start", last_start);
		params1.put("end", last_end);
		List listY = new ArrayList<>();
		List listThisYearX = new ArrayList<>();
		List listLastYearX = new ArrayList<>();
		List listDiffX = new ArrayList<>();
		if(selectSerachType.contains("销售区域")) {
			listAll = stoneService.findCompareDateAreaOfIndex822(params);
			if (listAll.size()>0) {//有销售数据 
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getArea());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getArea());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				   groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("柜台")) {
			listAll = stoneService.findCompareDateCounterOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getCounter());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getCounter());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				   groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("名称")) {
			listAll = stoneService.findCompareDateProductOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getProduct());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getProduct());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				   groupList.add(group);
				}
			}
		}
		if(selectSerachType.contains("门店")) {
			listAll = stoneService.findCompareDateRoomOfIndex822(params);
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getRoom());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getRoom());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
						listThisYearX.add(listAll.get(i).getNumberSum());
						group.setNewData(listAll.get(i).getNumberSum());
					}else if(selectType.contains("结算价")){
						listThisYearX.add(listAll.get(i).getSettlementpriceSum());
						group.setNewData(listAll.get(i).getSettlementpriceSum());
					}else if(selectType.contains("标价")){
						listThisYearX.add(listAll.get(i).getListpriceSum());
						group.setNewData(listAll.get(i).getListpriceSum());
					}else if(selectType.contains("金重")){
						listThisYearX.add(listAll.get(i).getGoldweightSum());
						group.setNewData(listAll.get(i).getGoldweightSum());
					}else if(selectType.contains("主石")){
						listThisYearX.add(listAll.get(i).getCenterstoneSum());
						group.setNewData(listAll.get(i).getCenterstoneSum());
					}
					params1.put("room", listAll.get(i).getRoom());
					listCompare = stoneService.findCompareDateRoomOfIndex822(params1);
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
							listLastYearX.add(listCompare.get(0).getNumberSum());
							group.setOldData(listCompare.get(0).getNumberSum());
						}else if(selectType.contains("结算价")){
							listLastYearX.add(listCompare.get(0).getSettlementpriceSum());
							group.setOldData(listCompare.get(0).getSettlementpriceSum());
						}else if(selectType.contains("标价")){
							listLastYearX.add(listAll.get(i).getListpriceSum());
							group.setOldData(listCompare.get(0).getListpriceSum());
						}else if(selectType.contains("金重")){
							listLastYearX.add(listAll.get(i).getGoldweightSum());
							group.setOldData(listCompare.get(0).getGoldweightSum());
						}else if(selectType.contains("主石")){
							listLastYearX.add(listAll.get(i).getCenterstoneSum());
							group.setOldData(listCompare.get(0).getCenterstoneSum());
						}
					}else {//去年同期没有销售数据 						
							listLastYearX.add(0);
							group.setOldData(0);
					}	
					if(listLastYearX.get(i).hashCode()==0) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						double diff = ((double)listThisYearX.get(i).hashCode()-((double)listLastYearX.get(i).hashCode()))/(double)listLastYearX.get(i).hashCode();
					    listDiffX.add(diff+"%");
					    group.setGroupper(""+diff+"%");
					}
				   groupList.add(group);
				}
			}
		}
		//清空表
				groupService.deleteAllGroup();
				//插入表数据
				for (int i = 0; i < groupList.size(); i++) {
					groupService.insertGroup(groupList.get(i));
					//System.out.println(groupList.get(i).getAttribute()+"==="+groupList.get(i).getGroupper()+"==="+groupList.get(i).getNewData());
				}
				//获取表数据
				List<Group> listGroup = new ArrayList<>();
				listGroup = groupService.findAllGroup();
				return listGroup;
	}
	/**
	 * index821页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index821页面,月销售数据同比分析,导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex821", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex821(HttpServletRequest request,HttpServletResponse response) {
		String str = request.getParameter("context");
		String result = "";
		System.out.println("str="+str);
		List<StoneAnalysis> listAll = new ArrayList<>();
		String[] re = str.split("&");
		if(re.length==0) {
			
		}else {
			String[][] strList = new String[4][re.length/4];
			for (int i = 0; i < re.length; i++) {
				if(i%4==3) {
					StoneAnalysis stoneAnalysis = new StoneAnalysis();
					stoneAnalysis.setIndex8Attributes(re[i-3]);
					stoneAnalysis.setIndex8Select(re[i-2]);
					stoneAnalysis.setIndex8Compare(re[i-1]);
					stoneAnalysis.setIndex8Diff(re[i]);
					listAll.add(stoneAnalysis);
				}
			}
			 stoneService.downloadExcelForIndex821(listAll,response);
			
		}
		return result;
	}
	/**
	 * index831页面 导出excel表格
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return String created at 2018年7月1日
	 */
	@ApiOperation(value="index831页面,月销售数据环比分析,导出excel表格")
	@RequestMapping(value = "downloadExcelForIndex831", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex831(HttpServletRequest request,HttpServletResponse response) {
		String str = request.getParameter("context");
		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		String[] re = str.split("&");
		if(re.length==0) {
			
		}else {
			String[][] strList = new String[4][re.length/4];
			for (int i = 0; i < re.length; i++) {
				if(i%4==3) {
					StoneAnalysis stoneAnalysis = new StoneAnalysis();
					stoneAnalysis.setIndex8Attributes(re[i-3]);
					stoneAnalysis.setIndex8Select(re[i-2]);
					stoneAnalysis.setIndex8Compare(re[i-1]);
					stoneAnalysis.setIndex8Diff(re[i]);
					listAll.add(stoneAnalysis);
				}
			}
			stoneService.downloadExcelForIndex831(listAll,response);
			
		}
		return result;
	}
	/**
	 * index7 贡献度  732  系列商品贡献度分析模型
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月1日 下午7:55:11
	 */
	@ApiOperation(value="跳转到index7页面,系列商品贡献度分析模型")
	@GetMapping(value = "index7")
	public String index7(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listCounter = new ArrayList<>();
		List listQuality = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getQuality()!=null) {
				if(!listQuality.contains(list.get(i).getQuality()) && list.get(i).getQuality().length()>0) {
					listQuality.add(list.get(i).getQuality());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listQuality, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listQuality", listQuality);
		return "index7";
	}

	/**
	 * index7  732  系列商品贡献度分析模型
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="index7页面,系列商品贡献度分析模型,查询")
	@RequestMapping(value = "productFind", method = RequestMethod.POST)
	@ResponseBody
	public String productFind(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String selectType = request.getParameter("selectType");
		String counter = request.getParameter("counter");
		String quality = request.getParameter("quality");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			params.put("counter", counter);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			params.put("quality", quality);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("start===" + st + "======end" + ed);
		params.put("start", st);
		params.put("end", ed);
		
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		list = stoneService.findProductByTime(params);
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
		String result = "";
		
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				listProduct.add(list.get(i).getProduct());
				if (selectType.contains("数量")) {
					listProductNum.add(list.get(i).getNumberSum());
				}else if (selectType.contains("结算价")) {
					listProductNum.add(list.get(i).getSettlementpriceSum());
				}else if (selectType.contains("标价")) {
					listProductNum.add(list.get(i).getListpriceSum());
				}else if (selectType.contains("金重")) {
					listProductNum.add(list.get(i).getGoldweightSum());
				}else if (selectType.contains("主石")) {
					listProductNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}

		System.out.println(list);
		System.out.println("===" + listProductNum);
		result = "" + listProductNum + "@" + listProduct;
		return result;
	}

	/**
	 * 跳转到sellsort页面  管理分析模型
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月4日 上午8:29:25
	 */
	@ApiOperation(value="跳转到sellsort页面,管理分析模型")
	@GetMapping(value = "sellsort")
	public String sellsort(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String product = list.get(i).getProduct();
			int index0 = listProduct.indexOf(product);
			if (index0 == -1) {
				listProduct.add(product);
			}
		}
		Collections.sort(listProduct, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listproduct", listProduct);

		return "sellsort";
	}

	/**
	 * 跳转到plan页面 销售计划分析模型
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 *             String created on 2018年7月4日 上午8:29:25
	 */
	@ApiOperation(value="跳转到plan页面,销售计划分析模型")
	@GetMapping(value = "plan")
	public String plan(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("stoneList", stoneService.findAllStone());
		
		List<StoneAnalysis> list = stoneService.findAllStone();
		List<Belong> blist = belongService.findAllBelong();
		
		List listArea = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listBelong = new ArrayList<>();
		
		for (int i = 0; i < blist.size(); i++) {
			if(blist.get(i).getBelong_name()!=null) {
				if(!listBelong.contains(blist.get(i).getBelong_name()) && blist.get(i).getBelong_name().length()>0) {
					listBelong.add(blist.get(i).getBelong_name());
				}
			}
		}
		
		for (int i = 0; i < list.size(); i++) {
			String area = list.get(i).getArea();
			String room = list.get(i).getRoom();
			String counter = list.get(i).getCounter();
			
			int index = listArea.indexOf(area);
			int index1 = listRoom.indexOf(room);
			int index2 = listCounter.indexOf(counter);
			
			
			if (index == -1) {
				listArea.add(area);
			}
			if (index1 == -1) {
				listRoom.add(room);
			}
			if (index2 == -1) {
				listCounter.add(counter);
			}
			
		}
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listBelong, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listarea", listArea);
		map.addAttribute("listroom", listRoom);
		map.addAttribute("listBelong", listBelong);
		

		return "plan";
	}
	/**
	 * 来源分析   source.html
	 *
	 * com.nenu.controller
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created  at 2018年10月19日
	 */
	@ApiOperation(value="跳转到source页面,来源分析模型")
	@GetMapping(value = "source")
	public String source(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listSource = new ArrayList<>();
		List listArea = new ArrayList<>();
		List listSupplier = new ArrayList<>();
		List listQuality = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getSource()!=null) {
				if(!listSource.contains(list.get(i).getSource()) && list.get(i).getSource().length()>0) {
					listSource.add(list.get(i).getSource());
				}
			}if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listQuality.contains(list.get(i).getQuality()) && list.get(i).getQuality().length()>0) {
					listQuality.add(list.get(i).getQuality());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSource, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listQuality, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listArea", listArea);
		map.addAttribute("listSource", listSource);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
		map.addAttribute("listQuality", listQuality);
		
		return "source";
	}
	/**
	 * source页面,来源分析模型  查询
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="source页面,来源分析模型  查询")
	@RequestMapping(value = "sourceFind", method = RequestMethod.POST)
	@ResponseBody
	public String sourceFind(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String areaName = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String source = request.getParameter("source");
		String room = request.getParameter("room");
		String quality = request.getParameter("quality");
		String counter = request.getParameter("counter");
		
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + areaName);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		if(source.contains("所有") || source.contains("来源")) {
		}else {
			System.out.println("有来源");
			params.put("source", source);
		}
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			System.out.println("有供应商");
			params.put("supplier", supplier);
		}
		if(quality.contains("所有") || quality.contains("成色")) {
		}else {
			System.out.println("有成色");
			params.put("quality", quality);
		}
		
		String st = request.getParameter("start");
		String ed = request.getParameter("end");

		System.out.println("start===" + st + "======end" + ed);

		params.put("start", st);
		params.put("end", ed);
	
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneBySource(params);
		list = stoneService.findStoneForSource(params);
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				listProduct.add(list.get(i).getSource());
				if(selectType.contains("数量")) {
					listProductNum.add(list.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(list.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(list.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(list.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(list.get(i).getCenterstoneSum());
				}
			}
		}	
		String result = "";
		
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllArea = new ArrayList<>();
		List listAllRoom = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllSource = new ArrayList<>();
		List listAllQuality = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllArea.add(listAll.get(i).getArea());
				listAllRoom.add(listAll.get(i).getRoom());
				listAllCounter.add(listAll.get(i).getCounter());
				listAllSource.add(listAll.get(i).getSource());
				listAllQuality.add(listAll.get(i).getQuality());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
			}
		}
		//System.out.println(list);
		//System.out.println(listAll);
		//System.out.println("===" + listProductNum);
		result = "" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + 
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllSource+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight+"@"+listAllQuality;
		return result;
	}
	/**
	 * 下载excel  source
	 *
	 * com.nenu.controller
	 * @param request
	 * @param response
	 * @return String
	 * created  at 2018年10月19日
	 */
	
	@ApiOperation(value="source页面,来源分析模型 下载excel")
	@RequestMapping(value = "downloadExcelForSource", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForSource(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String source = conList[4];
		String start = conList[5];
		String end = conList[6];
		String quality = conList[7];
		
		

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("地区")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
		if(supplier.contains("供应商") || supplier.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("supplier", supplier);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		if(source.contains("来源") || source.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("source", source);
		}
		if(quality.contains("成色") || quality.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("quality", quality);
		}
		listAll = stoneService.findStoneBySource(params);
		
		System.out.println("====" + listAll + "====");
		stoneService.downloadExcelForIndexSource(listAll,response);
		

		return result;
	}
	/**
	 * index724   主石区间销售分析
	 *
	 * com.nenu.controller
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created  at 2018年10月20日
	 */
	@ApiOperation(value="跳转到index724页面,主石区间销售分析 ")
	@GetMapping(value = "index724")
	public String index724(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listSource = new ArrayList<>();
		List listArea = new ArrayList<>();
		List listSupplier = new ArrayList<>();
        List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
            if (list.get(i).getProduct() != null) {
                if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    listProduct.add(list.get(i).getProduct());
                }
            }
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getSource()!=null) {
				if(!listSource.contains(list.get(i).getSource()) && list.get(i).getSource().length()>0) {
					listSource.add(list.get(i).getSource());
				}
			}if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
        Collections.sort(listProduct, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });

		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSource, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listArea", listArea);
		map.addAttribute("listSource", listSource);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
        map.addAttribute("listProduct", listProduct);

		
		return "index724";
	}
	/**
	 * index724   分析
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午11:08:48
	 */
	@ApiOperation(value="跳转到index724页面,主石区间销售分析   查询 ")
	@RequestMapping(value = "searchForindex724", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex724(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		
		String areaName = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String source = request.getParameter("source");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
        if(product.contains("所有") || product.contains("名称")) {
        }else {
            System.out.println("有名称");
            params.put("product", product);
        }

		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			System.out.println("有供应商");
			params.put("supplier", supplier);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		params.put("start", st);
		params.put("end", ed);
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		
		List listX = new ArrayList<>();
		List listY = new ArrayList<>();
		
		List<MainStone> mainStonelist = new ArrayList<MainStone>(); // 主石区间
		mainStonelist = mainStoneService.findAllMainStone();
		
		List<ListPrice> listPricelist = new ArrayList<ListPrice>(); // 标价区间
		listPricelist = listPriceService.findAllListPrice();
		
		List<SettlementPrice> SettlementPricelist = new ArrayList<SettlementPrice>(); // 结算价区间
		SettlementPricelist = settlementPriceService.findAllSettlementPrice();

		List<GoldWeight> goldWeightList = new ArrayList<>();//金重区间
		goldWeightList = goldWeightService.findAllGoldWeight();

		if(source.contains("主石区间") ) {
			if(mainStonelist.size()>0) {
				for (int i = 0; i < mainStonelist.size(); i++) {
					listX.add(""+mainStonelist.get(i).getMainStone_start()+"-"+mainStonelist.get(i).getMainStone_end());
					listY.add("0");
				}
			}
			list = stoneService.findStoneForIndex724MainStone(params);
			if(list.size()>0) {
				for (int j = 0; j < list.size(); j++) {
					if(mainStonelist.size()>0) {	
						for (int i = 0; i < mainStonelist.size(); i++) {		
							if(list.get(j).getCenterstone()>=mainStonelist.get(i).getMainStone_start() && list.get(j).getCenterstone()<=mainStonelist.get(i).getMainStone_end()) {
								if(selectType.contains("数量")) {
									listY.set(i, list.get(j).getNumberSum()+Integer.parseInt(listY.get(i).toString()));
								}else if(selectType.contains("结算价")) {
									listY.set(i,list.get(j).getSettlementpriceSum()+Double.parseDouble( listY.get(i).toString()));
								}else if(selectType.contains("标价")) {
									listY.set(i,list.get(j).getListpriceSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("金重")) {
									listY.set(i,list.get(j).getGoldweightSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("主石")) {
									listY.set(i,list.get(j).getCenterstoneSum()+Double.parseDouble((String) listY.get(i).toString()));
								}
							}
						}
					}	
				}
			}
		}else if(source.contains("标价区间")) {
			if(listPricelist.size()>0) {
				for (int i = 0; i < listPricelist.size(); i++) {
					listX.add(""+listPricelist.get(i).getListPrice_start()+"-"+listPricelist.get(i).getListPrice_end());
					listY.add("0");
				}
			}
			list = stoneService.findStoneForIndex724ListPrice(params);
			if(list.size()>0) {
				for (int j = 0; j < list.size(); j++) {
					if(listPricelist.size()>0) {	
						for (int i = 0; i < listPricelist.size(); i++) {		
							if(list.get(j).getListprice()>=listPricelist.get(i).getListPrice_start() && list.get(j).getListprice()<=listPricelist.get(i).getListPrice_end()) {
								if(selectType.contains("数量")) {
									listY.set(i, list.get(j).getNumberSum()+Integer.parseInt(listY.get(i).toString()));
								}else if(selectType.contains("结算价")) {
									listY.set(i,list.get(j).getSettlementpriceSum()+Double.parseDouble( listY.get(i).toString()));
								}else if(selectType.contains("标价")) {
									listY.set(i,list.get(j).getListpriceSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("金重")) {
									listY.set(i,list.get(j).getGoldweightSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("主石")) {
									listY.set(i,list.get(j).getCenterstoneSum()+Double.parseDouble((String) listY.get(i).toString()));
								}
							}
						}
					}	
				}
			}
		}else if(source.contains("结算价区间")) {
			if(SettlementPricelist.size()>0) {
				for (int i = 0; i < SettlementPricelist.size(); i++) {
					listX.add(""+SettlementPricelist.get(i).getSettlementPrice_start()+"-"+SettlementPricelist.get(i).getSettlementPrice_end());
					listY.add("0");
				}
			}
			list = stoneService.findStoneForIndex724SettlePrice(params);
			if(list.size()>0) {
				for (int j = 0; j < list.size(); j++) {
					if(SettlementPricelist.size()>0) {	
						for (int i = 0; i < SettlementPricelist.size(); i++) {		
							if(list.get(j).getSettlementprice()>=SettlementPricelist.get(i).getSettlementPrice_start() && list.get(j).getSettlementprice()<=SettlementPricelist.get(i).getSettlementPrice_end()) {
								if(selectType.contains("数量")) {
									listY.set(i, list.get(j).getNumberSum()+Integer.parseInt(listY.get(i).toString()));
								}else if(selectType.contains("结算价")) {
									listY.set(i,list.get(j).getSettlementpriceSum()+Double.parseDouble( listY.get(i).toString()));
								}else if(selectType.contains("标价")) {
									listY.set(i,list.get(j).getListpriceSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("金重")) {
									listY.set(i,list.get(j).getGoldweightSum()+Double.parseDouble((String) listY.get(i).toString()));
								}else if(selectType.contains("主石")) {
									listY.set(i,list.get(j).getCenterstoneSum()+Double.parseDouble((String) listY.get(i).toString()));
								}
							}
						}
					}	
				}
			}
        }else if(source.contains("金重区间")) {
            if(goldWeightList.size()>0) {
                for (int i = 0; i < goldWeightList.size(); i++) {
                    listX.add(""+goldWeightList.get(i).getGoldWeight_start()+"-"+goldWeightList.get(i).getGoldWeight_end());
                    listY.add("0");
                }
            }
            list = stoneService.findStoneForIndex724GoldWeight(params);
            if(list.size()>0) {
                for (int j = 0; j < list.size(); j++) {
                    if(goldWeightList.size()>0) {
                        for (int i = 0; i < goldWeightList.size(); i++) {
                            if(list.get(j).getGoldweight()>=goldWeightList.get(i).getGoldWeight_start() && list.get(j).getGoldweight()<=goldWeightList.get(i).getGoldWeight_end()) {
                                if(selectType.contains("数量")) {
                                    listY.set(i, list.get(j).getNumberSum()+Integer.parseInt(listY.get(i).toString()));
                                }else if(selectType.contains("结算价")) {
                                    listY.set(i,list.get(j).getSettlementpriceSum()+Double.parseDouble( listY.get(i).toString()));
                                }else if(selectType.contains("标价")) {
                                    listY.set(i,list.get(j).getListpriceSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }else if(selectType.contains("金重")) {
                                    listY.set(i,list.get(j).getGoldweightSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }else if(selectType.contains("主石")) {
                                    listY.set(i,list.get(j).getCenterstoneSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }
                            }
                        }
                    }
                }
            }
        }
		
		
		
		List<StoneAnalysis> listTemp = new ArrayList<StoneAnalysis>();// 表格数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据
		listTemp = stoneService.findStoneBySource(params);
		if(listTemp.size()>0) {
			if(source.contains("主石区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(mainStonelist.size()>0) {	
						for (int i = 0; i < mainStonelist.size(); i++) {		
							if(listTemp.get(j).getCenterstone()>=mainStonelist.get(i).getMainStone_start() && listTemp.get(j).getCenterstone()<=mainStonelist.get(i).getMainStone_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
			if(source.contains("标价区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(listPricelist.size()>0) {	
						for (int i = 0; i < listPricelist.size(); i++) {		
							if(listTemp.get(j).getListprice()>=listPricelist.get(i).getListPrice_start() && listTemp.get(j).getListprice()<=listPricelist.get(i).getListPrice_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
			if(source.contains("结算价区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(SettlementPricelist.size()>0) {	
						for (int i = 0; i < SettlementPricelist.size(); i++) {		
							if(listTemp.get(j).getSettlementprice()>=SettlementPricelist.get(i).getSettlementPrice_start() && listTemp.get(j).getSettlementprice()<=SettlementPricelist.get(i).getSettlementPrice_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
            if(source.contains("金重区间")) {
                for (int j = 0; j < listTemp.size(); j++) {
                    if(goldWeightList.size()>0) {
                        for (int i = 0; i < goldWeightList.size(); i++) {
                            if(listTemp.get(j).getGoldweight()>=goldWeightList.get(i).getGoldWeight_start() && listTemp.get(j).getGoldweight()<=goldWeightList.get(i).getGoldWeight_end()) {
                                listAll.add(listTemp.get(j));
                            }
                        }
                    }
                }
            }
		}
		
		
		String result = "";
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllArea = new ArrayList<>();
		List listAllRoom = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllSource = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		
		
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllArea.add(listAll.get(i).getArea());
				listAllRoom.add(listAll.get(i).getRoom());
				listAllCounter.add(listAll.get(i).getCounter());
				listAllSource.add(listAll.get(i).getSource());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
			}
		}
		//System.out.println(list);
		//System.out.println(listAll);
		//System.out.println("===" + listY);
		result = "" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + 
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllSource+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
		return result;
	}
	/**
	 * 跳转到index722
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	
	@ApiOperation(value="跳转到index722页面,款式销售排名分析")
	@GetMapping(value = "index722")
	public String index722(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listPriceNo = new ArrayList<>();
		List listArea = new ArrayList<>();
		List listSupplier = new ArrayList<>();
		List listProduct = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getPriceNo()!=null) {
				if(!listPriceNo.contains(list.get(i).getPriceNo()) && list.get(i).getPriceNo().length()>0) {
					listPriceNo.add(list.get(i).getPriceNo());
				}
			}if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
            if (list.get(i).getProduct() != null) {
                if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    listProduct.add(list.get(i).getProduct());
                }
            }
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listPriceNo, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
        Collections.sort(listProduct, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });
		Collections.sort(listArea, new Comparator<String>() {
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listArea", listArea);
		map.addAttribute("listPriceNo", listPriceNo);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
        map.addAttribute("listProduct", listProduct);

		
		return "index722";
	}
	/**
	 * 跳转到index723
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	@ApiOperation(value="跳转到index723页面,系列销售排名分析")
	@GetMapping(value = "index723")
	public String index723(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listSeries = new ArrayList<>();
		List listArea = new ArrayList<>();
		List listSupplier = new ArrayList<>();
        List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getSeries()!=null) {
				if(!listSeries.contains(list.get(i).getSeries()) && list.get(i).getSeries().length()>0) {
                    listSeries.add(list.get(i).getSeries());
				}
			}if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
            if (list.get(i).getProduct() != null) {
                if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    listProduct.add(list.get(i).getProduct());
                }
            }
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
        Collections.sort(listProduct, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSeries, new Comparator<String>() {
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listArea", listArea);
		map.addAttribute("listSeries", listSeries);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
        map.addAttribute("listProduct", listProduct);

		
		return "index723";
	}
	/**
	 * 跳转到index726  圈口
	 * 
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	@ApiOperation(value="跳转到index726页面,圈口分析")
	@GetMapping(value = "index726")
	public String index726(ModelMap map, HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<StoneAnalysis> list = stoneService.findAllStone();
		List listRoom = new ArrayList<>();
		List listCounter = new ArrayList<>();
		List listCircle = new ArrayList<>();
		List listArea = new ArrayList<>();
		List listSupplier = new ArrayList<>();
        List listProduct = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getRoom()!=null) {
				if(!listRoom.contains(list.get(i).getRoom()) && list.get(i).getRoom().length()>0) {
					listRoom.add(list.get(i).getRoom());
				}
			}
            if (list.get(i).getProduct() != null) {
                if (!listProduct.contains(list.get(i).getProduct()) && list.get(i).getProduct().length() > 0) {
                    listProduct.add(list.get(i).getProduct());
                }
            }
			if(list.get(i).getCounter()!=null) {
				if(!listCounter.contains(list.get(i).getCounter()) && list.get(i).getCounter().length()>0) {
					listCounter.add(list.get(i).getCounter());
				}
			}
			if(list.get(i).getCircle()!=null) {
				if(!listCircle.contains(list.get(i).getCircle()) && list.get(i).getCircle().length()>0) {
					listCircle.add(list.get(i).getCircle());
				}
			}if(list.get(i).getArea()!=null) {
				if(!listArea.contains(list.get(i).getArea()) && list.get(i).getArea().length()>0) {
					listArea.add(list.get(i).getArea());
				}
			}
			if(list.get(i).getSupplier()!=null) {
				if(!listSupplier.contains(list.get(i).getSupplier()) && list.get(i).getSupplier().length()>0) {
					listSupplier.add(list.get(i).getSupplier());
				}
			}
		}
		Collections.sort(listCounter, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
        Collections.sort(listProduct, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });
		Collections.sort(listRoom, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listSupplier, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listCircle, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		Collections.sort(listArea, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		
		map.addAttribute("listArea", listArea);
		map.addAttribute("listCircle", listCircle);
		map.addAttribute("listRoom", listRoom);
		map.addAttribute("listSupplier", listSupplier);
		map.addAttribute("listCounter", listCounter);
        map.addAttribute("listProduct", listProduct);

		
		return "index726";
	}
	/**
	 * index722 查找
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	@ApiOperation(value="index722页面,款式销售排名分析   查询")
	@RequestMapping(value = "searchForindex722", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex722(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String areaName = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String priceNo = request.getParameter("priceNo");
		String price = request.getParameter("price");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		String selectType = request.getParameter("selectType");
        //没有选择标价区间
        if(price.contains("无")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> params = new HashMap<String, Object>();

            if(areaName.contains("所有") || areaName.contains("地区")) {
            }else {
                System.out.println("有地区");
                params.put("area", areaName);
            }
            if(product.contains("所有") || product.contains("名称")) {
            }else {
                System.out.println("有名称");
                params.put("product", product);
            }
            if(room.contains("所有") || room.contains("门店")) {
            }else {
                System.out.println("有门店");
                params.put("room", room);
            }
            if(counter.contains("所有") || counter.contains("柜台")) {
            }else {
                System.out.println("有柜台");
                params.put("counter", counter);
            }
            if(priceNo.contains("所有") || priceNo.contains("款式")) {
            }else {
                System.out.println("有款号");
                params.put("priceNo", priceNo);
            }
            if(supplier.contains("所有") || supplier.contains("供应商")) {
            }else {
                System.out.println("有供应商");
                params.put("supplier", supplier);
            }
            String st = request.getParameter("start");
            String ed = request.getParameter("end");

            System.out.println("start===" + st + "======end" + ed);

            params.put("start", st);
            params.put("end", ed);

            List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
            List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

            listAll = stoneService.findStoneFor722And723(params);
            list = stoneService.findStoneFor722(params);
            List listX = new ArrayList<>();
            List listY = new ArrayList<>();

            if(list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    listX.add(list.get(i).getPriceNo());
                    if(selectType.contains("数量")) {
                        listY.add(list.get(i).getNumberSum());
                    }else if(selectType.contains("结算价")) {
                        listY.add(list.get(i).getSettlementpriceSum());
                    }else if(selectType.contains("标价")) {
                        listY.add(list.get(i).getListpriceSum());
                    }else if(selectType.contains("金重")) {
                        listY.add(list.get(i).getGoldweightSum());
                    }else if(selectType.contains("主石")) {
                        listY.add(list.get(i).getCenterstoneSum());
                    }
                }
            }
            String result = "";

            List listAllDate = new ArrayList<>();
            List listAllSupplier = new ArrayList<>();
            List listAllSettlementprice = new ArrayList<>();
            List listAllProduct = new ArrayList<>();
            List listAllArea = new ArrayList<>();
            List listAllRoom = new ArrayList<>();
            List listAllCounter = new ArrayList<>();
            List listAllPriceNo = new ArrayList<>();
            List listAllListprice = new ArrayList<>();
            List listAllCenterstone = new ArrayList<>();
            List listAllGoldweight = new ArrayList<>();
            if (listAll != null) {
                for (int i = 0; i < listAll.size(); i++) {
                    listAllDate.add(sdf.format(listAll.get(i).getDate()));
                    listAllSupplier.add(listAll.get(i).getSupplier());
                    listAllProduct.add(listAll.get(i).getProduct());
                    listAllSettlementprice.add(listAll.get(i).getSettlementprice());
                    listAllArea.add(listAll.get(i).getArea());
                    listAllRoom.add(listAll.get(i).getRoom());
                    listAllCounter.add(listAll.get(i).getCounter());
                    listAllPriceNo.add(listAll.get(i).getPriceNo());
                    listAllListprice.add(listAll.get(i).getListprice());
                    listAllCenterstone.add(listAll.get(i).getCenterstone());
                    listAllGoldweight.add(listAll.get(i).getGoldweight());
                }
            }

            result = "" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
                    listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
                    listAllPriceNo+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
            return result;
        //标价区间
        }else {
            List<ListPrice> listPricelist = new ArrayList<ListPrice>(); // 标价区间
            listPricelist = listPriceService.findAllListPrice();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> params = new HashMap<String, Object>();

            if(areaName.contains("所有") || areaName.contains("地区")) {
            }else {
                System.out.println("有地区");
                params.put("area", areaName);
            }
            if(product.contains("所有") || product.contains("名称")) {
            }else {
                System.out.println("有名称");
                params.put("product", product);
            }
            if(room.contains("所有") || room.contains("门店")) {
            }else {
                System.out.println("有门店");
                params.put("room", room);
            }
            if(counter.contains("所有") || counter.contains("柜台")) {
            }else {
                System.out.println("有柜台");
                params.put("counter", counter);
            }
            if(priceNo.contains("所有") || priceNo.contains("款式")) {
            }else {
                System.out.println("有款号");
                params.put("priceNo", priceNo);
            }
            if(supplier.contains("所有") || supplier.contains("供应商")) {
            }else {
                System.out.println("有供应商");
                params.put("supplier", supplier);
            }
            String st = request.getParameter("start");
            String ed = request.getParameter("end");

            params.put("start", st);
            params.put("end", ed);
            //图标数据
            List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
            List listX = new ArrayList<>();
            List listY = new ArrayList<>();
            if(listPricelist.size()>0) {
                for (int i = 0; i < listPricelist.size(); i++) {
                    listX.add(""+listPricelist.get(i).getListPrice_start()+"-"+listPricelist.get(i).getListPrice_end());
                    listY.add("0");
                }
            }
            list = stoneService.findStoneForIndex724ListPrice(params);
            if(list.size()>0) {
                for (int j = 0; j < list.size(); j++) {
                    if(listPricelist.size()>0) {
                        for (int i = 0; i < listPricelist.size(); i++) {
                            if(list.get(j).getListprice()>=listPricelist.get(i).getListPrice_start() && list.get(j).getListprice()<=listPricelist.get(i).getListPrice_end()) {
                                if(selectType.contains("数量")) {
                                    listY.set(i, list.get(j).getNumberSum()+Integer.parseInt(listY.get(i).toString()));
                                }else if(selectType.contains("结算价")) {
                                    listY.set(i,list.get(j).getSettlementpriceSum()+Double.parseDouble( listY.get(i).toString()));
                                }else if(selectType.contains("标价")) {
                                    listY.set(i,list.get(j).getListpriceSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }else if(selectType.contains("金重")) {
                                    listY.set(i,list.get(j).getGoldweightSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }else if(selectType.contains("主石")) {
                                    listY.set(i,list.get(j).getCenterstoneSum()+Double.parseDouble((String) listY.get(i).toString()));
                                }
                            }
                        }
                    }
                }
            }
            List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据
            listAll = stoneService.findStoneFor722And723(params);
            List listAllDate = new ArrayList<>();
            List listAllSupplier = new ArrayList<>();
            List listAllSettlementprice = new ArrayList<>();
            List listAllProduct = new ArrayList<>();
            List listAllArea = new ArrayList<>();
            List listAllRoom = new ArrayList<>();
            List listAllCounter = new ArrayList<>();
            List listAllPriceNo = new ArrayList<>();
            List listAllListprice = new ArrayList<>();
            List listAllCenterstone = new ArrayList<>();
            List listAllGoldweight = new ArrayList<>();
            if (listAll != null) {
                for (int i = 0; i < listAll.size(); i++) {
                    listAllDate.add(sdf.format(listAll.get(i).getDate()));
                    listAllSupplier.add(listAll.get(i).getSupplier());
                    listAllProduct.add(listAll.get(i).getProduct());
                    listAllSettlementprice.add(listAll.get(i).getSettlementprice());
                    listAllArea.add(listAll.get(i).getArea());
                    listAllRoom.add(listAll.get(i).getRoom());
                    listAllCounter.add(listAll.get(i).getCounter());
                    listAllPriceNo.add(listAll.get(i).getPriceNo());
                    listAllListprice.add(listAll.get(i).getListprice());
                    listAllCenterstone.add(listAll.get(i).getCenterstone());
                    listAllGoldweight.add(listAll.get(i).getGoldweight());
                }
            }
            String result = "";
            result = "" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
                    listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
                    listAllPriceNo+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
            return result;

        }

	}
	/**
	 * index723 查找
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	
	@ApiOperation(value="index723页面,系列销售排名分析  查询")
	@RequestMapping(value = "searchForindex723", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex723(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String areaName = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String series = request.getParameter("series");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + areaName);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
        if(product.contains("所有") || product.contains("名称")) {
        }else {
            System.out.println("有名称");
            params.put("product", product);
        }

		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		if(series.contains("所有") || series.contains("系列")) {
		}else {
			System.out.println("有系列");
			params.put("series", series);
		}
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			System.out.println("有供应商");
			params.put("supplier", supplier);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");

		System.out.println("start===" + st + "======end" + ed);

		params.put("start", st);
		params.put("end", ed);
	
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneFor722And723(params);
		list = stoneService.findStoneFor723(params);
		List listX = new ArrayList<>();
		List listY = new ArrayList<>();
	
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				listX.add(list.get(i).getSeries());
				if(selectType.contains("数量")) {
					listY.add(list.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listY.add(list.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listY.add(list.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listY.add(list.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listY.add(list.get(i).getCenterstoneSum());
				}
			}
		}	
		String result = "";
		
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllArea = new ArrayList<>();
		List listAllRoom = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllSeries = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllArea.add(listAll.get(i).getArea());
				listAllRoom.add(listAll.get(i).getRoom());
				listAllCounter.add(listAll.get(i).getCounter());
                listAllSeries.add(listAll.get(i).getSeries()); //改为了系列
				listAllListprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
			}
		}
		//System.out.println(list);
		//System.out.println(listAll);
		//System.out.println("===" + listY);
		result = "" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + 
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
        listAllSeries+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
		return result;
	}
	/**
	 * index726 查找
	 * 
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	@ApiOperation(value="index726页面,圈口分析  查询")
	@RequestMapping(value = "searchForindex726", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex726(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		String areaName = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String circle = request.getParameter("circle");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + areaName);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(areaName.contains("所有") || areaName.contains("地区")) {
		}else {
			System.out.println("有地区");
			params.put("area", areaName);
		}
        if(product.contains("所有") || product.contains("名称")) {
        }else {
            System.out.println("有名称");
            params.put("product", product);
        }
		if(room.contains("所有") || room.contains("门店")) {
		}else {
			System.out.println("有门店");
			params.put("room", room);
		}
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		if(circle.contains("所有") || circle.contains("圈口")) {
		}else {
			System.out.println("有圈口");
			params.put("circle", circle);
		}
		if(supplier.contains("所有") || supplier.contains("供应商")) {
		}else {
			System.out.println("有供应商");
			params.put("supplier", supplier);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");

		System.out.println("start===" + st + "======end" + ed);

		params.put("start", st);
		params.put("end", ed);
	
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图标数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据

		listAll = stoneService.findStoneFor726(params);
		list = stoneService.findStoneForIndex726(params);
		List listX = new ArrayList<>();
		List listY = new ArrayList<>();
	
		if(list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				listX.add(list.get(i).getCircle());
				if(selectType.contains("数量")) {
					listY.add(list.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listY.add(list.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listY.add(list.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listY.add(list.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listY.add(list.get(i).getCenterstoneSum());
				}
			}
		}	
		String result = "";
		
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllArea = new ArrayList<>();
		List listAllRoom = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAllCircle = new ArrayList<>();
		List listAllListprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();
		if (listAll != null) {
			for (int i = 0; i < listAll.size(); i++) {
				listAllDate.add(sdf.format(listAll.get(i).getDate()));
				listAllSupplier.add(listAll.get(i).getSupplier());
				listAllProduct.add(listAll.get(i).getProduct());
				listAllSettlementprice.add(listAll.get(i).getSettlementprice());
				listAllArea.add(listAll.get(i).getArea());
				listAllRoom.add(listAll.get(i).getRoom());
				listAllCounter.add(listAll.get(i).getCounter());
				listAllCircle.add(listAll.get(i).getCircle());
				listAllListprice.add(listAll.get(i).getListprice());
				listAllCenterstone.add(listAll.get(i).getCenterstone());
				listAllGoldweight.add(listAll.get(i).getGoldweight());
			}
		}
		//System.out.println(list);
		//System.out.println(listAll);
		//System.out.println("===" + listY);
		result = "" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + 
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllCircle+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight;
		return result;
	}
	/**
	 * index722  下载
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * created by lick on 2018年10月20日 下午11:01:43
	 */
	@ApiOperation(value="index722页面,款式销售排名分析  下载excel")
	@RequestMapping(value = "downloadExcelForIndex722", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex722(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String priceNo = conList[4];
		String start = conList[5];
		String end = conList[6];
		String product = conList[7];
		

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("地区")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("地区")  || product.contains("所有")) {
        }else {
            System.out.println("1");
            params.put("product", product);
        }
		if(supplier.contains("供应商") || supplier.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("supplier", supplier);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		if(priceNo.contains("款式") || priceNo.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("priceNo", priceNo);
		}
		listAll = stoneService.findStoneFor722And723(params);
		stoneService.downloadExcelForIndex722(listAll,response);
		

		return result;
	}
	/**
	 * index723  下载
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * created by lick on 2018年10月20日 下午11:01:43
	 */
	@ApiOperation(value="index723页面,系列方式销售排名分析  下载excel")
	@RequestMapping(value = "downloadExcelForIndex723", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex723(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String series = conList[4];
		String start = conList[5];
		String end = conList[6];
        String product = conList[7];

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(product.contains("地区")  || product.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("product", product);
		}
        if(area.contains("名称")  || area.contains("所有")) {
        }else {
            System.out.println("1");
            params.put("area", area);
        }
		if(supplier.contains("供应商") || supplier.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("supplier", supplier);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		if(series.contains("系列") || series.contains("所有")) {
		}else {
			System.out.println("4");
			params.put("series", series);
		}
		listAll = stoneService.findStoneFor722And723(params);
		stoneService.downloadExcelForIndex723(listAll,response);
		

		return result;
	}
	/**
	 * index724  下载
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * created by lick on 2018年10月20日 下午11:01:43
	 */
	@ApiOperation(value="index724页面,主石区间销售排名分析  下载excel")
	@RequestMapping(value = "downloadExcelForIndex724", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex724(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String source = conList[4];
		String start = conList[5];
		String end = conList[6];
		String product = conList[7];

		String result = "";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("地区")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("地区")  || product.contains("所有")) {
        }else {
            System.out.println("1");
            params.put("product", product);
        }
		if(supplier.contains("供应商") || supplier.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("supplier", supplier);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		
		List<MainStone> mainStonelist = new ArrayList<MainStone>(); // 主石区间
		mainStonelist = mainStoneService.findAllMainStone();
		
		List<ListPrice> listPricelist = new ArrayList<ListPrice>(); // 标价区间
		listPricelist = listPriceService.findAllListPrice();
		
		List<SettlementPrice> SettlementPricelist = new ArrayList<SettlementPrice>(); // 结算价区间
		SettlementPricelist = settlementPriceService.findAllSettlementPrice();

        List<GoldWeight> goldWeightList = new ArrayList<>();//金重区间
        goldWeightList = goldWeightService.findAllGoldWeight();
		
		
		
		List<StoneAnalysis> listTemp = new ArrayList<StoneAnalysis>();// 表格数据
		List<StoneAnalysis> listAll = new ArrayList<StoneAnalysis>();// 表格数据
		listTemp = stoneService.findStoneBySource(params);
		if(listTemp.size()>0) {
			if(source.contains("主石区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(mainStonelist.size()>0) {	
						for (int i = 0; i < mainStonelist.size(); i++) {		
							if(listTemp.get(j).getCenterstone()>=mainStonelist.get(i).getMainStone_start() && listTemp.get(j).getCenterstone()<=mainStonelist.get(i).getMainStone_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
			if(source.contains("标价区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(listPricelist.size()>0) {	
						for (int i = 0; i < listPricelist.size(); i++) {		
							if(listTemp.get(j).getListprice()>=listPricelist.get(i).getListPrice_start() && listTemp.get(j).getListprice()<=listPricelist.get(i).getListPrice_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
			if(source.contains("结算价区间")) {
				for (int j = 0; j < listTemp.size(); j++) {
					if(SettlementPricelist.size()>0) {	
						for (int i = 0; i < SettlementPricelist.size(); i++) {		
							if(listTemp.get(j).getSettlementprice()>=SettlementPricelist.get(i).getSettlementPrice_start() && listTemp.get(j).getSettlementprice()<=SettlementPricelist.get(i).getSettlementPrice_end()) {
								listAll.add(listTemp.get(j));
							}
						}
					}
				}
			}
            if(source.contains("金重区间")) {
                for (int j = 0; j < listTemp.size(); j++) {
                    if(goldWeightList.size()>0) {
                        for (int i = 0; i < goldWeightList.size(); i++) {
                            if(listTemp.get(j).getGoldweight()>=goldWeightList.get(i).getGoldWeight_start() && listTemp.get(j).getGoldweight()<=goldWeightList.get(i).getGoldWeight_end()) {
                                listAll.add(listTemp.get(j));
                            }
                        }
                    }
                }
            }
		}
		
		stoneService.downloadExcelForIndex724(listAll,response);
		

		return result;
	}
	/**
	 * index726  下载
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * created by lick on 2018年10月20日 下午11:01:43
	 */
	@ApiOperation(value="index726页面,圈口分析  下载excel")
	@RequestMapping(value = "downloadExcelForIndex726", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex726(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String circle = conList[4];
		String start = conList[5];
		String end = conList[6];
		String product = conList[7];
		

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.contains("地区")  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("地区")  || product.contains("所有")) {
        }else {
            System.out.println("1");
            params.put("product", product);
        }
		if(supplier.contains("供应商") || supplier.contains("所有")) {	
		}else {
			System.out.println("2");
			params.put("supplier", supplier);
		}
		if(room.contains("门店") ||  room.contains("所有")) {
		}else {
			System.out.println("3");
			params.put("room", room);
		}
		if(counter.contains("柜台") || counter.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("counter", counter);
		}
		if(circle.contains("圈口") || circle.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("circle", circle);
		}
		listAll = stoneService.findStoneFor726(params);
		stoneService.downloadExcelForIndex726(listAll,response);
		

		return result;
	}
	/**
	 * plan页面 计划销售   销售计划分析模型
	 *
	 * com.nenu.controller
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException String
	 * created  at 2018年10月24日
	 */
	@ApiOperation(value="plan页面,销售计划分析模型  查询")
	@RequestMapping(value = "searchForIndex741", method = RequestMethod.POST)
	@ResponseBody
	public String searchForIndex741(HttpServletRequest request, ModelMap map, HttpSession session) throws ParseException {
		
		String room = request.getParameter("room");
		String belong = request.getParameter("belong");
		String selectType = request.getParameter("selectType");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();	
		if(room.contains("所有") || room.contains("库房")) {	
		}else {
			System.out.println("有门店");
			params.put("plan_room", room);
		}
		if(belong.contains("所有") || belong.contains("统计对象")) {	
		}else {
			System.out.println("有统计对象");
			params.put("plan_belong", belong);
		}
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("start===" + st + "======end" + ed);
		params.put("plan_start", st);
		params.put("plan_end", ed);
		params.put("plan_index",selectType);
		List<Plan> planList = planService.findPlanByParams(params);
		List ListX = new ArrayList<>();
		List ListTrue = new ArrayList<>();
		List ListFalse = new ArrayList<>();
		List ListDiff = new ArrayList<>();
		DecimalFormat df = new DecimalFormat( "0.000");
		if(planList.size()>0) {//时间段内存在计划销量
			for (int i = 0; i < planList.size(); i++) {
				ListX.add(""+planList.get(i).getPlan_room()+"->"+planList.get(i).getPlan_belong()+"->"+planList.get(i).getPlan_index());
				ListFalse.add(planList.get(i).getPlan_num());
				ListTrue.add(0);
			}

			for (int i = 0; i < planList.size(); i++) {
				
					 Plan  plan = planList.get(i);
					 String plan_belong = plan.getPlan_belong();
					 List<BelongAndCounter> bcList = belongAndCounterService.findAllBelongAndCounterByBelong(plan_belong);
					 if(bcList.size()>0) {
						 for (int j = 0; j < bcList.size(); j++) {
							 Map<String, Object> pa = new HashMap<String, Object>();
							 pa.put("start", st);
							 pa.put("end", ed);
							 pa.put("room", plan.getPlan_room());
							 pa.put("counter", bcList.get(j).getBc_counter_name());
							 List<StoneAnalysis> listStone = stoneService.findStoneForIndex741(pa);
							 if(listStone.size()>0) {
								 if(selectType.contains("数量")) {
									 ListTrue.set(i, listStone.get(0).getNumberSum()+Integer.parseInt(ListTrue.get(i).toString()));
								 }else if(selectType.contains("标价")) {
									 ListTrue.set(i, df.format(listStone.get(0).getListpriceSum()+Double.parseDouble(ListTrue.get(i).toString())));
								 }else if(selectType.contains("结算价")) {
									 ListTrue.set(i, df.format(listStone.get(0).getSettlementpriceSum()+Double.parseDouble(ListTrue.get(i).toString())));
								 }else if(selectType.contains("金重")) {
									 ListTrue.set(i,  df.format(listStone.get(0).getGoldweightSum()+Double.parseDouble(ListTrue.get(i).toString())));
								 }else if(selectType.contains("主石")) {
									 ListTrue.set(i,  df.format(listStone.get(0).getCenterstoneSum()+Double.parseDouble(ListTrue.get(i).toString())));
								 }			 
							 }	 
						 }
					 }
				}
			}
		//System.out.println(ListX);
		//System.out.println(ListTrue);
		//System.out.println(ListFalse);
		List listStart = new ArrayList<>();
		List listEnd = new ArrayList<>();
		List listRoom = new ArrayList<>();
		List listBelong = new ArrayList<>();
		List listNum = new ArrayList<>();
		List listIndex = new ArrayList<>();
		List listDo = new ArrayList<>();
		List listDiff = new ArrayList<>();
		if(planList.size()>0) {
			for (int i = 0; i < planList.size(); i++) {
				listStart.add(sdf.format(planList.get(i).getPlan_start()));
				listEnd.add(sdf.format(planList.get(i).getPlan_end()));
				listRoom.add(planList.get(i).getPlan_room());
				listBelong.add(planList.get(i).getPlan_belong());
				listNum.add(planList.get(i).getPlan_num());
				listIndex.add(planList.get(i).getPlan_index());
				listDo.add(df.format(Double.parseDouble((String) ListTrue.get(i).toString())));
				listDiff.add(df.format(Double.parseDouble(ListTrue.get(i).toString())-Double.parseDouble(ListFalse.get(i).toString())));
			}
		}
		
		return ""+ListX+"#"+ListTrue+"#"+ListFalse+"#"+listStart+"#"+listEnd+"#"+listRoom+"#"+listBelong+"#"+listNum+"#"+listIndex+"#"+listDo+"#"+listDiff;
		}
	/**
	 * plan页面 下载  销售计划分析模型
	 * 
	 * @param request
	 * @param response
	 * @return String
	 * created by lick on 2018年10月25日 上午12:16:10
	 * @throws ParseException 
	 */
	@ApiOperation(value="plan页面,销售计划分析模型 下载excel")
	@RequestMapping(value = "downloadExcelForIndex741", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForIndex741(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String con  = request.getParameter("context");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String list[]= con.split("#");
		List<Plan> planList = new ArrayList<>();
		
		if(list.length>1) {
			for (int i = 0; i < list.length; i++) {
				if(i%8==7) {
					Plan plan = new Plan();
					plan.setPlan_start(sdf.parse(list[i-7]));
					plan.setPlan_end(sdf.parse(list[i-6]));
					plan.setPlan_room(list[i-5]);
					plan.setPlan_belong(list[i-4]);
					plan.setPlan_num(Float.parseFloat(list[i-3]));
					plan.setPlan_index(list[i-2]);
					plan.setPlan_do(Float.parseFloat(list[i-1]));
					plan.setPlan_diff(Float.parseFloat(list[i-0]));
					planList.add(plan);
				}
			}
		}
		String result = "";
		stoneService.downloadExcelForIndex741(planList,response);
		

		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor711", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor711(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor711(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor712", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor712(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor712(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForSource", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForSource(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForSource(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor722", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor722(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor722(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor723", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor723(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor723(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor724", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor724(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor724(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor726", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor726(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor726(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex7", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex7(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex7(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor743", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor743(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor743(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor744", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor744(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor744(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForS75", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForS75(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForS75(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForS752", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForS752(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForS752(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex3", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex3(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex3(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex11", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex11(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex11(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor7311", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor7311(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor7311(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor7312", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor7312(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor7312(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex811", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex811(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex811(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex812", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex812(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex812(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex813", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex813(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex813(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex814", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex814(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex814(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex5", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex5(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String conGraph  = request.getParameter("context");
		String result = "";
		if(conGraph.length()<=0) {
			conGraph +="no&no";
			stoneService.downloadGraphExcelForIndex5(conGraph,response);
			return result;	
		}else {
			String[] glist = conGraph.split("#");
			String[] type = glist[0].split("@");
			String[] tit = glist[1].substring(1, glist[1].length()-1).split(",");
			conGraph = "";
			for (int i = 1; i < tit.length; i++) {
				for (int j = 0; j < type.length; j++) {
					String[] m=type[j].split(",");
					conGraph +=tit[i]+"&"+m[0]+"&"+m[i]+"&";
				}
			}
			//System.out.println(conGraph);
			stoneService.downloadGraphExcelForIndex5(conGraph,response);
			return result;
		}
		
	}
	
}