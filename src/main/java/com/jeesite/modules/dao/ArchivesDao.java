/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.entity.Archives;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * archivesDAO接口
 * @author zht
 * @version 2020-12-09
 */
@MyBatisDao
public interface ArchivesDao extends CrudDao<Archives> {

    @Select("SELECT `name` FROM archives WHERE epc = #{epc}")
    String getNameByEpc(@Param("epc") String epc);

}