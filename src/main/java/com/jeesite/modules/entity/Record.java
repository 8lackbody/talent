/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * recordEntity
 * @author zht
 * @version 2020-12-18
 */
@Table(name="record", alias="a", columns={
		@Column(name="record_id", attrName="recordId", label="record_id", isPK=true),
		@Column(name="record_time", attrName="recordTime", label="检测时间"),
		@Column(name="epc", attrName="epc", label="检测到的标签号"),
		@Column(name="warehouse_id", attrName="warehouseId", label="哪个仓库上传的"),
		@Column(name="confirm_status", attrName="confirmStatus", label="确认状态"),
		@Column(name="create_date", attrName="createDate", label="create_date", isUpdate=false, isQuery=false),
	}, orderBy="a.record_id DESC"
)
public class Record extends DataEntity<Record> {
	
	private static final long serialVersionUID = 1L;
	private Long recordId;		// record_id
	private Date recordTime;		// 检测时间
	private String epc;		// 检测到的标签号
	private Long warehouseId;		// 哪个仓库上传的
	private Integer confirmStatus;		// 确认状态
	
	public Record() {
		this(null);
	}

	public Record(String id){
		super(id);
	}
	
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="检测时间不能为空")
	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	@NotBlank(message="检测到的标签号不能为空")
	@Length(min=0, max=255, message="检测到的标签号长度不能超过 255 个字符")
	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}
	
	@NotNull(message="哪个仓库上传的不能为空")
	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
	@NotNull(message="确认状态不能为空")
	public Integer getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(Integer confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	
	public Date getRecordTime_gte() {
		return sqlMap.getWhere().getValue("record_time", QueryType.GTE);
	}

	public void setRecordTime_gte(Date recordTime) {
		sqlMap.getWhere().and("record_time", QueryType.GTE, recordTime);
	}
	
	public Date getRecordTime_lte() {
		return sqlMap.getWhere().getValue("record_time", QueryType.LTE);
	}

	public void setRecordTime_lte(Date recordTime) {
		sqlMap.getWhere().and("record_time", QueryType.LTE, recordTime);
	}
	
}