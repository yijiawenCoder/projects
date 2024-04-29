package com.yijiawen.userSystem.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data

public class TeamListVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5130765802788290602L;
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

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /***
     * 展示已经加入的用户头像
     */

    List<String> joinUserAvatarURLList;


}
