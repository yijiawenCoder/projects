package com.yijiawen.userSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yijiawen.userSystem.model.entity.UserTeam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTeamMapper extends BaseMapper<UserTeam> {
    @Select({ "select u.avatar from user u\n" +
            "left join user_team ut on teamId = #{teamId} and u.userId = ut.userId"})
    List<String> joinUserAvatarUrl(@Param("teamId")String teamId);
}
