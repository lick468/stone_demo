package com.nenu.controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.nenu.domain.*;
import com.nenu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 数据分析系统
 * 管理员管理
 * @author nenu
 *
 */
@Api(value="AdminAnalysisController",description="数据分析系统管理员管理接口")
@Controller
@RequestMapping(value="/analysis/admin/")
public class AdminAnalysisController {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeRoleService employeeRoleService;
	@Autowired
	private StoneAnalysisService stoneService;
	@Autowired
	private MainStoneService mainStoneService;
	@Autowired
	private ListPriceService listPriceService;
	@Autowired
	private SettlementPriceService settlementPriceService;
	@Autowired
	private BelongService belongService;
	@Autowired
	private BelongAndCounterService belongAndCounterService;
	@Autowired
	private CounterService counterService;
	@Autowired
	private PlanService planService;

	@Autowired
	private GoldWeightService goldWeightService;
	/**
	 * 跳转到主页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/index")
	public String index() {
		
		return "analysis/admin/index";
	}
	/**
	 * 跳转到选择主页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/main")
	public String main() {
		return "main";
	}
	/**
	 * 跳转到exceltosql页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/exceltosqlhtml")
	public String excel() {
		return "analysis/admin/exceltosql";
	}
	
	/**
	 * 跳转到plan页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年10月24日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/planhtml")
	public String planhtml() {
		return "analysis/admin/plan";
	}
	
	/**
	 * 跳转到员工列表页面
	 *
	 * com.nenu.controller
	 * @param map
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/employeeList")
	public String employeeList(ModelMap map) {
		List<Employee> employeeList = employeeService.findAllEmployee();
		map.addAttribute("employeeList", employeeList);
		return "analysis/admin/employeeList";
	}
	/**
	 * 跳转到员工添加页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showEmployeeCreateForm")
	public String showEmployeeCreateForm() {
		return "analysis/admin/employeeCreateForm";
	}
	/**
	 * 跳转到员工更新页面
	 *
	 * com.nenu.controller
	 * @param id
	 * @param map
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showEmployeeUpdateForm/{id}")
	public String showEmployeeUpdateForm(@PathVariable("id") int id,ModelMap map) {
	
		Employee emp = employeeService.findEmployeeByID(id);
		map.addAttribute("employee", emp);
		
		return "analysis/admin/employeeUpdateForm";
	}
	/**
	 * 更新员工完毕跳转到员工列表显示页面
	 *
	 * com.nenu.controller
	 * @param employee
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/updateEmployee")
	public String updateEmployee(Employee employee) {
		System.out.println(employee.getId()+" == " + employee.getEmp_name() +"=="+employee.getEmp_password());
		employeeService.updateEmployee(employee);
		
		return "redirect:/analysis/admin/employeeList";
	}
	/**
	 * 新建员工完毕跳转到员工列表显示页面
	 *
	 * com.nenu.controller
	 * @param employee
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/createEmployee")
	public String createEmployee(Employee employee) {
		employeeService.createEmployee(employee);
		
		return "redirect:/analysis/admin/employeeList";
	}
	/**
	 * 删除员工完毕跳转到员工列表显示页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/deleteEmployee",method=RequestMethod.POST)
	public String deleteEmployee(HttpServletRequest request,ModelMap map) {
		String id = request.getParameter("id");
		employeeService.deleteEmployee(Integer.parseInt(id));

		return "redirect:/analysis/admin/employeeList";
	}
	/**
	 * 跳转到选权限分配页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/employeeListRole")
	public String main(ModelMap map) {
		List<Employee> employeeList = employeeService.findAllEmployee();
		map.addAttribute("employeeList", employeeList);
		return "analysis/admin/employeeListRole";
	}
	/**
	 * 跳转到权限分配页面
	 *
	 * com.nenu.controller
	 * @param id
	 * @param map
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showEmployeeRole/{id}")
	public String showEmployeeRole(@PathVariable("id") int id,ModelMap map) {
	
		List<EmployeeRole> empRoleList = employeeRoleService.findEmployeeRoleByEmpID(id);
		List roleList = new ArrayList<>();
		if(empRoleList!=null) {
			for (int i = 0; i < empRoleList.size(); i++) {
				roleList.add(empRoleList.get(i).getEmp_shownum());
			}
		}
		map.addAttribute("roleList", roleList);
		map.addAttribute("id", id);		
		return "analysis/admin/roleShow";
	}
	/**
	 * 添加权限
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年6月29日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/insertEmployeeRole",method=RequestMethod.POST)
	public String insertEmployeeRole(HttpServletRequest request) {
		String role = request.getParameter("role");
		int emp_id = Integer.parseInt(role);
		String selected = request.getParameter("selected");
		System.out.println(role+"====="+selected);
		String[] list = selected.split(",");
		
		employeeRoleService.deleteEmployeeRoleByEmpID(emp_id);//删除原来的
		for (int i = 0; i < list.length; i++) {//重新添加
			EmployeeRole er = new EmployeeRole();
			er.setEmp_id(emp_id);
			er.setEmp_shownum(list[i]);
			employeeRoleService.insertEmployeeRole(er);
		}
			
		return "analysis/admin/roleShow";
	}
	/**
	 * 导入Excel文件
	 * 
	 * @param file
	 * @param map
	 * @param session
	 * @param request
	 * @param model
	 * @return String
	 * created by lick on 2018年7月2日 下午11:12:50
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value = "/exceltosql", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String excel2sql(@RequestParam(value = "file") MultipartFile file, ModelMap map, HttpSession session,
			HttpServletRequest request, RedirectAttributes model) {
		
		// 获取文件名
		String fileName = file.getOriginalFilename();

		System.out.println(fileName);
		List<StoneAnalysis> stoneList = new ArrayList<StoneAnalysis>();
		stoneList = stoneService.excel2sql(fileName, file);
		map.addAttribute("stoneList", stoneList);
		
		if(stoneList.size()>0) {
			return "成功";
		}else {
			return "失败";
		}
	}
	/**
	 * 跳转到数据管理页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年9月12日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/manageDataNew")
	public String turnToDataListNew( ModelMap map) {
		return "analysis/admin/dataListNew";
	}
	@RequestMapping(value="/managePlanData")
	public String managePlanData( ModelMap map) {
		return "analysis/admin/plandataList";
	}
	/**
	 * 删除一条数据记录
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年9月12日
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value = "/deleteOneData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteOneData(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		System.out.println("id==="+id);
		int i = stoneService.deleteOneData(id);
		if(i==0) {
			return "删除失败";
		}else {
			return "删除成功";
		}
	}
	@ApiOperation(value="删除所有",notes="删除所有")
	@RequestMapping(value = "/deleteAllData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAllData() {
		stoneService.deleteAllData();
		return "删除成功";

	}
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/change")
	public String show2() {
		return "analysis/admin/change";
	}
	@ApiOperation(value="获取数据",notes="获取数据")
	@RequestMapping(value="/getTableData")
	@ResponseBody
	public DatatablesViewPage<StoneAnalysis> getTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> map = new HashMap<String, Object>();
		//System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		 //System.out.println(sEcho);
		 sEcho = (sEcho-1)*10;
		 params.put("sEcho",iDisplayStart);
		 params.put("len", 10);
		 List<StoneAnalysis> stoneList = new ArrayList<StoneAnalysis>();
		 List<StoneAnalysis> stoneNum = stoneService.findAllStone();
		stoneList = stoneService.findStoneByTableInfo(params);
		 
		//获取分页控件的信息
	    String start = request.getParameter("start");
	   // System.out.println(start);
	    String length = request.getParameter("length");
	   // System.out.println(length);
	//获取前台额外传递过来的查询条件
	    String extra_search = request.getParameter("extra_search");
	    //System.out.println(extra_search);
	    DatatablesViewPage<StoneAnalysis> view = new DatatablesViewPage<StoneAnalysis>();
	    view.setiTotalDisplayRecords(stoneNum.size());
	    view.setiTotalRecords(stoneNum.size());

	    view.setAaData(stoneList);
	    //System.out.println(view);
	    return view;
	
	}
	//mainStone
	/**
	 * 跳转到主石主页面
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到主石主页面",notes="跳转到主石主页面")
	@RequestMapping(value="/mainStoneList")
	public String mainStoneList(ModelMap map) {
		List<MainStone> mainStoneList = mainStoneService.findAllMainStone();
		map.addAttribute("mainStoneList", mainStoneList);
		return "analysis/admin/mainStoneList";
	}
	
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value="/showMainStoneCreateForm")
	public String showMainStoneCreateForm() {
		return "analysis/admin/mainStoneCreateForm";
	}
	
	/**
	 * 跳转到修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showMainStoneUpdateForm/{id}")
	public String showMainStoneUpdateForm(@PathVariable("id") int id,ModelMap map) {
		MainStone emp = mainStoneService.findMainStoneById(id);
		map.addAttribute("mainStone", emp);
		return "analysis/admin/mainStoneUpdateForm";
	}
	
	/**
	 * 修改一条主石记录
	 * @return
	 */
	@ApiOperation(value="修改一条主石记录",notes="修改一条主石记录")
	@RequestMapping(value="/updateMainStone")
	public String updateMainStone(MainStone mainStone) {
		mainStoneService.updateMainStone(mainStone);
		return "redirect:/analysis/admin/mainStoneList";
	}
	
	/**
	 * 添加一条主石记录
	 * @return
	 */
	@ApiOperation(value="添加一条主石记录",notes="添加一条主石记录")
	@RequestMapping(value="/createMainStone")
	public String createMainStone(MainStone mainStone) {
		mainStoneService.insertMainStone(mainStone);	
		return "redirect:/analysis/admin/mainStoneList";
	}
	
	/**
	 * 删除一条主石记录
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="删除一条主石记录",notes="删除一条主石记录")
	@RequestMapping(value="/deleteMainStone/{id}")
	public String deleteMainStone(@PathVariable("id") int id,ModelMap map) {
		mainStoneService.deleteMainStone(id);
		return "redirect:/analysis/admin/mainStoneList";
	}
	
	//listprice
	/**
	 * 跳转到标价主页面
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到标价主页面",notes="跳转到标价主页面")
	@RequestMapping(value="/listPriceList")
	public String listPriceList(ModelMap map) {
		List<ListPrice> listPriceList = listPriceService.findAllListPrice();
		map.addAttribute("listPriceList", listPriceList);
		return "analysis/admin/listPriceList";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value="/showListPriceCreateForm")
	public String showListPriceListCreateForm() {
		return "analysis/admin/listPriceCreateForm";
	}
	/**
	 * 跳转到修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showListPriceUpdateForm/{id}")
	public String showListPriceUpdateForm(@PathVariable("id") int id,ModelMap map) {
		ListPrice emp = listPriceService.findListPriceById(id);
		map.addAttribute("listPrice", emp);
		return "analysis/admin/listPriceUpdateForm";
	}
	/**
	 * 修改一条标价记录
	 * @return
	 */
	@ApiOperation(value="修改一条标价记录",notes="修改一条标价记录")
	@RequestMapping(value="/updateListPrice")
	public String updateListPrice(ListPrice listPrice) {
		listPriceService.updateListPrice(listPrice);
		return "redirect:/analysis/admin/listPriceList";
	}
	/**
	 * 添加一条标价记录
	 * @return
	 */
	@ApiOperation(value="添加一条标价记录",notes="添加一条标价记录")
	@RequestMapping(value="/createListPrice")
	public String createMainStone(ListPrice listPrice) {
		listPriceService.insertListPrice(listPrice);	
		return "redirect:/analysis/admin/listPriceList";
	}
	/**
	 * 删除一条标价记录
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="删除一条标价记录",notes="删除一条标价记录")
	@RequestMapping(value="/deleteListPrice/{id}")
	public String deleteListPrice(@PathVariable("id") int id,ModelMap map) {
		listPriceService.deleteListPrice(id);
		return "redirect:/analysis/admin/listPriceList";
	}

	//goldWeight
	/**
	 * 跳转到金重主页面
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到金重主页面",notes="跳转到金重主页面")
	@RequestMapping(value="/goldWeightList")
	public String goldWeightList(ModelMap map) {
		List<GoldWeight> allGoldWeight = goldWeightService.findAllGoldWeight();
		map.addAttribute("goldWeightList", allGoldWeight);
		return "analysis/admin/goldWeightList";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value="/showGoldWeightCreateForm")
	public String showGoldWeightCreateForm() {
		return "analysis/admin/goldWeightCreateForm";
	}
	/**
	 * 跳转到修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showGoldWeightUpdateForm/{id}")
	public String showGoldWeightUpdateForm(@PathVariable("id") int id,ModelMap map) {
        GoldWeight emp = goldWeightService.findGoldWeightById(id);
		map.addAttribute("goldWeight", emp);
		return "analysis/admin/goldWeightUpdateForm";
	}
	/**
	 * 修改一条金重记录
	 * @return
	 */
	@ApiOperation(value="修改一条金重记录",notes="修改一条金重记录")
	@RequestMapping(value="/updateGoldWeight")
	public String updateGoldWeight(GoldWeight goldWeight) {
	    goldWeightService.updateGoldWeight(goldWeight);
		return "redirect:/analysis/admin/goldWeightList";
	}
	/**
	 * 添加一条金重记录
	 * @return
	 */
	@ApiOperation(value="添加一条金重记录",notes="添加一条金重记录")
	@RequestMapping(value="/createGoldWeight")
	public String createGoldWeight(GoldWeight goldWeight) {
	    goldWeightService.insertGoldWeight(goldWeight);
		return "redirect:/analysis/admin/goldWeightList";
	}
	/**
	 * 删除一条金重记录
	 * @param id
	 * @return
	 */
	@ApiOperation(value="删除一条金重记录",notes="删除一条金重记录")
	@RequestMapping(value="/deleteGoldWeight/{id}")
	public String deleteGoldWeight(@PathVariable("id") int id) {
	    goldWeightService.deleteGoldWeight(id);
		return "redirect:/analysis/admin/goldWeightList";
	}



	//settlementPrice
	/**
	 * 跳转到结算价主页
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到结算价主页",notes="跳转到结算价主页")
	@RequestMapping(value="/settlementPriceList")
	public String SettlementPriceList(ModelMap map) {
		List<SettlementPrice> settlementPriceList = settlementPriceService.findAllSettlementPrice();
		map.addAttribute("settlementPriceList", settlementPriceList);
		return "analysis/admin/settlementPriceList";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value="/showSettlementPriceCreateForm")
	public String showSettlementPriceCreateForm() {
		return "analysis/admin/settlementPriceCreateForm";
	}
	/**
	 * 跳转到修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value="/showSettlementPriceUpdateForm/{id}")
	public String showSettlementPriceUpdateForm(@PathVariable("id") int id,ModelMap map) {
		SettlementPrice emp = settlementPriceService.findSettlementPriceById(id);
		map.addAttribute("settlementPrice", emp);
		return "analysis/admin/settlementPriceUpdateForm";
	}
	/**
	 * 更新一条结算价记录
	 * @param settlementPrice
	 * @return
	 */
	@ApiOperation(value="更新一条结算价记录",notes="更新一条结算价记录")
	@RequestMapping(value="/updateSettlementPrice")
	public String updateSettlementPrice(SettlementPrice settlementPrice) {
		settlementPriceService.updateSettlementPrice(settlementPrice);
		return "redirect:/analysis/admin/settlementPriceList";
	}
	/**
	 * 添加一条结算价记录
	 * @param settlementPrice
	 * @return
	 */
	@ApiOperation(value="添加一条结算价记录",notes="添加一条结算价记录")
	@RequestMapping(value="/createSettlementPrice")
	public String createSettlementPrice(SettlementPrice settlementPrice) {
		settlementPriceService.insertSettlementPrice(settlementPrice);	
		return "redirect:/analysis/admin/settlementPriceList";
	}
	/**
	 * 删除一条结算价记录
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value=" 删除一条结算价记录",notes=" 删除一条结算价记录")
	@RequestMapping(value="/deleteSettlementPrice/{id}")
	public String deleteSettlementPrice(@PathVariable("id") int id,ModelMap map) {
		settlementPriceService.deleteSettlementPrice(id);
		return "redirect:/analysis/admin/settlementPriceList";
	}
	//belong
	/**
	 * 跳转到统计对象主页
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到统计对象主页",notes="跳转到统计对象主页")
	@RequestMapping(value="/belongList")
	public String BelongList(ModelMap map) {
		List<Belong> BelongList = belongService.findAllBelongAndCounter();
		map.addAttribute("BelongList", BelongList);
		return "analysis/admin/belongList";
	}
	/**
	 * 跳转到添加页面
	 * @return
	 */
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value="/showBelongCreateForm")
	public String showBelongCreateForm(ModelMap map) {
        List<Counter> listCounter1 = counterService.findAllCounter();
        List listCounter = new ArrayList<>();
        if(listCounter1.size()>0) {
            for (int i = 0; i < listCounter1.size(); i++) {
                listCounter.add(listCounter1.get(i).getCounter_name());
            }
        }
        Collections.sort(listCounter, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
                return com.compare(o1, o2);
            }
        });
        map.addAttribute("listCounter", listCounter);

		return "analysis/admin/belongCreateForm";
	}
	/**
	 * 跳转到更新页面		
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到更新页面",notes="跳转到更新页面")
	@RequestMapping(value="/showBelongUpdateForm/{id}")
	public String showBelongUpdateForm(@PathVariable("id") int id,ModelMap map) {
		Belong emp = belongService.findBelongById(id);
		map.addAttribute("Belong", emp);
		return "analysis/admin/belongUpdateForm";
	}
	/**
	 * 更新一条统计对象		
	 * @param Belong
	 * @return
	 */
	@ApiOperation(value="更新一条统计对象",notes="更新一条统计对象")
	@RequestMapping(value="/updateBelong")
	public String updateBelong(Belong Belong) {
		belongService.updateBelong(Belong);
		return "redirect:/analysis/admin/belongList";
	}
	/**
	 * 添加一个统计对象	
	 * @param belong
	 * @return
	 */
	@ApiOperation(value="添加一个统计对象",notes="添加一个统计对象")
	@RequestMapping(value="/createBelong")
	public String createBelong(Belong belong) {
		List<Belong> belongList = belongService.findAllBelongByBelongName(belong.getBelong_name());
		if(belongList.size()>0) {
		}else {
			belongService.insertBelong(belong);
		}			
		return "redirect:/analysis/admin/belongList";
	}
	/**
	 * 删除一个统计对象和相关记录	
	 * @param id
	 * @param map
	 * @return
	 */
	@ApiOperation(value="删除一个统计对象和相关记录",notes="删除一个统计对象和相关记录")
	@RequestMapping(value="/deleteBelong/{id}")
	public String deleteBelong(@PathVariable("id") int id,ModelMap map) {
		//1.从统计对象和柜台表里删除统计对象为指定对象的相关记录
		Belong belong = belongService.findBelongById(id);
		BelongAndCounter belongAndCounter =new BelongAndCounter();
		belongAndCounter.setBc_belong_name(belong.getBelong_name());
		belongAndCounterService.deleteBelongAndCounterByBelong(belongAndCounter);
		//2.从统计对象表里删除相关的记录
		belongService.deleteBelong(id);
		return "redirect:/analysis/admin/belongList";
	}
	
	//belongAndCounter
	/**
	 * 跳转到统计对象和柜台页面
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转到统计对象和柜台页面",notes="跳转到统计对象和柜台页面")
	@RequestMapping(value="/showBelongAndCounterCreateForm")
	public String showBelongAndCounterCreateForm(ModelMap map) {
		List<Belong> listBelong1 = belongService.findAllBelong();
		List<Counter> listCounter1 = counterService.findAllCounter();
		List listBelong = new ArrayList<>();
		List listCounter = new ArrayList<>();
		if(listBelong1.size()>0) {
			for (int i = 0; i < listBelong1.size(); i++) {
				listBelong.add(listBelong1.get(i).getBelong_name());
			}
		}
		if(listCounter1.size()>0) {
			for (int i = 0; i < listCounter1.size(); i++) {
				listCounter.add(listCounter1.get(i).getCounter_name());
			}
		}
		
		Collections.sort(listBelong, new Comparator<String>() {            
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
		map.addAttribute("listBelong", listBelong);
		map.addAttribute("listCounter", listCounter);
		return "analysis/admin/belongAndCounterCreateForm";
	}
	/**
	 * 删除一条统计对象和柜台
	 * @param request
	 * @param map
	 * @return
	 */
	@ApiOperation(value="删除一条统计对象和柜台",notes="删除一条统计对象和柜台")
	@RequestMapping(value="/deleteBelongAndCounter",method=RequestMethod.POST)
	@ResponseBody
	public String deleteBelongAndCounter(HttpServletRequest request,ModelMap map) {
		//belongService.deleteBelong(id);
		String bc_belong_name = request.getParameter("bc_belong_name");
		String bc_counter_name = request.getParameter("bc_counter_name");
		System.out.println(bc_belong_name+"-------"+bc_counter_name);
		BelongAndCounter belongAndCounter =new BelongAndCounter();
		belongAndCounter.setBc_belong_name(bc_belong_name);
		belongAndCounter.setBc_counter_name(bc_counter_name);
		belongAndCounterService.deleteBelongAndCounterByBelongAndCounter(belongAndCounter);
				
		return "success";
	}
	/**
	 * 添加一个统计对象和柜台
	 * @param belongAndCounter
	 * @return
	 */
	@ApiOperation(value="添加一个统计对象和柜台",notes="添加一个统计对象和柜台")
	@RequestMapping(value="/createBelongAndCounter")
	public String createBelong(BelongAndCounter belongAndCounter) {
		List<BelongAndCounter> bcList = belongAndCounterService.findAllBelongAndCounterByBelongAndCounter(belongAndCounter);
		if(bcList.size()>0) {	
		}else {
			belongAndCounterService.insertBelongAndCounter(belongAndCounter);	
		}	
		return "redirect:/analysis/admin/belongList";
	}
			
	/**
	 * 导入计划任务excel	
	 * @param file
	 * @param map
	 * @param session
	 * @param request
	 * @param model
	 * @return
	 */
	@ApiOperation(value="导入计划任务excel",notes="导入计划任务excel")
	@RequestMapping(value = "/planexceltosql", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String planexceltosql(@RequestParam(value = "file") MultipartFile file, ModelMap map, HttpSession session,
			HttpServletRequest request, RedirectAttributes model) {
				
		// 获取文件名
		String fileName = file.getOriginalFilename();

		System.out.println(fileName);
		List<Plan> planList = new ArrayList<Plan>();
		planList = planService.planexcel2sql(fileName, file);
		
		if(planList.size()>0) {
			return "导入成功";
		}else {
			return "导入失败";
		}
	}
	/**
	 * 删除一条计划任务	
	 * @param request
	 * @return
	 */
	@ApiOperation(value="删除一条计划任务",notes="删除一条计划任务")
	@RequestMapping(value = "/deleteOnePlanData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteOnePlanData(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		System.out.println("id==="+id);
		int i = planService.deleteOneData(id);
		if(i==0) {
			return "删除失败";
		}else {
			return "删除成功";
		}
	}
	/**
	 * 删除所有计划任务	
	 * @return
	 */
	@ApiOperation(value="删除所有计划任务	",notes="删除所有计划任务	")
	@RequestMapping(value = "/deleteAllPlanData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAllPlanData() {
		planService.deleteAllData();
		return "删除成功";
	}
	/**
	 * 获取数据库表数据	
	 * @param aoData
	 * @param request
	 * @return
	 */
	@ApiOperation(value="获取数据库表数据",notes="获取数据库表数据")
	@RequestMapping(value="/getPlanTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Plan> getPlanTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> map = new HashMap<String, Object>();
		//System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();			
		//System.out.println(aoData);
				
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		//System.out.println(sEcho);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		List<Plan> planList = new ArrayList<Plan>();
		List<Plan> planNum = planService.findAllPlan();
		planList = planService.findPlanByTableInfo(params);
				 
		//获取分页控件的信息
		String start = request.getParameter("start");
				   
		String length = request.getParameter("length");
				    
	   //获取前台额外传递过来的查询条件
		String extra_search = request.getParameter("extra_search");
			   
		DatatablesViewPage<Plan> view = new DatatablesViewPage<Plan>();
	    view.setiTotalDisplayRecords(planNum.size());
		view.setiTotalRecords(planNum.size());

	    view.setAaData(planList);
			   
	   return view;
			
	}
	
}
