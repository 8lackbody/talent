/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.rds.entity.Archives;
import com.jeesite.modules.utils.MultipartFileToFile;
import com.jeesite.modules.rds.service.ArchivesService;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * archivesController
 *
 * @author zht
 * @version 2020-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/archives/archives")
public class ArchivesController extends BaseController {

    @Autowired
    private ArchivesService archivesService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Archives get(String archivesId, boolean isNewRecord) {
        return archivesService.get(archivesId, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("archives:archives:view")
    @RequestMapping(value = {"list", ""})
    public String list(Archives archives, Model model) {
        model.addAttribute("archives", archives);
        return "modules/archives/archivesList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("archives:archives:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Archives> listData(Archives archives, HttpServletRequest request, HttpServletResponse response) {
        archives.setPage(new Page<>(request, response));
        Page<Archives> page = archivesService.findPage(archives);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("archives:archives:view")
    @RequestMapping(value = "form")
    public String form(Archives archives, Model model) {
        model.addAttribute("archives", archives);
        return "modules/archives/archivesForm";
    }

    /**
     * 保存数据
     */
    @RequiresPermissions("archives:archives:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated Archives archives) {
        archivesService.save(archives);
        return renderResult(Global.TRUE, text("保存标签成功！"));
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("archives:archives:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Archives archives) {
        archivesService.delete(archives);
        return renderResult(Global.TRUE, text("删除标签成功！"));
    }

    /**
     * excel导入数据库
     */
    @RequiresPermissions("archives:archives:edit")
    @RequestMapping(value = "export")
    @ResponseBody
    public String export(@RequestParam("file") MultipartFile file) {

        List<Integer> list = new ArrayList<>();

        Workbook rwb;
        try {
            rwb = Workbook.getWorkbook(MultipartFileToFile.multipartFileToFile(file));
        } catch (Exception e) {
            return renderResult(Global.FALSE, text("文件格式接收出错，请检查格式！"));
        }

        Sheet rs = rwb.getSheet(0);//或者rwb.getSheet(0)
        int clos = rs.getColumns();//得到所有的列
        int rows = rs.getRows();//得到所有的行

        if (clos != 3) {
            return renderResult(Global.FALSE, text("文件格式接收出错，请检查格式！"));
        }

        for (int i = 1; i < rows; i++) {
            int j = 0;
            Archives archives = new Archives();
            //第一个是列数，第二个是行数
            String epc = rs.getCell(j++, i).getContents();
            if (StringUtils.isNotBlank(epc)) {
                archives.setEpc(epc);
            } else {
                logger.error("导入单个标签出错，第" + i + "行，epc为空");
                list.add(i);
                continue;
            }

            String name = rs.getCell(j++, i).getContents();
            if (StringUtils.isNotBlank(name)) {
                archives.setName(name);
            }

            String cardId = rs.getCell(j++, i).getContents();
            if (StringUtils.isNotBlank(cardId)) {
                archives.setCardId(cardId);
            }

            try {
                archivesService.save(archives);
            } catch (Exception e) {
                list.add(i);
                logger.error("导入单个标签出错，第" + i + "行，epc：" + epc);
            }
        }

        if (list.size() == 0) {
            return renderResult(Global.TRUE, text("导入标签成功！共导入" + (rows - list.size()) + "条信息"));
        } else {
            return renderResult(Global.TRUE, text("导入标签成功！共导入" + (rows - list.size()) + "条信息," + list.toString() + "行导入失败！"));
        }
    }
}