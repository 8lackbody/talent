package com.jeesite.modules.rds.entity;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class EPCTag {

    private Date date;

    private String epc;

    private String name;

    private String status;

    private Integer alert;

    public EPCTag(Date date, String epc, String name, String status, Integer alert) {
        this.date = date;
        this.epc = epc;
        this.name = name;
        this.status = status;
        this.alert = alert;
    }

    public EPCTag(String epc) {
        this.date = new Date();
        this.epc = epc;
        this.status = "未确认";
        this.alert = 0;
    }

    public EPCTag() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EPCTag)) return false;
        EPCTag epcTag = (EPCTag) o;
        return Objects.equals(getEpc(), epcTag.getEpc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEpc());
    }
}
