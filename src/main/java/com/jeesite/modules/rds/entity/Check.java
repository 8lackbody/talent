/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * checkEntity
 * @author zht
 * @version 2021-07-13
 */
@Table(name="check", alias="a", columns={
		@Column(name="check_id", attrName="checkId", label="check_id", isPK=true),
		@Column(name="start_epc", attrName="startEpc", label="start_epc", queryType=QueryType.LEFT_LIKE),
		@Column(name="end_epc", attrName="endEpc", label="end_epc", queryType=QueryType.LEFT_LIKE),
		@Column(name="create_dt", attrName="createDt", label="create_dt"),
		@Column(name="check_data", attrName="checkData", label="check_data", isQuery=false),
		@Column(name="check_status", attrName="checkStatus", label="check_status"),
		@Column(name="unknown_data", attrName="unknownData", label="unknown_data", isQuery=false),
		@Column(name="lack_data", attrName="lackData", label="lack_data", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="remarks", queryType=QueryType.LIKE),
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
	private String unknownData;		// unknown_data
	private String lackData;		// lack_data
	private String remarks;

	@Override
	@ExcelField(title="备注", sort=60)
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

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
	@ExcelField(title="开始标签", sort=1)
	public String getStartEpc() {
		return startEpc;
	}

	public void setStartEpc(String startEpc) {
		this.startEpc = startEpc;
	}
	
	@Length(min=0, max=15, message="标签号长度不能超过 15 个字符")
	@ExcelField(title="结束标签", sort=10)
	public String getEndEpc() {
		return endEpc;
	}

	public void setEndEpc(String endEpc) {
		this.endEpc = endEpc;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="盘库时间", sort=20)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Length(min=0, max=255, message="check_data长度不能超过 255 个字符")
	public String getCheckData() {
		return checkData;
	}

	public void setCheckData(String checkData) {
		this.checkData = checkData;
	}

	@ExcelField(title="盘库情况", sort=30 ,dictType = "check_status")
	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Length(min=0, max=2000, message="unknown_data长度不能超过 2000 个字符")
	@ExcelField(title="未知数据", sort=40)
	public String getUnknownData() {
		return unknownData;
	}

	public void setUnknownData(String unknownData) {
		this.unknownData = unknownData;
	}

	@Length(min=0, max=2000, message="lack_data长度不能超过 2000 个字符")
	@ExcelField(title="缺少数据", sort=50)
	public String getLackData() {
		return lackData;
	}

	public void setLackData(String lackData) {
		this.lackData = lackData;
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