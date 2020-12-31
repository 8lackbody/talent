/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.other.utils.ReaderUtil;
import com.jeesite.modules.other.utils.UpdateDictionary;
import com.jeesite.modules.service.WarehouseService;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * warehouseController
 *
 * @author zht
 * @version 2020-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/warehouse/warehouse")
public class WarehouseController extends BaseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Warehouse get(String warehouseId, boolean isNewRecord) {
        return warehouseService.get(warehouseId, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("warehouse:warehouse:view")
    @RequestMapping(value = {"list", ""})
    public String list(Warehouse warehouse, Model model) {
        model.addAttribute("warehouse", warehouse);
        return "modules/warehouse/warehouseList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("warehouse:warehouse:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Warehouse> listData(Warehouse warehouse, HttpServletRequest request, HttpServletResponse response) {
        warehouse.setPage(new Page<>(request, response));
        Page<Warehouse> page = warehouseService.findPage(warehouse);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("warehouse:warehouse:view")
    @RequestMapping(value = "form")
    public String form(Warehouse warehouse, Model model) {
        model.addAttribute("warehouse", warehouse);
        return "modules/warehouse/warehouseForm";
    }

    /**
     * 保存数据
     */
    @RequiresPermissions("warehouse:warehouse:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated Warehouse warehouse) {
        warehouseService.save(warehouse);
        updateDictionary();
        boolean restart = ReaderUtil.restart(warehouse);
        if(restart){
            return renderResult(Global.TRUE, text("保存重启reader成功！"));
        }else {
            return renderResult(Global.FALSE, text("保存成功，重启reader失败！"));
        }
    }

    /**
     * 删除数据
     */
    @RequiresPermissions("warehouse:warehouse:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Warehouse warehouse) {
        warehouseService.delete(warehouse);
        return renderResult(Global.TRUE, text("删除reader设置成功！"));
    }

    public void updateDictionary(){
        List<Warehouse> warehouseList = warehouseService.getAllWarehouse();
        List<String> idList = warehouseList.stream().map(Warehouse::getWarehouseId).collect(Collectors.toList());
        List<String> nameList = warehouseList.stream().map(Warehouse::getWarehouseName).collect(Collectors.toList());
        try {
            warehouseService.deleteAllTag();
            for (int i = 0; i < idList.size(); i++){
                if (i == 0){
                    String code = "1339833136263890001";
                    String nextTreeSort = "30";
                    String nextTreeSorts = "0000000030,";
                    warehouseService.insertTag(code,nextTreeSort,nextTreeSorts,nameList.get(i),nameList.get(i),idList.get(i));
                }else {
                    String code = UpdateDictionary.createCode(warehouseService.getLastCode());
                    String nextTreeSort = UpdateDictionary.treeSortRaise(warehouseService.getLastTreeSort());
                    String nextTreeSorts = UpdateDictionary.treeSortsRaise(warehouseService.getLastTreeSorts());
                    warehouseService.insertTag(code,nextTreeSort,nextTreeSorts,nameList.get(i),nameList.get(i),idList.get(i));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}