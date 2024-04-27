package com.yijiawen.userSystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户唯一标识符
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 登陆账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 性别--0=‘女’--1=‘男’
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态--0=正常
     */
    private Integer userState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     * 使用@TableLogic标识逻辑删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 0==普通用户；1==管理员
     */
    private Integer userRole;

    /**
     * 用户标签
     */
    private String tags;

    /**
     * 个人描述
     */
    private String profile;

    private int hasTeamNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}