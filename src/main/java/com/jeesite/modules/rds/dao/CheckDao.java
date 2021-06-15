/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.rds.entity.Check;

/**
 * checkDAO接口
 * @author zht
 * @version 2021-06-15
 */
@MyBatisDao
public interface CheckDao extends CrudDao<Check> {
	
}