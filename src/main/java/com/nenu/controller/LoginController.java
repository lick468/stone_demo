package com.nenu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nenu.domain.RoleUserOfIn;
import com.nenu.domain.User;
import com.nenu.service.LoginService;
import com.nenu.service.RoleUserOfInService;
import com.nenu.service.SupplierService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value="LoginController",description="钻石流转系统用户登录登出接口")
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private RoleUserOfInService roleUserOfInService;

	@Autowired
	private SupplierService supplierService;
		
	/**
	 * 显示登录页面
	 * 
	 * @return String created by lick on 2018年5月12日
	 */
	@ApiOperation(value="跳转登陆页面",notes="显示登陆页面")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	/**
	 * 登录 根据权限跳转不同的页面
	 * 
	 * @param ur
	 * @param map
	 * @param requset
	 * @return String created by lick on 2018年5月12日
	 */
	@ApiOperation(value="登陆",notes="根据权限跳转不同的页面")
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public String doLogi(User ur, ModelMap map, HttpServletRequest requset, RedirectAttributes model) {
		System.out.println(ur.getUser_name() + "---------------" + ur.getUser_role());
		User user = loginService.findUserByUser(ur);
		// System.out.println(user.toString()+"-----");
		if (user == null) {
			model.addFlashAttribute("msg", "没有此用户");
			return "redirect:/login";
		} else {
			map.addAttribute("role", ur.getUser_role());
			map.addAttribute("user", ur.getUser_name());

			HttpSession session = requset.getSession();
			
			session.setAttribute("user_name", ur.getUser_name());
			session.setMaxInactiveInterval(10 * 60 * 60); // 设置session失效时间 10小时 单位：秒
			session.setAttribute("level", ur.getUser_role());
			session.setAttribute("supplierList", supplierService.findAllSupplier());
			
			List<RoleUserOfIn> roleList = roleUserOfInService.findByRoleUser(ur.getUser_role());
			if (roleList == null)
				System.out.println("roleList是空的");
			else {
				System.out.println("roleList的值是：" + roleList);
				session.setAttribute("roleList", roleList);
			}
			//跳转到不同的用户界面
			if (user.getUser_role() == 0) {
				session.setAttribute("role0",1);
				return "redirect:/admin/index";
			} else if (user.getUser_role() == 1) {
				session.setAttribute("role1",1 );
				return "redirect:/admin/index";
			} else if (user.getUser_role() == 2) {
				session.setAttribute("role2",1 );
				return "redirect:/a/index";
			} else if (user.getUser_role() == 3) {
				session.setAttribute("role3",1 );
				return "redirect:/b/index";
			} else if (user.getUser_role() == 4) {
				session.setAttribute("role4",1 );
				return "redirect:/c/index";
			} else if(user.getUser_role() == 5){
				session.setAttribute("role5",1 );
				return "redirect:/d/index";
			}else {
				return "4040";
			}
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return String created by lick on 2018年5月12日
	 */
	@ApiOperation(value="登出",notes="退出登录")
	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		String user_name = (String) session.getAttribute("user_name");
		User ur =loginService.findUserByUserName(user_name);
		int role = ur.getUser_role();
		String rr = "role"+role;
		//session.removeAttribute("user_name");
		session.removeAttribute(rr);
		 
		return "redirect:/login";

	}

}
