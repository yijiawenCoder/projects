package com.yijiawen.userSystem.exception;


import com.yijiawen.userSystem.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{
     private final String code;

     private final  String description ;

    public BusinessException( String message,String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.description =description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
