	package com.nenu.controller;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nenu.domain.DatatablesViewPage;

import com.nenu.domain.Procord;
import com.nenu.domain.Stone;
import com.nenu.domain.StoneCopy;
import com.nenu.domain.Stoninproc;
import com.nenu.domain.Supplier;
import com.nenu.domain.SupplierStone;
import com.nenu.domain.TBackStone;
import com.nenu.service.ProcordService;
import com.nenu.service.StaffService;
import com.nenu.service.StoneService;
import com.nenu.service.StoninprocService;
import com.nenu.service.SupplierService;
import com.nenu.service.SupplierStoneService;
import com.nenu.service.TBackStoneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 钻石流转系统
 * 石头操作员操作
 * @author nenu
 *
 */
@Api(value="BController",description="钻石流转系统石头操作员接口")
@Controller
@RequestMapping(value = "/b")
public class BController {
	@Autowired
	private StoneService stoneService;
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private StoninprocService stoninprocService;
	
	@Autowired
	private ProcordService procordService;
	
	@Autowired
	private  StaffService staffService;
	
	@Autowired
	private  TBackStoneService tBackStoneService;
	
	@Autowired
	private  SupplierStoneService supplierStoneService;
	/**
	 * 进入主界面（石头库管理）
	 *
	 * com.nenu.controller
	 * @param map
	 * @param request
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="跳转页面",notes="显示主页面")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/index")
	public String index( ModelMap map, HttpServletRequest request,HttpSession session) {
		List<Stone> allStoneList = new ArrayList<Stone>();
		allStoneList = stoneService.findAllStone();

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.addAttribute("timeshow", d);

		
		map.addAttribute("allStoneList", allStoneList);// 获取石头库里所有的石头
		map.addAttribute("stoneCopyList", stoneService.findAllStoneCopy());//获取石头库临时表的石头
		map.addAttribute("stoneListNum", stoneService.findAllStoneNum());//获取石头库里数量不少于0的石头
//		map.addAttribute("mainList", stoneService.findDistinctMainStoneNo());// 获取石头库里所有主石编
//		map.addAttribute("subList", stoneService.findDistinctSubStoneNo());// 获取石头库里所有副石编
//		map.addAttribute("timeList", stoneService.findDistinctPurchdate());// 获取石头库里所有时间
		map.addAttribute("loosdoftyList", stoneService.findDistinctLoosdofty());// 获取石头库里所有裸石厂
		map.addAttribute("procordList", procordService.findAllProcord());//获取所有订单信息
		map.addAttribute("staffList", staffService.findAllStaff());//获取所有员工

		
		map.addAttribute("mainNumber", stoneService.findMainStoneInfo().iterator().next().getStone_mainNumber());// 主石数
		map.addAttribute("subNumber", stoneService.findSubStoneInfo().iterator().next().getStone_substoNumber());// 副石数
		map.addAttribute("mainWeight", stoneService.findMainStoneInfo().iterator().next().getStone_mainWeight());// 主石重
		map.addAttribute("subWeight", stoneService.findSubStoneInfo().iterator().next().getStone_substoWgt());// 副石重
		
		map.addAttribute("mainNumberCopy", stoneService.findMainStoneInfoCopy().iterator().next().getStone_mainNumber());// 主石数
		map.addAttribute("subNumberCopy", stoneService.findSubStoneInfoCopy().iterator().next().getStone_substoNumber());// 副石数
		map.addAttribute("mainWeightCopy", stoneService.findMainStoneInfoCopy().iterator().next().getStone_mainWeight());// 主石重
		map.addAttribute("subWeightCopy", stoneService.findSubStoneInfoCopy().iterator().next().getStone_substoWgt());// 副石重
		

		session = request.getSession();
		String user = (String) session.getAttribute("user_name");
		map.addAttribute("user", user);

		map.addAttribute("supplierList",supplierService.findDistinctSupplierName());//获取所有供应商集合
		List<Stone> stoneList = new ArrayList<Stone>();
		stoneList = (List<Stone>) session.getAttribute("stoneList");
		
		map.addAttribute("mes", "信息");
		
		
	
		return "operator_b/index";
	}
	@ApiOperation(value="临时表拷贝到石头表",notes="临时表拷贝到石头表")
	@RequestMapping(value = "/copyToStone", method = RequestMethod.POST)
	@ResponseBody
	public int copyToStone(HttpServletRequest request) {
		int finish=stoneService.copyToStone();
		if(finish!=0)
		{
			stoneService.clearCopy();
		}
		return finish;
	}
	
	
	/**
	 * 获取excel文件里的数据导入数据库中
	 *
	 * com.nenu.controller
	 * @param file
	 * @param map
	 * @param session
	 * @param request
	 * @param model
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="获取excel文件里的数据导入数据库中",notes="获取excel文件里的数据导入数据库中")
	@RequestMapping(value = "/exceltosql", method = { RequestMethod.POST, RequestMethod.GET })
	public String excel2sql(@RequestParam(value = "StoneFile") MultipartFile file, ModelMap map, HttpSession session,
			HttpServletRequest request, RedirectAttributes model) {
		String stone_channelNo = request.getParameter("stone_channelNo");
		System.out.println("stone_channelNo=" + stone_channelNo);
		// 获取文件名
		String fileName = file.getOriginalFilename();
		System.out.println("fileName=" + fileName);
		// 进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
		long size = file.getSize();
		if (file.isEmpty() || size == 0) {
			session.setAttribute("msg", "文件不能为空！");
			System.out.println("文件为空");
			return "redirect:/b/index";
		}
		List<String> stoneNoList = new ArrayList<>();

		stoneNoList = stoneService.excel2sql(fileName, file, stone_channelNo);
		// 有重复石编
		if(stoneNoList.size() > 0) {
            model.addFlashAttribute("stoneNoList",stoneNoList);
        }
			
		String user = (String) session.getAttribute("user_name");
		map.addAttribute("user", user);

		return "redirect:/b/index";
	}
	/**
	 * 根据条件生成excel文件，文件放在D：/stoneFile目录下
	 *
	 * com.nenu.controller
	 * @param request
	 * @return
	 * @throws ParseException String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="生成excel文件",notes="生成excel文件")
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcel(HttpServletRequest request,HttpSession session,HttpServletResponse response) throws ParseException {
		List<Stone> list = new ArrayList<Stone>();
		int i=1;
		String result =request.getParameter("result");
		System.out.println("==========="+result);
		if(result.contains("first")) {
			list = (List<Stone>) session.getAttribute("firstSearchList");
			stoneService.downloadExcel(list,response);
		}else if(result.contains("second")) {
			list = (List<Stone>) session.getAttribute("secondSearchList");
			stoneService.downloadExcel(list,response);
		}else if(result.contains("all")) {
			list = stoneService.findAllStone();
			stoneService.downloadExcel(list,response);
		}
		
		String result1="";
		return result1;
	}
	/**
	 * 下载所有石头库存
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="生成excel文件",notes="生成excel文件")
	@RequestMapping(value = "/downloadAllStoneExcel", method = RequestMethod.POST)
	@ResponseBody
	public String downloadAllStoneExcel(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		List<Stone> list = stoneService.findAllStone();
		stoneService.downloadExcel(list,response);
		String result1="";
		return result1;
	}
	/**
	 * 下载所有石头库存
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="生成excel文件",notes="生成excel文件")
	@RequestMapping(value = "/downloadProcord", method = RequestMethod.GET)
	@ResponseBody
	public String downloadProcord(HttpServletRequest request,HttpServletResponse response) throws ParseException {
        List<Procord> list = procordService.findAllProcord();
        procordService.downloadExcel(list,response);
		String result1="";
		return result1;
	}
	@ApiOperation(value="生成excel文件",notes="生成excel文件")
	@RequestMapping(value = "/downloadExcelSupplier", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelSupplier(HttpSession session,HttpServletResponse response) throws ParseException {
		List<SupplierStone> supplierStoneList = (List<SupplierStone>) session.getAttribute("supplierStoneList");
		
		stoneService.downloadExcelSupplier(supplierStoneList,response);
		String result1="";
		return result1;
	}
	/**
	 * 供应商库存汇总下载
	 *
	 * com.nenu.controller
	 * @param request
	 * @param session
	 * @param response
	 * @return
	 * @throws ParseException String
	 * created  at 2018年12月19日
	 */
	@ApiOperation(value="供应商库存汇总下载",notes="供应商库存汇总下载")
	@RequestMapping(value = "/downloadExcelAllSupplier", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelAllSupplier(HttpServletRequest request,HttpSession session,HttpServletResponse response) throws ParseException {
		List listSupplier = new ArrayList<>();
		int i;
		List<Stoninproc> list = new ArrayList<Stoninproc>();
		List<Stoninproc> list_supplier = new ArrayList<Stoninproc>();
		list = stoninprocService.findAllStoninprocWithStateEqualZero();
		for ( i = 0; i < list.size(); i++) {
			String s1 = list.get(i).getStoninproc_supplier();
			if ( s1 != null) {
				if (!listSupplier.contains(s1) && s1.length() > 0) {
					listSupplier.add(s1);
					Stoninproc s = new Stoninproc();
					s.setStoninproc_supplier(s1);
					s.setMainNumber(0);
					s.setMainWeight(0);
					s.setSubNumber(0);
					s.setSubWeight(0);
					list_supplier.add(s);
					
				}
			}
		}
		for ( i = 0; i < list.size(); i++) {
			String s1 = list.get(i).getStoninproc_supplier();
			for (int j = 0; j < list_supplier.size(); j++) {
				String s2 = list_supplier.get(j).getStoninproc_supplier();
				if(s1.contains(s2)) {
					Stoninproc s = list_supplier.get(j);
					if(list.get(i).getStoninproc_stoneNo().matches(".*[a-zA-Z].*")) {//主石
						int old_n = s.getMainNumber();
						double old_w = s.getMainWeight(); 
						s.setMainNumber(old_n+list.get(i).getStoninproc_number());
						s.setMainWeight(old_w+list.get(i).getStoninproc_stoneWeight());
					}else {
						int old_n = s.getSubNumber();
						double old_w = s.getSubWeight();
						s.setSubNumber(old_n+list.get(i).getStoninproc_number());
						s.setSubWeight(old_w+list.get(i).getStoninproc_stoneWeight());
					}
				}
			}
		}
		
		List<SupplierStone> supplierStoneList = supplierStoneService.findSupplierStoneCountWithSupplierName();
		System.out.println(supplierStoneList);
		
		stoneService.downloadExcelAllSupplier(list_supplier,response);
		String result1="result";
		return result1;
	}

	
		
	/**
	 * ajax传值 动态查找（主石编，副石编，采购日期，裸石厂）
	 * 
	 * @param request
	 * @param map
	 * @return List<Stone> created by lick on 2018年5月20日
	 * @throws ParseException 
	 */
	@ApiOperation(value="ajax传值 动态查找（主石编，副石编，采购日期，裸石厂）",notes="ajax传值 动态查找（主石编，副石编，采购日期，裸石厂）")
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> test(HttpServletRequest request, ModelMap map,HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String queryType = request.getParameter("type");
		//System.out.println(queryType+"=="+request.getParameter("purchDateStart")+"====="+request.getParameter("purchdateEnd"));
		
		List<Stone> list = new ArrayList<Stone>();
		if (queryType.equals("stone_mainStoneNo")) {
			if( request.getParameter("mainNo")!=null) { //按主石编查找
				String mainNo = request.getParameter("mainNo");
				list = stoneService.findStoneByMainNoForManage(mainNo);
			}		
		}else if (queryType.equals("stone_substoNo")) {
			if( request.getParameter("substoNo")!=null) {//副石编查找
				String substoNo = request.getParameter("substoNo");
				list = stoneService.findStoneBySubNoForManage(substoNo);
			}
		}else if (queryType.equals("stone_loosdofty")) {
			if( request.getParameter("loosdofty")!=null) {//按裸石厂查找
				String stone_loosdofty = request.getParameter("loosdofty");
				list = stoneService.findStoneByLoosdofty(stone_loosdofty);	
			}
		}else if (queryType.equals("stone_purchdate")) {
			if( request.getParameter("start")!=null) {//按日期查找
				if(request.getParameter("end")!=null) {
					String start = request.getParameter("start");
					String end = request.getParameter("end");
					Date purchdateStart = new Date();
					Date purchdateEnd = new Date();
					purchdateStart = sdf.parse(start);
					purchdateEnd = sdf.parse(end);
					System.out.println("日期"+purchdateStart+"-===="+purchdateEnd);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("purchdateStart", purchdateStart);
					params.put("purchdateEnd", purchdateEnd);
					list = stoneService.findStoneByPurchdate(params);
				}
			}
		}
	
		session.setAttribute("firstSearchList", list);
		return list;
	}
	
	/**
	 * 二次检索
	 *
	 * com.nenu.controller
	 * @param request
	 * @param map
	 * @param session
	 * @return
	 * @throws ParseException List<Stone>
	 * created  at 2018年6月8日
	 */
	@ApiOperation(value="ajax传值 动态查找（二次检索）",notes="ajax传值 动态查找（二次检索）")
	@RequestMapping(value = "/second", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> testsecond(HttpServletRequest request, ModelMap map,HttpSession session) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String queryType = request.getParameter("type");
		//System.out.println(queryType+"=="+request.getParameter("purchDateStart")+"====="+request.getParameter("purchdateEnd"));
		
		List<Stone> list = new ArrayList<Stone>();
		
		if (queryType.equals("stone_mainStoneNo")) {
			if( request.getParameter("mainNo")!=null) { //按主石编查找
				String mainNo = request.getParameter("mainNo");
				list = stoneService.findStoneByMainNoForManage(mainNo);
			}		
		}else if (queryType.equals("stone_substoNo")) {
			if( request.getParameter("substoNo")!=null) {//副石编查找
				String substoNo = request.getParameter("substoNo");
				list = stoneService.findStoneBySubNoForManage(substoNo);
			}
		}else if (queryType.equals("stone_loosdofty")) {
			if( request.getParameter("loosdofty")!=null) {//按裸石厂查找
				String stone_loosdofty = request.getParameter("loosdofty");
				list = stoneService.findStoneByLoosdofty(stone_loosdofty);	
			}
		}else if (queryType.equals("stone_purchdate")) {
			if( request.getParameter("start")!=null) {//按日期查找
				if(request.getParameter("end")!=null) {
					String start = request.getParameter("start");
					String end = request.getParameter("end");
					Date purchdateStart = new Date();
					Date purchdateEnd = new Date();
					purchdateStart = sdf.parse(start);
					purchdateEnd = sdf.parse(end);
					System.out.println("日期"+purchdateStart+"-===="+purchdateEnd);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("purchdateStart", purchdateStart);
					params.put("purchdateEnd", purchdateEnd);
					list = stoneService.findStoneByPurchdate(params);
				}
			}
		}
		List<Stone> firstList = new ArrayList<Stone>();
		List<Stone> result = new ArrayList<Stone>();
		firstList = (List<Stone>) session.getAttribute("firstSearchList");
		if(firstList!=null) {	
			for(int i=0;i<list.size();i++) {
				for(int j = 0;j<firstList.size();j++) {
					if(list.get(i).getStone_ID().equals(firstList.get(j).getStone_ID())) {
						result.add(list.get(i));
					}
				}
			}
		}
		//System.out.println(result);
		
		session.setAttribute("secondSearchList", result);
		return result;
	}
	/**
	 * 日期转换函数
	 *
	 * com.nenu.controller
	 * @param binder void
	 * created  at 2018年6月4日
	 */
	@InitBinder    
	public void initBinder(WebDataBinder binder) {    

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    

	        dateFormat.setLenient(false);    

	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));    

	}
	
	/**
	 * 石库管理石编查找
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@ApiOperation(value="石库管理石编查找",notes="石库管理石编查找")
	@RequestMapping(value = "/manageStoneNoSearch", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> manageStoneNoSearch(HttpServletRequest request, ModelMap map)  {	
		List<Stone> list = new ArrayList<Stone>();
		String sno = request.getParameter("sno");
		if(sno.matches(".*[a-zA-Z].*")) {//按主石编查找
			list = stoneService.findStoneByMainNoForManage(sno);
		}else {//副石编查找
			list = stoneService.findStoneBySubNoForManage(sno);
		}
		return list;
	}
	
	
	/**
	 * 入库 插入一条石头记录
	 *
	 * com.nenu.controller
	 * @param s
	 * @param bin
	 * @param request
	 * @param map
	 * @return
	 * @throws ParseException String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="入库 插入一条石头记录",notes="入库 插入一条石头记录")
	@RequestMapping(value = "/insertstone", method = RequestMethod.POST)
	public String insertsstone(StoneCopy s,BindingResult bin, HttpServletRequest request,ModelMap map) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		s.setStone_ID(uuid);
		Date d = new Date();
		String t = request.getParameter("stone_purchdate");
		System.out.println(t);
		s.setStone_purchdate(sdf.parse(t));
		stoneService.insertStone(s);
		
		//System.out.println(s.getStone_purchdate() + "---" + s.getStone_loosdofty());

		return "redirect:/b/index";
	}
	/**
	 * 跳转到new.html页面 
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月4日
	 */
	@RequestMapping(value = "/new")
	public String dd() {
		return "operator_b/new";
	}
	/**
	 * 跳转到main.html页面 
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月4日
	 */
	@RequestMapping(value = "/main")
	public String dds() {
		return "main";
	}
	@ApiOperation(value="查找一条临时表石头记录",notes="查找一条临时表石头记录")
	@RequestMapping(value = "/findStoneCopyByStoneIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public StoneCopy findStoneCopyByStoneIDAJAX(HttpServletRequest request) {
		String stone_ID = request.getParameter("sid");
		StoneCopy stone = stoneService.findStoneCopyByStoneIDAJAX(stone_ID);

		return stone;
	}
	
	/**
	 * 更新石头信息
	 *
	 * com.nenu.controller
	 * @param stone
	 * @param bin
	 * @param map
	 * @return String
	 * created  at 2018年6月4日
	 * @throws ParseException 
	 */
	@ApiOperation(value="更新石头信息",notes="更新石头信息")
	@RequestMapping(value = "/updateStone", method = RequestMethod.POST)
	public String stoneUpdate(Stone stone,BindingResult bin, HttpServletRequest request,ModelMap map,RedirectAttributes redirectAttributes) throws ParseException {
		//System.out.println(stone.getStone_ID()+"==="+stone.getStone_channelNo()+"===="+stone.getStone_porter());
		String dadd = request.getParameter("stone_purchdate").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		stone.setStone_purchdate(sdf.parse(dadd));
		stoneService.updateStone(stone);
		redirectAttributes.addFlashAttribute("message", "updateStone");
		return "redirect:/b/index";
	}
	/**
	 * 更新临时库石头信息
	 *
	 * com.nenu.controller
	 * @param stone
	 * @param bin
	 * @param request
	 * @param map
	 * @return
	 * @throws ParseException String
	 * created  at 2018年6月25日
	 */
	@ApiOperation(value="更新临时库石头信息",notes="更新临时库石头信息")
	@RequestMapping(value = "/updateStoneCopy", method = RequestMethod.POST)
	public String stoneCopyUpdate(StoneCopy stone,BindingResult bin, HttpServletRequest request,ModelMap map,RedirectAttributes redirectAttributes) throws ParseException {
		System.out.println(stone.getStone_ID()+"==="+stone.getStone_channelNo()+"===="+stone.getStone_porter());
		String dadd = request.getParameter("stone_purchdate").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		stone.setStone_purchdate(sdf.parse(dadd));
		stoneService.updateStoneCopy(stone);
		redirectAttributes.addFlashAttribute("message", "updateStoneCopy");
		return "redirect:/b/index";
	}
	@ApiOperation(value="删除一条临时库石头信息",notes="删除一条临时库石头信息")
	@RequestMapping(value="/deleteStoneCopyByStoneIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public String deleteStoneCopy(HttpServletRequest request) {
		String stone_ID = request.getParameter("sid");
		stoneService.deleteStoneCopy(stone_ID);
		
		return "删除成功";
		
	}
	
	
	
	/**
	 * 生成订单
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年6月4日
	 * @throws ParseException 
	 */
	@ApiOperation(value="生成订单",notes="生成订单")
	@RequestMapping(value = "/insertOrder", method = RequestMethod.POST)
	public String sendStone(HttpServletRequest request,RedirectAttributes redirectAttributes) throws ParseException  {
		String supplier = request.getParameter("stone_supplierName");//供应商
		String procord_batch = request.getParameter("procord_batch");//批次
		String procord_delydate = request.getParameter("procord_delydate");//出货日期
		String procord_delyman = request.getParameter("procord_delyman");//送货人
		String procord_preparer = request.getParameter("procord_preparer");//经手人
		String procord_porter = request.getParameter("procord_porter");//接货人
		String qqq = request.getParameter("table_body");
		//System.out.println("qqq="+qqq);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Stoninproc   stoninproc =  stoninprocService.findLastStoninproc();//订单石头库最后一条记录
		String date = sdf.format(new Date()).replaceAll("-", "");//获取现在时间 例如：20180602
		long stoninproc_procordNo = 0;//订单号
		int stoninproc_seqno=0;//序号
		
		if(stoninproc!=null) {
			stoninproc_seqno = stoninproc.getStoninproc_seqno();
			String num = String.valueOf(stoninproc.getStoninproc_procordNo()).substring(0, 8);
			System.out.println(num+"======="+date);
			if(date.equals(num)) {
				System.out.println("日期相同");
				stoninproc_procordNo = stoninproc.getStoninproc_procordNo()+1;		
			}else {
				stoninproc_procordNo = Long.parseLong(date)*1000+1;//订单号		
			}
		}else {
			stoninproc_procordNo = Long.parseLong(date)*1000+1;//订单号
		}
		
		System.out.println(qqq+"======="+supplier);
		String[] rr = qqq.split("&");
		System.out.println("rr.length="+rr.length);
		int n = stoninproc_seqno;
		int num=0;//石头数量
		double money = 0;//总金额
		double total_weight = 0;//总重
		for(int i=0;i<=rr.length;i++)
		{	
			if(i%6==5) {	
				Stoninproc  stoninpro = new Stoninproc();
				Stone stone  = new Stone();
				SupplierStone supplierStone = new SupplierStone();
				String stoninproc_ID = UUID.randomUUID().toString().replaceAll("-", "");// UUID
				stoninpro.setStoninproc_ID(stoninproc_ID);
				stoninpro.setStoninproc_procordNo(stoninproc_procordNo);
				stoninpro.setStoninproc_seqno(n);
				stoninpro.setStoninproc_stoneNo(rr[i-3]);
				stoninpro.setStoninproc_number(Integer.parseInt(rr[i-2]));
				
				supplierStone.setSupplier_delyman(procord_delyman);
				supplierStone.setSupplier_name(supplier);
				supplierStone.setSupplier_porter(procord_porter);
				supplierStone.setSupplier_preparer(procord_preparer);
				supplierStone.setSupplier_procorNo(stoninproc_procordNo);
				
				if(!rr[i-3].matches(".*[a-zA-Z].*")) {//不包含字母 的是 副石编
					List<Stone> list = stoneService.findStoneBySubNo(rr[i-3]);//根据副石编获取副石信息
					if(!list.isEmpty()) {
						total_weight += list.get(0).getStone_substoWgt();
						stoninpro.setStoninproc_stoneClarity( list.get(0).getStone_clarity());
						stoninpro.setStoninproc_stoneColor(list.get(0).getStone_color());
						stoninpro.setStoninproc_stoneShape(list.get(0).getStone_shape());
						stoninpro.setStoninproc_stoneWeight(list.get(0).getStone_substoWgt());
						stoninpro.setStoninproc_subpay(list.get(0).getStone_substoPrperct()*list.get(0).getStone_substoWgt());
						stoninpro.setStoninproc_stoneState(0);
						
						//供应商库存
						supplierStone.setSupplier_subNumber(Integer.parseInt(rr[i-2]));
						supplierStone.setSupplier_subStoneNo(list.get(0).getStone_substoNo());
						supplierStone.setSupplier_subWeight(list.get(0).getStone_substoWgt());
						supplierStone.setSupplier_purchdate(list.get(0).getStone_purchdate());
					}
					
					if(rr[i].contains("none")) {
						stoninpro.setStoninproc_stoneContext("");
					}else {
						stoninpro.setStoninproc_stoneContext(rr[i]);
					}
					stoninpro.setStoninproc_supplier(supplier);
					stoninpro.setStoninproc_batch(procord_batch);
					stoninprocService.insertStoninproc(stoninpro);//插入进数据库
					supplierStoneService.insertSupplierStone(supplierStone);//插入进数据库
					double price = list.get(0).getStone_substoPrperct();
					double weight = list.get(0).getStone_substoWgt();
					int number = Integer.parseInt(rr[i-2]);
					money += price* weight;
					int before = list.get(0).getStone_substoNumber();//获取副石数量
					int after = before-number;//获得选石之后的副石数量
					System.out.println("number="+number+",after="+after);
					stone.setStone_substoNo(rr[i-3]);
					stone.setStone_substoNumber(after);
					
					stoneService.updateSubStoneNumber(stone);//更新副石数量
				}else {
					List<Stone> list = stoneService.findStoneByMainNo(rr[i-3]);//根据主石编获取主石信息
					if(!list.isEmpty()) {
						total_weight +=list.get(0).getStone_mainWeight();
						stoninpro.setStoninproc_stoneClarity( list.get(0).getStone_clarity());
						stoninpro.setStoninproc_stoneColor(list.get(0).getStone_color());
						stoninpro.setStoninproc_stoneShape(list.get(0).getStone_shape());
						stoninpro.setStoninproc_stoneWeight(list.get(0).getStone_mainWeight());
						stoninpro.setStoninproc_subpay(list.get(0).getStone_mainPrperct()*list.get(0).getStone_mainWeight());
						stoninpro.setStoninproc_stoneState(0);
						
						//供应商库存
						supplierStone.setSupplier_mainNumber(Integer.parseInt(rr[i-2]));
						supplierStone.setSupplier_mainStoneNo(list.get(0).getStone_mainStoneNo());
						supplierStone.setSupplier_mainWeight(list.get(0).getStone_mainWeight());
						supplierStone.setSupplier_purchdate(list.get(0).getStone_purchdate());
					}
					
					if(rr[i].contains("none")) {
						stoninpro.setStoninproc_stoneContext("");
					}else {
						stoninpro.setStoninproc_stoneContext(rr[i]);
					}
					stoninpro.setStoninproc_supplier(supplier);
					stoninpro.setStoninproc_batch(procord_batch);
					stoninprocService.insertStoninproc(stoninpro);//插入进数据库
					supplierStoneService.insertSupplierStone(supplierStone);//插入进数据库
					double price = list.get(0).getStone_mainPrperct();
					double weight = list.get(0).getStone_mainWeight();
					int number = Integer.parseInt(rr[i-2]);
					money += price * weight;
					int before = list.get(0).getStone_mainNumber();//获取主石数量
					int after = before-number;//获得选石之后的主石数量 
					System.out.println("number="+number+",after="+after);
					stone.setStone_mainStoneNo(rr[i-3]);
					stone.setStone_mainNumber(after);
					
					stoneService.updateMainStoneNumber(stone);//更新主石数量
				}
				n++;
				num++;
				
				//System.out.println("石编是："+rr[i-2]+"==============石数是："+rr[i-1]+"===============石价是："+rr[i]);
			}
		}
		
		
		
		Procord procord = new Procord();//加工订单表
		procord.setProcord_status(0);//订单状态
		procord.setProcord_backcount(0);//回库数量
		procord.setProcord_no(stoninproc_procordNo);//订单号
		procord.setProcord_date(new Date());//订单生成日期
		procord.setProcord_supplier(supplier);//供应商
		procord.setProcord_goodscount(num);//石头数量
		procord.setProcord_pay(money);//货款金额
		procord.setProcord_weight(total_weight);//石重
		procord.setProcord_delydate(sdf.parse(procord_delydate));//出货日期
		procord.setProcord_delyman(procord_delyman);
		procord.setProcord_porter(procord_porter);
		procord.setProcord_preparer(procord_preparer);
		procord.setProcord_batch(procord_batch);//批次
		procordService.insertProcord(procord);
		
		redirectAttributes.addFlashAttribute("message", "insertOrder");
		return "redirect:/b/index";

	}
	/**
	 * ajax根据stone_ID获取一条石头信息并返回
	 *
	 * com.nenu.controller
	 * @param request
	 * @return Stone
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="ajax根据stone_ID获取一条石头信息并返回",notes="ajax根据stone_ID获取一条石头信息并返回")
	@RequestMapping(value = "/findStoneByStoneIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public Stone findStoneByStoneIDAJAX(HttpServletRequest request) {
		String stone_ID = request.getParameter("sid");
		Stone stone = new Stone();
		if(stone_ID.length()<10) {
            SupplierStone supplierStone = supplierStoneService.getSupplierStoneByID(Integer.parseInt(stone_ID));
            if(supplierStone!=null) {
                String mainNo = supplierStone.getSupplier_mainStoneNo();
                if(!mainNo.isEmpty()) {
                    List<Stone> sList =  stoneService.findStoneByMainNo(mainNo);
                    if(!sList.isEmpty()) {
                        stone = sList.get(0);
                    }
                }
                String subNo = supplierStone.getSupplier_subStoneNo();
                if(subNo.length()>1) {
                    List<Stone> sList = stoneService.findStoneBySubNo(subNo);
                    if(!sList.isEmpty()) {
                        stone = sList.get(0);
                    }
                }
            }
        }else {
            stone = stoneService.findStoneByStoneIDAJAX(stone_ID);
        }


		return stone;
	}
	/**
	 * ajax根据procord_no获取一条订单信息并返回
	 *
	 * com.nenu.controller
	 * @param request
	 * @return Procord
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="ajax根据procord_no获取一条订单信息并返回",notes="ajax根据procord_no获取一条订单信息并返回")
	@RequestMapping(value = "/editProcord", method = RequestMethod.POST)
	@ResponseBody
	public Procord editProcord(HttpServletRequest request) {
		String procord_no = request.getParameter("procord_no");
		Procord procord = procordService.findProcordByProcordNo(Long.parseLong(procord_no));

		return procord;
	}
	/**
	 * 更新订单信息
	 *
	 * com.nenu.controller
	 * @param procord
	 * @param bin
	 * @param map
	 * @return String
	 * created  at 2018年6月5日
	 * @throws ParseException 
	 */
	@ApiOperation(value="更新订单信息",notes="更新订单信息")
	@RequestMapping(value = "/updateProcord", method = RequestMethod.POST)
	public String updateProcord(Procord procord,BindingResult bin,HttpServletRequest request, 
			ModelMap map,RedirectAttributes redirectAttributes) throws ParseException {
		String dadd = request.getParameter("procord_date").toString();
		String dely = request.getParameter("procord_delydate").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.println(procord.getProcord_date()+"=================="+dadd);
		procord.setProcord_date(sdf.parse(dadd));
		procord.setProcord_delydate(sdf.parse(dely));
		procordService.updateProcord(procord);
		redirectAttributes.addFlashAttribute("message", "updateProcord");
		return "redirect:/b/index";
	}
	@ApiOperation(value="清空数据",notes="清空数据")
	@RequestMapping(value = "/deleteAllData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAllData() {
		stoneService.deleteAllData();
		return "删除成功";
	}
	/**
	 * 总部退石
	 *
	 * com.nenu.controller
	 * @param request
	 * @param redirectAttributes
	 * @return String
	 * created  at 2018年11月9日
	 */
	@ApiOperation(value="总部退石",notes="总部退石")
	@RequestMapping(value = "/deleteStoneByStoneIDAJAX", method = RequestMethod.POST)
	public String deleteStoneByStoneIDAJAX(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String sid = request.getParameter("delete_ID");
		System.out.println(sid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Stone s = new Stone();
		s.setStone_ID(sid);
		s.setStone_reserved1(1);
		stoneService.deleteStoneByStoneIDAJAX(s);
		Stone ss = new Stone();
		ss =stoneService.findStoneByStoneID(sid); 
		if(ss.getStone_substoNo().length()>1) {//副石
			TBackStone tb = new TBackStone();
			tb.setTback_time(new Date());
			tb.setTback_stoneNo(String.valueOf(ss.getStone_substoNo()));
			tb.setTback_content(""+sdf.format(new Date())+"：将副石编为>"+ss.getStone_substoNo()+"<的石头从总部退石。");
			tBackStoneService.insertTBackStone(tb);
		}else {//主石
			TBackStone tb = new TBackStone();
			tb.setTback_time(new Date());
			tb.setTback_stoneNo(ss.getStone_mainStoneNo());
			tb.setTback_content(""+sdf.format(new Date())+"：将主石编为>"+ss.getStone_mainStoneNo()+"<的石头从总部退石。");
			tBackStoneService.insertTBackStone(tb);
		}
		
		redirectAttributes.addFlashAttribute("message", "deleteStone");
		return "redirect:/b/index";
	}
	/**
	 * 总部退石查询
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="总部退石查询",notes="总部退石查询")
	@RequestMapping(value = "/tbackStoneSearch", method = RequestMethod.POST)
	@ResponseBody
	public List<TBackStone> tbackStoneSearch(HttpServletRequest request,HttpSession session) throws ParseException {
		String type =request.getParameter("type");
		List<TBackStone> tbList = new ArrayList<TBackStone>();
		if(type.contains("tback_no")) {
			String sno = request.getParameter("sno");
			tbList = tBackStoneService.findTBackStoneByStoneNo(sno);
		}else if(type.contains("tback_time")) {
			String start = request.getParameter("start");
			String end = request.getParameter("end");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start", sdf.parse(start));
			params.put("end", sdf.parse(end));
			tbList = tBackStoneService.findTBackStoneByTime(params);
		}
		session.setAttribute("tbList", tbList);
		return tbList;
	}
	@ApiOperation(value="生成excel文件",notes="生成excel文件")
	@RequestMapping(value = "/downloadBackExcel", method = RequestMethod.POST)
	@ResponseBody
	public String downloadBackExcel(HttpServletRequest request,HttpSession session,HttpServletResponse response) throws ParseException {
		List<TBackStone> list = new ArrayList<TBackStone>();
		list = (List<TBackStone>) session.getAttribute("tbList");
		tBackStoneService.downloadExcel(list,response);
	
		String result1="";
		return result1;
	}
	
	
	
	/**
	 * 选石  一键选石
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List<TBackStone>
	 * created  at 2018年10月21日
	 */
	@ApiOperation(value="选石  一键选石",notes="选石  一键选石")
	@RequestMapping(value = "/xuanshiForAll", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> xuanshiForAll(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		List<Stone> slist = new ArrayList<>();
		String type = request.getParameter("type");
		System.out.println("type="+type);
		if(type.contains("all")) {
			slist = stoneService.findAllStone();
		}else if(type.contains("time")){
			String stone_purchdate = request.getParameter("selectTime");
			params.put("stone_purchdate", stone_purchdate);
			slist = stoneService.findAllStoneNumWithTime(params);
		}else if(type.contains("no")){
			String stone_no = request.getParameter("selectNo");
			if(stone_no.matches(".*[a-zA-Z].*")) {//主石
				slist = stoneService.findStoneByMainNoForManage(stone_no);
			}else {
				slist = stoneService.findStoneBySubNoForManage(stone_no);
			}
		}
		return slist;
	}
	/**
	 * 选石  追个添加
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List<TBackStone>
	 * created  at 2018年10月21日
	 */
	@ApiOperation(value="选石    追个添加",notes="选石    追个添加")
	@RequestMapping(value = "/xuanshiForOne", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> xuanshiForOne(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		List<Stone> slist = new ArrayList<>();
		Stone s = new Stone();
		String sid = request.getParameter("sid");
		
		s =  stoneService.findStoneByStoneID(sid);
		slist.add(s);
		
		System.out.println(slist.size()+"=="+slist);
		return slist;
	}
	/**
	 * 根据采购日期查找显示
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List<Stone>
	 * created  at 2018年10月21日
	 */
	@ApiOperation(value="根据采购日期查找显示",notes="根据采购日期查找显示")
	@RequestMapping(value = "/xuanshiForShow", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> xuanshiForShow(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Stone> slist = new ArrayList<>();
		String stone_purchdate = request.getParameter("selectTime");
		params.put("stone_purchdate", stone_purchdate);
		slist = stoneService.findAllStoneNumWithTime(params);
		return slist;
	}
	/**
	 * 根据石编选石
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List<Stone>
	 * created  at 2018年12月21日
	 */
	@ApiOperation(value="根据石编选石",notes="根据石编选石")
	@RequestMapping(value = "/xuanshiForShowForNo", method = RequestMethod.POST)
	@ResponseBody
	public List<Stone> xuanshiForShowForNo(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Stone> slist = new ArrayList<>();
		String stone_no = request.getParameter("selectNo");
		if(stone_no.matches(".*[a-zA-Z].*")) {//主石
			slist = stoneService.findStoneByMainNoForManage(stone_no);
		}else {
			slist = stoneService.findStoneBySubNoForManage(stone_no);
		}
		return slist;
	}
	/**
	 * 获取表格数据（分页）
	 *
	 * com.nenu.controller
	 * @param aoData
	 * @param request
	 * @return DatatablesViewPage<Plan>
	 * created  at 2018年12月20日
	 */
	@ApiOperation(value="获取表格数据（分页）",notes="获取表格数据（分页）")
	@RequestMapping(value="/getTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Stone> getStoneTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> map = new HashMap<String, Object>();
		//System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();			
		// System.out.println(aoData);
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		 //System.out.println(sEcho);
		 sEcho = (sEcho-1)*10;
		 params.put("sEcho",iDisplayStart);
		 params.put("len", 10);
		 List<Stone> stoneList = new ArrayList<Stone>();
		 List<Stone> stoneNum = stoneService.findAllStone();
		 stoneList = stoneService.findStoneByTableInfo(params);
		 
		//获取分页控件的信息
	    String start = request.getParameter("start");
	    //System.out.println(start);
	    String length = request.getParameter("length");
	   // System.out.println(length);
	 //获取前台额外传递过来的查询条件
	    String extra_search = request.getParameter("extra_search");
	   // System.out.println(extra_search);
	    DatatablesViewPage<Stone> view = new DatatablesViewPage<Stone>();
	    view.setiTotalDisplayRecords(stoneNum.size());
	    view.setiTotalRecords(stoneNum.size());

	    view.setAaData(stoneList);
	   // System.out.println(view);
	    return view;
	
	}
	/**
	 * 获取表格数据（分页）
	 *
	 * com.nenu.controller
	 * @param aoData
	 * @param request
	 * @return DatatablesViewPage<Plan>
	 * created  at 2018年12月20日
	 */
	@ApiOperation(value="获取表格数据（分页）",notes="获取表格数据（分页）")
	@RequestMapping(value="/getTableDataCopy",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<StoneCopy> getStoneCopyTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> params = new HashMap<String, Object>();			
		// System.out.println(aoData);
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		 //System.out.println(sEcho);
		 sEcho = (sEcho-1)*10;
		 params.put("sEcho",iDisplayStart);
		 params.put("len", 10);
		 List<StoneCopy> stoneList = new ArrayList<StoneCopy>();
		 List<StoneCopy> stoneNum = stoneService.findAllStoneCopy();
		 stoneList = stoneService.findStoneCopyByTableInfo(params);
		 
		//获取分页控件的信息
	    String start = request.getParameter("start");
	    //System.out.println(start);
	    String length = request.getParameter("length");
	    //System.out.println(length);
	 //获取前台额外传递过来的查询条件
	    String extra_search = request.getParameter("extra_search");
	    //System.out.println(extra_search);
	    DatatablesViewPage<StoneCopy> view = new DatatablesViewPage<StoneCopy>();
	    view.setiTotalDisplayRecords(stoneNum.size());
	    view.setiTotalRecords(stoneNum.size());

	    view.setAaData(stoneList);
	    //System.out.println(view);
	    return view;
	
	}
	@ApiOperation(value="获取表格数据（分页）",notes="获取表格数据（分页）")
	@RequestMapping(value="/getProcordTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Procord> getProcordTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> params = new HashMap<String, Object>();			
		// System.out.println(aoData);
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		// System.out.println(sEcho);
		 sEcho = (sEcho-1)*10;
		 params.put("sEcho",iDisplayStart);
		 params.put("len", 10);
		 List<Procord> procordList = new ArrayList<Procord>();
		 List<Procord> procordNum = procordService.findAllProcord();
		 procordList = procordService.findProcordByTableInfo(params);
		 
		//获取分页控件的信息
	    String start = request.getParameter("start");
	    //System.out.println(start);
	    String length = request.getParameter("length");
	    //System.out.println(length);
	 //获取前台额外传递过来的查询条件
	    String extra_search = request.getParameter("extra_search");
	    //System.out.println(extra_search);
	    DatatablesViewPage<Procord> view = new DatatablesViewPage<Procord>();
	    view.setiTotalDisplayRecords(procordNum.size());
	    view.setiTotalRecords(procordNum.size());

	    view.setAaData(procordList);
	    //System.out.println(view);
	    return view;
	
	}
	@ApiOperation(value="获取表格数据（分页）",notes="获取表格数据（分页）")
	@RequestMapping(value="/getSupplierStoneTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<SupplierStone> getSupplierStoneTableData(@RequestParam String aoData, @RequestParam String supplierName,HttpServletRequest request,HttpSession session )  {
		
		System.out.println("supplierName====="+supplierName);
		Map<String, Object> params = new HashMap<String, Object>();			
		System.out.println(aoData);
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		 
		//查询与统计   供应商库存显示
		int supplierStoneMainNumber=0;
		int supplierStoneSubNumber=0;
		double supplierStoneMainWeight=0;
	    double supplierStoneSubWeight=0;
		
		List<SupplierStone> supplierStoneList = new ArrayList<SupplierStone>();
		List<SupplierStone> supplierStoneNum = new ArrayList<SupplierStone>();
		if(supplierName.equals("none")) {
			 supplierStoneNum = supplierStoneService.findAllSupplierStone();
			 supplierStoneList = supplierStoneService.findSupplierStoneByTableInfo(params); 
		}else {
			 params.put("supplier_name", supplierName);
			 supplierStoneNum = supplierStoneService.findAllSupplierStoneWithSupplierName(supplierName);
			 supplierStoneList = supplierStoneService.findSupplierStoneByTableInfoWithSupplierName(params);
		}
		if(supplierStoneNum.size() > 0) {
			for (int j = 0; j < supplierStoneNum.size(); j++) {		
				if(supplierStoneNum.get(j).getSupplier_mainStoneNo().length()>0) {//主石
					supplierStoneMainNumber += supplierStoneNum.get(j).getSupplier_mainNumber();
					supplierStoneMainWeight += supplierStoneNum.get(j).getSupplier_mainWeight();
				}else {
					supplierStoneSubNumber += 1;
					supplierStoneSubWeight += supplierStoneNum.get(j).getSupplier_subWeight();
				}
			}
		}
		session.setAttribute("supplierStoneMainNumber", supplierStoneMainNumber);
		session.setAttribute("supplierStoneSubNumber", supplierStoneSubNumber);
		session.setAttribute("supplierStoneMainWeight", supplierStoneMainWeight);
		session.setAttribute("supplierStoneSubWeight", supplierStoneSubWeight);
		session.setAttribute("supplierStoneList", supplierStoneNum);
	    DatatablesViewPage<SupplierStone> view = new DatatablesViewPage<SupplierStone>();
	    view.setiTotalDisplayRecords(supplierStoneNum.size());
	    view.setiTotalRecords(supplierStoneNum.size());

	    view.setAaData(supplierStoneList);
	    return view;
	
	}
}
