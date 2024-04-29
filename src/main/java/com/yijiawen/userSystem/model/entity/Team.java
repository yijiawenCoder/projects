package com.yijiawen.userSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {
    /**
     * 队伍id主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String teamId;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 队伍最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date expireTime;

    /**
     * 队伍拥有者id
     */
    private String userId;

    /**
     * 队伍状态 0-‘公开’，1-‘私有’，2-‘加密’
     */
    private Integer teamState;

    /**
     * 加密密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除（1-删除，0-未删除）
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}