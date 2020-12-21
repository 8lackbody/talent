/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.entity.Record;

/**
 * recordDAO接口
 *
 * @author zht
 * @version 2020-12-09
 */
@MyBatisDao
public interface RecordDao extends CrudDao<Record> {

}