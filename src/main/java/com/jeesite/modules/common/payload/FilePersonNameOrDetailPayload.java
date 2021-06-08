package com.jeesite.modules.common.payload;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FilePersonNameOrDetailPayload {

    @JSONField(name = "err_code")
    Integer errCode;

    @JSONField(name = "err_msg")
    String errMsg;

    List<Map<String, String>> data;

}
