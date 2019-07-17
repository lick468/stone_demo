package com.nenu.controller;

import org.springframework.boot.web.servlet.error.ErrorController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value="ErrorRController",description="错误跳转接口")
@Controller
public class ErrorRController implements ErrorController {
	private static final String ERROR_PATH = "/error";  
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return ERROR_PATH;
	}
	@ApiOperation(value="跳转到404页面",notes="跳转到404页面")
	@GetMapping(value=ERROR_PATH)
    public String handleError(){  
        return "4040";  
    }  
	

}
