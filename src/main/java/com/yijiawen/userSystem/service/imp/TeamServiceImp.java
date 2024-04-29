package com.yijiawen.userSystem.service.imp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.mapper.UserTeamMapper;
import com.yijiawen.userSystem.model.dto.TeamQuery;
import com.yijiawen.userSystem.model.entity.Team;
import com.yijiawen.userSystem.model.entity.User;
import com.yijiawen.userSystem.model.entity.UserTeam;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.mapper.TeamMapper;
import com.yijiawen.userSystem.model.vo.TeamListVO;
import com.yijiawen.userSystem.service.TeamService;
import com.yijiawen.userSystem.service.UserService;
import com.yijiawen.userSystem.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TeamServiceImp extends ServiceImpl<TeamMapper, Team>
        implements TeamService {
    @Resource
    UserService userService;
    @Resource
    UserTeamService userTeamService;
    @Resource
    UserTeamMapper userTeamMapper;

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

    @Override
    public List<TeamListVO> listTeams(TeamQuery teamQuery) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            String teamId = teamQuery.getId();
            if (teamId != null) {
                queryWrapper.eq("teamId", teamId);
            }
            if (teamQuery.getSearchText() != null) {
                String searchText = teamQuery.getSearchText();
                queryWrapper.and(qw -> qw.like("teamName", searchText).or().like("description", searchText));
            }
            String teamName = teamQuery.getName();
            if (teamName != null) {
                queryWrapper.like("teamName", teamName);
            }
            String description = teamQuery.getDescription();
            if (description != null) {
                queryWrapper.like("description", description);
            }
            int maxNum = teamQuery.getMaxNum();
            if (maxNum != 0 && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            String userId = teamQuery.getUserId();
            if (userId != null) {
                queryWrapper.eq("userId", userId);
            }
            int teamState = teamQuery.getStatus();
            if (teamState > -1) {
                queryWrapper.eq("teamState", teamState);

            }


        }
        List<Team> list = this.list(queryWrapper);
        //关联查询已加入的用户头像URL
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        //过滤过期的队伍
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));

        List<TeamListVO> teamListVOList = new ArrayList<>();
        for (Team team : list) {
            TeamListVO teamListVO = new TeamListVO();
            BeanUtils.copyProperties(team, teamListVO);
            //todo 查询已加入该队伍的用户头像，并且过滤掉过期的用户，不展示删除的用户
            teamListVO.setJoinUserAvatarURLList(userTeamMapper.joinUserAvatarUrl(team.getTeamId()));
            teamListVOList.add(teamListVO);
        }

        return teamListVOList;
    }
}
