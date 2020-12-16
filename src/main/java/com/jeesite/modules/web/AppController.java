package com.jeesite.modules.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.dao.WarehouseDao;
import com.jeesite.modules.entity.InventoryCheckForm;
import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.result.ResultCode;
import com.jeesite.modules.result.ResultVo;
import com.jeesite.modules.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * archivesController
 *
 * @author zht
 * @version 2020-12-15
 */
@Controller
@RequestMapping(value = "app/")
public class AppController {

    @Autowired
    WarehouseService warehouseService;

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
            if(androidIp!=null && androidIp.matches(check)){
                warehouse = warehouseService.findByAndroidIp(androidIp);
            }else {
                //失败返回校验数据错误
                return ResultVo.fail(ResultCode.PARAMETER);
            }
        }catch (Exception e){
            //500报错返回
            return ResultVo.fail(ResultCode.ERROR).put(e.getMessage());
        }
        //成功返回
        return ResultVo.ok().put(warehouse);
    }

    /**
     * 手持机发送请求，盘库操作
     */
    @RequestMapping(value = "inventoryCheck")
    @ResponseBody
    public ResultVo inventoryCheck(@RequestBody InventoryCheckForm inventoryCheckForm) {

        return ResultVo.ok();
    }

}