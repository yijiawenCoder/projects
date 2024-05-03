package com.yijiawen.userSystem.model.dto;

import lombok.Data;

import java.util.List;
@Data

public class TeamQuery {
    private String id;

    /**
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private int maxNum;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private int status;
}
