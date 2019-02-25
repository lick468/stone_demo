package com.nenu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nenu.config.SessionCounter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



//在线访问人数
@Api(value="CountController",description="钻石流转系统统计在线人数接口")
@Controller
public class CountController {
	@ApiOperation(value="统计在线人数",notes="统计在线人数")
	@RequestMapping(value="/count",method = RequestMethod.POST)
	@ResponseBody
	public int count(HttpServletRequest request) {
		HttpSession session = request.getSession(); 
		//System.out.println("role0======"+session.getAttribute("role0"));
		//System.out.println("role1======"+session.getAttribute("role1"));
		//System.out.println("role2======"+session.getAttribute("role2"));
		//System.out.println("role3======"+session.getAttribute("role3"));
		//System.out.println("role4======"+session.getAttribute("role4"));
		//System.out.println("role5======"+session.getAttribute("role5"));
		
		int res=0;
		if(session.getAttribute("role0")!=null) {
			res +=(int) session.getAttribute("role0");
		}
		if(session.getAttribute("role1")!=null) {
			res +=(int) session.getAttribute("role1");
		}
		if(session.getAttribute("role2")!=null) {
			res +=(int) session.getAttribute("role2");
		}
		if(session.getAttribute("role3")!=null) {
			res +=(int) session.getAttribute("role3");
		}
		if(session.getAttribute("role4")!=null) {
			res +=(int) session.getAttribute("role4");
		}
		if(session.getAttribute("role5")!=null) {
			res +=(int) session.getAttribute("role5");
		}
		
		
		return res;
	}

}
