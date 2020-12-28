/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.dao.ArchivesDao;
import com.jeesite.modules.entity.Archives;
import com.jeesite.modules.entity.EPCTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * archivesService
 *
 * @author zht
 * @version 2020-12-21
 */
@Service
@Transactional(readOnly = true)
public class ArchivesService extends CrudService<ArchivesDao, Archives> {

    /**
     * 获取单条数据
     *
     * @param archives
     * @return
     */
    @Override
    public Archives get(Archives archives) {
        return super.get(archives);
    }

    /**
     * 查询分页数据
     *
     * @param archives 查询条件
     * @return
     */
    @Override
    public Page<Archives> findPage(Archives archives) {
        return super.findPage(archives);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param archives
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Archives archives) {
        super.save(archives);
    }

    /**
     * 更新状态
     *
     * @param archives
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Archives archives) {
        super.updateStatus(archives);
    }

    /**
     * 删除数据
     *
     * @param archives
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Archives archives) {
        super.delete(archives);
    }

    /**
     * 根据epc获取名字
     *
     * @param epc
     * @return
     */
    public String getNameByEpc(String epc) {
        return super.dao.getNameByEpc(epc);
    }

    /**
     * 获取在库中的epc
     * @param epcs
     * @return
     */
    public List<String> findBatchByEpcs(List<String> epcs){
        return super.dao.findBatchByEpcs(epcs);
    }

    public List<String> findListBySets(Set<EPCTag> sets){
        return null;
    }

}