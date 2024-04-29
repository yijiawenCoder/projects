package com.yijiawen.userSystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yijiawen.userSystem.common.BaseResponse;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.model.dto.TeamQuery;
import com.yijiawen.userSystem.model.entity.Team;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.model.vo.TeamListVO;
import com.yijiawen.userSystem.service.TeamService;
import com.yijiawen.userSystem.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    TeamService teamService;

    @PostMapping("/addTeam")
    public BaseResponse<String> addTeam(@RequestBody Team team, HttpServletRequest request) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }

        String resId = teamService.addTeam(team, request);
        return ResultUtil.success(resId);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamListVO>> teamList(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }

        List<TeamListVO> list = teamService.listTeams(teamQuery);
        return ResultUtil.success(list);

    }

}
