package com.yijiawen.userSystem.common;

public enum ErrorCode {
    SUCCESS("0", "成功", ""),
    PARAMS_ERRORS("40000", "参数错误", ""),
    PARAMS_NULL_ERRORS("40000", "参数错误", ""),
    NO_AUTH("40101", "未登录", ""),
    SYSTEM_ERROR("50000", "系统错误", ""),
    NO_LOGIN("40100", "未登录", ""),
    NO_PERMISSION("40102", "没有权限", "");

    /**
     * 状态码信息
     */
    private final String code;
    private final String msg;
    private final String description;

    ErrorCode(String code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}
