package com.yijiawen.userSystem.service.imp;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.mapper.UserTeamMapper;
import com.yijiawen.userSystem.model.dto.TeamQuery;
import com.yijiawen.userSystem.model.dto.request.TeamDeleteRequest;
import com.yijiawen.userSystem.model.dto.request.TeamJoinRequest;
import com.yijiawen.userSystem.model.dto.request.TeamQuitRequest;
import com.yijiawen.userSystem.model.dto.request.TeamUpdateRequest;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    //todo 并发请求会出现问题 待优化
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
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getUserId());
        long count = userTeamService.count(queryWrapper);
        if (count > 5) {
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

    //todo 并发请求会出现问题 待优化
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
            try {
                BeanUtils.copyProperties(team, teamListVO);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            //todo 测试查询已加入该队伍的用户头像，并且过滤掉过期的用户，不展示删除的用户
            teamListVO.setJoinUserAvatarURLList(userTeamMapper.joinUserAvatarUrl(team.getTeamId()));
            teamListVOList.add(teamListVO);
        }

        return teamListVOList;
    }

    /***
     * 根据前端传来的队伍id来进行修改，前端必须传来队伍id
     * @param teamUpdateRequest
     * @param request
     * @return 被修改的队伍id
     */
    //todo 并发请求会出现问题 待优化
    @Override
    public String updateTeam(TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        if (teamUpdateRequest.getTeamId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        String oldTeamId = teamUpdateRequest.getTeamId();
        if (oldTeamId == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        Team oldTeam = this.getById(oldTeamId);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        if (userService.getLoginUser(request).getUserId().equals(oldTeam.getUserId()) && userService.isAdmin(request)) {
            //可以修改
            try {
                BeanUtils.copyProperties(oldTeam, teamUpdateRequest);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            this.updateById(oldTeam);
            return oldTeamId;

        } else {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }


    }

    //todo 并发请求会出现问题 待优化
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
//参数不能为空
        if (teamJoinRequest == null && teamJoinRequest.getTeamId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        String teamId = teamJoinRequest.getTeamId();

        Team team = this.getById(teamId);
        //判断队伍是否存在
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        //判断用户不能超加
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getUserId());
        long count = userTeamService.count(queryWrapper);
        if (count > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "加入队伍失败，每个用户最多加入5个队伍");
        }
        //不能加入过期的队伍
        if (team.getExpireTime() != null && team.getExpireTime().before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "加入队伍失败，队伍已过期");
        }
        //判断队伍是否已满
        if (team.getMaxNum() == userTeamService.getCurrentTeamJoinCount(teamId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "加入队伍失败，队伍已满");
        }
        //判断用户是否已经加入过该队伍
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", loginUser.getUserId());
        userTeamQueryWrapper.eq("teamId", teamId);
        long userTeamCount = userTeamService.count(userTeamQueryWrapper);
        if (userTeamCount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        //判断队伍状态
        Integer teamState = team.getTeamState();
        if (teamState == 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "无权限加入该队伍");
        } else if (teamState == 2) {
            if (teamJoinRequest.getPassword() == null || !teamJoinRequest.getPassword().equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERRORS, "密码错误");
            }
        }
        //新增关联表
        UserTeam userTeam = new UserTeam();

        userTeam.setUserId(loginUser.getUserId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Timestamp(new java.util.Date().getTime()));
        userTeam.setCreateTime(new Timestamp(new java.util.Date().getTime()));
        userTeam.setUpdateTime(new Timestamp(new java.util.Date().getTime()));


        return userTeamService.save(userTeam);
    }

    @Override
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }
        User loginUser = userService.getLoginUser(request);
        Team team = this.getById(teamQuitRequest.getTeamId());
        if (team == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍不存在");
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getUserId());
        queryWrapper.eq("teamId", team.getTeamId());
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没加入");
        }
        if (userTeamService.getCurrentTeamJoinCount(teamQuitRequest.getTeamId()) == 1) {
            //删除队伍以及关系
            this.removeById(team.getTeamId());
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            queryWrapper.eq("teamId", team.getTeamId());
            //删除所有的user_team关系
            userTeamService.remove(userTeamQueryWrapper);
        } else {
            if (team.getUserId().equals(loginUser.getUserId())) {
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                queryWrapper.eq("teamId", team.getTeamId());
                userTeamQueryWrapper.last("order by joinTime  asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() < 2) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam userTeam = userTeamList.get(1);
                //更新新队长id
                Team updateTeam = new Team();
                updateTeam.setTeamId(userTeam.getUserId());
                if (!this.updateById(updateTeam)) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                //删除原队长关系
                QueryWrapper<UserTeam> teamLeaderQueryWrapper = new QueryWrapper<>();
                teamLeaderQueryWrapper.eq("teamId", team.getTeamId());
                teamLeaderQueryWrapper.eq("userId", loginUser.getUserId());
                return userTeamService.remove(teamLeaderQueryWrapper);
            } else {
                QueryWrapper<UserTeam> teamLeaderQueryWrapper = new QueryWrapper<>();
                teamLeaderQueryWrapper.eq("teamId", team.getTeamId());
                teamLeaderQueryWrapper.eq("userId", loginUser.getUserId());
                return userTeamService.remove(teamLeaderQueryWrapper);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public boolean deleteTeam(TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        if (teamDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        Team team = this.getById(teamDeleteRequest.getTeamId());
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        User loginUser = userService.getLoginUser(request);
        if (!team.getUserId().equals(loginUser.getUserId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "无权限删除");
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", team.getTeamId());
        boolean res = userTeamService.remove(queryWrapper);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return this.removeById(team.getTeamId());
    }
}
