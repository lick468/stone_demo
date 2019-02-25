package com.nenu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nenu.dao.SupplierDao;
import com.nenu.domain.Supplier;
import com.nenu.service.SupplierService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 系统管理员操作  ----->  供应商列表
 * @author nenu
 *
 */
@Api(value="SupplierController",description="钻石流转系统系统管理员操作  -----> 供应商列表接口")
@Controller
@RequestMapping("/admin")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;
	/**
	 * 查询出所有的供应商
	 *
	 * com.nenu.controller
	 * @param map
	 * @param session
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="跳转主页",notes="跳转主页")
	@RequestMapping(value = "findAllSupplier")
	public String findAll(ModelMap map, HttpSession session) {
		map.addAttribute("supplierList", supplierService.findAllSupplier());
		return "admin/supplierList";
	}
	/**
	 * 显示添加供应商页面
	 *
	 * com.nenu.controller
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="跳转添加页面",notes="显示添加供应商页面")
	@RequestMapping(value = "showSupplierCreateForm")
	public String showCtrateForm() {
		return "admin/supplierCreateForm";
	}
	/**
	 * 添加供应商
	 *
	 * com.nenu.controller
	 * @param supplier
	 * @param map
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="添加供应商",notes="添加供应商")
	@RequestMapping(value = "createSupplier", method = RequestMethod.POST)
	public String createSupplier(Supplier supplier, ModelMap map) {
		int i = supplierService.insertSupplier(supplier);
		if (i == 1)
			return "redirect:/admin/findAllSupplier";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/supplierCreateForm";
		}
	}
	/**
	 * 显示供应商修改页面
	 *
	 * com.nenu.controller
	 * @param supplier_ID
	 * @param map
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="跳转供应商修改页面",notes="显示供应商修改页面")
	@RequestMapping(value = "/showSupplierUpdate/{supplier_ID}", method = RequestMethod.GET)
	public String showSupplierUpdate(@PathVariable("supplier_ID") int supplier_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("supplier", supplierService.findSupplierById(supplier_ID));
		return "admin/supplierUpdateForm";
	}
	/**
	 * 修改供应商信息
	 *
	 * com.nenu.controller
	 * @param supplier
	 * @param map
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="修改供应商信息",notes="修改供应商信息")
	@RequestMapping(value = "/updateSupplier", method = RequestMethod.POST)
	public String updateSupplier(Supplier supplier, ModelMap map) {
		int i = supplierService.updateSupplier(supplier);
		if (i == 1)
			return "redirect:/admin/findAllSupplier";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/findAllSupplier";
		}
	}
	/**
	 * 删除一个供应商根据ID
	 *
	 * com.nenu.controller
	 * @param supplier_ID
	 * @return String
	 * created  at 2018年6月4日
	 */
	@ApiOperation(value="删除一个供应商根据ID",notes="删除一个供应商根据ID")
	@RequestMapping(value = "/deleteSupplier/{supplier_ID}", method = RequestMethod.GET)
	public String deleteSupplier(@PathVariable("supplier_ID") int supplier_ID) {
		int i = supplierService.deleteSupplier(supplier_ID);
		System.out.println("i的值是：" + i);// 1 代表删除成功 0 代表删除失败
		return "redirect:/admin/findAllSupplier";
	}
	
}
