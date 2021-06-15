/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * checkEntity
 * @author zht
 * @version 2021-06-15
 */
@Table(name="check", alias="a", columns={
		@Column(name="check_id", attrName="checkId", label="check_id", isPK=true),
		@Column(name="start_epc", attrName="startEpc", label="start_epc", queryType=QueryType.LEFT_LIKE),
		@Column(name="end_epc", attrName="endEpc", label="end_epc", queryType=QueryType.LEFT_LIKE),
		@Column(name="create_dt", attrName="createDt", label="create_dt"),
		@Column(name="check_data", attrName="checkData", label="check_data"),
		@Column(name="check_status", attrName="checkStatus", label="check_status"),
	}, orderBy="a.check_id DESC"
)
public class Check extends DataEntity<Check> {
	
	private static final long serialVersionUID = 1L;
	private String checkId;		// check_id
	private String startEpc;		// start_epc
	private String endEpc;		// end_epc
	private Date createDt;		// create_dt
	private String checkData;		// check_data
	private Integer checkStatus;		// check_status
	
	public Check() {
		this(null);
	}

	public Check(String id){
		super(id);
	}
	
	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	
	@Length(min=0, max=15, message="标签号长度不能超过 15 个字符")
	public String getStartEpc() {
		return startEpc;
	}

	public void setStartEpc(String startEpc) {
		this.startEpc = startEpc;
	}
	
	@Length(min=0, max=15, message="标签号长度不能超过 15 个字符")
	public String getEndEpc() {
		return endEpc;
	}

	public void setEndEpc(String endEpc) {
		this.endEpc = endEpc;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	public String getCheckData() {
		return checkData;
	}

	public void setCheckData(String checkData) {
		this.checkData = checkData;
	}
	
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	public Date getCreateDt_gte() {
		return sqlMap.getWhere().getValue("create_dt", QueryType.GTE);
	}

	public void setCreateDt_gte(Date createDt) {
		sqlMap.getWhere().and("create_dt", QueryType.GTE, createDt);
	}
	
	public Date getCreateDt_lte() {
		return sqlMap.getWhere().getValue("create_dt", QueryType.LTE);
	}

	public void setCreateDt_lte(Date createDt) {
		sqlMap.getWhere().and("create_dt", QueryType.LTE, createDt);
	}
	
}