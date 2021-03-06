package com.nenu.controller;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nenu.domain.DatatablesViewPage;
import com.nenu.domain.Fediff;
import com.nenu.domain.Finpord;
import com.nenu.domain.FinpordCopy;
import com.nenu.domain.Procord;
import com.nenu.domain.RoleUserOfIn;
import com.nenu.domain.Stone;
import com.nenu.domain.Stoninproc;
import com.nenu.service.StoneService;
import com.nenu.service.StoninprocService;
import com.nenu.service.SupplierService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.nenu.service.FediffService;
import com.nenu.service.FinpordService;
import com.nenu.service.ProcordService;
/**
 * 钻石流转系统
 * 成品A操作员
 * @author nenu
 *
 */
@Api(value="AController",description="钻石流转系统成品A操作员接口")
@Controller
@RequestMapping(value = "/a")
public class AController {
	@Autowired
	private StoneService stoneService;
	@Autowired
	private FinpordService finpordService;
	@Autowired
	private FediffService fediffService;
	@Autowired
	private ProcordService procordService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private StoninprocService stoninprocService;

	/**
	 * 显示主页面
	 *
	 * com.nenu.controller
	 * @param map
	 * @param request
	 * @return String
	 * created  at 2018年6月19日
	 */
	@ApiOperation(value="跳转页面",notes="显示主页面")
	@RequestMapping(value = "/index")
	public String index(ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("user_name");
		List<RoleUserOfIn> list = (List<RoleUserOfIn>) session.getAttribute("roleList");

		String rs = "";
		for (int i = 0; i < list.size(); i++) {
			rs += list.get(i).getRole_type()+",";
		}
		List<Finpord> finpordList =finpordService.findAllFinpord();


		map.addAttribute("supplierList",supplierService.findDistinctSupplierName());//获取所有供应商集合
		map.addAttribute("fediffTimeList", fediffService.findDistinctFediffTime());
		map.addAttribute("barcodeNoList", finpordService.findDistinctBarcode());

		map.addAttribute("finpordList", finpordList);
		map.addAttribute("finpordCopyList", finpordService.findAllFinpordCopy());
		map.addAttribute("user", user);
		map.addAttribute("rs", rs);
		map.addAttribute("procordNoList", finpordService.findDistinctProcordNo());
		map.addAttribute("orderList", finpordService.findDistinctProcordNo());
		map.addAttribute("dateList", finpordService.findDistinctInBoundate());

		map.addAttribute("fediffList", fediffService.findAllFediff());
		return "operator_a/index";
	}
	/**
	 * 提交入库
	 *
	 * com.nenu.controller
	 * @param request
	 * @return int
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="提交入库",notes="从临时表拷贝到成品表")
	@RequestMapping(value = "copyToFinpord", method = RequestMethod.POST)
	@ResponseBody
	public String copyToFinpord(HttpServletRequest request) {

		List<FinpordCopy> finpordList =finpordService.findAllFinpordCopy();
		List<FinpordCopy> finpordCopyList_right = new ArrayList<>();
		List<FinpordCopy> finpordCopyList_wrong = new ArrayList<>();


		List<Stoninproc>   stoninprocList = stoninprocService.findAllStoninproc();
		List stoninprocMainList = new ArrayList<>();
		for (int i = 0; i < stoninprocList.size(); i++) {
			if(stoninprocList.get(i).getStoninproc_stoneNo().matches(".*[a-zA-Z].*")) {
				stoninprocMainList.add(stoninprocList.get(i).getStoninproc_stoneNo());
			}
		}

		int flag=0;
		String res = "";
		List<Long> list = new ArrayList<>();
		if(finpordList!=null) {
			for(int i=0;i<finpordList.size();i++) {
				if(finpordList.get(i).getFinpord_mainstono()!=null && finpordList.get(i).getFinpord_mainstono().matches(".*[a-zA-Z].*") && finpordList.get(i).getFinpord_mainstowgt()!=0) {
					Stoninproc stoninp  = stoninprocService.findStoninprocByStoneNo(finpordList.get(i).getFinpord_mainstono());
					if(stoninp!=null) {

//								System.out.println("1:"+finpordList.get(i).getFinpord_mainstono());
//								System.out.println("2:"+stoninp.getStoninproc_batch() +"||" +stoninp.getStoninproc_procordNo());
						//1.生成入料差
						Fediff fediff = new Fediff();
						fediff.setFediff_time(new Date());//时间
						fediff.setFediff_procordNo(stoninp.getStoninproc_procordNo());//订单号
						fediff.setFediff_batch(stoninp.getStoninproc_batch());//批次
						fediff.setFediff_stoneNo(finpordList.get(i).getFinpord_mainstono());//石编


						fediff.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());//成品入库时的单号
						double ediff = 0;
						List<Stone> stoneList =  stoneService.findStoneByMainNo(finpordList.get(i).getFinpord_mainstono());
						if(stoneList.size()>0) {
							double weight = stoneList.get(0).getStone_mainWeight();//石头库里的石重
							fediff.setStone_weight(weight);
							double weight1 = finpordList.get(i).getFinpord_mainstowgt();//成品库的石重
							ediff = weight-weight1;
						}
						fediff.setFinpord_weight(finpordList.get(i).getFinpord_mainstowgt()); //现石重
						fediff.setFediff_weightDiff(ediff);
						fediffService.insertFediff(fediff);//添加进入料差表
						//2.更新订单状态
						Procord procord = procordService.findProcordByProcordNo(stoninp.getStoninproc_procordNo());
						if(procord!=null) {
							int before = procord.getProcord_backcount();
							int after = before+1;
							procord.setProcord_backcount(after);
							if(after==procord.getProcord_goodscount()) {
								procord.setProcord_status(1);
							}else {
								procord.setProcord_status(0);
							}
							procordService.updateProcordState(procord);//更新订单状态
						}
						//3.更改订单详细石头表中石头的状态和成品入库的单号
						Stoninproc stoninproc = new Stoninproc();
						stoninproc.setStoninproc_procordNo(stoninp.getStoninproc_procordNo());
						stoninproc.setStoninproc_stoneNo(String.valueOf(finpordList.get(i).getFinpord_mainstono()));
						stoninproc.setStoninproc_stoneState(1);
						stoninproc.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());
						stoninprocService.updateStoninprocState(stoninproc);
					}


				}
				if(finpordList.get(i).getFinpord_substo1no()!=null  && finpordList.get(i).getFinpord_substo1no().length()>1 && finpordList.get(i).getFinpord_substo1wgt()!=0) {
					List<Stoninproc> stoninpList  = stoninprocService.findStoninprocBySubStoneNo(String.valueOf(finpordList.get(i).getFinpord_substo1no()));
					if(stoninpList.size()>0) {
						Stoninproc stoninp = stoninpList.get(0);
						if(stoninp!=null) {
							Fediff fediff = new Fediff();
							fediff.setFediff_time(new Date());//时间
							fediff.setFediff_procordNo(stoninp.getStoninproc_procordNo());//订单号
							fediff.setFediff_batch(stoninp.getStoninproc_batch());//批次
							fediff.setFinpord_weight(finpordList.get(i).getFinpord_substo1wgt());
							fediff.setFediff_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo1no()));//石编
							fediff.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());//成品入库时的单号
							double ediff = 0;
							List<Stone> stoneList =  stoneService.findStoneBySubNo(finpordList.get(i).getFinpord_substo1no());
							if(stoneList.size()>0) {
								double weight = stoneList.get(0).getStone_substoWgt();//石头库里的石重
								fediff.setStone_weight(weight);
								double weight1 = finpordList.get(i).getFinpord_substo1wgt();//成品库的石重
								ediff = weight-weight1;
							}
							fediff.setFediff_weightDiff(ediff);
							fediffService.insertFediff(fediff);
							Procord procord = procordService.findProcordByProcordNo(stoninp.getStoninproc_procordNo());
							if(procord!=null) {
								int before = procord.getProcord_backcount();
								int after = before+1;
								procord.setProcord_backcount(after);
								if(after==procord.getProcord_goodscount()) {
									procord.setProcord_status(1);
								}else {
									procord.setProcord_status(0);
								}
								procordService.updateProcordState(procord);//更新订单状态
							}

							Stoninproc stoninproc = new Stoninproc();
							stoninproc.setStoninproc_procordNo(stoninp.getStoninproc_procordNo());
							stoninproc.setStoninproc_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo1no()));
							stoninproc.setStoninproc_stoneState(1);
							stoninproc.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());
							stoninprocService.updateStoninprocState(stoninproc);
						}
					}
				}
				if(finpordList.get(i).getFinpord_substo2no()!=null  && finpordList.get(i).getFinpord_substo2no().length()>1 && finpordList.get(i).getFinpord_substo2wgt()!=0) {
					List<Stoninproc> stoninpList  = stoninprocService.findStoninprocBySubStoneNo(String.valueOf(finpordList.get(i).getFinpord_substo2no()));
					if(stoninpList.size()>0) {
						Stoninproc stoninp = stoninpList.get(0);
						if(stoninp!=null) {
							Fediff fediff = new Fediff();
							fediff.setFediff_time(new Date());//时间
							fediff.setFediff_procordNo(stoninp.getStoninproc_procordNo());//订单号
							fediff.setFediff_batch(stoninp.getStoninproc_batch());//批次
							fediff.setFediff_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo2no()));//石编
							fediff.setFinpord_weight(finpordList.get(i).getFinpord_substo2wgt());
							fediff.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());//成品入库时的单号
							double ediff = 0;
							List<Stone> stoneList =  stoneService.findStoneBySubNo(finpordList.get(i).getFinpord_substo2no());
							if(stoneList.size()>0) {
								double weight = stoneList.get(0).getStone_substoWgt();//石头库里的石重
								fediff.setStone_weight(weight);
								double weight1 = finpordList.get(i).getFinpord_substo2wgt();//成品库的石重
								ediff = weight-weight1;
							}
							fediff.setFediff_weightDiff(ediff);
							fediffService.insertFediff(fediff);
							Procord procord = procordService.findProcordByProcordNo(stoninp.getStoninproc_procordNo());
							if(procord!=null) {
								int before = procord.getProcord_backcount();
								int after = before+1;
								procord.setProcord_backcount(after);
								if(after==procord.getProcord_goodscount()) {
									procord.setProcord_status(1);
								}else {
									procord.setProcord_status(0);
								}
								procordService.updateProcordState(procord);//更新订单状态
							}

							Stoninproc stoninproc = new Stoninproc();
							stoninproc.setStoninproc_procordNo(stoninp.getStoninproc_procordNo());
							stoninproc.setStoninproc_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo2no()));
							stoninproc.setStoninproc_stoneState(1);
							stoninproc.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());
							stoninprocService.updateStoninprocState(stoninproc);
						}
					}
				}
				if(finpordList.get(i).getFinpord_substo3no()!=null  && finpordList.get(i).getFinpord_substo3no().length()>1 && finpordList.get(i).getFinpord_substo3wgt()!=0) {

					List<Stoninproc> stoninpList  = stoninprocService.findStoninprocBySubStoneNo(String.valueOf(finpordList.get(i).getFinpord_substo3no()));
					if(stoninpList.size()>0) {
						Stoninproc stoninp = stoninpList.get(0);
						if(stoninp!=null) {
							Fediff fediff = new Fediff();
							fediff.setFediff_time(new Date());//时间
							fediff.setFediff_procordNo(stoninp.getStoninproc_procordNo());//订单号
							fediff.setFediff_batch(stoninp.getStoninproc_batch());//批次
							fediff.setFinpord_weight(finpordList.get(i).getFinpord_substo3wgt());
							fediff.setFediff_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo3no()));//石编
							fediff.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());//成品入库时的单号
							double ediff = 0;
							List<Stone> stoneList =  stoneService.findStoneBySubNo(finpordList.get(i).getFinpord_substo3no());
							if(stoneList.size()>0) {
								double weight = stoneList.get(0).getStone_substoWgt();//石头库里的石重
								fediff.setStone_weight(weight);
								double weight1 = finpordList.get(i).getFinpord_substo3wgt();//成品库的石重
								ediff = weight-weight1;
							}
							fediff.setFediff_weightDiff(ediff);
							fediffService.insertFediff(fediff);
							Procord procord = procordService.findProcordByProcordNo(stoninp.getStoninproc_procordNo());
							if(procord!=null) {
								int before = procord.getProcord_backcount();
								int after = before+1;
								procord.setProcord_backcount(after);
								if(after==procord.getProcord_goodscount()) {
									procord.setProcord_status(1);
								}else {
									procord.setProcord_status(0);
								}
								procordService.updateProcordState(procord);//更新订单状态
							}

							Stoninproc stoninproc = new Stoninproc();
							stoninproc.setStoninproc_procordNo(stoninp.getStoninproc_procordNo());
							stoninproc.setStoninproc_stoneNo(String.valueOf(finpordList.get(i).getFinpord_substo3no()));
							stoninproc.setStoninproc_stoneState(1);
							stoninproc.setFinpord_procordNo(finpordList.get(i).getFinpord_procordNo());
							stoninprocService.updateStoninprocState(stoninproc);
						}
					}
				}
			}
			int j2,addFlag;
			for (int j = 0; j < finpordList.size(); j++) {
				if(finpordList.get(j).getFinpord_mainstono()==null) {
					continue;
				}
				addFlag=0;
				for (j2 = 0; j2 < stoninprocMainList.size(); j2++) {
					if(finpordList.get(j).getFinpord_mainstono().contains(stoninprocMainList.get(j2).toString())
							&& finpordList.get(j).getFinpord_mainstono().length()==stoninprocMainList.get(j2).toString().length() ) {
						finpordCopyList_right.add(finpordList.get(j));
						addFlag=1;
					}
				}
				if(addFlag==0) {
					finpordCopyList_wrong.add(finpordList.get(j));
				}
			}
//			System.out.println(finpordCopyList_right);
//			System.out.println(finpordCopyList_wrong);

			//1.清空临时表
			finpordService.clearCopy();//清空临时表
			//2.临时表重新插入正确的记录
			if(finpordCopyList_right.size() > 0) {
				finpordService.batchInsertFinpordCopy(finpordCopyList_right);
				//3.从临时表拷贝到成品表
				flag = 	finpordService.copyToFinpord();//从临时表拷贝到成品表
			}
			//4.清空临时表
			finpordService.clearCopy();//清空临时表
			//5.临时表重新插入错误的记录
			if(finpordCopyList_wrong.size() > 0) {
				finpordService.batchInsertFinpordCopy(finpordCopyList_wrong);
				for (int i=0;i<finpordCopyList_wrong.size();i++) {
					if(finpordCopyList_wrong.get(i).getFinpord_procordNo() != 0) {
						list.add(finpordCopyList_wrong.get(i).getFinpord_procordNo());
					}
				}
			}
		}

		System.out.println("flag======"+flag);
		// flag  表示成功提交的条数

		if(flag==0) {
			if(finpordCopyList_wrong.size()>0) {
				if(finpordCopyList_right.size()>0) {
					res="提交入库成功,下列单号中的主石编订单表中没有，请查看&"+list;
				}else {
					res="提交入库失败,下列单号中的主石编订单表中没有，请查看&"+list;
				}

			}else {
				res="提交入库失败&";
			}

		}else if(flag>0) {
			if(finpordCopyList_wrong.size()>0) {
				res="提交入库成功,下列单号中的主石编订单表中没有，请查看&"+list;
			}else {
				res="提交入库成功,有入料差请到入料差管理查看&";
			}
		}
		return res;
	}

	/**
	 * 导入excel数据进入临时表
	 *
	 * com.nenu.controller
	 * @param file
	 * @param request
	 * @return String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="导入excel",notes="导入excel数据进入临时表")
	@RequestMapping(value="/finprodExcelToSql",method = RequestMethod.POST)
	public String finprodExcelToSql(@RequestParam(value = "fileName") MultipartFile file,HttpServletRequest request) {
		// 获取文件名
		String fileName = file.getOriginalFilename();
		System.out.println("fileName=============="+fileName);
		String levele = request.getParameter("level");
		int level = Integer.parseInt(levele);

		finpordService.finprodexcel2sql(fileName, file,level);

		return "redirect:/a/index";
	}
	/**
	 * 下载成品excel
	 *
	 * com.nenu.controller
	 * @param request
	 * @param session
	 * @return String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="下载成品excel",notes="下载成品excel")
	@RequestMapping(value="/downloadFinprodExcel",method = RequestMethod.POST)
	@ResponseBody
	public String downloadFinprod(HttpServletRequest request,HttpSession session,HttpServletResponse response) {

		Integer level = (Integer) session.getAttribute("level");

		String finpordListStartTime  = request.getParameter("finpordListStartTime");
		String finpordListEndTime = request.getParameter("finpordListEndTime");
		String finpordListProcoedNo = request.getParameter("finpordListProcoedNo");
		String finpordListBarcode  = request.getParameter("finpordListBarcode");
		String finpordListSupplier  = request.getParameter("finpordListSupplier");

		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date start = sdf.parse(finpordListStartTime, pos);
		pos = new ParsePosition(0);
		Date end = sdf.parse(finpordListEndTime, pos);

		if(finpordListBarcode.length() > 0) {
			params.put("finpord_barcode",finpordListBarcode);
		}
		if(finpordListProcoedNo.length() > 0) {
			params.put("finpord_procordNo",finpordListProcoedNo);
		}
		if(finpordListSupplier.length() > 0) {
			params.put("finpord_supplier",finpordListSupplier);
		}
		if(finpordListStartTime.length() > 4) {
			params.put("start",start);
		}
		if(finpordListEndTime.length() > 4) {
			params.put("end",end);
		}
		List<Finpord> finpordNum = finpordService.findFinpordForTableInfo(params);


		finpordService.downloadfinprod2sql(finpordNum, level,response);

		String result1="";

		return result1;
	}
	/**
	 * 下载入料差excel
	 */
	@ApiOperation(value="下载入料差excel",notes="下载入料差excel")
	@RequestMapping(value="/downloadExcelFediff",method = RequestMethod.POST)
	@ResponseBody
	public String downFediff(HttpServletRequest request,HttpServletResponse response) {

		String fediffListStoneNo  = request.getParameter("fediffListStoneNo");
		String fediffListEndTime = request.getParameter("fediffListEndTime");
		String fediffListStartTime = request.getParameter("fediffListStartTime");
		String fediffListProcoedNo  = request.getParameter("fediffListProcoedNo");
		String fediffListFinpordNo  = request.getParameter("fediffListFinpordNo");
		String fediffListBatch  = request.getParameter("fediffListBatch");

		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date start = sdf.parse(fediffListStartTime, pos);
		pos = new ParsePosition(0);
		Date end = sdf.parse(fediffListEndTime, pos);

		if(fediffListStoneNo.length() > 0) {
			params.put("fediff_stoneNo",fediffListStoneNo);
		}
		if(fediffListProcoedNo.length() > 0) {
			params.put("fediff_procordNo",fediffListProcoedNo);
		}
		if(fediffListFinpordNo.length() > 0) {
			params.put("finpord_procordNo",fediffListFinpordNo);
		}
		if(fediffListBatch.length() > 0) {
			params.put("fediff_batch",fediffListBatch);
		}
		if(fediffListStartTime.length() > 4) {
			params.put("start",start);
		}
		if(fediffListEndTime.length() > 4) {
			params.put("end",end);
		}
		String result = "";
		List<Fediff> fediffForTableInfo = fediffService.findFediffForTableInfo(params);
		fediffService.downloadFediff(fediffForTableInfo,response);

		return result;
	}
	/**
	 * 插入一条成品
	 *
	 * com.nenu.controller
	 * @param finpordCopy
	 * @param bin
	 * @param request
	 * @return String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="插入一条成品",notes="插入一条成品")
	@RequestMapping(value="/insertFinpord",method = RequestMethod.POST)
	public String insertFinpord(FinpordCopy finpordCopy,BindingResult bin,HttpServletRequest request) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		//String time = request.getParameter("findprod_inboundate");
		Date now = new Date();
		String dd = request.getParameter("finpord_procordNo");
		//System.out.println(finpordCopy.getFinpord_procordNo()+"wwwwwwwwwwwwwwwwwww"+dd);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ss = sdf.format(now).replaceAll("-", "").trim().substring(2, 6);
		//System.out.println(ss);
		String supplier = request.getParameter("finpord_supplier");
		FinpordCopy lastone = finpordService.findLastFinpordCopy();
		String finpord_barcode="";
		int finpord_seqno=1;
		if(lastone!=null) {
			String barcode = lastone.getFinpord_barcode();
			int d = lastone.getFinpord_seqno();
			finpord_seqno = d+1;
			String comp =barcode.substring(2, 6);
			if(ss.equals(comp)) {
				String code = barcode.substring(6);
				int numb = Integer.parseInt(code)+10001;
				String after = String.valueOf(numb);
				finpord_barcode = supplier+ss+after;
			}else {
				finpord_barcode = supplier+ss+10000;
			}
		}else {
			finpord_barcode = supplier+ss+10000;
		}
		finpordCopy.setFinpord_ID(uuid);
		finpordCopy.setFinpord_inboundate(now);
		finpordCopy.setFinpord_barcode(finpord_barcode);
		finpordCopy.setFinpord_seqno(finpord_seqno);
		finpordService.insertFinpordByExcel(finpordCopy);



		return "redirect:/a/index";
	}
	/**
	 * 查询成品一条
	 *
	 * com.nenu.controller
	 * @param request
	 * @return Finpord
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="查询一条成品",notes="查询一条成品")
	@RequestMapping(value = "/findfinpordByfinpordIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public Finpord findfinpordByfinpordIDAJAX(HttpServletRequest request) {
		String finprod_ID = request.getParameter("sid");
		Finpord finpord =finpordService.findfinpordByfinpordIDAJAX(finprod_ID);
		return finpord;
	}
	/**
	 * 查询临时表数据一条
	 *
	 * com.nenu.controller
	 * @param request
	 * @return FinpordCopy
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="查询临时表数据一条",notes="查询临时表数据一条")
	@RequestMapping(value = "/findfinpordCopyByfinpordIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public FinpordCopy findfinpordCopyByfinpordIDAJAX(HttpServletRequest request) {
		String finprod_ID = request.getParameter("sid");
		FinpordCopy finpord =finpordService.findfinpordCopyByfinpordIDAJAX(finprod_ID);
		return finpord;
	}
	/**
	 * 从临时库中删除一条成品记录
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年6月25日
	 */
	@ApiOperation(value="从临时库中删除一条成品记录",notes="从临时库中删除一条成品记录")
	@RequestMapping(value="/deletefinpordCopyByfinpordIDAJAX", method = RequestMethod.POST)
	@ResponseBody
	public String deletefinpordCopy(HttpServletRequest request) {
		String finprod_ID = request.getParameter("sid");
		finpordService.deleteFinpordCopy(finprod_ID);

		return "删除成功";

	}



	/**
	 * 更新临时表成品
	 *
	 * com.nenu.controller
	 * @param finpordCopy
	 * @param bin
	 * @param request
	 * @param map
	 * @return
	 * @throws ParseException String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="更新临时表成品",notes="更新临时表成品")
	@RequestMapping(value = "/updateFinpordCopy", method = RequestMethod.POST)
	public String finpordCopyUpdate(FinpordCopy finpordCopy,BindingResult bin, HttpServletRequest request,ModelMap map) throws ParseException {

		//更新成品的同时，查看订单号是否存在，不存在的话，同时更新订单号
		String stone_main_no = finpordCopy.getFinpord_mainstono();
		if(stone_main_no!=null) {
			Stoninproc s = stoninprocService.findStoninprocByStoneNo(stone_main_no);
			if(s!=null) {
				long procoed_no = s.getStoninproc_procordNo();
				finpordCopy.setFinpord_procordNo(procoed_no);
			}
			List<Stone> stone = stoneService.findStoneByMainNo(stone_main_no);
			if(stone.size() > 0) {
				finpordCopy.setFinpord_mainstopr(stone.get(0).getStone_mainPrperct());
			}
		}
		finpordService.updateFinpordCopy(finpordCopy);


		return "redirect:/a/index";
	}
	/**
	 *
	 * 更新成品
	 *
	 * com.nenu.controller
	 * @param finpord
	 * @param bin
	 * @param request
	 * @param map
	 * @return
	 * @throws ParseException String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="更新成品",notes="更新成品")
	@RequestMapping(value = "/updateFinpord", method = RequestMethod.POST)
	public String finpordUpdate(Finpord finpord,BindingResult bin, HttpServletRequest request,ModelMap map,RedirectAttributes redirectAttributes) throws ParseException {
		finpordService.updateFinpord(finpord);
		redirectAttributes.addFlashAttribute("message", "updateFinpord");
		return "redirect:/a/index";
	}
	/**
	 * 核算利润
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List<Finpord>
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="核算利润",notes="核算利润")
	@RequestMapping(value = "/ajaxSearchForFediff", method = RequestMethod.POST)
	@ResponseBody
	public List<Finpord> discount(HttpServletRequest request) {
		String dis = request.getParameter("dis");
		String order = request.getParameter("order");
		String barcode = request.getParameter("barcode");
		String selectType = request.getParameter("selectType");
		double discount = Double.parseDouble(dis);
		List<Finpord> finpordList = new ArrayList<>();
		if(selectType.contains("f_order")) {
			finpordList = finpordService.findFinpordByProcordNo(Long.parseLong(order));
		}else {
			finpordList = finpordService.findFinpordByBarcodeNo(barcode);
		}
		if(finpordList!=null) {
			for (int i = 0; i < finpordList.size(); i++) {
				double biaojia = finpordList.get(i).getFinpord_tagedpr();
				double fushi = finpordList.get(i).getFinpord_substo1pr()*finpordList.get(i).getFinpord_substo1wgt()
						+finpordList.get(i).getFinpord_substo2pr()*finpordList.get(i).getFinpord_substo2wgt()
						+finpordList.get(i).getFinpord_substo3pr()*finpordList.get(i).getFinpord_substo3wgt();
				double zhushi = finpordList.get(i).getFinpord_mainstopr()*finpordList.get(i).getFinpord_mainstowgt();

				double jiagong = finpordList.get(i).getFinpord_baslabcost();
				double wuliao = finpordList.get(i).getFinpord_goldwgt()*finpordList.get(i).getFinpord_goldlos()*finpordList.get(i).getFinpord_goldpr();
				double qita = finpordList.get(i).getFinpord_othrcost();

				double lirun = biaojia*discount-zhushi-fushi-jiagong-qita;
				finpordList.get(i).setProfit(lirun);
				finpordList.get(i).setMaterialCost(wuliao);
				finpordList.get(i).setMainStoneCost(zhushi);
				finpordList.get(i).setSubStoneCost(fushi);
			}
		}
		return finpordList;
	}
	/**
	 * 下载利润表格
	 *
	 * com.nenu.controller
	 * @param request
	 * @return String
	 * created  at 2018年6月20日
	 */
	@ApiOperation(value="下载利润表格",notes="下载利润表格")
	@RequestMapping(value = "/downloadExcelprofit", method = RequestMethod.POST)
	@ResponseBody
	public String downloadExcelprofit(HttpServletRequest request,HttpServletResponse response) {
		String str = request.getParameter("str");
		String dis = request.getParameter("dis");
		String s[] =str.split("&");
		//System.out.println(s+"@@@@@@@@"+dis);
		String[][] dataList = new String[s.length/12][12];

		int j=0;
		for(int i=0;i<s.length;i++) {
			if(i%12==11) {
				dataList[j][0]=s[i-11];
				dataList[j][1]=s[i-10];
				dataList[j][2]=s[i-9];
				dataList[j][3]=s[i-8];
				dataList[j][4]=s[i-7];
				dataList[j][5]=s[i-6];
				dataList[j][6]=s[i-5];
				dataList[j][7]=s[i-4];
				dataList[j][8]=s[i-3];
				dataList[j][9]=s[i-2];
				dataList[j][10]=s[i-1];
				dataList[j][11]=s[i-0];
				j++;
			}
		}
		finpordService.downloadprofit(dataList,response);
		String ss="";
		return ss;
	}

	/**
	 * 删除临时表中全部数据
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年9月24日
	 */
	@ApiOperation(value="删除临时表中全部数据",notes="删除临时表中全部数据")
	@RequestMapping(value = "/deleteAllData", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAllData() {
		finpordService.deleteAllData();
		return "删除成功";
	}

	/**
	 * 查看入料差详情
	 *
	 * com.nenu.controller
	 * @param request
	 * @return List
	 * created  at 2018年10月22日
	 */
	@ApiOperation(value="查看入料差详情",notes="查看入料差详情")
	@RequestMapping(value = "/findFediffDetails", method = RequestMethod.POST)
	@ResponseBody
	public List findFediffDetails(HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sno = request.getParameter("sno");
		String pno = request.getParameter("pno");
		List<Stone> s = new ArrayList<>();
		List<Finpord> f = new ArrayList<>();
		List result=new ArrayList<>();
		long finpord_procordNo = Long.parseLong(pno);
		f = finpordService.findFinpordByProcordNo(finpord_procordNo);

		if(!sno.matches(".*[a-zA-Z].*")) {//不包含字母 的是 副石编
			s = stoneService.findStoneBySubNo(sno);
			if(s.size()>0) {
				result.add(sdf.format(s.get(0).getStone_purchdate()));
				result.add(s.get(0).getStone_substoName());
				result.add(s.get(0).getStone_substoNo());
				result.add(s.get(0).getStone_substoWgt());
			}else {
				result.add("");
				result.add("");
				result.add("");
				result.add("");
			}
			if(f.size()>0) {
				if(sno.contains(String.valueOf(f.get(0).getFinpord_substo1no())) && f.get(0).getFinpord_substo1no().length()>1) {
					result.add(sdf.format(f.get(0).getFinpord_inboundate()));
					result.add(f.get(0).getFinpord_substo1name());
					result.add(f.get(0).getFinpord_substo1no());
					result.add(f.get(0).getFinpord_substo1wgt());
				}else if(sno.contains(String.valueOf(f.get(0).getFinpord_substo2no())) && f.get(0).getFinpord_substo2no().length()>1) {
					result.add(sdf.format(f.get(0).getFinpord_inboundate()));
					result.add(f.get(0).getFinpord_substo2name());
					result.add(f.get(0).getFinpord_substo2no());
					result.add(f.get(0).getFinpord_substo2wgt());
				}else if(sno.contains(String.valueOf(f.get(0).getFinpord_substo3no())) && f.get(0).getFinpord_substo3no().length()>1) {
					result.add(sdf.format(f.get(0).getFinpord_inboundate()));
					result.add(f.get(0).getFinpord_substo3name());
					result.add(f.get(0).getFinpord_substo3no());
					result.add(f.get(0).getFinpord_substo3wgt());
				}else {
					result.add("");
					result.add("");
					result.add("");
					result.add("");
				}
			}else {
				result.add("");
				result.add("");
				result.add("");
				result.add("");
			}
		}else {
			//int stone_mainStoneNo = Integer.parseInt(sno);
			s = stoneService.findStoneByMainNo(sno);
			if(s.size()>0) {
				result.add(sdf.format(s.get(0).getStone_purchdate()));
				result.add(s.get(0).getStone_mainName());
				result.add(s.get(0).getStone_mainStoneNo());
				result.add(s.get(0).getStone_mainWeight());
			}else {
				result.add("");
				result.add("");
				result.add("");
				result.add("");
			}
			if(f.size()>0) {
				if(sno.contains(f.get(0).getFinpord_mainstono().trim())) {

					result.add(sdf.format(f.get(0).getFinpord_inboundate()));
					result.add(f.get(0).getFinpord_mainstoname());
					result.add(f.get(0).getFinpord_mainstono());
					result.add(f.get(0).getFinpord_mainstowgt());
				}else {

					result.add("");
					result.add("");
					result.add("");
					result.add("");
				}
			}else {

				result.add("");
				result.add("");
				result.add("");
				result.add("");
			}

		}
		System.out.println(result);
		return result;

	}
	@ApiOperation(value="动态加载表格数据（按页获取）",notes="动态加载表格数据（按页获取）")
	@RequestMapping(value="/getFinpordListTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Finpord> getFinpordListTableData(@RequestParam String aoData,@RequestParam String finpordListStartTime,
															   @RequestParam String finpordListSupplier,@RequestParam String finpordListProcoedNo,
															   @RequestParam String finpordListBarcode,@RequestParam String finpordListEndTime)  {

		Map<String, Object> params = new HashMap<String, Object>();
		// System.out.println(aoData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date start = sdf.parse(finpordListStartTime, pos);
		pos = new ParsePosition(0);
		Date end = sdf.parse(finpordListEndTime, pos);

		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]);
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		if(finpordListBarcode.length() > 0) {
			params.put("finpord_barcode",finpordListBarcode);
		}
		if(finpordListProcoedNo.length() > 0) {
			params.put("finpord_procordNo",finpordListProcoedNo);
		}
		if(finpordListSupplier.length() > 0) {
			params.put("finpord_supplier",finpordListSupplier);
		}
		if(finpordListStartTime.length() > 4) {
			params.put("start",start);
		}
		if(finpordListEndTime.length() > 4) {
			params.put("end",end);
		}
		List<Finpord> finpordList = finpordService.findFinpordByTableInfo(params);
		List<Finpord> finpordNum = finpordService.findFinpordForTableInfo(params);


		DatatablesViewPage<Finpord> view = new DatatablesViewPage<Finpord>();
		view.setiTotalDisplayRecords(finpordNum.size());
		view.setiTotalRecords(finpordNum.size());

		view.setAaData(finpordList);
		return view;

	}

	@ApiOperation(value="动态加载表格数据（按页获取）",notes="动态加载表格数据（按页获取）")
	@RequestMapping(value="/getFinpordManageTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Finpord> getFinpordManageTableData(@RequestParam String aoData,@RequestParam String finpordManageStartTime,
																 @RequestParam String finpordManageSupplier,@RequestParam String finpordManageProcoedNo,
																 @RequestParam String finpordManageBarcode,@RequestParam String finpordManageEndTime)  {

		Map<String, Object> params = new HashMap<String, Object>();
		// System.out.println(aoData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date start = sdf.parse(finpordManageStartTime, pos);
		pos = new ParsePosition(0);
		Date end = sdf.parse(finpordManageEndTime, pos);

		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]);
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		if(finpordManageBarcode.length() > 0) {
			params.put("finpord_barcode",finpordManageBarcode);
		}
		if(finpordManageProcoedNo.length() > 0) {
			params.put("finpord_procordNo",finpordManageProcoedNo);
		}
		if(finpordManageSupplier.length() > 0) {
			params.put("finpord_supplier",finpordManageSupplier);
		}
		if(finpordManageStartTime.length() > 4) {
			params.put("start",start);
		}
		if(finpordManageEndTime.length() > 4) {
			params.put("end",end);
		}
		List<Finpord> finpordList = finpordService.findFinpordByTableInfo(params);
		List<Finpord> finpordNum = finpordService.findFinpordForTableInfo(params);


		DatatablesViewPage<Finpord> view = new DatatablesViewPage<Finpord>();
		view.setiTotalDisplayRecords(finpordNum.size());
		view.setiTotalRecords(finpordNum.size());

		view.setAaData(finpordList);
		return view;

	}
	@ApiOperation(value="动态加载表格数据（按页获取）",notes="动态加载表格数据（按页获取）")
	@RequestMapping(value="/getFinpordCopyTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<FinpordCopy> getFinpordCopyTableData(@RequestParam String aoData, HttpServletRequest request )  {
		Map<String, Object> map = new HashMap<String, Object>();
		//System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();
		// System.out.println(aoData);

		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]);
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		// System.out.println(sEcho);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		List<FinpordCopy> finpordList = new ArrayList<FinpordCopy>();
		List<FinpordCopy> finpordNum = finpordService.findAllFinpordCopy();
		finpordList = finpordService.findFinpordCopyByTableInfo(params);


		DatatablesViewPage<FinpordCopy> view = new DatatablesViewPage<FinpordCopy>();
		view.setiTotalDisplayRecords(finpordNum.size());
		view.setiTotalRecords(finpordNum.size());

		view.setAaData(finpordList);
		return view;

	}
	@ApiOperation(value="动态加载表格数据（按页获取）",notes="动态加载表格数据（按页获取）")
	@RequestMapping(value="/getFediffTableData",method=RequestMethod.POST)
	@ResponseBody
	public DatatablesViewPage<Fediff> getFediffTableData(@RequestParam String aoData,@RequestParam String fediffListStoneNo,
														 @RequestParam String fediffListEndTime,@RequestParam String fediffListStartTime,
														 @RequestParam String fediffListProcoedNo,@RequestParam String fediffListFinpordNo,
														 @RequestParam String fediffListBatch)  {
		Map<String, Object> map = new HashMap<String, Object>();
		//System.out.println("这里");
		Map<String, Object> params = new HashMap<String, Object>();
		//System.out.println(aoData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date start = sdf.parse(fediffListStartTime, pos);
		pos = new ParsePosition(0);
		Date end = sdf.parse(fediffListEndTime, pos);

		int sEcho =Integer.parseInt(aoData.split(":")[2].split("}")[0]);
		int iDisplayStart = Integer.parseInt(aoData.split(":")[8].split("}")[0]);
		// System.out.println(sEcho);
		sEcho = (sEcho-1)*10;
		params.put("sEcho",iDisplayStart);
		params.put("len", 10);
		if(fediffListStoneNo.length() > 0) {
			params.put("fediff_stoneNo",fediffListStoneNo);
		}
		if(fediffListProcoedNo.length() > 0) {
			params.put("fediff_procordNo",fediffListProcoedNo);
		}
		if(fediffListFinpordNo.length() > 0) {
			params.put("finpord_procordNo",fediffListFinpordNo);
		}
		if(fediffListBatch.length() > 0) {
			params.put("fediff_batch",fediffListBatch);
		}
		if(fediffListStartTime.length() > 4) {
			params.put("start",start);
		}
		if(fediffListEndTime.length() > 4) {
			params.put("end",end);
		}
		List<Fediff> fediffList = fediffService.findFediffByTableInfo(params);
		List<Fediff> fediffNum = fediffService.findFediffForTableInfo(params);


		DatatablesViewPage<Fediff> view = new DatatablesViewPage<Fediff>();
		view.setiTotalDisplayRecords(fediffNum.size());
		view.setiTotalRecords(fediffNum.size());

		view.setAaData(fediffList);
		return view;

	}


}
