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

/**
 * archivesEntity
 *
 * @author zht
 * @version 2020-12-21
 */
@Table(name = "archives", alias = "a", columns = {
        @Column(name = "archives_id", attrName = "archivesId", label = "archives_id", isPK = true),
        @Column(name = "epc", attrName = "epc", label = "电子标签号"),
        @Column(name = "name", attrName = "name", label = "姓名", queryType = QueryType.LIKE),
        @Column(name = "card_id", attrName = "cardId", label = "身份证号"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.update_date DESC"
)
public class Archives extends DataEntity<Archives> {

    private static final long serialVersionUID = 1L;
    private String archivesId;        // archives_id
    private String epc;        // 电子标签号
    private String name;        // 姓名
    private String cardId;        // 身份证号

    public Archives() {
        this(null);
    }

    public Archives(String id) {
        super(id);
    }

    public String getArchivesId() {
        return archivesId;
    }

    public void setArchivesId(String archivesId) {
        this.archivesId = archivesId;
    }

    @NotBlank(message = "标签号不能为空")
    @Length(min = 0, max = 20, message = "标签号长度不能超过 20 个字符")
    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    @Length(min = 0, max = 20, message = "姓名长度不能超过 20 个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 50, message = "身份证号长度不能超过 50 个字符")
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

}