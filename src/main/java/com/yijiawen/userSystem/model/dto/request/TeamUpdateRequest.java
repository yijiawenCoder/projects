package com.yijiawen.userSystem.model.dto.request;

import lombok.Data;

import java.util.Date;
@Data
public class TeamUpdateRequest {
    private String teamId;


    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 描述
     */
    private String description;


    private Date expireTime;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private int status;

    private String password;
}
