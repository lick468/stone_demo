package com.nenu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nenu.domain.RoleUserOfIn;
import com.nenu.service.RoleUserOfInService;

import io.swagger.annotations.Api;
@Api(value="RoleUserOfInController",description="钻石流转系统角色管理接口")
@Controller
public class RoleUserOfInController {
	@Autowired
	private RoleUserOfInService roleUserOfInService;

	/**
	 * 获取用户操作石头入库字段
	 * 
	 * @param role_user
	 * @return List<RoleUserOfIn> created by lick on 2018年5月12日
	 */
	public List<RoleUserOfIn> findByRoleUser(int role_user) {
		return roleUserOfInService.findByRoleUser(role_user);
	}
	
	public int deleteByRoleUser(int role_user) {
		return roleUserOfInService.deleteRoleUserOfIn(role_user);
	}
	
	@RequestMapping(value="/roleUser/insertRoleUser",method = RequestMethod.POST)
	@ResponseBody
	public String[] insertRoleUser(HttpServletRequest request,ModelMap map) {
		String role = request.getParameter("role");
		String list = request.getParameter("selected");
		String[] sel = list.split(",");
		int roleUser = Integer.parseInt(role);
		System.out.println(list+"llllllllllllllllllllll");
		roleUserOfInService.deleteRoleUserOfIn(roleUser);
		
		for(int i=0;i<sel.length;i++) {
			RoleUserOfIn roleUserOfIn = new RoleUserOfIn();
			roleUserOfIn.setRole_user(roleUser);
			roleUserOfIn.setRole_type(sel[i]);
			roleUserOfInService.insertRoleUserOfIn(roleUserOfIn);
		}
		map.addAttribute("roleListNew", sel);
		return sel;
		
	}
	

}
