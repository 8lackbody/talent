package com.jeesite.modules.web;


import com.jeesite.modules.entity.InventoryCheckForm;
import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.other.result.ResultCode;
import com.jeesite.modules.other.result.ResultVo;
import com.jeesite.modules.other.utils.IsNumeric;
import com.jeesite.modules.service.ArchivesService;
import com.jeesite.modules.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 手持机发送请求，盘库操作
     */
    @RequestMapping(value = "inventoryCheck")
    @ResponseBody
    public ResultVo inventoryCheck(@RequestBody InventoryCheckForm inventoryCheckForm) {

        String startEpc = inventoryCheckForm.getStartEpc();
        String endEpc = inventoryCheckForm.getEndEpc();
        List<String> foundList = inventoryCheckForm.getFound();
        //判断入参不为空且都为整数
        if (IsNumeric.isInteger(startEpc) || IsNumeric.isInteger(endEpc) || IsNumeric.isIntegerList(foundList)) {
            return ResultVo.fail(ResultCode.PARAMETER);
        }

        Map<String, List<String>> map = new HashMap<>();
        List<String> tempList = new ArrayList<>();
        List<String> index = new ArrayList<>(foundList);

        try {
            for (long i = 0L; i < (Long.parseLong(endEpc) - Long.parseLong(startEpc) + 1); i++) {
                tempList.add(String.valueOf(Long.parseLong(startEpc) + i));
            }
            List<String> inLibraryList = archivesService.findBatchByEpcs(tempList);

            foundList.removeAll(inLibraryList);
            inLibraryList.removeAll(index);

            map.put("Data", inLibraryList);
            map.put("Unknown", foundList);
        } catch (Exception e) {
            ResultVo.fail(ResultCode.ERROR);
        }

        return ResultVo.ok().put(map);
    }

}