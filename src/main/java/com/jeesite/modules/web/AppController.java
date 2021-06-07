package com.jeesite.modules.web;


import com.jeesite.modules.common.form.InventoryCheckForm;
import com.jeesite.modules.common.form.QueryNumberForm;
import com.jeesite.modules.rds.entity.Warehouse;
import com.jeesite.modules.common.result.ResultCode;
import com.jeesite.modules.common.result.ResultVo;
import com.jeesite.modules.rds.service.ArchivesService;
import com.jeesite.modules.rds.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AppController
 *
 * @author zht
 * @version 2020-12-15
 */
@Controller
@RequestMapping(value = "app/")
public class AppController {

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    ArchivesService archivesService;

    /**
     * 查询列表
     */
    @RequestMapping(value = "getWarehouseName")
    @ResponseBody
    public ResultVo getWarehouseName(@RequestBody String androidIp) {
        String check = "^((25[0-5]|2[0-4]\\d|[1]{1}\\d{1}\\d{1}|[1-9]{1}\\d{1}|\\d{1})($|(?!\\.$)\\.)){4}$";
        Warehouse warehouse;
        try {
            //判断传入的参数不能为空且做ip地址正则校验
            if (androidIp != null && androidIp.matches(check)) {
                warehouse = warehouseService.findByReaderIp(androidIp);
                if (warehouse != null) {
                    return ResultVo.ok().put(warehouse);
                } else {
                    return ResultVo.fail(ResultCode.ERROR).put("没有对应仓库");
                }
            } else {
                //失败返回校验数据错误
                return ResultVo.fail(ResultCode.PARAMETER).put("入参有误");
            }
        } catch (Exception e) {
            //500报错返回
            return ResultVo.fail(ResultCode.ERROR).put(e.getMessage());
        }
    }

    /**
     * 手持机发送请求，盘库操作，查看异常数据
     */
    @RequestMapping(value = "inventoryCheck")
    @ResponseBody
    public ResultVo inventoryCheck(@RequestBody InventoryCheckForm inventoryCheckForm) {


        return ResultVo.ok().put(null);
    }

    /**
     * 查找需要盘库的数量是多少
     */
    @RequestMapping(value = "getQueryNumber")
    @ResponseBody
    public ResultVo getQueryNumber(@RequestBody QueryNumberForm queryNumberForm) {


        return ResultVo.ok().put(null);
    }

}