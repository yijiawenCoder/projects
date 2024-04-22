package com.yijiawen.userSystem.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private String code;
    private T Data;
    private String message;
    private String description ;

    // 构造函数
    public BaseResponse(String code, String message, T data, String description) {
        this.code = code;
        this.message = message;
        this.Data = data;
        this.description = description;
    }

    public BaseResponse(String code, T data,String message) {
        this(code,message,data,"");

    }

    public BaseResponse(String code, T data) {
        this(code,"",data,"");

    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMsg(),null,errorCode.getDescription());
    }


}
