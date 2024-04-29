package com.yijiawen.userSystem.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1805012292704478280L;


    private  String userAccount;
    private String userPassword;

}
