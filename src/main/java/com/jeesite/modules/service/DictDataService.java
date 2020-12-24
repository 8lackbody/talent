/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.entity.DictData;
import com.jeesite.modules.dao.DictDataDao;

/**
 * 字典数据表Service
 * @author zht
 * @version 2020-12-24
 */
@Service
@Transactional(readOnly=true)
public class DictDataService extends TreeService<DictDataDao, DictData> {
	
	/**
	 * 获取单条数据
	 * @param dictData
	 * @return
	 */
	@Override
	public DictData get(DictData dictData) {
		return super.get(dictData);
	}
	
	/**
	 * 查询列表数据
	 * @param dictData
	 * @return
	 */
	@Override
	public List<DictData> findList(DictData dictData) {
		return super.findList(dictData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param dictData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DictData dictData) {
		super.save(dictData);
	}
	
	/**
	 * 更新状态
	 * @param dictData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(DictData dictData) {
		super.updateStatus(dictData);
	}
	
	/**
	 * 删除数据
	 * @param dictData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(DictData dictData) {
		super.delete(dictData);
	}
	
}