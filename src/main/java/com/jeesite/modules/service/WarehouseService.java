/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dao.WarehouseDao;
import com.jeesite.modules.entity.Warehouse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * warehouseService
 *
 * @author zht
 * @version 2020-12-21
 */
@Service
@Transactional(readOnly = true)
public class WarehouseService extends CrudService<WarehouseDao, Warehouse> {

    /**
     * 获取单条数据
     *
     * @param warehouse
     * @return
     */
    @Override
    public Warehouse get(Warehouse warehouse) {
        return super.get(warehouse);
    }

    /**
     * 查询分页数据
     *
     * @param warehouse 查询条件
     * @return
     */
    @Override
    public Page<Warehouse> findPage(Warehouse warehouse) {
        return super.findPage(warehouse);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param warehouse
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Warehouse warehouse) {
        super.save(warehouse);
    }

    /**
     * 更新状态
     *
     * @param warehouse
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Warehouse warehouse) {
        super.updateStatus(warehouse);
    }

    /**
     * 删除数据
     *
     * @param warehouse
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Warehouse warehouse) {
        super.delete(warehouse);
    }

    /**
     * 根据安卓ip获取仓库信息
     *
     * @param ip
     * @return
     */
    public Warehouse findByAndroidIp(String ip) {
        return super.dao.findByAndroidIp(ip);
    }

    /**
     * 获取仓库id
     */
    public List<String> getWarehouseId(){ return super.dao.getWarehouseId(); }

    /**
     * 获取仓库name
     */
    public List<String> getWarehouseName(){ return super.dao.getWarehouseName(); }

    /**
     * 获取最后一位tree_sort
     */
    public String getLastTreeSort(){ return super.dao.getLastTreeSort(); }

    /**
     * 获取最后一位tree_sorts
     */
    public String getLastTreeSorts(){ return super.dao.getLastTreeSorts(); }

    /**
     * 获取最后一位code
     */
    public String getLastCode(){ return super.dao.getLastCode(); }

    /**
     * 根据仓库id获取字典标签
     *
     * @param id
     */
    public String getDictionaryTag(String id){ return super.dao.getDictionaryTag(id); }

    /**
     * 根据仓库id获取code
     *
     * @param id
     */
    public String getCodeById(String id){ return super.dao.getCodeByid(id); }
    /**
     * 更新字典标签
     *
     * @param treeNames
     * @param dictLabel
     * @param dictCode
     *
     */
    @Transactional(readOnly = false)
    public boolean updateTag(String treeNames, String dictLabel, String dictCode){ return super.dao.updateTag(treeNames,
            dictLabel,dictCode); }
    /**
     * 新增字典标签
     *
     * @param dictCode
     * @param treeSort
     * @param treeSorts
     * @param treeNames
     * @param dictLabel
     * @param warehouseId
     *
     */
    @Transactional(readOnly = false)
    public boolean insertTag(String dictCode, String treeSort, String treeSorts, String treeNames, String dictLabel, String warehouseId){
        return super.dao.insertTag(dictCode,treeSort,treeSorts,treeNames,dictLabel,warehouseId);
    }
}