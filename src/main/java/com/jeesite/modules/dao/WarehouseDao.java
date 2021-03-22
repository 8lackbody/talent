/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.entity.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * warehouseDAO接口
 * @author zht
 * @version 2020-12-24
 */
@MyBatisDao
public interface WarehouseDao extends CrudDao<Warehouse> {

    @Select("SELECT * FROM warehouse")
    List<Warehouse> findAll();

    @Select("SELECT * FROM warehouse WHERE reader_ip = #{ip}")
    Warehouse findByReaderIp(@Param("ip") String ip);

    @Select("SELECT * FROM warehouse")
    List<Warehouse> getAllWarehouse();

}