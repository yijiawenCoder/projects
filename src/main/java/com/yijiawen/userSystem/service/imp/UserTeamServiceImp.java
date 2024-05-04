package com.yijiawen.userSystem.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.model.entity.UserTeam;
import com.yijiawen.userSystem.mapper.UserTeamMapper;
import com.yijiawen.userSystem.service.UserTeamService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserTeamServiceImp extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {
    @Resource
    UserTeamMapper userTeamMapper;

    @Override
    public long getCurrentTeamJoinCount(String teamId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        long count = this.count(queryWrapper);
        return count;
    }
}
