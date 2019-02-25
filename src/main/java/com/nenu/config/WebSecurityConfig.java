package com.nenu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(
					"classpath:/META-INF/resources/webjars/");
		}
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations(
					CLASSPATH_RESOURCE_LOCATIONS);
		}
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
       

	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("这里是控制拦截设置");
		registry.addInterceptor(new com.nenu.interceptor.MyInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/login", "/analysis/login","/analysis/doLogin","/doLogin","/js/*","/js/hAdmin.js","/admin/css/*","/admin/fonts/*","/admin/js/*","/admin/images/*","/bootstrap/*","/img/*","/css/*","/fonts/*","/images/icons/*",
						"/bootstrap/css/bootstrap-theme.min.css","/bootstrap/css/bootstrap.min.css","/js/jquery.min.js","/login/css","/login/img","/login/js","/login/layui");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addViewController("/admin/table_jqgrid").setViewName("admin/table_jqgrid.html");
		registry.addViewController("/admin/table_data_tables").setViewName("admin/table_data_tables.html");
		registry.addViewController("/admin/table_bootstrap").setViewName("admin/table_bootstrap.html");
		registry.addViewController("/admin/table_basic").setViewName("admin/table_basic.html");
		registry.addViewController("/admin/form_basic").setViewName("admin/form_basic.html");
		registry.addViewController("/admin/buttons").setViewName("admin/buttons.html");
		registry.addViewController("/admin/timeline_v2").setViewName("admin/timeline_v2.html");
		registry.addViewController("/admin/layerdate").setViewName("admin/layerdate.html");
		registry.addViewController("/admin/index_v1").setViewName("admin/index_v1.html");
		registry.addViewController("/admin/employeeList").setViewName("admin/employeeList.html");
		registry.addViewController("/admin/employeeList2").setViewName("admin/employeeList2.html");
		registry.addViewController("/admin/employeeList3").setViewName("admin/employeeList3.html");
		registry.addViewController("/admin/employeeList4").setViewName("admin/employeeList4.html");
		registry.addViewController("/admin/employeeList5").setViewName("admin/employeeList5.html");
		registry.addViewController("/").setViewName("login.html");
		registry.addViewController("/analysis").setViewName("analysis/login.html");

		super.addViewControllers(registry);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// TODO Auto-generated method stub
		configurer.enable();
		super.configureDefaultServletHandling(configurer);
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/api/*").allowedOrigins("*");
	}
	
}
