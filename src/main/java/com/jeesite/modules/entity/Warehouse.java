/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * warehouseEntity
 *
 * @author zht
 * @version 2020-12-21
 */
@Table(name = "warehouse", alias = "a", columns = {
        @Column(name = "warehouse_id", attrName = "warehouseId", label = "warehouse_id", isPK = true),
        @Column(name = "reader_ip", attrName = "readerIp", label = "广告机IP地址"),
        @Column(name = "warehouse_name", attrName = "warehouseName", label = "仓库名字", queryType = QueryType.LIKE),
        @Column(name = "mechanism_name", attrName = "mechanismName", label = "机构名称", queryType = QueryType.LIKE),
}, orderBy = "a.warehouse_id DESC"
)
public class Warehouse extends DataEntity<Warehouse> {

    private static final long serialVersionUID = 1L;
    private String warehouseId;        // warehouse_id
    private String readerIp;        // 广告机IP地址
    private String warehouseName;        // 仓库名字
    private String mechanismName;        // 机构名称

    public Warehouse() {
        this(null);
    }

    public Warehouse(String id) {
        super(id);
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Length(min = 0, max = 20, message = "广告机IP地址长度不能超过 20 个字符")
    public String getReaderIp() {
        return readerIp;
    }

    public void setReaderIp(String readerIp) {
        this.readerIp = readerIp;
    }

    @NotBlank(message = "仓库名字不能为空")
    @Length(min = 0, max = 20, message = "仓库名字长度不能超过 20 个字符")
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @NotBlank(message = "机构名称不能为空")
    @Length(min = 0, max = 20, message = "机构名称长度不能超过 20 个字符")
    public String getMechanismName() {
        return mechanismName;
    }

    public void setMechanismName(String mechanismName) {
        this.mechanismName = mechanismName;
    }

}