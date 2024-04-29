package com.yijiawen.userSystem.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
@Data
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9039566319409944234L;
    /**
     * 用户唯一标识符
     */

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


}
