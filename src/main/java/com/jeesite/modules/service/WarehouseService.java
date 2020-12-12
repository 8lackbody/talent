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

/**
 * warehouseService
 * @author zht
 * @version 2020-12-09
 */
@Service
@Transactional(readOnly=true)
public class WarehouseService extends CrudService<WarehouseDao, Warehouse> {
	
	/**
	 * 获取单条数据
	 * @param warehouse
	 * @return
	 */
	@Override
	public Warehouse get(Warehouse warehouse) {
		return super.get(warehouse);
	}
	
	/**
	 * 查询分页数据
	 * @param warehouse 查询条件
	 * @return
	 */
	@Override
	public Page<Warehouse> findPage(Warehouse warehouse) {
		return super.findPage(warehouse);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param warehouse
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Warehouse warehouse) {
		super.save(warehouse);
	}
	
	/**
	 * 更新状态
	 * @param warehouse
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Warehouse warehouse) {
		super.updateStatus(warehouse);
	}
	
	/**
	 * 删除数据
	 * @param warehouse
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Warehouse warehouse) {
		super.delete(warehouse);
	}
	
}