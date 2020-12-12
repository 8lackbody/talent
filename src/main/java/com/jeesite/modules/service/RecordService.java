/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dao.RecordDao;
import com.jeesite.modules.entity.Record;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * recordService
 * @author zht
 * @version 2020-12-09
 */
@Service
@Transactional(readOnly=true)
public class RecordService extends CrudService<RecordDao, Record> {
	
	/**
	 * 获取单条数据
	 * @param record
	 * @return
	 */
	@Override
	public Record get(Record record) {
		return super.get(record);
	}
	
	/**
	 * 查询分页数据
	 * @param record 查询条件
	 * @return
	 */
	@Override
	public Page<Record> findPage(Record record) {
		return super.findPage(record);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param record
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Record record) {
		super.save(record);
	}
	
	/**
	 * 更新状态
	 * @param record
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Record record) {
		super.updateStatus(record);
	}
	
	/**
	 * 删除数据
	 * @param record
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Record record) {
		super.delete(record);
	}
	
}