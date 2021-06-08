package com.jeesite.modules.web;


import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.common.config.UrlConfig;
import com.jeesite.modules.common.form.InventoryCheckForm;
import com.jeesite.modules.common.form.QueryNumberForm;
import com.jeesite.modules.common.payload.FilePersonNameOrDetailPayload;
import com.jeesite.modules.common.payload.FilePersonNumberPayload;
import com.jeesite.modules.common.result.ResultCode;
import com.jeesite.modules.common.result.ResultVo;
import com.jeesite.modules.rds.entity.Warehouse;
import com.jeesite.modules.rds.service.ArchivesService;
import com.jeesite.modules.rds.service.WarehouseService;
import com.jeesite.modules.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AppController
 *
 * @author zht
 * @version 2020-12-15
 */
@Controller
@RequestMapping(value = "app/")
public class AppController {

    private final static Logger logger = LoggerFactory.getLogger(AppController.class);

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
            logger.error(e.getMessage());
            return ResultVo.fail(ResultCode.ERROR).put(e.getMessage());
        }
    }

    /**
     * 手持机发送请求，盘库操作，查看异常数据
     */
    @RequestMapping(value = "inventoryCheck")
    @ResponseBody
    public ResultVo inventoryCheck(@RequestBody InventoryCheckForm inventoryCheckForm) {
        if (inventoryCheckForm.getFound() == null) {
            return ResultVo.fail();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("filePerson.startIcCard", inventoryCheckForm.getStartEpc());
        map.put("filePerson.endIcCard", inventoryCheckForm.getEndEpc());

        FilePersonNameOrDetailPayload filePersonNameOrDetailPayload;
        String response = null;
        try {
            response = HttpUtil.doPost(UrlConfig.URL_DETAIL, map);
            filePersonNameOrDetailPayload = JSONObject.parseObject(response, FilePersonNameOrDetailPayload.class);
        } catch (Exception e) {
            logger.error("接口返回消息序列化错误；" + response);
            return ResultVo.fail();
        }
        // 检查返回对象序列化后是否为空
        if (filePersonNameOrDetailPayload == null) {
            logger.error("接口返回消息为空；" + response);
            return ResultVo.fail();
        }
        // 检查接口的返回码是否为0
        if (filePersonNameOrDetailPayload.getErrCode() == null || filePersonNameOrDetailPayload.getErrCode() != 0) {
            logger.error("接口返回错误码不为0；" + filePersonNameOrDetailPayload.getErrMsg());
            return ResultVo.fail();
        }
        // 处理缺少的标签和未知的标签
        List<String> icCards = filePersonNameOrDetailPayload.getData().stream().map(stringStringMap -> stringStringMap.get("IC_CARD")).collect(Collectors.toList());
        List<String> foundList = inventoryCheckForm.getFound();
        List<String> index = new ArrayList<>(foundList);
        foundList.removeAll(icCards);
        icCards.removeAll(index);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lack", icCards);
        jsonObject.put("unknown", foundList);

        return ResultVo.ok().put(jsonObject);
    }

    /**
     * 查找需要盘库的数量是多少
     */
    @RequestMapping(value = "getQueryNumber")
    @ResponseBody
    public ResultVo getQueryNumber(@RequestBody QueryNumberForm queryNumberForm) {
        Map<String, Object> map = new HashMap<>();
        map.put("filePerson.startIcCard", queryNumberForm.getStartEpc());
        map.put("filePerson.endIcCard", queryNumberForm.getEndEpc());

        FilePersonNumberPayload filePersonNumberPayload;
        String response = null;
        try {
            response = HttpUtil.doPost(UrlConfig.URL_NUMBER, map);
            filePersonNumberPayload = JSONObject.parseObject(response, FilePersonNumberPayload.class);
        } catch (Exception e) {
            logger.error("接口返回消息序列化错误；" + response);
            return ResultVo.fail();
        }
        // 检查返回对象序列化后是否为空
        if (filePersonNumberPayload == null) {
            logger.error("接口返回消息为空；" + response);
            return ResultVo.fail();
        }
        // 检查接口的返回码是否为0
        if (filePersonNumberPayload.getErrCode() == null || filePersonNumberPayload.getErrCode() != 0) {
            logger.error("接口返回错误码不为0；" + response);
            return ResultVo.fail();
        }

        Map<String, String> data = filePersonNumberPayload.getData();
        // 检查返回的data是否为null
        if (data == null) {
            logger.error("接口返回的数据为null；" + response);
            return ResultVo.fail();
        }
        try {
            int number = Integer.parseInt(data.get("FILE_NUMBER"));
            return ResultVo.ok().put(Math.max(number, 0));
        } catch (Exception e) {
            logger.error("FILE_NUMBER 数据异常；" + data);
            return ResultVo.fail();
        }
    }

}