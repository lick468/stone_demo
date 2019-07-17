package com.nenu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nenu.dao.CounterDao;

import com.nenu.domain.Counter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 系统管理员操作  ----->  柜台列表
 * @author nenu
 *
 */
@Api(value="CounterController",description="钻石流转系统系统管理员操作  ----->柜台列表接口")
@Controller
@RequestMapping("/admin")
public class CounterController {
	@Autowired
	private CounterDao counterDao;
	@ApiOperation(value="跳转到counterList页面",notes="跳转到主页")
	@GetMapping(value = "/findAllCounter")
	public String findAll(ModelMap map, HttpSession session) {
		map.addAttribute("counterList", counterDao.findAllCounter());
		return "admin/counterList";
	}
	@ApiOperation(value="跳转到CounterCreateForm页面",notes="跳转到CounterCreateForm页面")
	@GetMapping(value = "/showCounterCreateForm")
	public String showCtrateForm() {
		return "admin/CounterCreateForm";
	}
	@ApiOperation(value="添加柜台",notes="添加柜台")
	@RequestMapping(value = "/createCounter", method = RequestMethod.POST)
	public String createCounter(Counter counter, ModelMap map) {
		int i = counterDao.insertCounter(counter);
		if (i == 1)
			return "redirect:/admin/findAllCounter";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/counterCreateForm";
		}
	}
	@ApiOperation(value="跳转到counterUpdateForm页面",notes="跳转到counterUpdateForm页面")
	@RequestMapping(value = "/showCounterUpdate/{counter_ID}", method = RequestMethod.GET)
	public String showCounterUpdate(@PathVariable("counter_ID") int counter_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("counter", counterDao.findCounterById(counter_ID));
		return "admin/counterUpdateForm";
	}
	@ApiOperation(value="修改柜台信息",notes="修改柜台信息")
	@RequestMapping(value = "/updateCounter", method = RequestMethod.POST)
	public String updateCounter(Counter counter, ModelMap map) {
		int i = counterDao.updateCounter(counter);
		if (i == 1)
			return "redirect:/admin/findAllCounter";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/findAllCounter";
		}
	}
	@ApiOperation(value="删除一条柜台信息",notes="删除一条柜台信息")
	@RequestMapping(value = "/deleteCounter/{counter_ID}", method = RequestMethod.GET)
	public String deleteCounter(@PathVariable("counter_ID") int counter_ID) {
		int i = counterDao.deleteCounter(counter_ID);
		System.out.println("i的值是：" + i);// 1 代表删除成功 0 代表删除失败
		return "redirect:/admin/findAllCounter";
	}
}
