package com.jeesite.modules.common.form;

import lombok.Data;

import java.util.List;

@Data
public class InventoryCheckForm {

    private String startEpc;

    private String endEpc;

    private List<String> found;

}
