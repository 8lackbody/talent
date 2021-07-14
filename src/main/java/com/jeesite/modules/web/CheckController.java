/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.rds.entity.Check;
import com.jeesite.modules.rds.entity.Record;
import com.jeesite.modules.rds.service.CheckService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * checkController
 * @author zht
 * @version 2021-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/check/check")
public class CheckController extends BaseController {

	@Autowired
	private CheckService checkService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Check get(String checkId, boolean isNewRecord) {
		return checkService.get(checkId, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("check:check:view")
	@RequestMapping(value = {"list", ""})
	public String list(Check check, Model model) {
		model.addAttribute("check", check);
		return "modules/check/checkList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("check:check:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Check> listData(Check check, HttpServletRequest request, HttpServletResponse response) {
		check.setPage(new Page<>(request, response));
		Page<Check> page = checkService.findPage(check);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("check:check:view")
	@RequestMapping(value = "form")
	public String form(Check check, Model model) {
		model.addAttribute("check", check);
		return "modules/check/checkForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("check:check:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Check check) {
		checkService.save(check);
		return renderResult(Global.TRUE, text("保存check成功！"));
	}

	/**
	 * 删除数据
	 */
	@RequiresPermissions("check:check:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Check check) {
		checkService.delete(check);
		return renderResult(Global.TRUE, text("删除check成功！"));
	}

	@RequestMapping(value = "checkExport")
	@ResponseBody
	public boolean checkExport(Check check) {
		List<Check> list = checkService.findList(check);
		if (list.size() > 2000) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = "export")
	@ResponseBody
	public void exportFile(Check check, HttpServletRequest request, HttpServletResponse response) {
		String fileName = DateUtils.getDate("yyyy-MM-dd") + "盘库记录" + ".xlsx";
		List<Check> list = checkService.findList(check);
		new ExcelExport("盘库记录", Check.class).setDataList(list).write(response, fileName).close();
	}

}