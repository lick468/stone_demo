package com.nenu.controller;

import javax.servlet.http.HttpSession;

import com.nenu.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nenu.dao.ChannelDao;
import com.nenu.domain.Channel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 钻石流转系统
 * 系统管理员操作  ----->  来源列表
 * @author nenu
 *
 */
@Api(value="ChannelController",description="钻石流转系统系统管理员操作  ----->来源列表接口")
@Controller
@RequestMapping("/admin")
public class ChannelController {

	@Autowired
	private ChannelService channelService;
	/**
	 * 跳转主页
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转主页",notes="跳转主页")
	@GetMapping(value = "/findAllChannel")
	public String findAll(ModelMap map) {
		map.addAttribute("channelList", channelService.findAllChannel());
		return "admin/channelList";
	}
	/**
	 * 跳转添加页面
	 * @return
	 */
	@ApiOperation(value="跳转添加页面",notes="显示添加页面")
	@GetMapping(value = "/showChannelCreateForm")
	public String showCreateForm() {
		return "admin/channelCreateForm";
	}
	/**
	 * 添加一条来源记录
	 * @param channel
	 * @param map
	 * @return
	 */
	@ApiOperation(value="添加一条来源记录",notes="添加一条来源记录")
	@RequestMapping(value = "/createChannel", method = RequestMethod.POST)
	public String createChannel(Channel channel, ModelMap map) {
		int i = channelService.insertChannel(channel);
		if (i == 1)
			return "redirect:/admin/findAllChannel";
		else {
			map.addAttribute("msg", "添加失败");
			return "admin/channelCreateForm";
		}
	}
	/**
	 * 跳转修改页面
	 * @param channel_ID
	 * @param map
	 * @return
	 */
	@ApiOperation(value="跳转修改页面",notes="显示修改页面")
	@RequestMapping(value = "/showChannelUpdate/{channel_ID}", method = RequestMethod.GET)
	public String showChannelUpdate(@PathVariable("channel_ID") int channel_ID, ModelMap map) {
		// System.out.println("user_ID="+user_ID);

		map.addAttribute("channel", channelService.findChannelById(channel_ID));
		return "admin/channelUpdateForm";
	}
	/**
	 * 更新一条来源记录
	 * @param channel
	 * @param map
	 * @return
	 */
	@ApiOperation(value="更新一条来源记录",notes="更新一条来源记录")
	@RequestMapping(value = "/updateChannel", method = RequestMethod.POST)
	public String updateChannel(Channel channel, ModelMap map) {
		int i = channelService.updateChannel(channel);
		if (i == 1)
			return "redirect:/admin/findAllChannel";
		else {
			map.addAttribute("msg", "更新失败");
			return "redirect:/admin/findAllChannel";
		}
	}
	/**
	 * 根据ID删除一条来源记录
	 * @param channel_ID
	 * @return
	 */
	@ApiOperation(value="根据ID删除一条来源记录",notes="根据ID删除一条来源记录")
	@RequestMapping(value = "/deleteChannel/{channel_ID}", method = RequestMethod.GET)
	public String deleteChannel(@PathVariable("channel_ID") int channel_ID) {
		int i = channelService.deleteChannel(channel_ID);
		System.out.println("i的值是：" + i);// 1 代表删除成功 0 代表删除失败
		return "redirect:/admin/findAllChannel";
	}
}
