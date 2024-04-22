package com.yijiawen.userSystem.utils;


import com.yijiawen.userSystem.common.BaseResponse;
import com.yijiawen.userSystem.common.ErrorCode;

/**
 * 返回工具类
 */
public class ResultUtil {
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {

        return new BaseResponse<>("0", "ok", data,"");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static  BaseResponse error(ErrorCode errorCode) {

        return new BaseResponse<>(errorCode.getCode(), errorCode.getMsg(), null,errorCode.getDescription());
    }

    public static  BaseResponse error(ErrorCode errorCode,String message,String description) {

        return new BaseResponse<>(errorCode.getCode(), message, null,description);
    }

    public static  BaseResponse error(ErrorCode errorCode,String description) {

        return new BaseResponse<>(errorCode.getCode(), errorCode.getMsg(), null,description);
    }

    public static  BaseResponse error(String code,String message,String description) {

        return new BaseResponse<>(code, message,description);
    }

}
