package com.jeesite.modules.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.dao.WarehouseDao;
import com.jeesite.modules.entity.InventoryCheckForm;
import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.result.ResultCode;
import com.jeesite.modules.result.ResultVo;
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
    ArchivesService archivesServicel;

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
        String startEpc = inventoryCheckForm.getStartEpc();
        String endEpc = inventoryCheckForm.getEndEpc();
        List<String> foundList = inventoryCheckForm.getFound();
        List<String> inLibraryList = new ArrayList<>();
        for (Long i = Long.valueOf("0"); i < (Long.valueOf(endEpc) - Long.valueOf(startEpc) + 1); i ++){
            String temp = String.valueOf(Long.valueOf(startEpc) + i);
            String name = archivesServicel.getNameByEpc(temp);
            if (name != null){
                inLibraryList.add(temp);
            }
        }

        //找出未知的标签
        List<String> unknowList = new ArrayList<>(foundList);
        for (int i = 0; i < inLibraryList.size(); i++){
            for (int j = 0; j < unknowList.size(); j++){
                if (inLibraryList.get(i).equals(unknowList.get(j))){
                    unknowList.remove(j);
                }
            }
        }

        //找出丢失的标签
        List<String> lostList = new ArrayList<>(inLibraryList);
        for (int i = 0; i < foundList.size(); i++){
            for (int j = 0; j < lostList.size(); j++){
                if (foundList.get(i).equals(lostList.get(j))){
                    lostList.remove(j);
                }
            }
        }

        Map<String,List<String>> map = new HashMap<String, List<String>>();
        map.put("Data",lostList);
        map.put("Unknow",unknowList);

        return ResultVo.ok().put(map);
    }

}