package com.nenu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import com.nenu.domain.Staff;
import com.nenu.service.StaffService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value="StaffController",description="钻石流转系统系统管理员操作  -----> 人员列表接口")
@Controller
@RequestMapping("/admin")
public class StaffController {
	@Autowired
	private StaffService staffService;
	@ApiOperation(value="跳转到admin/staffList页面",notes="跳转到admin/staffList页面")
	@GetMapping(value = "/findAllStaff")
	public String findAll(ModelMap map, HttpSession session) {
		map.addAttribute("staffList", staffService.findAllStaff());
		return "admin/staffList";
	}
	@ApiOperation(value="跳转到添加页面",notes="跳转到添加页面")
	@GetMapping(value = "/showStaffCreateForm")
	public String showCtrateForm() {
		return "admin/staffCreateForm";
	}
	@ApiOperation(value="添加一条人员记录",notes="添加一条人员记录")
	@RequestMapping(value = "createStaff", method = RequestMethod.POST)
	public String createStaff(Staff staff, ModelMap map) {
		int i = staffService.insertStaff(staff);
		if (i == 1)
			return "redirect:/admin/findAllStaff";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/staffCreateForm";
		}
	}
	@ApiOperation(value="跳转到修改页面",notes="跳转到修改页面")
	@RequestMapping(value = "/showStaffUpdate/{staff_ID}", method = RequestMethod.GET)
	public String showStaffUpdate(@PathVariable("staff_ID") int staff_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("staff", staffService.findStaffById(staff_ID));
		return "admin/staffUpdateForm";
	}
	@ApiOperation(value="修改一条人员记录",notes="修改一条人员记录")
	@RequestMapping(value = "/updateStaff", method = RequestMethod.POST)
	public String updateStaff(Staff staff, ModelMap map) {
		int i = staffService.updateStaff(staff);
		if (i == 1)
			return "redirect:/admin/findAllStaff";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/findAllStaff";
		}
	}
	@ApiOperation(value="删除一条人员记录",notes="删除一条人员记录")
	@RequestMapping(value = "/deleteStaff/{staff_ID}", method = RequestMethod.GET)
	public String deleteStaff(@PathVariable("staff_ID") int staff_ID) {
		int i = staffService.deleteStaff(staff_ID);
		System.out.println("i的值是：" + i);// 1 代表删除成功 0 代表删除失败
		return "redirect:/admin/findAllStaff";
	}
}
