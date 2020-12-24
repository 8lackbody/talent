/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.dao;

import com.jeesite.common.dao.TreeDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.entity.DictData;

/**
 * 字典数据表DAO接口
 * @author zht
 * @version 2020-12-24
 */
@MyBatisDao
public interface DictDataDao extends TreeDao<DictData> {
	
}