package com.yijiawen.userSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户队伍关系表
 * @TableName user_team
 */
@TableName(value ="user_team")
@Data
public class UserTeam implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String teamId;

    /**
     * 
     */
    private Date joinTime;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}