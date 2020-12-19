/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.MultipartFileToFile;
import com.jeesite.modules.entity.Archives;
import com.jeesite.modules.service.ArchivesService;
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
import java.io.File;

/**
 * archivesController
 *
 * @author zht
 * @version 2020-12-15
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
    public Archives get(Long archivesId, boolean isNewRecord) {
        return archivesService.get(String.valueOf(archivesId), isNewRecord);
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
        return renderResult(Global.TRUE, text("保存archives成功！"));
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("archives:archives:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Archives archives) {
        archivesService.delete(archives);
        return renderResult(Global.TRUE, text("删除archives成功！"));
    }

    /**
     * excel导入数据库
     */
    @RequestMapping(value = "export")
    public String export(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request, HttpServletResponse response) {
        System.out.println(file);
        MultipartFileToFile multipartFileToFile = new MultipartFileToFile();
        File file1 = null;
        try {
            file1 = multipartFileToFile.multipartFileToFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Workbook rwb = Workbook.getWorkbook(file1);
            Sheet rs = rwb.getSheet("Sheet1");//或者rwb.getSheet(0)
            int clos = rs.getColumns();//得到所有的列
            int rows = rs.getRows();//得到所有的行

            System.out.println(clos + " rows:" + rows);
            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < clos; j++) {
                    //第一个是列数，第二个是行数
                    String epc = rs.getCell(j++, i).getContents();
                    String name = rs.getCell(j++, i).getContents();
                    String cardId = rs.getCell(j++, i).getContents();
                    Integer wareHouseId = Integer.valueOf(rs.getCell(j++, i).getContents());
                    Archives archives = new Archives();
                    archives.setEpc(epc);
                    archives.setName(name);
                    archives.setCardId(cardId);
                    archives.setWarehouseId(wareHouseId);
                    archivesService.save(archives);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "modules/archives/archivesList";
    }
}