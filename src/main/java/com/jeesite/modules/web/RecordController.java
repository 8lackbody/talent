/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.entity.Record;
import com.jeesite.modules.service.RecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * recordController
 * @author zht
 * @version 2020-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/record/record")
public class RecordController extends BaseController {

	@Autowired
	private RecordService recordService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Record get(Long recordId, boolean isNewRecord) {
		return recordService.get(String.valueOf(recordId), isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("record:record:view")
	@RequestMapping(value = {"list", ""})
	public String list(Record record, Model model) {
		model.addAttribute("record", record);
		return "modules/record/recordList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("record:record:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Record> listData(Record record, HttpServletRequest request, HttpServletResponse response) {
		record.setPage(new Page<>(request, response));
		Page<Record> page = recordService.findPage(record);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("record:record:view")
	@RequestMapping(value = "form")
	public String form(Record record, Model model) {
		model.addAttribute("record", record);
		return "modules/record/recordForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("record:record:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Record record) {
		recordService.save(record);
		return renderResult(Global.TRUE, text("保存record成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("record:record:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Record record) {
		recordService.delete(record);
		return renderResult(Global.TRUE, text("删除record成功！"));
	}
	
}