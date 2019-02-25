package com.nenu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.nenu.domain.Quality;
import com.nenu.service.QualityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 系统管理员操作  ----->  成色列表
 * @author nenu
 *
 */
@Api(value="QualityController",description="钻石流转系统系统管理员操作  ----->成色列表接口")
@Controller
@RequestMapping("/admin")
public class QualityController {
	@Autowired
	private QualityService qualityService;
	@ApiOperation(value="跳转到admin/qualityList页面",notes="跳转到admin/qualityList页面")
	@RequestMapping(value = "findAllQuality")
	public String findAll(ModelMap map, HttpSession session) {
		map.addAttribute("qualityList", qualityService.findAllQuality());
		return "admin/qualityList";
	}
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@RequestMapping(value = "showQualityCreateForm")
	public String showCtrateForm() {
		return "admin/qualityCreateForm";
	}
	@ApiOperation(value="添加一条成色记录",notes="添加一条成色记录")
	@RequestMapping(value = "createQuality", method = RequestMethod.POST)
	public String createQuality(Quality quality, ModelMap map) {
		int i = qualityService.insertQuality(quality);
		if (i == 1)
			return "redirect:/admin/findAllQuality";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/qualityCreateForm";
		}
	}
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value = "/showQualityUpdate/{quality_ID}", method = RequestMethod.GET)
	public String showQualityUpdate(@PathVariable("quality_ID") int quality_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("quality", qualityService.findQualityById(quality_ID));
		return "admin/qualityUpdateForm";
	}
	@ApiOperation(value="修改一条成色记录",notes="修改一条成色记录")
	@RequestMapping(value = "/updateQuality", method = RequestMethod.POST)
	public String updateQuality(Quality quality, ModelMap map) {
		int i = qualityService.updateQuality(quality);
		if (i == 1)
			return "redirect:/admin/findAllQuality";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/findAllQuality";
		}
	}
	@ApiOperation(value="删除一条成色记录",notes="删除一条成色记录")
	@RequestMapping(value = "/deleteQuality/{quality_ID}", method = RequestMethod.GET)
	public String updateQuality(@PathVariable("quality_ID") int quality_ID) {
		int i = qualityService.deleteQuality(quality_ID);
		System.out.println("i的值是：" + i);// 1 代表删除成功 0 代表删除失败
		return "redirect:/admin/findAllQuality";
	}
}
