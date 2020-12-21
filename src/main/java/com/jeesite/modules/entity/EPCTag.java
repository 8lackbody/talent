package com.jeesite.modules.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class EPCTag {

    private LocalDateTime date;

    private String epc;

    private String name;

    private String status;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EPCTag(LocalDateTime date, String epc, String name, String status) {
        this.date = date;
        this.epc = epc;
        this.name = name;
        this.status = status;
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
