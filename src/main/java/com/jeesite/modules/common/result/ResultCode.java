package com.jeesite.modules.common.result;

/**
 * @author zht
 * @describe 统一响应类
 * @date 2020/12/16
 */
public enum ResultCode {

    /**
     * 成功状态码
     */
    SUCCESS(0, "成功"),
    /**
     * 错误状态码
     */
    ERROR(500, "失败"),
    /**
     * 错误状态码
     */
    LOST(404, "URL不存在"),
    /**
     * 错误状态码
     */
    PARAMETER(400, "入参有误");



    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
