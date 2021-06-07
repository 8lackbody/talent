package com.jeesite.modules.common.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zht
 * @describe 统一响应类
 * @date 2020/12/16
 */
@Setter
@Getter
public class ResultVo {

    /**
     * 结果码
     */
    private Integer code;

    /**
     * 结果消息
     */
    private String msg;

    /**
     * 结果数据载体
     */
    private Object data;

    /**
     * 私有构造
     */
    private ResultVo(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    /**
     * 填充数据
     */
    public ResultVo put(Object data) {
        this.data = data;
        return this;
    }

    public static ResultVo ok() {
        return new ResultVo(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static ResultVo ok(String msg) {
        return new ResultVo(ResultCode.SUCCESS.getCode(), msg);
    }

    public static ResultVo fail() {
        return new ResultVo(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage());
    }

    public static ResultVo fail(String msg) {
        return new ResultVo(ResultCode.ERROR.getCode(), msg);
    }

    public static ResultVo fail(int errCode, String msg) {
        return new ResultVo(errCode, msg);
    }

    public static ResultVo fail(ResultCode resultCode) {
        return new ResultVo(resultCode.getCode(), resultCode.getMessage());
    }

    public static ResultVo fail(Exception exception) {
        return new ResultVo(ResultCode.ERROR.getCode(), null != exception ? exception.getMessage() : ResultCode.ERROR.getMessage());
    }

}
