package com.yijiawen.userSystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yijiawen.userSystem.model.entity.UserTeam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTeamMapper extends BaseMapper<UserTeam> {
    @Select({ "select u.avatar from user_team ut\n" +
            "left join user u on u.userId=ut.userId and ut.isDelete=1\n" +
            "where teamId=#{teamId}" })
    List<String> joinUserAvatarUrl(@Param("teamId")String teamId);
}
