package com.yijiawen.userSystem.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/***
 * 用户注册请求体
 * 与数据库字段保持一致
 *  @author  <a href = "https://github.com/yijiawenCoder">yijiawenCoder </>
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 4111887321618376210L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String userName;
}
