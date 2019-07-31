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
	 * @return
	 *             String created on 2018年7月1日 下午7:03:29
	 */
	@ApiOperation(value="跳转index页面",notes="显示index页面")
	@GetMapping(value = "index")
	public String index(ModelMap map) {
		map.addAttribute("listsupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index";
	}


	/**
	 * index页面 供应商查询
	 *
	 * com.nenu.controller
	 * 
	 * @param request
	 * @return
	 *             String created at 2018年6月27日
	 */
	@ApiOperation(value="供应商查询",notes="index页面 供应商查询")
	@RequestMapping(value = "supplierFind", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFind(HttpServletRequest request)  {
		String supplier = request.getParameter("supplier");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		String selectType = request.getParameter("selectType");

		System.out.println("名称=============" + product);
		System.out.println("供应商=============" + supplier);
		System.out.println("类别=============" + selectType);
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		System.out.println("st===" + st + ",ed======" + ed);
		params.put("start", st);
		params.put("end", ed);
		
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

		List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByDate(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByDate(params); // 图表数据 退回
		List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

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

        StringBuilder result = new StringBuilder();
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
		//销售数据
		if (listSale != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < listSale.size(); i++) {
					listDate.add(sdf.format(listSale.get(i).getDate()));
					listNum.add(listSale.get(i).getNumberSum());
				}
			} else if (selectType.contains("结算价")){
				for (int i = 0; i < listSale.size(); i++) {
					listDate.add(sdf.format(listSale.get(i).getDate()));
					listNum.add(listSale.get(i).getSettlementpriceSum());
				}
			} else if (selectType.contains("标价")){
				for (int i = 0; i < listSale.size(); i++) {
					listDate.add(sdf.format(listSale.get(i).getDate()));
					listNum.add(listSale.get(i).getListpriceSum());
				}
			} else if (selectType.contains("金重")){
				for (int i = 0; i < listSale.size(); i++) {
					listDate.add(sdf.format(listSale.get(i).getDate()));
					listNum.add(listSale.get(i).getGoldweightSum());
				}
			} else if (selectType.contains("主石")){
				for (int i = 0; i < listSale.size(); i++) {
					listDate.add(sdf.format(listSale.get(i).getDate()));
					listNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}
		//退回数据
        if (listBack != null) {
            if (selectType.contains("数量")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String time = sdf.format(listBack.get(i).getDate());
                    if(listDate.contains(time)) {
                        int index = listDate.indexOf(time);
                        listNum.set(index,(Integer) listNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }
            } else if (selectType.contains("结算价")){
                for (int i = 0; i < listBack.size(); i++) {
                    String time = sdf.format(listBack.get(i).getDate());
                    if(listDate.contains(time)) {
                        int index = listDate.indexOf(time);
                        listNum.set(index,(Float) listNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }
            } else if (selectType.contains("标价")){
                for (int i = 0; i < listBack.size(); i++) {
                    String time = sdf.format(listBack.get(i).getDate());
                    if(listDate.contains(time)) {
                        int index = listDate.indexOf(time);
                        listNum.set(index,(Float)listNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }
            } else if (selectType.contains("金重")){
                for (int i = 0; i < listBack.size(); i++) {
                    String time = sdf.format(listBack.get(i).getDate());
                    if(listDate.contains(time)) {
                        int index = listDate.indexOf(time);
                        listNum.set(index,(Float)listNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }
            } else if (selectType.contains("主石")){
                for (int i = 0; i < listBack.size(); i++) {
                    String time = sdf.format(listBack.get(i).getDate());
                    if(listDate.contains(time)) {
                        int index = listDate.indexOf(time);
                        listNum.set(index,(Float)listNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
        result.append("" + listNum + "@" + listDate + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct
                + "@" + listAllSettlementprice+ "@" + listAllListprice+ "@"
                + listAllGoldweight+ "@" + lisAlltCenterstone+ "@" + listAllCounter);
		return result.toString();
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
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

		stoneService.downloadExcelForIndex(listAll,response);

		return result;
	}

	/**
	 * 
	 * main跳转到productnum页面 获取供应商
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月1日 下午7:26:57
	 */
	@ApiOperation(value="跳转到productnum页面",notes="跳转到productnum页面")
	@GetMapping(value = "productnum")
	public String productnum(ModelMap map)  {

		//map.addAttribute("stoneList", stoneService.findAllStone());

		map.addAttribute("listsupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listQuality", stoneService.findDistinctQuality());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
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
	public String supplierFind2(HttpServletRequest request){
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


        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByProduct(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据


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

		StringBuilder result = new StringBuilder();
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
        //销售数据
		if (listSale != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < listSale.size(); i++) {
					listProduct.add(listSale.get(i).getProduct());
					listProductNum.add(listSale.get(i).getNumberSum());
				}
			} else if(selectType.contains("结算价")) {
				for (int i = 0; i < listSale.size(); i++) {
					listProduct.add(listSale.get(i).getProduct());
					listProductNum.add(listSale.get(i).getSettlementpriceSum());
				}
			} else if(selectType.contains("标价")) {
				for (int i = 0; i < listSale.size(); i++) {
					listProduct.add(listSale.get(i).getProduct());
					listProductNum.add(listSale.get(i).getListpriceSum());
				}
			} else if(selectType.contains("金重")) {
				for (int i = 0; i < listSale.size(); i++) {
					listProduct.add(listSale.get(i).getProduct());
					listProductNum.add(listSale.get(i).getGoldweightSum());
				}
			} else if(selectType.contains("主石")) {
				for (int i = 0; i < listSale.size(); i++) {
					listProduct.add(listSale.get(i).getProduct());
					listProductNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}

        //退回数据
        if (listBack != null) {
            if (selectType.contains("数量")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Integer)listProductNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }
            } else if(selectType.contains("结算价")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }
            } else if(selectType.contains("标价")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }
            } else if(selectType.contains("金重")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }
            } else if(selectType.contains("主石")) {
                for (int i = 0; i < listBack.size(); i++) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
        result.append("" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct
        + "@" + listAllSettlementprice+ "@" + listAllListprice+ "@" + listAllGoldweight+ "@" + listAllCenterstone
        + "@" + listAllCounter+ "@" + listAllQuality);

		return result.toString();
	}

	/**
	 * index2 productnum页面 导出excel表格
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

		List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);
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
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index3";
	}

	/**
	 * index3页面 725 兑换销售排名分析
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
		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);

		
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

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByProduct(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

		 
		// 图标数据
		List listY_t = new ArrayList<>();
		List listXHave_t = new ArrayList<>();
		List listXNotHave_t = new ArrayList<>();
		List listY = new ArrayList<>();
		List listXHave = new ArrayList<>();
		List listXNotHave = new ArrayList<>();
		//销售数据
		if (listSale != null) {
			for (int i = 0; i < listSale.size(); i++) {
				if (selectType.contains("数量")) {
					if (!listY.contains(listSale.get(i).getProduct())) {
						listY.add(listSale.get(i).getProduct());
					}
					listY_t.add(listSale.get(i).getProduct());

					if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
						listXHave_t.add(listSale.get(i).getNumberSum());
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(listSale.get(i).getNumberSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("结算价")) {
					if (!listY.contains(listSale.get(i).getProduct())) {
						listY.add(listSale.get(i).getProduct());
					}
					listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
						listXHave_t.add(Math.abs(listSale.get(i).getSettlementpriceSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(listSale.get(i).getSettlementpriceSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("标价")) {
					if (!listY.contains(listSale.get(i).getProduct())) {
						listY.add(listSale.get(i).getProduct());
					}
					listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
						listXHave_t.add(Math.abs(listSale.get(i).getListpriceSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(listSale.get(i).getListpriceSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("金重")) {
					if (!listY.contains(listSale.get(i).getProduct())) {
						listY.add(listSale.get(i).getProduct());
					}
					listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
						listXHave_t.add(Math.abs(listSale.get(i).getGoldweightSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(listSale.get(i).getGoldweightSum());
						listXHave_t.add(0);
					}
				} else if (selectType.contains("主石")) {
					if (!listY.contains(listSale.get(i).getProduct())) {
						listY.add(listSale.get(i).getProduct());
					}
					listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
						listXHave_t.add(Math.abs(listSale.get(i).getCenterstoneSum()));
						listXNotHave_t.add(0);
					} else {
						listXNotHave_t.add(listSale.get(i).getCenterstoneSum());
						listXHave_t.add(0);
					}
				}

			}
		}
        //退回数据
        if (listBack != null) {
            for (int i = 0; i < listSale.size(); i++) {
                if (selectType.contains("数量")) {
                    if (!listY.contains(listSale.get(i).getProduct())) {
                        listY.add(listSale.get(i).getProduct());
                    }
                    listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
                        listXHave_t.add(listSale.get(i).getNumberSum());
                        listXNotHave_t.add(0);
                    } else {
                        listXNotHave_t.add(listSale.get(i).getNumberSum());
                        listXHave_t.add(0);
                    }
                } else if (selectType.contains("结算价")) {
                    if (!listY.contains(listSale.get(i).getProduct())) {
                        listY.add(listSale.get(i).getProduct());
                    }
                    listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
                        listXHave_t.add(Math.abs(listSale.get(i).getSettlementpriceSum()));
                        listXNotHave_t.add(0);
                    } else {
                        listXNotHave_t.add(listSale.get(i).getSettlementpriceSum());
                        listXHave_t.add(0);
                    }
                } else if (selectType.contains("标价")) {
                    if (!listY.contains(listSale.get(i).getProduct())) {
                        listY.add(listSale.get(i).getProduct());
                    }
                    listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
                        listXHave_t.add(Math.abs(listSale.get(i).getListpriceSum()));
                        listXNotHave_t.add(0);
                    } else {
                        listXNotHave_t.add(listSale.get(i).getListpriceSum());
                        listXHave_t.add(0);
                    }
                } else if (selectType.contains("金重")) {
                    if (!listY.contains(listSale.get(i).getProduct())) {
                        listY.add(listSale.get(i).getProduct());
                    }
                    listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
                        listXHave_t.add(Math.abs(listSale.get(i).getGoldweightSum()));
                        listXNotHave_t.add(0);
                    } else {
                        listXNotHave_t.add(listSale.get(i).getGoldweightSum());
                        listXHave_t.add(0);
                    }
                } else if (selectType.contains("主石")) {
                    if (!listY.contains(listSale.get(i).getProduct())) {
                        listY.add(listSale.get(i).getProduct());
                    }
                    listY_t.add(listSale.get(i).getProduct());

                    if (listSale.get(i).getExchangegoldweight() > 0 || listSale.get(i).getExchangemoney() > 0) {
                        listXHave_t.add(Math.abs(listSale.get(i).getCenterstoneSum()));
                        listXNotHave_t.add(0);
                    } else {
                        listXNotHave_t.add(listSale.get(i).getCenterstoneSum());
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
		List listExchangemoney = new ArrayList<>();
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
                listExchangemoney.add(listAll.get(i).getExchangemoney());
				listDate.add(sdf.format(listAll.get(i).getDate()));
			}
		}

		result = "" + listY + "@" + listXHave + "@" + listXNotHave + "@" + listSupplier + "@" + listCounter + "@" + listProduct + "@" 
		+ listSource + "@" + listSettlementprice + "@" + listDate + "@" + listAll.size()
		+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone+"@"+listExchangegoldweight+"@"+listExchangemoney;

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
	 * 跳转到index731页面  系列商品走势
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月1日 下午7:41:02
	 */
	@ApiOperation(value="跳转到index731页面  ",notes="系列商品走势页面  ")
	@GetMapping(value = "seriesproduct")
	public String seriesproduct(ModelMap map)  {
		map.addAttribute("listsupplier",  stoneService.findDistinctSupplier());
		map.addAttribute("listProduct",stoneService.findDistinctProduct());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		return "index731";
	}
	/**
	 * index731页面 查询
	 * @param request
	 * @return
	 */
	@ApiOperation(value="index731页面  查询 ",notes="系列商品走势页面  ")
	@RequestMapping(value = "supplierFind731", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFind731(HttpServletRequest request){
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
		
		List<StoneAnalysis> list = new ArrayList<StoneAnalysis>(); // 图表数据


		List listMonth = new ArrayList<>();// 年走势月份
		List listNum = new ArrayList<>();// 年走势数量

		for (int i = 0; i < 12; i++) {
			listNum.add(0);
			String Mon = "" + (i + 1) + "月";
			listMonth.add(Mon);
		}
        //销售数据
        list = stoneService.findSourceEqualsSaleByDate(params);
		if (list != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int m = list.get(i).getDate().getMonth();
						for (int j = 0; j < 12; j++) {
							if (m == j) {
								int num = 0;
								num = (Integer) listNum.get(j) + list.get(i).getNumberSum();
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
                                float num = 0;
								num = Float.parseFloat(listNum.get(j).toString()) + list.get(i).getSettlementpriceSum();
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

        //退回数据
        list = stoneService.findSourceEqualsBackByDate(params);
        if (list != null) {
            if (selectType.contains("数量")) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getDate() != null) {
                        int m = list.get(i).getDate().getMonth();
                        for (int j = 0; j < 12; j++) {
                            if (m == j) {
                                int num = 0;
                                num = (Integer) listNum.get(j) + list.get(i).getNumberSum();
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
                                float num = 0;
                                num = Float.parseFloat(listNum.get(j).toString()) + list.get(i).getSettlementpriceSum();
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
                                num = Float.parseFloat(listNum.get(j).toString()) - list.get(i).getListpriceSum();
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
		//销售数据
		ListOneMonth = stoneService.findSourceEqualsSaleByDate(params_day);
		if (ListOneMonth != null) {
			if (selectType.contains("数量")) {
				for (int i = 0; i < ListOneMonth.size(); i++) {
					if (ListOneMonth.get(i).getDate() != null) {
						int m = ListOneMonth.get(i).getDate().getDate()-1;
						System.out.println("m=="+m);
						for (int j = 0; j < 31; j++) {
							if (m == j) {
								int num = 0;
								num = (Integer) listMonthNum.get(j) + ListOneMonth.get(i).getNumberSum();
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
								float num = 0;
								num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getSettlementpriceSum();
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
								num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getListpriceSum();
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
								num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getGoldweightSum();
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
								num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getCenterstoneSum();
								listMonthNum.set(j, num);
							}
						}
					}
				}
			}
		}

        //退回数据
        ListOneMonth = stoneService.findSourceEqualsBackByDate(params_day);
        if (ListOneMonth != null) {
            if (selectType.contains("数量")) {
                for (int i = 0; i < ListOneMonth.size(); i++) {
                    if (ListOneMonth.get(i).getDate() != null) {
                        int m = ListOneMonth.get(i).getDate().getDate()-1;
                        System.out.println("m=="+m);
                        for (int j = 0; j < 31; j++) {
                            if (m == j) {
                                int num = 0;
                                num = (Integer) listMonthNum.get(j) + ListOneMonth.get(i).getNumberSum();
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
                                float num = 0;
                                num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getSettlementpriceSum();
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
                                num = Float.parseFloat(listNum.get(j).toString()) - ListOneMonth.get(i).getListpriceSum();
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
                                num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getGoldweightSum();
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
                                num = Float.parseFloat(listNum.get(j).toString()) + ListOneMonth.get(i).getCenterstoneSum();
                                listMonthNum.set(j, num);
                            }
                        }
                    }
                }
            }
        }
        StringBuilder result = new StringBuilder();
		result.append("" + listNum + "@" + listMonth + "@" + listMonthNum + "@" + listDate);
		return result.toString();
	}

	/**
	 * index5页面 跳转到index5页面 分析销售增减趋势
	 * 
	 * @param map
	 *             String created on 2018年6月27日
	 */
	@ApiOperation(value="跳转到index5页面 ",notes="分析销售增减趋势页面  ")
	@GetMapping(value = "index5")
	public String index5(ModelMap map) {
		List<StoneAnalysis> listArea = stoneService.findDistinctArea();
		map.addAttribute("listArea", listArea);
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
	 * @return String created on 2018年6月28日
	 */
	@ApiOperation(value="index5页面  销售增减分析，回显表格和图标",notes="index5页面  742 销售增减分析，回显表格和图标")
	@RequestMapping(value = "saleTrendFind", method = RequestMethod.POST)
	@ResponseBody
	public String saleTrendFind(HttpServletRequest request) {
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
		    //销售数据
			listAllContent = stoneService.findSourceEqualsSaleByArea(params);
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
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
								}
							}else if(selectType.contains("标价")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								}
							}
						}
					}
				}
			}
            //退回数据
            listAllContent = stoneService.findSourceEqualsBackByArea(params);
            for (int i = 0; i < listAllContent.size(); i++) {
                for (int j = 0; j < str.length; j++) {
                    if (listAllContent.get(i).getArea() != null) {
                        if (listAllContent.get(i).getArea().contains(str[j][0])) {
                            int year_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(0, 4));
                            int month_st = Integer.parseInt(sdf.format(listAllContent.get(i).getDate()).substring(5, 7));
                            if (selectType.contains("数量")) {
                                if (year_s == year_st) {
                                    int month_ss = month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
                                } else {
                                    int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Integer.parseInt(str[j][month_ss]) + listAllContent.get(i).getNumberSum()));
                                }
                            } else if (selectType.contains("结算价")) {
                                if (year_s == year_st) {
                                    int month_ss = month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
                                } else {
                                    int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getSettlementpriceSum()));
                                }
                            } else if (selectType.contains("标价")) {
                                if (year_s == year_st) {
                                    int month_ss = month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) - listAllContent.get(i).getListpriceSum()));
                                } else {
                                    int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) - listAllContent.get(i).getListpriceSum()));
                                }
                            } else if (selectType.contains("金重")) {
                                if (year_s == year_st) {
                                    int month_ss = month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
                                } else {
                                    int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
                                }
                            } else if (selectType.contains("主石")) {
                                if (year_s == year_st) {
                                    int month_ss = month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
                                } else {
                                    int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
                                    str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
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
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
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
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getListpriceSum()));
								}
							}else if(selectType.contains("金重")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getGoldweightSum()));
								}
							}else if(selectType.contains("主石")) {
								if (year_s == year_st) {
									int month_ss = month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
								} else {
									int month_ss = (year_st - year_s) * 12 + month_st - month_s + 1;
									str[j][month_ss] = String.valueOf((Float.parseFloat(str[j][month_ss]) + listAllContent.get(i).getCenterstoneSum()));
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
		System.out.println(result);
		return result;
	}

	/**
	 * index6 S743 销售结构分析 获取柜台
	 * @param map
	 * @return
	 *             String created on 2018年7月1日 下午7:55:11
	 */
	@ApiOperation(value="跳转到s743页面",notes=" 销售结构分析")
	@GetMapping(value = "s743")
	public String s743(ModelMap map) {
		map.addAttribute("stoneList", stoneService.findAllStone());
		map.addAttribute("listcounter", stoneService.findDistinctCounter());
		return "s743";
	}

	/**
	 * index6 s743页面  查询
	 * 
	 * @param request
	 * @return
	 *             String created on 2018年7月1日 下午7:56:15
	 */
	@ApiOperation(value="s743页面  查询",notes="销售结构分析")
	@RequestMapping(value = "counterFind", method = RequestMethod.POST)
	@ResponseBody
	public String counterFind(HttpServletRequest request) {
		String counter = request.getParameter("counter");
		String selectType = request.getParameter("selectType");

		System.out.println("柜台=============" + counter);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		
		String st = request.getParameter("start");
		String ed = request.getParameter("end");
		params.put("start", st);
		params.put("end", ed);

		System.out.println("start===" + st + "======end" + ed);

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByProduct(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	   //销售数据
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				listProduct.add(listSale.get(i).getProduct());
				if(selectType.contains("数量")) {
					listProductNum.add(listSale.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(listSale.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(listSale.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(listSale.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Integer)listProductNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
		List listAllDate = new ArrayList<>();
		List listAllSupplier = new ArrayList<>();
		List listAllSettlementprice = new ArrayList<>();
		List listAllProduct = new ArrayList<>();
		List listAllCounter = new ArrayList<>();
		List listAlllistprice = new ArrayList<>();
		List listAllCenterstone = new ArrayList<>();
		List listAllGoldweight = new ArrayList<>();

		StringBuilder result = new StringBuilder();
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

        result.append("" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" + listAllSettlementprice
                + "@" + listAllCounter+ "@" + listAlllistprice+ "@" + listAllCenterstone+ "@" + listAllGoldweight);
		return result.toString();
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
		String counter = conList[0];
		String start = conList[1];
		String end = conList[2];
		

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(counter.contains("所有") || counter.contains("柜台")) {
		}else {
			System.out.println("有柜台");
			params.put("counter", counter);
		}
		params.put("start", start);
		params.put("end", end);

		List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);
		stoneService.downloadExcelFor743(listAll,response);
		
		return result;
	}

	/**
	 * main 转s744 // s744 区域经营分析 获取地区
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月1日 下午8:35:49
	 */
	@ApiOperation(value="跳转到s744页面，区域经营分析",notes="区域经营分析")
	@GetMapping(value = "s744")
	public String s744(ModelMap map) {
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listQuality", stoneService.findDistinctQuality());
		return "s744";
	}

	/**
	 * s744
	 * 
	 * @param request
	 * @return
	 *             String created on 2018年7月1日 下午8:36:42
	 */
	@ApiOperation(value="s744页面，区域经营分析 查询",notes="区域经营分析")
	@RequestMapping(value = "areaFind", method = RequestMethod.POST)
	@ResponseBody
	public String areaFind(HttpServletRequest request)  {
		String area = request.getParameter("area");
		String room = request.getParameter("room");
		String quality = request.getParameter("quality");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + area);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(area.contains("所有") || area.length()<3) {
		}else {
			System.out.println("有地区");
			params.put("area", area);
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


        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByProduct(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	   //销售数据
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				listProduct.add(listSale.get(i).getProduct());
				if(selectType.contains("数量")) {
					listProductNum.add(listSale.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(listSale.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(listSale.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(listSale.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Integer)listProductNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
		StringBuilder result = new StringBuilder();
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
        result.append("" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
                listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
                listAllQuality+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
		return result.toString();
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
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String quality = conList[3];

		String start = conList[4];
		String end = conList[5];

		String result = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if(area.contains("所有") || area.length() < 3) {
		}else {
			System.out.println("有地区");
			params.put("area", area);
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
		List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);
		stoneService.downloadExcelFor744(listAll,response);
		return result;
	}


	/**
	 * 跳转到productsum页面 供应商品群分析
	 * @param map
	 * @return
	 */
	@ApiOperation(value="productsum页面,供应商品群分析")
	@GetMapping(value = "productsum")
	public String productsum(ModelMap map) {
		map.addAttribute("listsupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listQuality", stoneService.findDistinctQuality());
		return "productsum";
	}

	/**
	 * 跳转到index9页面 销售趋势成长分析模型页面
	 * @param map
	 * @return
	 */
	@ApiOperation(value="index9页面,销售趋势成长分析模型页面")
	@GetMapping(value = "s752")
	public String s752(ModelMap map) {
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index9";
	}
	/**
	 * index9页面 销售结构分析 查询
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="index9页面销售趋势成长分析模型页面,查询")
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

		List listThisYearX = new ArrayList<>();// 今年每个月销售
		List listLastYearX = new ArrayList<>();// 去年每个月销售
		List<StoneAnalysis> list = new  ArrayList<StoneAnalysis>(); //销售数据
        List<StoneAnalysis> listBack = new  ArrayList<StoneAnalysis>(); //退回数据
		if((!counter.contains("所有") && !counter.contains("柜台")) && (product.contains("所有") || product.contains("名称")) ) {	
			System.out.println("根据柜台查询");
			 list = stoneService.findSourceEqualsSaleByDateAndCounter(params);
            listBack = stoneService.findSourceEqualsBackByDateAndCounter(params);
		}else {
			System.out.println("根据商品查询");
			list = stoneService.findSourceEqualsSaleByDateAndProduct(params);
            listBack = stoneService.findSourceEqualsBackByDateAndCounter(params);
		}
		
		
		if (selectType.contains("数量")) {
			for (int i = 0; i < 12; i++) {
				listThisYearX.add(0);
				listLastYearX.add(0);
			}
			//销售数据
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = (Integer)listThisYearX.get(j) + list.get(i).getNumberSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									int num = 0;
									num = (Integer)listLastYearX.get(j) + list.get(i).getNumberSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}
            //退回数据
            if (listBack != null) {
                for (int i = 0; i < listBack.size(); i++) {
                    if (listBack.get(i).getDate() != null) {
                        int year_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(0, 4));
                        int month_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(5, 7))-1;
                        if (year_st == year_s) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    int num = 0;
                                    num = (Integer)listThisYearX.get(j) + listBack.get(i).getNumberSum();
                                    listThisYearX.set(j, num);
                                }
                            }
                        } else if (year_st == year_e) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    int num = 0;
                                    num = (Integer)listLastYearX.get(j) + listBack.get(i).getNumberSum();
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
			//销售数据
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDate() != null) {
						int year_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(0, 4));
						int month_st = Integer.parseInt(sdf.format(list.get(i).getDate()).substring(5, 7))-1;
						if (year_st == year_s) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listThisYearX.get(j).toString()) + list.get(i).getSettlementpriceSum();
									listThisYearX.set(j, num);
								}
							}
						} else if (year_st == year_e) {
							for (int j = 0; j < 12; j++) {
								if (month_st == j) {
									float num = 0;
									num = Float.parseFloat(listLastYearX.get(j).toString()) + list.get(i).getSettlementpriceSum();
									listLastYearX.set(j, num);
								}
							}
						}
					}
				}
			}

            //退回数据
            if (listBack != null) {
                for (int i = 0; i < listBack.size(); i++) {
                    if (listBack.get(i).getDate() != null) {
                        int year_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(0, 4));
                        int month_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(5, 7))-1;
                        if (year_st == year_s) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listThisYearX.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                    listThisYearX.set(j, num);
                                }
                            }
                        } else if (year_st == year_e) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listLastYearX.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
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
			//销售数据
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
            //退回数据
            if (listBack != null) {
                for (int i = 0; i < listBack.size(); i++) {
                    if (listBack.get(i).getDate() != null) {
                        int year_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(0, 4));
                        int month_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(5, 7))-1;
                        if (year_st == year_s) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listThisYearX.get(j).toString()) - listBack.get(i).getListpriceSum();
                                    listThisYearX.set(j, num);
                                }
                            }
                        } else if (year_st == year_e) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listLastYearX.get(j).toString()) - listBack.get(i).getListpriceSum();
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
			//销售数据
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

            //退回数据
            if (listBack != null) {
                for (int i = 0; i < listBack.size(); i++) {
                    if (listBack.get(i).getDate() != null) {
                        int year_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(0, 4));
                        int month_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(5, 7))-1;
                        if (year_st == year_s) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listThisYearX.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                    listThisYearX.set(j, num);
                                }
                            }
                        } else if (year_st == year_e) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listLastYearX.get(j).toString()) + listBack.get(i).getGoldweightSum();
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
			//销售数据
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
            //退回数据
            if (listBack != null) {
                for (int i = 0; i < listBack.size(); i++) {
                    if (listBack.get(i).getDate() != null) {
                        int year_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(0, 4));
                        int month_st = Integer.parseInt(sdf.format(listBack.get(i).getDate()).substring(5, 7))-1;
                        if (year_st == year_s) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listThisYearX.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                    listThisYearX.set(j, num);
                                }
                            }
                        } else if (year_st == year_e) {
                            for (int j = 0; j < 12; j++) {
                                if (month_st == j) {
                                    float num = 0;
                                    num = Float.parseFloat(listLastYearX.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                    listLastYearX.set(j, num);
                                }
                            }
                        }
                    }
                }
            }
		}	
		StringBuilder result = new StringBuilder();
		result.append("" + startList + "@" + endList + "@" + listThisYearX + "@" + listLastYearX);
		return result.toString();
	}

	/**
	 * 跳转到s75页面   管理分析模型
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月2日 上午9:42:32
	 */
	@ApiOperation(value="跳转到s75页面,管理分析")
	@GetMapping(value = "s75")
	public String s75(ModelMap map) {
		map.addAttribute("stoneList", stoneService.findAllStone());
		map.addAttribute("listproduct", stoneService.findDistinctProduct());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
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
		if(area.length() < 3 ||area.contains("所有")) {
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
					list = stoneService.findSourceEqualsSaleByArea(params); //销售数据
					if(list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							listX.add(list.get(i).getArea());
							listY.add(list.get(i).getNumberSum());
						}
					}
                    list = stoneService.findSourceEqualsBackByArea(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String areaName = list.get(i).getArea();
                            if(listX.contains(areaName)) {
                               int index = listX.indexOf(areaName);
                               listY.set(index,(Integer)listY.get(index)+list.get(i).getNumberSum());
                            }
                        }
                    }
				} else if (selectType.contains("结算价")){
                    list = stoneService.findSourceEqualsSaleByArea(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getArea());
                            listY.add(list.get(i).getSettlementpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByArea(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String areaName = list.get(i).getArea();
                            if(listX.contains(areaName)) {
                                int index = listX.indexOf(areaName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getSettlementpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("标价")){
                    list = stoneService.findSourceEqualsSaleByArea(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getArea());
                            listY.add(list.get(i).getListpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByArea(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String areaName = list.get(i).getArea();
                            if(listX.contains(areaName)) {
                                int index = listX.indexOf(areaName);
                                listY.set(index,(Float)listY.get(index)-list.get(i).getListpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("金重")){
                    list = stoneService.findSourceEqualsSaleByArea(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getArea());
                            listY.add(list.get(i).getGoldweightSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByArea(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String areaName = list.get(i).getArea();
                            if(listX.contains(areaName)) {
                                int index = listX.indexOf(areaName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getGoldweightSum());
                            }
                        }
                    }
				} else if (selectType.contains("主石")){
                    list = stoneService.findSourceEqualsSaleByArea(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getArea());
                            listY.add(list.get(i).getCenterstoneSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByArea(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String areaName = list.get(i).getArea();
                            if(listX.contains(areaName)) {
                                int index = listX.indexOf(areaName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getCenterstoneSum());
                            }
                        }
                    }
				}
			}
			if (select.contains("门店")) {
				if (selectType.contains("数量")) {
                    list = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getRoom());
                            listY.add(list.get(i).getNumberSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByRoom(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String roomName = list.get(i).getRoom();
                            if(listX.contains(roomName)) {
                                int index = listX.indexOf(roomName);
                                listY.set(index,(Integer)listY.get(index)+list.get(i).getNumberSum());
                            }
                        }
                    }
				} else if (selectType.contains("结算价")){
                    list = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getRoom());
                            listY.add(list.get(i).getSettlementpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByRoom(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String roomName = list.get(i).getRoom();
                            if(listX.contains(roomName)) {
                                int index = listX.indexOf(roomName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getSettlementpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("标价")){
                    list = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getRoom());
                            listY.add(list.get(i).getListpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByRoom(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String roomName = list.get(i).getRoom();
                            if(listX.contains(roomName)) {
                                int index = listX.indexOf(roomName);
                                listY.set(index,(Float)listY.get(index)-list.get(i).getListpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("金重")){
                    list = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getRoom());
                            listY.add(list.get(i).getGoldweightSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByRoom(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String roomName = list.get(i).getRoom();
                            if(listX.contains(roomName)) {
                                int index = listX.indexOf(roomName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getGoldweightSum());
                            }
                        }
                    }
				} else if (selectType.contains("主石")){
                    list = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getRoom());
                            listY.add(list.get(i).getCenterstoneSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByRoom(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String roomName = list.get(i).getRoom();
                            if(listX.contains(roomName)) {
                                int index = listX.indexOf(roomName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getCenterstoneSum());
                            }
                        }
                    }
				}
			}
			if (select.contains("柜台")) {
				if (selectType.contains("数量")) {
                    list = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getCounter());
                            listY.add(list.get(i).getNumberSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByCounter(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String counterName = list.get(i).getCounter();
                            if(listX.contains(counterName)) {
                                int index = listX.indexOf(counterName);
                                listY.set(index,(Integer)listY.get(index)+list.get(i).getNumberSum());
                            }
                        }
                    }
				} else if (selectType.contains("结算价")){
                    list = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getCounter());
                            listY.add(list.get(i).getSettlementpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByCounter(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String counterName = list.get(i).getCounter();
                            if(listX.contains(counterName)) {
                                int index = listX.indexOf(counterName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getSettlementpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("标价")){
                    list = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getCounter());
                            listY.add(list.get(i).getListpriceSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByCounter(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String counterName = list.get(i).getCounter();
                            if(listX.contains(counterName)) {
                                int index = listX.indexOf(counterName);
                                listY.set(index,(Float)listY.get(index)-list.get(i).getListpriceSum());
                            }
                        }
                    }
				} else if (selectType.contains("金重")){
                    list = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getCounter());
                            listY.add(list.get(i).getGoldweightSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByCounter(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String counterName = list.get(i).getCounter();
                            if(listX.contains(counterName)) {
                                int index = listX.indexOf(counterName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getGoldweightSum());
                            }
                        }
                    }
				} else if (selectType.contains("主石")){
                    list = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            listX.add(list.get(i).getCounter());
                            listY.add(list.get(i).getCenterstoneSum());
                        }
                    }
                    list = stoneService.findSourceEqualsBackByCounter(params); //退回数据
                    if(list.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            String counterName = list.get(i).getCounter();
                            if(listX.contains(counterName)) {
                                int index = listX.indexOf(counterName);
                                listY.set(index,(Float)listY.get(index)+list.get(i).getCenterstoneSum());
                            }
                        }
                    }
				}
			}

		StringBuilder result = new StringBuilder();
		result.append("" + listY + "@" + listX);

		return result.toString();
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
	    map.addAttribute("listProduct", stoneService.findDistinctProduct());
        map.addAttribute("listCounter", stoneService.findDistinctCounter());
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
		String product = request.getParameter("product");
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
		if(product.contains("所有") || product.contains("名称")) {
		}else {
			params.put("product", product);
			params1.put("product", product);
		}

		List listSupplier = new ArrayList<>();
		List listThisYear = new ArrayList<>();
		List listLastYear = new ArrayList<>();

        List<StoneAnalysis> list = new ArrayList<>();
		//销售数据
		list = stoneService.findSourceEqualsSaleBySupplier(params);
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				listSupplier.add(list.get(i).getSupplier());
				if(selectType.contains("数量")) {
                    params.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySupplier(params);
                    if(listBack.size() > 0) {
                        listThisYear.add(list.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                    }else {
                        listThisYear.add(list.get(i).getNumberSum());
                    }
					params1.put("supplier", list.get(i).getSupplier());
					List<StoneAnalysis> one = stoneService.findSourceEqualsSaleBySupplier(params1);
					if(one.size()>0) {
                        List<StoneAnalysis> listLastBack = stoneService.findSourceEqualsBackBySupplier(params1);
                        if(listLastBack.size() > 0) {
                            listLastYear.add(one.get(0).getNumberSum()+listLastBack.get(0).getNumberSum());
                        }else {
                            listLastYear.add(one.get(0).getNumberSum());
                        }
					}else {
						listLastYear.add(0);
					}
				}else if(selectType.contains("结算价")) {
                    params.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySupplier(params);
                    if(listBack.size() > 0) {
                        listThisYear.add(list.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                    }else {
                        listThisYear.add(list.get(i).getSettlementpriceSum());
                    }
                    params1.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> one = stoneService.findSourceEqualsSaleBySupplier(params1);
                    if(one.size()>0) {
                        List<StoneAnalysis> listLastBack = stoneService.findSourceEqualsBackBySupplier(params1);
                        if(listLastBack.size() > 0) {
                            listLastYear.add(one.get(0).getSettlementpriceSum()+listLastBack.get(0).getSettlementpriceSum());
                        }else {
                            listLastYear.add(one.get(0).getSettlementpriceSum());
                        }
                    }else {
                        listLastYear.add(0);
                    }
				}else if(selectType.contains("标价")) {
                    params.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySupplier(params);
                    if(listBack.size() > 0) {
                        listThisYear.add(list.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                    }else {
                        listThisYear.add(list.get(i).getListpriceSum());
                    }
                    params1.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> one = stoneService.findSourceEqualsSaleBySupplier(params1);
                    if(one.size()>0) {
                        List<StoneAnalysis> listLastBack = stoneService.findSourceEqualsBackBySupplier(params1);
                        if(listLastBack.size() > 0) {
                            listLastYear.add(one.get(0).getListpriceSum()-listLastBack.get(0).getListpriceSum());
                        }else {
                            listLastYear.add(one.get(0).getListpriceSum());
                        }
                    }else {
                        listLastYear.add(0);
                    }
				}else if(selectType.contains("金重")) {
                    params.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySupplier(params);
                    if(listBack.size() > 0) {
                        listThisYear.add(list.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                    }else {
                        listThisYear.add(list.get(i).getGoldweightSum());
                    }
                    params1.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> one = stoneService.findSourceEqualsSaleBySupplier(params1);
                    if(one.size()>0) {
                        List<StoneAnalysis> listLastBack = stoneService.findSourceEqualsBackBySupplier(params1);
                        if(listLastBack.size() > 0) {
                            listLastYear.add(one.get(0).getGoldweightSum()+listLastBack.get(0).getGoldweightSum());
                        }else {
                            listLastYear.add(one.get(0).getGoldweightSum());
                        }
                    }else {
                        listLastYear.add(0);
                    }
				}else if(selectType.contains("主石")) {
                    params.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySupplier(params);
                    if (listBack.size() > 0) {
                        listThisYear.add(list.get(i).getCenterstoneSum() + listBack.get(0).getCenterstoneSum());
                    } else {
                        listThisYear.add(list.get(i).getCenterstoneSum());
                    }
                    params1.put("supplier", list.get(i).getSupplier());
                    List<StoneAnalysis> one = stoneService.findSourceEqualsSaleBySupplier(params1);
                    if (one.size() > 0) {
                        List<StoneAnalysis> listLastBack = stoneService.findSourceEqualsBackBySupplier(params1);
                        if (listLastBack.size() > 0) {
                            listLastYear.add(one.get(0).getCenterstoneSum() + listLastBack.get(0).getCenterstoneSum());
                        } else {
                            listLastYear.add(one.get(0).getCenterstoneSum());
                        }
                    } else {
                        listLastYear.add(0);
                    }
                }
			}
		}
		StringBuilder result = new StringBuilder();
		result.append(""+listSupplier+"@"+listThisYear+"@"+listLastYear);
		return result.toString();
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
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());

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
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());

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
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());

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
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listProduct", stoneService.findDistinctProduct());

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
		listAll = stoneService.findDateForIndex8888(params);

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByDateAndProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByDateAndProduct(params); // 图表数据 退回
		
		
		List listX = new ArrayList<>();
		List listY = new ArrayList<>();
		//销售数据
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				if (!listX.contains(listSale.get(i).getProduct()) && listSale.get(i).getProduct().length() > 0) {
                    String regex="^[0-9].*$";
                    Pattern p = Pattern.compile(regex);
                    //数字开头 加个下划线
                    if(p.matcher(listSale.get(i).getProduct()).matches()) {
                        listX.add("_"+listSale.get(i).getProduct());
                    }else {
                        listX.add(listSale.get(i).getProduct());
                    }
				}
			}
		}
		for (int i = 0; i < listX.size(); i++) {
			listY.add(0);
		}
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				if(selectType.contains("销量")) {
					int num = 0;
					for (int j = 0; j < listX.size(); j++) {
						if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
							num = (Integer)listY.get(j) + listSale.get(i).getNumberSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("结算价")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("标价")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + listSale.get(i).getListpriceSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("金重")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + listSale.get(i).getGoldweightSum();
							listY.set(j, num);
						}
					}
				}
				if(selectType.contains("主石")) {
					float num = 0;
					for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
							num = Float.parseFloat(listY.get(j).toString()) + listSale.get(i).getCenterstoneSum();
							listY.set(j, num);
						}
					}
				}
			}
		}
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("销量")) {
                    int num = 0;
                    for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                            num = (Integer)listY.get(j) + listBack.get(i).getNumberSum();
                            listY.set(j, num);
                        }
                    }
                }
                if(selectType.contains("结算价")) {
                    float num = 0;
                    for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                            num = Float.parseFloat(listY.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                            listY.set(j, num);
                        }
                    }
                }
                if(selectType.contains("标价")) {
                    float num = 0;
                    for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                            num = Float.parseFloat(listY.get(j).toString()) - listBack.get(i).getListpriceSum();
                            listY.set(j, num);
                        }
                    }
                }
                if(selectType.contains("金重")) {
                    float num = 0;
                    for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                            num = Float.parseFloat(listY.get(j).toString()) + listBack.get(i).getGoldweightSum();
                            listY.set(j, num);
                        }
                    }
                }
                if(selectType.contains("主石")) {
                    float num = 0;
                    for (int j = 0; j < listX.size(); j++) {
                        if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                            num = Float.parseFloat(listY.get(j).toString()) + listBack.get(i).getCenterstoneSum();
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
		
		StringBuilder result = new StringBuilder();
		result.append(""+listX+"@"+listY+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone);

		return result.toString();
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

        List<StoneAnalysis> listAll = stoneService.findDateForIndex8888(params);//表格数据

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByDateAndProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByDateAndProduct(params); // 图表数据 退回


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
		
        if(listSale.size()>0) {
            for (int i = 0; i < listSale.size(); i++) {
                if (!listX.contains(listSale.get(i).getProduct()) && listSale.get(i).getProduct().length() > 0) {
                    String regex="^[0-9].*$";
                    Pattern p = Pattern.compile(regex);
                    //数字开头 加个下划线
                    if(p.matcher(listSale.get(i).getProduct()).matches()) {
                        listX.add("_"+listSale.get(i).getProduct());
                    }else {
                        listX.add(listSale.get(i).getProduct());
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
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
		
					if (m == 1) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY1.get(j) + listSale.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY2.get(j) + listSale.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY3.get(j) + listSale.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY4.get(j) + listSale.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY5.get(j) + listSale.get(i).getNumberSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY6.get(j) + listSale.get(i).getNumberSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY7.get(j) + listSale.get(i).getNumberSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY8.get(j) + listSale.get(i).getNumberSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY9.get(j) + listSale.get(i).getNumberSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY10.get(j) + listSale.get(i).getNumberSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY11.get(j) + listSale.get(i).getNumberSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY12.get(j) + listSale.get(i).getNumberSum();
								listY12.set(j, num);
							}
						}
					}
					
					
				}
			}
		}else if(selectType.contains("结算价")){//标价
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("标价")){//标价
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("金重")){
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}else if(selectType.contains("主石")){
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
					if (m == 1) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
					}
					if (m == 2) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if (m == 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if (m == 4) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					if (m == 5) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY5.set(j, num);
							}
						}
					}
					if (m == 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY6.set(j, num);
							}
						}
					}
					if (m == 7) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY7.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY7.set(j, num);
							}
						}
					}
					if (m == 8) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY8.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY8.set(j, num);
							}
						}
					}
					if (m == 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY9.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY9.set(j, num);
							}
						}
					}
					if (m == 10) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY10.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY10.set(j, num);
							}
						}
					}
					if (m == 11) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY11.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY11.set(j, num);
							}
						}
					}
					if (m == 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY12.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY12.set(j, num);
							}
						}
					}
				}
			}
		}
        if(selectType.contains("销量")) {
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;

                    if (m == 1) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY1.get(j) + listBack.get(i).getNumberSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m == 2) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY2.get(j) + listBack.get(i).getNumberSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m == 3) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY3.get(j) + listBack.get(i).getNumberSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m == 4) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY4.get(j) + listBack.get(i).getNumberSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if (m == 5) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY5.get(j) + listBack.get(i).getNumberSum();
                                listY5.set(j, num);
                            }
                        }
                    }
                    if (m == 6) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY6.get(j) + listBack.get(i).getNumberSum();
                                listY6.set(j, num);
                            }
                        }
                    }
                    if (m == 7) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY7.get(j) + listBack.get(i).getNumberSum();
                                listY7.set(j, num);
                            }
                        }
                    }
                    if (m == 8) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY8.get(j) + listBack.get(i).getNumberSum();
                                listY8.set(j, num);
                            }
                        }
                    }
                    if (m == 9) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY9.get(j) + listBack.get(i).getNumberSum();
                                listY9.set(j, num);
                            }
                        }
                    }
                    if (m == 10) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY10.get(j) + listBack.get(i).getNumberSum();
                                listY10.set(j, num);
                            }
                        }
                    }
                    if (m == 11) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY11.get(j) + listBack.get(i).getNumberSum();
                                listY11.set(j, num);
                            }
                        }
                    }
                    if (m == 12) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY12.get(j) + listBack.get(i).getNumberSum();
                                listY12.set(j, num);
                            }
                        }
                    }


                }
            }
        }else if(selectType.contains("结算价")){//结算价
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;
                    if (m == 1) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m == 2) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m == 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m == 4) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if (m == 5) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY5.set(j, num);
                            }
                        }
                    }
                    if (m == 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY6.set(j, num);
                            }
                        }
                    }
                    if (m == 7) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY7.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY7.set(j, num);
                            }
                        }
                    }
                    if (m == 8) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY8.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY8.set(j, num);
                            }
                        }
                    }
                    if (m == 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY9.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY9.set(j, num);
                            }
                        }
                    }
                    if (m == 10) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY10.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY10.set(j, num);
                            }
                        }
                    }
                    if (m == 11) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY11.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY11.set(j, num);
                            }
                        }
                    }
                    if (m == 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY12.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY12.set(j, num);
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("标价")){//标价
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;
                    if (m == 1) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m == 2) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m == 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m == 4) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getListpriceSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if (m == 5) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY5.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY5.set(j, num);
                            }
                        }
                    }
                    if (m == 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY6.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY6.set(j, num);
                            }
                        }
                    }
                    if (m == 7) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY7.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY7.set(j, num);
                            }
                        }
                    }
                    if (m == 8) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY8.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY8.set(j, num);
                            }
                        }
                    }
                    if (m == 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY9.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY9.set(j, num);
                            }
                        }
                    }
                    if (m == 10) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY10.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY10.set(j, num);
                            }
                        }
                    }
                    if (m == 11) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY11.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY11.set(j, num);
                            }
                        }
                    }
                    if (m == 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY12.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY12.set(j, num);
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("金重")){
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;
                    if (m == 1) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m == 2) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m == 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m == 4) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if (m == 5) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY5.set(j, num);
                            }
                        }
                    }
                    if (m == 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY6.set(j, num);
                            }
                        }
                    }
                    if (m == 7) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY7.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY7.set(j, num);
                            }
                        }
                    }
                    if (m == 8) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY8.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY8.set(j, num);
                            }
                        }
                    }
                    if (m == 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
                                num = Float.parseFloat(listY9.get(j).toString()) + listSale.get(i).getGoldweightSum();
                                listY9.set(j, num);
                            }
                        }
                    }
                    if (m == 10) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY10.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY10.set(j, num);
                            }
                        }
                    }
                    if (m == 11) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY11.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY11.set(j, num);
                            }
                        }
                    }
                    if (m == 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY12.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY12.set(j, num);
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("主石")){
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;
                    if (m == 1) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m == 2) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m == 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m == 4) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if (m == 5) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY5.set(j, num);
                            }
                        }
                    }
                    if (m == 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY6.set(j, num);
                            }
                        }
                    }
                    if (m == 7) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY7.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY7.set(j, num);
                            }
                        }
                    }
                    if (m == 8) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY8.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY8.set(j, num);
                            }
                        }
                    }
                    if (m == 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY9.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY9.set(j, num);
                            }
                        }
                    }
                    if (m == 10) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY10.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY10.set(j, num);
                            }
                        }
                    }
                    if (m == 11) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY11.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY11.set(j, num);
                            }
                        }
                    }
                    if (m == 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY12.get(j).toString()) + listBack.get(i).getCenterstoneSum();
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

		StringBuilder result = new StringBuilder();
		result.append(""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4+"@"+
				listY5+"@"+listY6+"@"+listY7+"@"+listY8+"@"+listY9+"@"+listY10+"@"+listY11+"@"+listY12
				+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone);

		return result.toString();
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


        List<StoneAnalysis> listAll = stoneService.findDateForIndex8888(params);//表格数据

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByDateAndProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByDateAndProduct(params); // 图表数据 退回
		
		List listX = new ArrayList<>();
		List listY1 = new ArrayList<>();
		List listY2 = new ArrayList<>();
		List listY3 = new ArrayList<>();
		List listY4 = new ArrayList<>();
		
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				if (!listX.contains(listSale.get(i).getProduct()) && listSale.get(i).getProduct().length() > 0) {
                    String regex="^[0-9].*$";
                    Pattern p = Pattern.compile(regex);
                    //数字开头 加个下划线
                    if(p.matcher(listSale.get(i).getProduct()).matches()) {
                        listX.add("_"+listSale.get(i).getProduct());
                    }else {
                        listX.add(listSale.get(i).getProduct());
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
		
		
		//销售数据
		if(selectType.contains("销量")) {
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					int m = listSale.get(i).getDate().getMonth() + 1;
					if (m >= 1 && m <= 3) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY1.get(j) + listSale.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if (m >= 4 && m <= 6) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY2.get(j) + listSale.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY3.get(j) + listSale.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY4.get(j) + listSale.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("结算价")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if (m >= 4 && m <= 6) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("标价")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("金重")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}else if(selectType.contains("主石")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int m = listSale.get(i).getDate().getMonth() + 1;
		
					if (m >= 1 && m <= 3) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
						
					}
					if (m >= 4 && m <= 6) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if (m >= 7 && m <= 9) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if (m >= 10 && m <= 12) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					
				}
			}
		}

        //退回数据
        if(selectType.contains("销量")) {
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {
                    int m = listBack.get(i).getDate().getMonth() + 1;
                    if (m >= 1 && m <= 3) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY1.get(j) + listBack.get(i).getNumberSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m >= 4 && m <= 6) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY2.get(j) + listBack.get(i).getNumberSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m >= 7 && m <= 9) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY3.get(j) + listBack.get(i).getNumberSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m >= 10 && m <= 12) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer) listY4.get(j) + listBack.get(i).getNumberSum();
                                listY4.set(j, num);
                            }
                        }
                    }

                }
            }
        }else if(selectType.contains("结算价")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;

                    if (m >= 1 && m <= 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if (m >= 4 && m <= 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m >= 7 && m <= 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m >= 10 && m <= 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY4.set(j, num);
                            }
                        }
                    }

                }
            }
        }else if(selectType.contains("标价")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;

                    if (m >= 1 && m <= 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY1.set(j, num);
                            }
                        }

                    }
                    if (m >= 4 && m <= 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m >= 7 && m <= 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m >= 10 && m <= 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY4.set(j, num);
                            }
                        }
                    }

                }
            }
        }else if(selectType.contains("金重")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;

                    if (m >= 1 && m <= 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY1.set(j, num);
                            }
                        }

                    }
                    if (m >= 4 && m <= 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m >= 7 && m <= 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m >= 10 && m <= 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY4.set(j, num);
                            }
                        }
                    }

                }
            }
        }else if(selectType.contains("主石")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int m = listBack.get(i).getDate().getMonth() + 1;

                    if (m >= 1 && m <= 3) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY1.set(j, num);
                            }
                        }

                    }
                    if (m >= 4 && m <= 6) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if (m >= 7 && m <= 9) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if (m >= 10 && m <= 12) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getCenterstoneSum();
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

		StringBuilder result  = new StringBuilder();
		result.append(""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4+"@"+
                listListprice+"@"+listGoldweight+"@"+listCenterstone);

		return result.toString();
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
        List<StoneAnalysis> listAll = stoneService.findDateForIndex8888(params);//表格数据

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByDateAndProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByDateAndProduct(params); // 图表数据 退回
		
		List listX = new ArrayList<>();
		List listY1 = new ArrayList<>();
		List listY2 = new ArrayList<>();
		List listY3 = new ArrayList<>();
		List listY4 = new ArrayList<>();
		List listY5 = new ArrayList<>();
		List listY6 = new ArrayList<>();
		
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					if (!listX.contains(listSale.get(i).getProduct()) && listSale.get(i).getProduct().length() > 0) {
                        String regex="^[0-9].*$";
                        Pattern p = Pattern.compile(regex);
                        //数字开头 加个下划线
                        if(p.matcher(listSale.get(i).getProduct()).matches()) {
                            listX.add("_"+listSale.get(i).getProduct());
                        }else {
                            listX.add(listSale.get(i).getProduct());
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
        //销售数据
		if(selectType.contains("销量")) {
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int thisday = Integer.parseInt(listSale.get(i).getDate().toString().substring(8, 10));//这个月的几号
					//System.out.println(thisday+"---------");
					if(thisday>=1 && thisday <(Integer) listMonday.get(0)) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY1.get(j) + listSale.get(i).getNumberSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=(Integer) listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY2.get(j) + listSale.get(i).getNumberSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY3.get(j) + listSale.get(i).getNumberSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
						int num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = (Integer) listY4.get(j) + listSale.get(i).getNumberSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
							int num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
									num = (Integer) listY5.get(j) + listSale.get(i).getNumberSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=(Integer) listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
								int num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
										num = (Integer) listY6.get(j) + listSale.get(i).getNumberSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("结算价")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int thisday = Integer.parseInt(listSale.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=(Integer) listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=(Integer) listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=(Integer) listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getSettlementpriceSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("标价")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int thisday = Integer.parseInt(listSale.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <(Integer) listMonday.get(0)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY1.set(j, num);
							}
						}
					
					}
					if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getListpriceSum();
								listY1.set(4, num);
							}
						}
					}
					System.out.println("sum="+sum);
					if(sum>4) {
						if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getListpriceSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getListpriceSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("金重")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int thisday = Integer.parseInt(listSale.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getGoldweightSum();
								listY4.set(j, num);
							}
						}
					}
					
					if(sum>4) {
						if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getGoldweightSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=(Integer)listMonday.get(4)&& thisday <(Integer)listMonday.get(5)) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getGoldweightSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}else if(selectType.contains("主石")){//营业额
			if(listSale.size()>0) {
				for (int i = 0; i < listSale.size(); i++) {
					
					int thisday = Integer.parseInt(listSale.get(i).getDate().toString().substring(8, 10));//这个月的几号
					if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY1.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY1.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY2.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY2.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY3.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY3.set(j, num);
							}
						}
					}
					if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
						float num = 0;
						for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
								num = Float.parseFloat(listY4.get(j).toString()) + listSale.get(i).getCenterstoneSum();
								listY4.set(j, num);
							}
						}
					}
					if(sum>4) {
						if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
							float num = 0;
							for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
									num = Float.parseFloat(listY5.get(j).toString()) + listSale.get(i).getCenterstoneSum();
									listY5.set(j, num);
								}
							}
						}
						if(sum==6) {
							if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
								float num = 0;
								for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listSale.get(i).getProduct())) {
										num = Float.parseFloat(listY6.get(j).toString()) + listSale.get(i).getCenterstoneSum();
										listY6.set(j, num);
									}
								}
							}
						}
					}
				}
			}
		}

        //退回数据
        if(selectType.contains("销量")) {
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int thisday = Integer.parseInt(listBack.get(i).getDate().toString().substring(8, 10));//这个月的几号
                    //System.out.println(thisday+"---------");
                    if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer)listY1.get(j) + listBack.get(i).getNumberSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer)listY2.get(j) + listBack.get(i).getNumberSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer)listY3.get(j) + listBack.get(i).getNumberSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        int num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = (Integer)listY4.get(j) + listBack.get(i).getNumberSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if(sum>4) {
                        if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            int num = 0;
                            for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                    num = (Integer)listY5.get(j) + listBack.get(i).getNumberSum();
                                    listY5.set(j, num);
                                }
                            }
                        }
                        if(sum==6) {
                            if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                int num = 0;
                                for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                        num = (Integer)listY6.get(j) + listBack.get(i).getNumberSum();
                                        listY6.set(j, num);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("结算价")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int thisday = Integer.parseInt(listBack.get(i).getDate().toString().substring(8, 10));//这个月的几号
                    if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if(sum>4) {
                        if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            float num = 0;
                            for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                    num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                    listY5.set(j, num);
                                }
                            }
                        }
                        if(sum==6) {
                            if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                float num = 0;
                                for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                        num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getSettlementpriceSum();
                                        listY6.set(j, num);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("标价")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int thisday = Integer.parseInt(listBack.get(i).getDate().toString().substring(8, 10));//这个月的几号
                    if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY1.set(j, num);
                            }
                        }

                    }
                    if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) - listBack.get(i).getListpriceSum();
                                listY1.set(4, num);
                            }
                        }
                    }
                    System.out.println("sum="+sum);
                    if(sum>4) {
                        if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            float num = 0;
                            for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                    num = Float.parseFloat(listY5.get(j).toString()) - listBack.get(i).getListpriceSum();
                                    listY5.set(j, num);
                                }
                            }
                        }
                        if(sum==6) {
                            if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                float num = 0;
                                for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                        num = Float.parseFloat(listY6.get(j).toString()) - listBack.get(i).getListpriceSum();
                                        listY6.set(j, num);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("金重")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int thisday = Integer.parseInt(listBack.get(i).getDate().toString().substring(8, 10));//这个月的几号
                    if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                listY4.set(j, num);
                            }
                        }
                    }

                    if(sum>4) {
                        if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            float num = 0;
                            for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                    num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                    listY5.set(j, num);
                                }
                            }
                        }
                        if(sum==6) {
                            if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                float num = 0;
                                for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                        num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getGoldweightSum();
                                        listY6.set(j, num);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else if(selectType.contains("主石")){//营业额
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {

                    int thisday = Integer.parseInt(listBack.get(i).getDate().toString().substring(8, 10));//这个月的几号
                    if(thisday>=1 && thisday <(Integer)listMonday.get(0)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY1.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY1.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(0) && thisday <(Integer)listMonday.get(1)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY2.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY2.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(1) && thisday <(Integer)listMonday.get(2)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY3.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY3.set(j, num);
                            }
                        }
                    }
                    if(thisday>=(Integer)listMonday.get(2) && thisday <(Integer)listMonday.get(3)) {
                        float num = 0;
                        for (int j = 0; j < listX.size(); j++) {
                            if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                num = Float.parseFloat(listY4.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                listY4.set(j, num);
                            }
                        }
                    }
                    if(sum>4) {
                        if(thisday>=(Integer)listMonday.get(3) && thisday <(Integer)listMonday.get(4)) {
                            float num = 0;
                            for (int j = 0; j < listX.size(); j++) {
                                if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                    num = Float.parseFloat(listY5.get(j).toString()) + listBack.get(i).getCenterstoneSum();
                                    listY5.set(j, num);
                                }
                            }
                        }
                        if(sum==6) {
                            if(thisday>=(Integer)listMonday.get(4) && thisday <(Integer)listMonday.get(5)) {
                                float num = 0;
                                for (int j = 0; j < listX.size(); j++) {
                                    if(listX.get(j).toString().contains(listBack.get(i).getProduct())) {
                                        num = Float.parseFloat(listY6.get(j).toString()) + listBack.get(i).getCenterstoneSum();
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

		StringBuilder result = new StringBuilder();
		result.append(""+listX+"@"+listY1+"@"+listArea+"@"+listRoom+"@"+listCounter+"@"+
		listProduct+"@"+listSettlementprice+"@"+listDate+"@"+listY2+"@"+listY3+"@"+listY4
		+"@"+listY5+"@"+listY6+"@"+sum+"@"+listListprice+"@"+listGoldweight+"@"+listCenterstone);

		return result.toString();
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
			listAll = stoneService.findSourceEqualsSaleByArea(params); //销售数据
			if (listAll.size()>0) {
				for (int i = 0; i < listAll.size(); i++) {
					listY.add(listAll.get(i).getArea());
					Group group =  new Group();
					group.setAttribute(listAll.get(i).getArea());
					//根据指标选择不同的求和结果
					if(selectType.contains("销量")) {
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
					}else if(selectType.contains("结算价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
					}else if(selectType.contains("标价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
					}else if(selectType.contains("金重")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
					}else if(selectType.contains("主石")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
					}
					params1.put("area", listAll.get(i).getArea());
					listCompare = stoneService.findSourceEqualsSaleByArea(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params1); //退回数据
					if(listCompare.size()>0 ) {//去年同期有销售数据 
						
						if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
						}else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
						}else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
						}else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
						}else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
						}
					}else {//去年同期没有销售数据 
						listLastYearX.add(0);
						group.setOldData(0);
					}
                    if(listLastYearX.get(i).toString().equals("0")) {
						listDiffX.add("100%");
						group.setGroupper("100%");
					}else {
						if(selectType.contains("销量")) {
							float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
							float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
							double diff = (thisyear-lastyear)/lastyear;
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
					}
				groupList.add(group);	
				}
			}
		}
        if(selectSerachType.contains("柜台")) {
            listAll = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getCounter());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getCounter());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("counter", listAll.get(i).getCounter());
                    listCompare = stoneService.findSourceEqualsSaleByCounter(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("名称")) {
            listAll = stoneService.findSourceEqualsSaleByProduct(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getProduct());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getProduct());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("product", listAll.get(i).getProduct());
                    listCompare = stoneService.findSourceEqualsSaleByProduct(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("门店")) {
            listAll = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getRoom());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getRoom());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("room", listAll.get(i).getRoom());
                    listCompare = stoneService.findSourceEqualsSaleByRoom(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
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
            listAll = stoneService.findSourceEqualsSaleByArea(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getArea());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getArea());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("area", listAll.get(i).getArea());
                    listCompare = stoneService.findSourceEqualsSaleByArea(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("柜台")) {
            listAll = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getCounter());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getCounter());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("counter", listAll.get(i).getCounter());
                    listCompare = stoneService.findSourceEqualsSaleByCounter(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("名称")) {
            listAll = stoneService.findSourceEqualsSaleByProduct(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getProduct());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getProduct());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("product", listAll.get(i).getProduct());
                    listCompare = stoneService.findSourceEqualsSaleByProduct(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("门店")) {
            listAll = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getRoom());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getRoom());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("room", listAll.get(i).getRoom());
                    listCompare = stoneService.findSourceEqualsSaleByRoom(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
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
            listAll = stoneService.findSourceEqualsSaleByArea(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getArea());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getArea());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("area", listAll.get(i).getArea());
                    listCompare = stoneService.findSourceEqualsSaleByArea(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("柜台")) {
            listAll = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getCounter());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getCounter());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("counter", listAll.get(i).getCounter());
                    listCompare = stoneService.findSourceEqualsSaleByCounter(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("名称")) {
            listAll = stoneService.findSourceEqualsSaleByProduct(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getProduct());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getProduct());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("product", listAll.get(i).getProduct());
                    listCompare = stoneService.findSourceEqualsSaleByProduct(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("门店")) {
            listAll = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getRoom());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getRoom());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("room", listAll.get(i).getRoom());
                    listCompare = stoneService.findSourceEqualsSaleByRoom(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
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
			last_end = selectChoiceYear+"-"+(Integer.parseInt(selectChoiceMonth)-1)+"-"+last_days;
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
            listAll = stoneService.findSourceEqualsSaleByArea(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getArea());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getArea());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("area", listAll.get(i).getArea());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("area", listAll.get(i).getArea());
                    listCompare = stoneService.findSourceEqualsSaleByArea(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByArea(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("柜台")) {
            listAll = stoneService.findSourceEqualsSaleByCounter(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getCounter());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getCounter());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("counter", listAll.get(i).getCounter());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("counter", listAll.get(i).getCounter());
                    listCompare = stoneService.findSourceEqualsSaleByCounter(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByCounter(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("名称")) {
            listAll = stoneService.findSourceEqualsSaleByProduct(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getProduct());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getProduct());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("product", listAll.get(i).getProduct());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("product", listAll.get(i).getProduct());
                    listCompare = stoneService.findSourceEqualsSaleByProduct(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByProduct(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
                    }
                    groupList.add(group);
                }
            }
        }
        if(selectSerachType.contains("门店")) {
            listAll = stoneService.findSourceEqualsSaleByRoom(params); //销售数据
            if (listAll.size()>0) {
                for (int i = 0; i < listAll.size(); i++) {
                    listY.add(listAll.get(i).getRoom());
                    Group group =  new Group();
                    group.setAttribute(listAll.get(i).getRoom());
                    //根据指标选择不同的求和结果
                    if(selectType.contains("销量")) {
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getNumberSum());
                            group.setNewData(listAll.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getSettlementpriceSum());
                            group.setNewData(listAll.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getListpriceSum());
                            group.setNewData(listAll.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getGoldweightSum());
                            group.setNewData(listAll.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")){
                        params.put("room", listAll.get(i).getRoom());
                        List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params); //退回数据
                        if(listBack.size() > 0) {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                        }else {
                            listThisYearX.add(listAll.get(i).getCenterstoneSum());
                            group.setNewData(listAll.get(i).getCenterstoneSum());
                        }
                    }
                    params1.put("room", listAll.get(i).getRoom());
                    listCompare = stoneService.findSourceEqualsSaleByRoom(params1);
                    List<StoneAnalysis> listBack =  stoneService.findSourceEqualsBackByRoom(params1); //退回数据
                    if(listCompare.size()>0 ) {//去年同期有销售数据

                        if(selectType.contains("销量")) {
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getNumberSum()+ listBack.get(0).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum()+listBack.get(0).getNumberSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getNumberSum());
                                group.setOldData(listAll.get(i).getNumberSum());
                            }
                        }else if(selectType.contains("结算价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum()+ listBack.get(0).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum()+listBack.get(0).getSettlementpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getSettlementpriceSum());
                                group.setOldData(listAll.get(i).getSettlementpriceSum());
                            }
                        }else if(selectType.contains("标价")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getListpriceSum()- listBack.get(0).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum()-listBack.get(0).getListpriceSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getListpriceSum());
                                group.setOldData(listAll.get(i).getListpriceSum());
                            }
                        }else if(selectType.contains("金重")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getGoldweightSum()+ listBack.get(0).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum()+listBack.get(0).getGoldweightSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getGoldweightSum());
                                group.setOldData(listAll.get(i).getGoldweightSum());
                            }
                        }else if(selectType.contains("主石")){
                            if(listBack.size() > 0) {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum()+ listBack.get(0).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum()+listBack.get(0).getCenterstoneSum());
                            }else {
                                listLastYearX.add(listAll.get(i).getCenterstoneSum());
                                group.setOldData(listAll.get(i).getCenterstoneSum());
                            }
                        }
                    }else {//去年同期没有销售数据
                        listLastYearX.add(0);
                        group.setOldData(0);
                    }
                    if(listLastYearX.get(i).toString().equals("0")) {
                        listDiffX.add("100%");
                        group.setGroupper("100%");
                    }else {
						if(selectType.contains("销量")) {
                            float thisyear = ((Integer)listThisYearX.get(i)).floatValue();
                            float lastyear = ((Integer)listLastYearX.get(i)).floatValue();
                            double diff = (thisyear-lastyear)/lastyear;
                            listDiffX.add(diff+"%");
                            group.setGroupper(""+diff+"%");
						}else {
							double diff = ((Float)listThisYearX.get(i)-((Float)listLastYearX.get(i)))/(Float)listLastYearX.get(i);
							listDiffX.add(diff+"%");
							group.setGroupper(""+diff+"%");
						}
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
	 * @return
	 *             String created on 2018年7月1日 下午7:55:11
	 */
	@ApiOperation(value="跳转到index7页面,系列商品贡献度分析模型")
	@GetMapping(value = "index7")
	public String index7(ModelMap map) {
		map.addAttribute("stoneList", stoneService.findAllStone());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listQuality", stoneService.findDistinctQuality());
		return "index7";
	}

	/**
	 * index7  732  系列商品贡献度分析模型
	 * 
	 * @param request
	 */
	@ApiOperation(value="index7页面,系列商品贡献度分析模型,查询")
	@RequestMapping(value = "productFind", method = RequestMethod.POST)
	@ResponseBody
	public String productFind(HttpServletRequest request) {
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

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByProduct(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByProduct(params); // 图表数据 退回
		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
		
		if (listSale != null) {
			for (int i = 0; i < listSale.size(); i++) {
				listProduct.add(listSale.get(i).getProduct());
				if (selectType.contains("数量")) {
					listProductNum.add(listSale.get(i).getNumberSum());
				}else if (selectType.contains("结算价")) {
					listProductNum.add(listSale.get(i).getSettlementpriceSum());
				}else if (selectType.contains("标价")) {
					listProductNum.add(listSale.get(i).getListpriceSum());
				}else if (selectType.contains("金重")) {
					listProductNum.add(listSale.get(i).getGoldweightSum());
				}else if (selectType.contains("主石")) {
					listProductNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Integer)listProductNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String product = listBack.get(i).getProduct();
                    if(listProduct.contains(product)) {
                        int index = listProduct.indexOf(product);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
        StringBuilder result = new StringBuilder();
		result.append("" + listProductNum + "@" + listProduct);
		return result.toString();
	}

	/**
	 * 跳转到sellsort页面  管理分析模型
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月4日 上午8:29:25
	 */
	@ApiOperation(value="跳转到sellsort页面,管理分析模型")
	@GetMapping(value = "sellsort")
	public String sellsort(ModelMap map){
		map.addAttribute("stoneList", stoneService.findAllStone());
		map.addAttribute("listproduct", stoneService.findDistinctProduct());
		return "sellsort";
	}

	/**
	 * 跳转到plan页面 销售计划分析模型
	 * 
	 * @param map
	 * @return
	 *             String created on 2018年7月4日 上午8:29:25
	 */
	@ApiOperation(value="跳转到plan页面,销售计划分析模型")
	@GetMapping(value = "plan")
	public String plan(ModelMap map){

		map.addAttribute("stoneList", stoneService.findAllStone());
		List<Belong> blist = belongService.findAllBelong();
		List listBelong = new ArrayList<>();
		
		for (int i = 0; i < blist.size(); i++) {
			if(blist.get(i).getBelong_name()!=null) {
				if(!listBelong.contains(blist.get(i).getBelong_name()) && blist.get(i).getBelong_name().length()>0) {
					listBelong.add(blist.get(i).getBelong_name());
				}
			}
		}

		Collections.sort(listBelong, new Comparator<String>() {            
			@Override            
			public int compare(String o1, String o2) {                
				Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);               
				return com.compare(o1, o2);            
			}        
		});
		map.addAttribute("listarea", stoneService.findDistinctArea());
		map.addAttribute("listroom", stoneService.findDistinctRoom());
		map.addAttribute("listBelong", listBelong);

		return "plan";
	}
	/**
	 * 成色分析   source.html
	 *
	 * com.nenu.controller
	 * @param map
	 * @return
	 * created  at 2018年10月19日
	 */
	@ApiOperation(value="跳转到source页面,成色分析模型")
	@GetMapping(value = "source")
	public String source(ModelMap map) {
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
		map.addAttribute("listQuality", stoneService.findDistinctQuality());
		return "source";
	}
	/**
	 * source页面,成色分析模型  查询
	 * @param request
	 */
	@ApiOperation(value="source页面,成色分析模型  查询")
	@RequestMapping(value = "sourceFind", method = RequestMethod.POST)
	@ResponseBody
	public String sourceFind(HttpServletRequest request) {
		String area = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String room = request.getParameter("room");
		String quality = request.getParameter("quality");
		String counter = request.getParameter("counter");
		
		
		String selectType = request.getParameter("selectType");


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(area.contains("所有") || area.length() < 3) {
		}else {
			params.put("area", area);
		}
		if(room.contains("所有") || room.contains("门店")) {
		}else {
			params.put("room", room);
		}
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

		params.put("start", st);
		params.put("end", ed);

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByQuality(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByQuality(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据


		List listProduct = new ArrayList<>();
		List listProductNum = new ArrayList<>();
	
		if(listSale.size()>0) {
			for (int i = 0; i < listSale.size(); i++) {
				listProduct.add(listSale.get(i).getQuality());
				if(selectType.contains("数量")) {
					listProductNum.add(listSale.get(i).getNumberSum());
				}else if(selectType.contains("结算价")) {
					listProductNum.add(listSale.get(i).getSettlementpriceSum());
				}else if(selectType.contains("标价")) {
					listProductNum.add(listSale.get(i).getListpriceSum());
				}else if(selectType.contains("金重")) {
					listProductNum.add(listSale.get(i).getGoldweightSum());
				}else if(selectType.contains("主石")) {
					listProductNum.add(listSale.get(i).getCenterstoneSum());
				}
			}
		}
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String quality1 = listBack.get(i).getQuality();
                    if(listProduct.contains(quality1)) {
                        int index = listProduct.indexOf(quality1);
                        listProductNum.set(index,(Integer)listProductNum.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String quality1 = listBack.get(i).getQuality();
                    if(listProduct.contains(quality1)) {
                        int index = listProduct.indexOf(quality1);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String quality1 = listBack.get(i).getQuality();
                    if(listProduct.contains(quality1)) {
                        int index = listProduct.indexOf(quality1);
                        listProductNum.set(index,(Float)listProductNum.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String quality1 = listBack.get(i).getQuality();
                    if(listProduct.contains(quality1)) {
                        int index = listProduct.indexOf(quality1);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String quality1 = listBack.get(i).getQuality();
                    if(listProduct.contains(quality1)) {
                        int index = listProduct.indexOf(quality1);
                        listProductNum.set(index,(Float)listProductNum.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }

		
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
	    StringBuilder result = new StringBuilder();
		result.append("" + listProductNum + "@" + listProduct + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllSource+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight+"@"+listAllQuality);
		return result.toString();
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
	
	@ApiOperation(value="source页面,成色分析模型 下载excel")
	@RequestMapping(value = "downloadExcelForSource", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelForSource(HttpServletRequest request,HttpServletResponse response) {
		String con  = request.getParameter("context");
		String conList[]=con.split("&");
		String area = conList[0];
		String room = conList[1];
		String counter = conList[2];
		String supplier = conList[3];
		String start = conList[4];
		String end = conList[5];
		String quality = conList[6];
		
		

		String result = "";
		List<StoneAnalysis> listAll = new ArrayList<>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		if(area.length() < 3  || area.contains("所有")) {
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
		if(quality.contains("成色") || quality.contains("所有")) {	
		}else {
			System.out.println("4");
			params.put("quality", quality);
		}
		listAll = stoneService.findStoneByParams(params);

		stoneService.downloadExcelForIndexSource(listAll,response);
		

		return result;
	}
	/**
	 * index724   主石区间销售分析
	 *
	 * com.nenu.controller
	 * @param map
	 * @return
	 * created  at 2018年10月20日
	 */
	@ApiOperation(value="跳转到index724页面,主石区间销售分析 ")
	@GetMapping(value = "index724")
	public String index724(ModelMap map) {
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listSource", stoneService.findDistinctSource());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
        map.addAttribute("listProduct", stoneService.findDistinctProduct());
        return "index724";
	}
	/**
	 * index724   分析
	 * 
	 * @param request
	 * created by lick on 2018年10月20日 下午11:08:48
	 */
	@ApiOperation(value="跳转到index724页面,主石区间销售分析   查询 ")
	@RequestMapping(value = "searchForindex724", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex724(HttpServletRequest request) {
		
		String area = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String source = request.getParameter("source");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(area.contains("所有") || area.length() < 3) {
		}else {
			System.out.println("有地区");
			params.put("area", area);
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
            //销售数据
			list = stoneService.findSourceEqualsSaleByCenterstone(params);
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
            //退回数据
            list = stoneService.findSourceEqualsBackByCenterstone(params);
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
                                    listY.set(i,Double.parseDouble((String) listY.get(i).toString())-list.get(j).getListpriceSum());
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
			//销售数据
			list = stoneService.findSourceEqualsSaleByListprice(params);
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
            //退回数据
            list = stoneService.findSourceEqualsBackByListprice(params);
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
                                    listY.set(i,Double.parseDouble((String) listY.get(i).toString())-list.get(j).getListpriceSum());
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
			//销售数据
			list = stoneService.findSourceEqualsSaleBySettlementprice(params);
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
            //退回数据
            list = stoneService.findSourceEqualsBackBySettlementprice(params);
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
                                    listY.set(i,Double.parseDouble((String) listY.get(i).toString())-list.get(j).getListpriceSum());
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
            list = stoneService.findSourceEqualsSaleByGoldweight(params);
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
            //退回数据
            list = stoneService.findSourceEqualsBackByGoldweight(params);
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
                                    listY.set(i,Double.parseDouble((String) listY.get(i).toString())-list.get(j).getListpriceSum());
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
		listTemp = stoneService.findStoneByParams(params);
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
        StringBuilder result = new StringBuilder();
		result.append("" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllSource+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
		return result.toString();
	}
	/**
	 * 跳转到index722
	 * 
	 * @param map
	 * @return
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	@ApiOperation(value="跳转到index722页面,款式销售排名分析")
	@GetMapping(value = "index722")
	public String index722(ModelMap map){
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listPriceNo", stoneService.findDistinctPriceNo());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
        map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index722";
	}
	/**
	 * 跳转到index723
	 * 
	 * @param map
	 * @return
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	@ApiOperation(value="跳转到index723页面,系列销售排名分析")
	@GetMapping(value = "index723")
	public String index723(ModelMap map){
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listSeries", stoneService.findDistinctSeries());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
        map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index723";
	}
	/**
	 * 跳转到index726  圈口
	 * 
	 * @param map
	 * @return
	 * created by lick on 2018年10月20日 下午10:36:47
	 */
	@ApiOperation(value="跳转到index726页面,圈口分析")
	@GetMapping(value = "index726")
	public String index726(ModelMap map) {
		map.addAttribute("listArea", stoneService.findDistinctArea());
		map.addAttribute("listCircle", stoneService.findDistinctCircle());
		map.addAttribute("listRoom", stoneService.findDistinctRoom());
		map.addAttribute("listSupplier", stoneService.findDistinctSupplier());
		map.addAttribute("listCounter", stoneService.findDistinctCounter());
        map.addAttribute("listProduct", stoneService.findDistinctProduct());
		return "index726";
	}
	/**
	 * index722 查找
	 * 
	 * @param request
	 * @return
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	@ApiOperation(value="index722页面,款式销售排名分析   查询")
	@RequestMapping(value = "searchForindex722", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex722(HttpServletRequest request) {
		String area = request.getParameter("area");
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

            if(area.contains("所有") || area.length()< 3) {
            }else {
                System.out.println("有地区");
                params.put("area", area);
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


            List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByPriceNo(params); // 图表数据 销售
            List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByPriceNo(params); // 图表数据 退回
            List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

            List listX = new ArrayList<>();
            List listY = new ArrayList<>();

            if(listSale.size()>0) {
                for (int i = 0; i < listSale.size(); i++) {
                    listX.add(listSale.get(i).getPriceNo());
                    if(selectType.contains("数量")) {
                        listY.add(listSale.get(i).getNumberSum());
                    }else if(selectType.contains("结算价")) {
                        listY.add(listSale.get(i).getSettlementpriceSum());
                    }else if(selectType.contains("标价")) {
                        listY.add(listSale.get(i).getListpriceSum());
                    }else if(selectType.contains("金重")) {
                        listY.add(listSale.get(i).getGoldweightSum());
                    }else if(selectType.contains("主石")) {
                        listY.add(listSale.get(i).getCenterstoneSum());
                    }
                }
            }
            //退回数据
            if(listBack.size()>0) {
                for (int i = 0; i < listBack.size(); i++) {
                    if(selectType.contains("数量")) {
                        String priceNo1 = listBack.get(i).getPriceNo();
                        if(listX.contains(priceNo1)) {
                            int index = listX.indexOf(priceNo1);
                            listY.set(index,(Integer)listY.get(index)+listBack.get(i).getNumberSum());
                        }
                    }else if(selectType.contains("结算价")) {
                        String priceNo1 = listBack.get(i).getPriceNo();
                        if(listX.contains(priceNo1)) {
                            int index = listX.indexOf(priceNo1);
                            listY.set(index,(Float)listY.get(index)+listBack.get(i).getSettlementpriceSum());
                        }
                    }else if(selectType.contains("标价")) {
                        String priceNo1 = listBack.get(i).getPriceNo();
                        if(listX.contains(priceNo1)) {
                            int index = listX.indexOf(priceNo1);
                            listY.set(index,(Float)listY.get(index)-listBack.get(i).getListpriceSum());
                        }
                    }else if(selectType.contains("金重")) {
                        String priceNo1 = listBack.get(i).getPriceNo();
                        if(listX.contains(priceNo1)) {
                            int index = listX.indexOf(priceNo1);
                            listY.set(index,(Float)listY.get(index)+listBack.get(i).getGoldweightSum());
                        }
                    }else if(selectType.contains("主石")) {
                        String priceNo1 = listBack.get(i).getPriceNo();
                        if(listX.contains(priceNo1)) {
                            int index = listX.indexOf(priceNo1);
                            listY.set(index,(Float)listY.get(index)+listBack.get(i).getCenterstoneSum());
                        }
                    }
                }
            }
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
            StringBuilder result = new StringBuilder();
            result.append("" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
                    listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
                    listAllPriceNo+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
            return result.toString();
        //标价区间
        }else {
            List<ListPrice> listPricelist = new ArrayList<ListPrice>(); // 标价区间
            listPricelist = listPriceService.findAllListPrice();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> params = new HashMap<String, Object>();

            if(area.contains("所有") || area.length() < 3) {
            }else {
                System.out.println("有地区");
                params.put("area", area);
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
            //销售数据
            list = stoneService.findSourceEqualsSaleByListprice(params);
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
            //退回数据
            list = stoneService.findSourceEqualsBackByListprice(params);
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
                                    listY.set(i,Double.parseDouble((String) listY.get(i).toString())-list.get(j).getListpriceSum());
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
            List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);;// 表格数据
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
            StringBuilder result = new StringBuilder();
            result.append("" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
                    listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
                    listAllPriceNo+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
            return result.toString();

        }

	}
	/**
	 * index723 查找
	 * 
	 * @param request
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	@ApiOperation(value="index723页面,系列销售排名分析  查询")
	@RequestMapping(value = "searchForindex723", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex723(HttpServletRequest request){
		String area = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String series = request.getParameter("series");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + area);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(area.contains("所有") || area.length() < 3) {
		}else {
			System.out.println("有地区");
			params.put("area", area);
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


        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleBySeries(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackBySeries(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

        List listX = new ArrayList<>();
        List listY = new ArrayList<>();

        if(listSale.size()>0) {
            for (int i = 0; i < listSale.size(); i++) {
                listX.add(listSale.get(i).getSeries());
                if(selectType.contains("数量")) {
                    listY.add(listSale.get(i).getNumberSum());
                }else if(selectType.contains("结算价")) {
                    listY.add(listSale.get(i).getSettlementpriceSum());
                }else if(selectType.contains("标价")) {
                    listY.add(listSale.get(i).getListpriceSum());
                }else if(selectType.contains("金重")) {
                    listY.add(listSale.get(i).getGoldweightSum());
                }else if(selectType.contains("主石")) {
                    listY.add(listSale.get(i).getCenterstoneSum());
                }
            }
        }
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String series1 = listBack.get(i).getSeries();
                    if(listX.contains(series1)) {
                        int index = listX.indexOf(series1);
                        listY.set(index,(Integer)listY.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String series1 = listBack.get(i).getSeries();
                    if(listX.contains(series1)) {
                        int index = listX.indexOf(series1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String series1 = listBack.get(i).getSeries();
                    if(listX.contains(series1)) {
                        int index = listX.indexOf(series1);
                        listY.set(index,(Float)listY.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String series1 = listBack.get(i).getSeries();
                    if(listX.contains(series1)) {
                        int index = listX.indexOf(series1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String series1 = listBack.get(i).getSeries();
                    if(listX.contains(series1)) {
                        int index = listX.indexOf(series1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }

		
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
        StringBuilder result = new StringBuilder();
		result.append("" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" +
        listAllSeries+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
		return result.toString();
	}
	/**
	 * index726 查找
	 * 
	 * @param request
	 * created by lick on 2018年10月20日 下午10:56:35
	 */
	@ApiOperation(value="index726页面,圈口分析  查询")
	@RequestMapping(value = "searchForindex726", method = RequestMethod.POST)
	@ResponseBody
	public String searchForindex726(HttpServletRequest request) {
		String area = request.getParameter("area");
		String supplier = request.getParameter("supplier");
		String circle = request.getParameter("circle");
		String room = request.getParameter("room");
		String product = request.getParameter("product");
		String counter = request.getParameter("counter");
		
		String selectType = request.getParameter("selectType");

		System.out.println("地区=============" + area);
		System.out.println("类别=============" + selectType);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(area.contains("所有") || area.length() < 3) {
		}else {
			System.out.println("有地区");
			params.put("area", area);
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

        List<StoneAnalysis> listSale = stoneService.findSourceEqualsSaleByCircle(params); // 图表数据 销售
        List<StoneAnalysis> listBack = stoneService.findSourceEqualsBackByCircle(params); // 图表数据 退回
        List<StoneAnalysis> listAll = stoneService.findStoneByParams(params);// 表格数据

        List listX = new ArrayList<>();
        List listY = new ArrayList<>();

        if(listSale.size()>0) {
            for (int i = 0; i < listSale.size(); i++) {
                listX.add(listSale.get(i).getCircle());
                if(selectType.contains("数量")) {
                    listY.add(listSale.get(i).getNumberSum());
                }else if(selectType.contains("结算价")) {
                    listY.add(listSale.get(i).getSettlementpriceSum());
                }else if(selectType.contains("标价")) {
                    listY.add(listSale.get(i).getListpriceSum());
                }else if(selectType.contains("金重")) {
                    listY.add(listSale.get(i).getGoldweightSum());
                }else if(selectType.contains("主石")) {
                    listY.add(listSale.get(i).getCenterstoneSum());
                }
            }
        }
        //退回数据
        if(listBack.size()>0) {
            for (int i = 0; i < listBack.size(); i++) {
                if(selectType.contains("数量")) {
                    String circle1 = listBack.get(i).getCircle();
                    if(listX.contains(circle1)) {
                        int index = listX.indexOf(circle1);
                        listY.set(index,(Integer)listY.get(index)+listBack.get(i).getNumberSum());
                    }
                }else if(selectType.contains("结算价")) {
                    String circle1 = listBack.get(i).getCircle();
                    if(listX.contains(circle1)) {
                        int index = listX.indexOf(circle1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getSettlementpriceSum());
                    }
                }else if(selectType.contains("标价")) {
                    String circle1 = listBack.get(i).getCircle();
                    if(listX.contains(circle1)) {
                        int index = listX.indexOf(circle1);
                        listY.set(index,(Float)listY.get(index)-listBack.get(i).getListpriceSum());
                    }
                }else if(selectType.contains("金重")) {
                    String circle1 = listBack.get(i).getCircle();
                    if(listX.contains(circle1)) {
                        int index = listX.indexOf(circle1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getGoldweightSum());
                    }
                }else if(selectType.contains("主石")) {
                    String circle1 = listBack.get(i).getCircle();
                    if(listX.contains(circle1)) {
                        int index = listX.indexOf(circle1);
                        listY.set(index,(Float)listY.get(index)+listBack.get(i).getCenterstoneSum());
                    }
                }
            }
        }
		
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
        StringBuilder result = new StringBuilder();
		result.append("" + listY + "@" + listX + "@" + listAllDate + "@" + listAllSupplier + "@" + listAllProduct + "@" +
		listAllSettlementprice + "@" + listAllArea+"@" + listAllRoom+"@" + listAllCounter+"@" + 
		listAllCircle+"@" + listAllListprice+"@" + listAllCenterstone+"@" + listAllGoldweight);
		return result.toString();
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
		if(area.length() < 3  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("名称")  || product.contains("所有")) {
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
		listAll = stoneService.findStoneByParams(params);
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
		if(product.length() < 3  || product.contains("所有")) {
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
		listAll = stoneService.findStoneByParams(params);
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
		if(area.length() < 3  || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("名称")  || product.contains("所有")) {
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
		listTemp = stoneService.findStoneByParams(params);
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
		if(area.length() < 3 || area.contains("所有")) {
		}else {
			System.out.println("1");
			params.put("area", area);	
		}
        if(product.contains("名称")  || product.contains("所有")) {
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
		listAll = stoneService.findStoneByParams(params);
		stoneService.downloadExcelForIndex726(listAll,response);
		

		return result;
	}
	/**
	 * plan页面 计划销售   销售计划分析模型
	 *
	 * com.nenu.controller
	 * @param request
	 * created  at 2018年10月24日
	 */
	@ApiOperation(value="plan页面,销售计划分析模型  查询")
	@RequestMapping(value = "searchForIndex741", method = RequestMethod.POST)
	@ResponseBody
	public String searchForIndex741(HttpServletRequest request)  {
		
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
                         List<StoneAnalysis> listStone = new ArrayList<>();
                         //销售数据
                         listStone = stoneService.findSourceEqualsSaleByCounter(pa);
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
                         //退回数据
                         listStone = stoneService.findSourceEqualsBackByCounter(pa);
                         if(listStone.size()>0) {
                             if(selectType.contains("数量")) {
                                 ListTrue.set(i, listStone.get(0).getNumberSum()+Integer.parseInt(ListTrue.get(i).toString()));
                             }else if(selectType.contains("标价")) {
                                 ListTrue.set(i, df.format(Double.parseDouble(ListTrue.get(i).toString())-listStone.get(0).getListpriceSum()));
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
		StringBuilder result = new StringBuilder();
		result.append(""+ListX+"#"+ListTrue+"#"+ListFalse+"#"+listStart+"#"+listEnd+"#"+listRoom+"#"+listBelong+"#"+listNum+"#"+listIndex+"#"+listDo+"#"+listDiff);
		
		return result.toString();
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
	public String downloadGraphExcelFor711(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor711(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor712", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor712(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor712(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForSource", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForSource(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForSource(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor722", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor722(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor722(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor723", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor723(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor723(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor724", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor724(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor724(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor726", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor726(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor726(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex7", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex7(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex7(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor743", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor743(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor743(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor744", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor744(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor744(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForS75", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForS75(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForS75(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForS752", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForS752(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForS752(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex3", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex3(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex3(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex11", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex11(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex11(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor7311", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor7311(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor7311(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelFor7312", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelFor7312(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelFor7312(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex811", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex811(HttpServletRequest request,HttpServletResponse response)  {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex811(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex812", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex812(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex812(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex813", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex813(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex813(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex814", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex814(HttpServletRequest request,HttpServletResponse response) {
		String conGraph  = request.getParameter("context");
		String result = "";
		stoneService.downloadGraphExcelForIndex814(conGraph,response);
		return result;
	}
	@ApiOperation(value="下载excel")
	@RequestMapping(value = "downloadGraphExcelForIndex5", method = RequestMethod.POST)
	@ResponseBody
	public String downloadGraphExcelForIndex5(HttpServletRequest request,HttpServletResponse response)  {
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
			stoneService.downloadGraphExcelForIndex5(conGraph,response);
			return result;
		}
		
	}
	
}