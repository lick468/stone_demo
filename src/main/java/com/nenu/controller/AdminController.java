package com.nenu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nenu.domain.RoleUserOfIn;
import com.nenu.domain.User;
import com.nenu.service.LoginService;
import com.nenu.service.RoleUserOfInService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 管理员操作
 * 
 * @author nenu
 *
 */
@Api(value="AdminController",description="钻石流转系统管理员管理接口")
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private RoleUserOfInService roleUserOfInService;
	@ApiOperation(value="跳转到admin/index页面",notes="跳转到主页")
	@GetMapping(value = "/index")
	public String index() {
		return "admin/index";
	}
	@ApiOperation(value="跳转到admin/roleUserList页面",notes="跳转到主页")
	@GetMapping(value = "/roleUserList")
	public String roleUserList(ModelMap map) {
		map.addAttribute("roleUserList", loginService.findRoleList());
		return "admin/roleUserList";
	}
	@ApiOperation(value="跳转到admin/roleShow页面",notes="跳转到主页")
	@RequestMapping(value = "/showUserRole/{user_role}", method = RequestMethod.GET)
	public String showUserRole(@PathVariable("user_role") int user_role, ModelMap map) {
		map.addAttribute("user_role", user_role);
		List<RoleUserOfIn> list = roleUserOfInService.findByRoleUser(user_role);
		List ll = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ll.add(list.get(i).getRole_type());
		}
		map.addAttribute("roleList", ll);
		
		return "admin/roleShow";
	}
	@ApiOperation(value="跳转到admin/userCreateForm页面",notes="跳转到主页")
	@GetMapping(value = "/createForm")
	public String createForm() {
		return "admin/userCreateForm";
	}
	@ApiOperation(value="添加一条员工记录",notes="修改一条员工记录")
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public String createUser(User user, ModelMap map) {
		int i = loginService.createUser(user);
		if (i == 1)
			return "redirect:/admin/roleUserList";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/userCreateForm";
		}
	}
	@ApiOperation(value="跳转到admin/userUpdateForm页面",notes="跳转到主页")
	@RequestMapping(value = "/showUserUpdate/{user_ID}", method = RequestMethod.GET)
	public String showUserUpdate(@PathVariable("user_ID") int user_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("user", loginService.findUserByUserID(user_ID));
		return "admin/userUpdateForm";
	}
	@ApiOperation(value="修改一条员工记录",notes="修改一条员工记录")
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(User user, ModelMap map) {
		int i = loginService.updateUser(user);
		if (i == 1)
			return "redirect:/admin/roleUserList";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/roleUserList";
		}
	}

}
