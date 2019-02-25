package com.nenu.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class MyInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("user_name");
		String emp_name = (String) session.getAttribute("emp_name");
		//System.out.println(url);
		//System.out.println("缓存user_name是否是值:" + name);

		 if(url.indexOf("analysis")>=0) {
			 if (emp_name == null) {
					response.sendRedirect("/analysis/login");
					return false;
				} else {
					return true;
				}
		 }else {
			 if (name == null) {
					response.sendRedirect("/login");
					return false;
				} else {
					return true;
				}
		 }
		
	}
}
