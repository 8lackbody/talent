/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * warehouseEntity
 * @author zht
 * @version 2020-12-24
 */
@Table(name="warehouse", alias="a", columns={
		@Column(name="warehouse_id", attrName="warehouseId", label="warehouse_id", isPK=true),
		@Column(name="reader_ip", attrName="readerIp", label="广告机IP地址"),
		@Column(name="warehouse_name", attrName="warehouseName", label="仓库名字", queryType=QueryType.LIKE),
		@Column(name="mechanism_name", attrName="mechanismName", label="机构名称", queryType=QueryType.LIKE),
		@Column(name="antenna1_enable", attrName="antenna1Enable", label="天线一开关 1开0关"),
		@Column(name="antenna2_enable", attrName="antenna2Enable", label="天线二开关 1开0关"),
		@Column(name="antenna1_power", attrName="antenna1Power", label="天线一功率"),
		@Column(name="antenna2_power", attrName="antenna2Power", label="天线二功率"),
		@Column(name="antenna1_sensitivity", attrName="antenna1Sensitivity", label="天线一灵敏度"),
		@Column(name="antenna2_sensitivity", attrName="antenna2Sensitivity", label="天线二灵敏度"),
	}, orderBy="a.warehouse_id DESC"
)
public class Warehouse extends DataEntity<Warehouse> {
	
	private static final long serialVersionUID = 1L;
	private String warehouseId;		// warehouse_id
	private String readerIp;		// 广告机IP地址
	private String warehouseName;		// 仓库名字
	private String mechanismName;		// 机构名称
	private Integer antenna1Enable;		// 天线一开关 1开0关
	private Integer antenna2Enable;		// 天线二开关 1开0关
	private Double antenna1Power;		// 天线一功率
	private Double antenna2Power;		// 天线二功率
	private Double antenna1Sensitivity;		// 天线一灵敏度
	private Double antenna2Sensitivity;		// 天线二灵敏度
	
	public Warehouse() {
		this(null);
	}

	public Warehouse(String id){
		super(id);
	}
	
	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	@NotBlank(message="广告机IP地址不能为空")
	@Length(min=0, max=20, message="广告机IP地址长度不能超过 20 个字符")
	public String getReaderIp() {
		return readerIp;
	}

	public void setReaderIp(String readerIp) {
		this.readerIp = readerIp;
	}
	
	@NotBlank(message="库房名字不能为空")
	@Length(min=0, max=20, message="库房名字长度不能超过 20 个字符")
	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	@NotBlank(message="机构名称不能为空")
	@Length(min=0, max=20, message="机构名称长度不能超过 20 个字符")
	public String getMechanismName() {
		return mechanismName;
	}

	public void setMechanismName(String mechanismName) {
		this.mechanismName = mechanismName;
	}
	
	@NotNull(message="天线一开关 1开0关不能为空")
	public Integer getAntenna1Enable() {
		return antenna1Enable;
	}

	public void setAntenna1Enable(Integer antenna1Enable) {
		this.antenna1Enable = antenna1Enable;
	}
	
	@NotNull(message="天线二开关 1开0关不能为空")
	public Integer getAntenna2Enable() {
		return antenna2Enable;
	}

	public void setAntenna2Enable(Integer antenna2Enable) {
		this.antenna2Enable = antenna2Enable;
	}
	
	@NotNull(message="天线一功率不能为空")
	public Double getAntenna1Power() {
		return antenna1Power;
	}

	public void setAntenna1Power(Double antenna1Power) {
		this.antenna1Power = antenna1Power;
	}
	
	@NotNull(message="天线二功率不能为空")
	public Double getAntenna2Power() {
		return antenna2Power;
	}

	public void setAntenna2Power(Double antenna2Power) {
		this.antenna2Power = antenna2Power;
	}
	
	@NotNull(message="天线一灵敏度不能为空")
	public Double getAntenna1Sensitivity() {
		return antenna1Sensitivity;
	}

	public void setAntenna1Sensitivity(Double antenna1Sensitivity) {
		this.antenna1Sensitivity = antenna1Sensitivity;
	}
	
	@NotNull(message="天线二灵敏度不能为空")
	public Double getAntenna2Sensitivity() {
		return antenna2Sensitivity;
	}

	public void setAntenna2Sensitivity(Double antenna2Sensitivity) {
		this.antenna2Sensitivity = antenna2Sensitivity;
	}
	
}