package com.jeesite.modules.common.payload;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

@Data
public class FilePersonNumberPayload {

    @JSONField(name = "err_code")
    Integer errCode;

    @JSONField(name = "err_msg")
    String errMsg;

    Map<String, String> data;

}
