package com.nenu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nenu.domain.Stone;
import com.nenu.service.StoneService;

@Controller
@RequestMapping(value = "/stone")
public class StoneController {
	@Autowired
	private StoneService stoneService;
	
	@InitBinder    
	public void initBinder(WebDataBinder binder) {    

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    

	        dateFormat.setLenient(false);    

	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));    

	}
	
	@RequestMapping(value = "/insertStone", method = RequestMethod.POST)
	public String insertStone(Stone stone,BindingResult bin,HttpServletRequest request, ModelMap map) {
		String dd = request.getParameter("stone_purchdate") ;
		System.out.println(dd+"====================");
		System.out.println("----------------" + stone);

		return "operator_b/new";
	}

	@RequestMapping(value = "/sel")
	public String stoneList() {

		return "index";
	}

	public String excel2sql() {
		return "index";
	}

}
