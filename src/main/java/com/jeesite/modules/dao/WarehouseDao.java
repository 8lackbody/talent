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

    @Select("SELECT tree_sort FROM js_sys_dict_data WHERE dict_type = 'warehouse_name' ORDER BY tree_sort DESC LIMIT 1")
    String getLastTreeSort();

    @Select("SELECT tree_sorts FROM js_sys_dict_data WHERE dict_type = 'warehouse_name' ORDER BY tree_sort DESC LIMIT 1")
    String getLastTreeSorts();

    @Select("SELECT dict_code FROM js_sys_dict_data WHERE dict_type = 'warehouse_name' ORDER BY dict_value DESC LIMIT 1")
    String getLastCode();

    @Insert("INSERT INTO js_sys_dict_data VALUES (#{code}, '0', '0,', #{treeSort}, #{treeSorts}, '1', 0, #{treeNames}, #{dictLabel}, #{id}, 'warehouse_name', '0', '', '', '', '0', 'system', '2020-12-18 15:21:27', 'system', '2020-12-18 15:21:27', '', '0', 'JeeSite', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);")
    Boolean insertTag(@Param("code") String dictCode,@Param("treeSort") String treeSort,@Param("treeSorts") String treeSorts,@Param("treeNames") String treeName,@Param("dictLabel") String dictLabel,@Param("id") String id);

    @Delete("DELETE FROM js_sys_dict_data WHERE dict_type = 'warehouse_name'")
    Boolean deleteAllTag();
}