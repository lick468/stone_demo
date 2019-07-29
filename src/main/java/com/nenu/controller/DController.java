package com.nenu.controller;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.nenu.domain.BackStone;
import com.nenu.domain.DatatablesViewPage;
import com.nenu.domain.Finpord;
import com.nenu.domain.Procord;
import com.nenu.domain.Stone;
import com.nenu.domain.Stoninproc;
import com.nenu.service.BackStoneService;
import com.nenu.service.FinpordService;
import com.nenu.service.ProcordService;
import com.nenu.service.StoneService;
import com.nenu.service.StoninprocService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 厂家管理员操作
 * @author nenu
 *
 */
@Api(value="DController",description="钻石流转系统厂家管理员接口")
@Controller
@RequestMapping(value = "/d")
public class DController {
	@Autowired
	private ProcordService procordService;
	@Autowired
	private StoneService stoneService;
	@Autowired
	private StoninprocService stoninprocService;
	@Autowired
	private BackStoneService backStoneService;
	
	/**
	 * 厂家管理主界面
	 *
	 * com.nenu.controller
	 * @param map
	 * @return String
	 * created  at 2018年6月13日
	 */
	@ApiOperation(value="跳转到index页面",notes="跳转到主页")
	@GetMapping(value = "/index")
	public String index(ModelMap map) {
		return "operator_d/index";
	}
	
	/**
	 * 根据订单号或石编查找订单详情
	 *
	 * com.nenu.controller
	 * @param stoninproc
	 * @param map
	 * @return String
	 * created  at 2018年9月25日
	 */
	@ApiOperation(value="根据订单号或石编查找订单详情",notes="根据订单号或石编查找订单详情")
	@RequestMapping(value = "/findStoninprocByProcordNoOrStoneNo",method=RequestMethod.POST)
	public String findStoninprocByProcordNo(Stoninproc stoninproc,HttpServletRequest request,ModelMap map) {
		String type = request.getParameter("queryTypeBackPath");
		System.out.println(type+"----------");
		if(type.contains("stoninproc_procordNo")) {
			List<Stoninproc> stoninprocList = stoninprocService.findAllStoninprocByProcordNo(stoninproc.getStoninproc_procordNo());
			if(stoninprocList.size()>0) {
				map.addAttribute("stoninprocList", stoninprocList);
				map.addAttribute("procordNo", stoninproc.getStoninproc_procordNo());	
			}else {
				map.addAttribute("msg", "procordNo");
			}	
		}else {
			List<Stoninproc> stoninprocList = stoninprocService.findStoninprocBySubStoneNo(stoninproc.getStoninproc_stoneNo());
			if(stoninprocList.size()>0) {
				map.addAttribute("stoninprocList", stoninprocList);
				map.addAttribute("procordNo", stoninproc.getStoninproc_procordNo());	
			}else {
				map.addAttribute("msg", "stoneNo");
			}	
		}

		return "operator_d/tuishi";
	}
	/**
	 * 根据订单号查找退石记录
	 *
	 * com.nenu.controller
	 * @param backStone
	 * @param requset
	 * @param map
	 * @return String
	 * created  at 2018年9月25日
	 */
	@ApiOperation(value="根据订单号查找退石记录",notes="根据订单号查找退石记录")
	@RequestMapping(value = "/findBackStoneByProcordNo",method=RequestMethod.POST)
	public String findBackStoneByProcordNo(BackStone backStone,HttpServletRequest requset,ModelMap map) {
		System.out.println(backStone.getBack_procodeNo());
		List<BackStone> backStoneList = backStoneService.findBackStoneByProcodeNo(backStone.getBack_procodeNo());
		map.addAttribute("backStoneList", backStoneList);	
		return "operator_d/history";
	}
	/**
	 * 查找订单详情
	 *
	 * com.nenu.controller
	 * @param map
	 * @return String
	 * created  at 2018年9月25日
	 */
	@ApiOperation(value="查找订单详情",notes="查找订单详情")
	@RequestMapping(value = "/findProcodeDetails",method=RequestMethod.POST)
	@ResponseBody
	public List<Stoninproc> findAllBackStone(HttpServletRequest requset,ModelMap map) {
		String pro = requset.getParameter("procode_no");
		long stoninproc_procordNo = Long.parseLong(pro);
		List<Stoninproc> stoninprocList = stoninprocService.findAllStoninprocByProcordNo(stoninproc_procordNo);
			
		return stoninprocList;
	}

	
	/**
	 * 退石
	 *
	 * com.nenu.controller
	 * @param requset
	 * @return String
	 * created  at 2018年6月13日
	 */
	@ApiOperation(value="退石",notes="退石")
	@RequestMapping(value="/deleteStoneFromProcord",method=RequestMethod.POST)
	@ResponseBody
	public String del(HttpServletRequest requset) {
		//1.获取石编和订单号
		String stoneNo = requset.getParameter("stoneNo");
		String procord = requset.getParameter("procordNo");
		long procordNo = Long.parseLong(procord);
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("stoneNo", stoneNo);
		params.put("procordNo", procordNo);
		//2.根据石编和订单号从Stoninproc中获取Stoninproc（主要是获取石数）
		Stoninproc stoninproc = stoninprocService.findStoneNumberFromStoninproc(params);
		int stoneNumber = stoninproc.getStoninproc_number();
		//3.根据石编分辨出主副石,然后获取stone库里的石数，将退的石头放回到石头库中
		if(!stoneNo.matches(".*[a-zA-Z].*")) {//是副石编
			//3.1 副石数量更新   
			List<Stone> stone =  stoneService.findStoneBySubNo(stoneNo);
			int stonen = stone.get(0).getStone_substoNumber();
			int after = stonen+stoneNumber;
			Stone newStone = new Stone();
			newStone.setStone_substoNumber(after);
			newStone.setStone_substoNo(stoneNo);
			stoneService.updateSubStoneNumber(newStone);
		}else {
			//3.2主石数量更新
			//int mainNo = Integer.parseInt(stoneNo);
			List<Stone> stone =  stoneService.findStoneByMainNo(stoneNo);
			int stonen = stone.get(0).getStone_mainNumber();
			int after = stonen+stoneNumber;
			Stone newStone = new Stone();
			newStone.setStone_mainNumber(after);
			newStone.setStone_mainStoneNo(stoneNo);
			stoneService.updateMainStoneNumber(newStone);
		}
		stoninproc.setStoninproc_stoneState(2);
		//4.根据石编和订单号，从Stoninproc表中修改对应的记录
		int i = stoninprocService.updateStoninprocState(stoninproc);
		if(i==1) {//修改成功
		//5.更新订单表tb_procord里石数总量和货款金额
		Procord pro =  procordService.findProcordByProcordNo(procordNo);
		int num = pro.getProcord_goodscount();
		int back = pro.getProcord_backcount();
		double money =pro.getProcord_pay();
		if(!stoneNo.matches(".*[a-zA-z].*")) {//是副石编
			List<Stone> stone =  stoneService.findStoneBySubNo(stoneNo);
			double price = stone.get(0).getStone_substoPrperct();
			double weight = stone.get(0).getStone_substoWgt();
			double mon = price*stoneNumber*weight;
			double aftermoney = money-mon;
			Procord p = new Procord();
			p.setProcord_pay(aftermoney);
			p.setProcord_no(procordNo);
			procordService.updateProcordPay(p);
		}else {
			//int mainNo = Integer.parseInt(stoneNo);
			List<Stone> stone =  stoneService.findStoneByMainNo(stoneNo);
			double price = stone.get(0).getStone_mainPrperct();
			double weight = stone.get(0).getStone_mainWeight();
			double mon = price*stoneNumber*weight;
			double aftermoney = money-mon;
			Procord p = new Procord();
			p.setProcord_pay(aftermoney);
			p.setProcord_no(procordNo);
			procordService.updateProcordPay(p);
		}
		int after = num-1;
		Procord newPro = new Procord();
		if(after==back) {
			newPro.setProcord_status(1);
		}
		newPro.setProcord_goodscount(after);
		newPro.setProcord_no(procordNo);
		procordService.updateProcordAccounts(newPro);
		//6.生成退石记录
		Date day = new Date();
		String content = "";
		content = "将订单号为"+procord+"的里石编是"+stoneNo+"的石头退石";
		BackStone bs = new BackStone();
		bs.setBack_content(content);
		bs.setBack_procodeNo(procordNo);
		bs.setBack_time(day);
		bs.setBack_stoneNo(stoneNo);
		backStoneService.insertBackStone(bs);
		}
		return "退石成功";
	}
	@ApiOperation(value="获取数据库表数据",notes="获取数据库表数据")
	@RequestMapping(value="/getProcordTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Procord> getProcordTableData(@RequestParam String aoData,@RequestParam String procordNo,
														   @RequestParam String procordBatch,@RequestParam String procordSupplier,
														   @RequestParam String procordDate,@RequestParam String procordDelyDate )  {
		Map<String, Object> map = new HashMap<String, Object>();
		// System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();			
		// System.out.println(aoData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date delydate = sdf.parse(procordDelyDate, pos);
		pos = new ParsePosition(0);
		Date date = sdf.parse(procordDate, pos);
		
		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]); 
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		// System.out.println(sEcho);
		 sEcho = (sEcho-1)*10;
		 params.put("sEcho",iDisplayStart);
		 params.put("len", 10);
		if(procordNo.length() > 0) {
			params.put("procord_no",procordNo);
		}
		if(procordBatch.length() > 0) {
			params.put("procord_batch",procordBatch);
		}
		if(procordSupplier.length() > 0) {
			params.put("procord_supplier",procordSupplier);
		}
		if(procordDelyDate.length() > 4) {
			params.put("procord_delydate",delydate);
		}
		if(procordDate.length() > 4) {
			params.put("procord_date",date);
		}
		 List<Procord> procordList =procordService.findProcordByTableInfo(params);
		 List<Procord> procordNum = procordService.findProcordForTableInfo(params);

	    //System.out.println(extra_search);
	    DatatablesViewPage<Procord> view = new DatatablesViewPage<Procord>();
	    view.setiTotalDisplayRecords(procordNum.size());
	    view.setiTotalRecords(procordNum.size());

	    view.setAaData(procordList);
	    //System.out.println(view);
	    return view;
	
	}
	/**
	 * 导出某一订单详情表
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value="导出某一订单详情表")
	@RequestMapping(value = "/downloadProcordDetails", method = RequestMethod.POST)
	@ResponseBody
	public String downloadProcordDetails(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		String context  = request.getParameter("context");
		String result = "";
		stoninprocService.downloadProcordDetails(context,response);
		return result;
	}
	
}
