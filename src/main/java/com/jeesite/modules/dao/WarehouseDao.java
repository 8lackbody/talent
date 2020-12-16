/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.entity.Warehouse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * warehouseDAO接口
 * @author zht
 * @version 2020-12-09
 */
@MyBatisDao
public interface WarehouseDao extends CrudDao<Warehouse> {

    @Select("SELECT * FROM warehouse WHERE andriod_ip = #{ip}")
    Warehouse findByAndroidIp(@Param("ip") String ip);
	
}