package com.nenu.controller;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nenu.domain.Employee;
import com.nenu.domain.EmployeeRole;
import com.nenu.service.EmployeeRoleService;
import com.nenu.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value="LoginAnalysisController",description="数据分析系统用户登录登出接口")
@Controller
@RequestMapping(value = "/analysis/")
public class LoginAnalysisController {
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeRoleService employeeRoleService;
	@ApiOperation(value="跳转到analysis/login页面",notes="跳转到analysis/login页面")
	@RequestMapping(value = "login")
	public String login() {
		return "analysis/login";
	}
	
	@ApiOperation(value="登陆",notes="登陆")
	@RequestMapping(value = "doLogin", method = RequestMethod.POST)
	public String doLogi(Employee emp, ModelMap map, HttpServletRequest requset, RedirectAttributes model) {
		System.out.println(emp.getEmp_name() + "---------------" + emp.getEmp_password());
		Employee employee = employeeService.findEmployeeByName(emp.getEmp_name());

		if (employee == null) {
			model.addFlashAttribute("msg", "没有此用户");
			return "redirect:/analysis/login";
		} else if(!employee.getEmp_password().contains(emp.getEmp_password())){
			model.addFlashAttribute("msg", "密码不对");
			return "redirect:/analysis/login";
		}else {
			HttpSession session = requset.getSession();
			session.setAttribute("emp_name", emp.getEmp_name());
			session.setMaxInactiveInterval(10 * 60 * 60); // 设置session失效时间 10小时 单位：秒
			List<EmployeeRole> employeeRoleList = employeeRoleService.findEmployeeRoleByEmpID(employee.getId());
			session.setAttribute("employeeRoleList", employeeRoleList);
			if(emp.getEmp_name().contains("admin")) {
				return "redirect:/analysis/admin/index";
			}else {
				return "redirect:/analysis/main";
			}
		}	
			
			
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return String created by lick on 2018年5月12日
	 */
	@ApiOperation(value="退出登录",notes="退出登录")
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		
		return "redirect:/analysis/login";

	}

}
