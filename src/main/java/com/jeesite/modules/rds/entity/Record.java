/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.rds.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * recordEntity
 *
 * @author zht
 * @version 2020-12-21
 */
@Table(name="record", alias="a", columns={
        @Column(name="record_id", attrName="recordId", label="record_id", isPK=true),
        @Column(name="record_time", attrName="recordTime", label="检测时间"),
        @Column(name="epc", attrName="epc", label="检测到的标签号"),
        @Column(name="name", attrName="name", label="姓名", queryType=QueryType.LIKE),
        @Column(name="warehouse_id", attrName="warehouseId", label="哪个仓库上传的"),
        @Column(name="confirm_status", attrName="confirmStatus", label="确认状态"),
        @Column(name="create_date", attrName="createDate", label="create_date", isUpdate=false, isQuery=false),
        @Column(name="alarm_status", attrName="alarmStatus", label="alarm_status"),
}, orderBy="a.record_id DESC"
)
public class Record extends DataEntity<Record> {

    private static final long serialVersionUID = 1L;
    private String recordId;		// record_id
    private Date recordTime;		// 检测时间
    private String epc;		// 检测到的标签号
    private String name;		// 姓名
    private String warehouseId;		// 哪个仓库上传的
    private Integer confirmStatus;		// 确认状态
    private Integer alarmStatus;		// alarm_status

    public Record() {
        this(null);
    }

    public Record(String id){
        super(id);
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
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

    @NotBlank(message="标签号不能为空")
    @Length(min=0, max=255, message="标签号长度不能超过 255 个字符")
    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    @Length(min=0, max=20, message="姓名长度不能超过 20 个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message="库房不能为空")
    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    @NotNull(message="确认状态不能为空")
    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    @NotNull(message="警报状态不能为空")
    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
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

    public String[] getAlarmStatus_in(){
        return sqlMap.getWhere().getValue("alarm_status", QueryType.IN);
    }

    public void setAlarmStatus_in(String[] codes){
        sqlMap.getWhere().and("alarm_status", QueryType.IN, codes);
    }
}