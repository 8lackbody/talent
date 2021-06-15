/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.rds.entity.Check;
import com.jeesite.modules.rds.dao.CheckDao;

/**
 * checkService
 * @author zht
 * @version 2021-06-15
 */
@Service
@Transactional(readOnly=true)
public class CheckService extends CrudService<CheckDao, Check> {
	
	/**
	 * 获取单条数据
	 * @param check
	 * @return
	 */
	@Override
	public Check get(Check check) {
		return super.get(check);
	}
	
	/**
	 * 查询分页数据
	 * @param check 查询条件
	 * @param check.page 分页对象
	 * @return
	 */
	@Override
	public Page<Check> findPage(Check check) {
		return super.findPage(check);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param check
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Check check) {
		super.save(check);
	}
	
	/**
	 * 更新状态
	 * @param check
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Check check) {
		super.updateStatus(check);
	}
	
	/**
	 * 删除数据
	 * @param check
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Check check) {
		super.delete(check);
	}
	
}