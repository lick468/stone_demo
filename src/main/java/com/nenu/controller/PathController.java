package com.nenu.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nenu.domain.BackStone;
import com.nenu.domain.Finpord;
import com.nenu.domain.Procord;
import com.nenu.domain.Stone;
import com.nenu.domain.Stoninproc;
import com.nenu.domain.TBackStone;
import com.nenu.service.BackStoneService;
import com.nenu.service.FinpordService;
import com.nenu.service.ProcordService;
import com.nenu.service.StoneService;
import com.nenu.service.StoninprocService;
import com.nenu.service.TBackStoneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value="PathController",description="钻石流转系统石头路径接口")
@Controller
@RequestMapping(value="/path")
public class PathController {
	@Autowired
	private StoneService stoneService;
	@Autowired
	private StoninprocService StoninprocService;
	@Autowired
	private FinpordService finpordService;
	@Autowired
	private ProcordService procordService;
	@Autowired
	private BackStoneService backStoneService;
	@Autowired
	private TBackStoneService tbackStoneService;
	
	/**
	 * 路径查询
	 * 
	 * @param requset
	 * @return
	 */
	@ApiOperation(value="路径查询",notes="路径查询")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="find",method=RequestMethod.POST)
	@ResponseBody
	public List find(HttpServletRequest requset) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List result = new ArrayList<>();
		String type = requset.getParameter("type");
		if(type.equals("stone_mainStoneNo")) {//主石路径查询
			
			String main = requset.getParameter("mainNo");
			if(main==null) {
				return result;
			}else {
				List<TBackStone> tblist = tbackStoneService.findTBackStoneByStoneNo(main);
				if(tblist.size()>0) {//总部退石
					//添加退主石标记
					result.add("back");
					//int mainNo = Integer.parseInt(main);
					List<Stone> list = stoneService.findStoneByMainNo(main);
					if(list.size()>0) {
						//添加入库时间
						result.add(sdf.format(list.get(0).getStone_purchdate()));
					}
					//添加退主石信息
					result.add(sdf.format(tblist.get(0).getTback_time())+",被总部退石");	
				}else {
					List<BackStone> bkList = backStoneService.findBackStoneByStoneNo(main);
					if(bkList.size()>0) {//主石有退石情况
						result.add("back");//添加退主石标记
						//int mainNo = Integer.parseInt(main);
						List<Stone> list = stoneService.findStoneByMainNo(main);
						if(list.size()>0) {
							result.add(sdf.format(list.get(0).getStone_purchdate()));					
							Stoninproc stoninproc = StoninprocService.findStoninprocByStoneNo(main);
							if(stoninproc!=null) {
								result.add(sdf.format(stoninproc.getOrderDate())+",生成订单："+stoninproc.getStoninproc_procordNo());
								if(stoninproc.getChukuDate()!=null) {
									result.add(sdf.format(stoninproc.getChukuDate())+",订单："+stoninproc.getStoninproc_procordNo()+"出库");
									//添加退主石信息
									result.add(sdf.format(bkList.get(0).getBack_time())+",订单："+stoninproc.getStoninproc_procordNo()+"该主石被退石");
								}
							}
						}
					}else {//主石没有退石情况
						//int mainNo = Integer.parseInt(main);
						List<Stone> list = stoneService.findStoneByMainNo(main);
						if(list.size()>0) {
							result.add("main");//添加主石标记
							result.add(sdf.format(list.get(0).getStone_purchdate()));
							Finpord finpord = finpordService.findFinpordByMainNo(main);
							if(finpord!=null) {//成品回库
								Stoninproc stoninproc = StoninprocService.findStoninprocByStoneNo(main);
								if(stoninproc!=null) {
									result.add(sdf.format(stoninproc.getOrderDate())+",生成订单："+stoninproc.getStoninproc_procordNo());
									if(stoninproc.getChukuDate()!=null) {
										result.add(sdf.format(stoninproc.getChukuDate())+",订单："+stoninproc.getStoninproc_procordNo()+"出库");
									}
								}
								result.add(sdf.format(finpord.getFinpord_inboundate())+",订单："+stoninproc.getStoninproc_procordNo()+"加工完毕返回");
							}
						}
					}	
				}
			}		
		}else if(type.equals("stone_substoNo")) {//副石路径查询
			
			String subNo =  requset.getParameter("substoNo");
			if(subNo==null || subNo.matches(".*[a-zA-Z].*")) {
				return result;
			}else {
				List<TBackStone> tblist = tbackStoneService.findTBackStoneByStoneNo(subNo);
				
				List<Stone> list1 = stoneService.findStoneBySubNo(Long.parseLong(subNo));
				if(list1.size()>0)  {
					result.add("sub");//添加副石标记
					result.add(sdf.format(list1.get(0).getStone_purchdate()));
				}
				if(tblist.size()>0) {//总部退石
					//添加退副石信息
					result.add(sdf.format(tblist.get(0).getTback_time())+"被总部退石");	
				}else {
					List<BackStone> bkList = backStoneService.findBackStoneByStoneNo(subNo);
					if(bkList.size()>0) {//副石有退石情况
						List<Stone> list = stoneService.findStoneBySubNo(Long.parseLong(subNo));
						if(list.size()>0)  {
							for (int i = 0; i < bkList.size(); i++) {
								Procord procord = procordService.findProcordByProcordNo( bkList.get(i).getBack_procodeNo());
								result.add(sdf.format(procord.getProcord_date())+",生成订单："+ bkList.get(i).getBack_procodeNo());
								if(procord.getProcord_delydate()!=null) {
									result.add(sdf.format(procord.getProcord_delydate())+",订单："+ bkList.get(i).getBack_procodeNo()+"出库");
								}
								result.add(sdf.format(bkList.get(i).getBack_time())+",订单"+bkList.get(i).getBack_procodeNo()+"该副石被退石");
							}
						}
					}else {//副石没有退石情况
						List<Stone> list = stoneService.findStoneBySubNo(Long.parseLong(subNo));
						if(list.size()>0)  {						
						List<Finpord> fList = finpordService.findAllFinpord();
							if(fList.size()>0) {
								for (int i = 0; i < fList.size(); i++) {
									if(String.valueOf(fList.get(i).getFinpord_substo1no()).contains(subNo) || String.valueOf(fList.get(i).getFinpord_substo2no()).contains(subNo) || String.valueOf(fList.get(i).getFinpord_substo3no()).contains(subNo)) {
										Procord procord = procordService.findProcordByProcordNo(fList.get(i).getFinpord_procordNo());
										result.add(sdf.format(procord.getProcord_date())+",生成订单："+fList.get(i).getFinpord_procordNo());
										if(procord.getProcord_delydate()!=null) {
											result.add(sdf.format(procord.getProcord_delydate())+",订单："+fList.get(i).getFinpord_procordNo()+"出库");
										}
										result.add(sdf.format(fList.get(i).getFinpord_inboundate())+",订单："+fList.get(i).getFinpord_procordNo()+"加工完毕返回");
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	
}
