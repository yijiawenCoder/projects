package com.yijiawen.userSystem.service.imp;

import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.entity.Team;
import com.yijiawen.userSystem.entity.User;
import com.yijiawen.userSystem.entity.UserTeam;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.mapper.TeamMapper;
import com.yijiawen.userSystem.mapper.UserMapper;
import com.yijiawen.userSystem.mapper.UserTeamMapper;
import com.yijiawen.userSystem.service.TeamService;
import com.yijiawen.userSystem.service.UserService;
import com.yijiawen.userSystem.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImp extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    UserService userService;
    @Resource
    UserTeamService userTeamService;

    @Override
    public String addTeam(Team team, HttpServletRequest request) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);

        }
        User loginUser = userService.getLoginUser(request);

        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);

        }
      /*  if (team.getMaxNum() < 1 || team.getMaxNum() > 20 || team.getDescription().length() >= 512 || team.getTeamName().length() >= 32 || StringUtils.isNotBlank(team.getTeamName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }*/
        if (team.getMaxNum() < 1 || team.getMaxNum() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        if (team.getTeamName().length() >= 32) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }


        if (team.getTeamState() == 2 && (team.getPassword() == null || team.getPassword().length() >= 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        // 6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "超时时间 > 当前时间");
        }

        if (loginUser.getHasTeamNum() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }


        //todo 增加条件
        team.setUserId(loginUser.getUserId());
        team.setCreateTime(new Timestamp(new java.util.Date().getTime()));
        team.setUpdateTime(new Timestamp(new java.util.Date().getTime()));
        team.setIsDelete(0);
        boolean save = save(team);

        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(loginUser.getUserId());
        userTeam.setTeamId(team.getTeamId());
        userTeam.setJoinTime(new Timestamp(new java.util.Date().getTime()));
        userTeam.setCreateTime(new Timestamp(new java.util.Date().getTime()));
        userTeam.setUpdateTime(new Timestamp(new java.util.Date().getTime()));
        userTeam.setIsDelete(0);
        boolean saveUserTeam = userTeamService.save(userTeam);
        if (save && saveUserTeam) {
            return team.getTeamName();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }


    }
}
