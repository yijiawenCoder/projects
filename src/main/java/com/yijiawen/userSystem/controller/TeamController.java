package com.yijiawen.userSystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yijiawen.userSystem.common.BaseResponse;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.model.dto.TeamQuery;
import com.yijiawen.userSystem.model.dto.request.TeamDeleteRequest;
import com.yijiawen.userSystem.model.dto.request.TeamJoinRequest;
import com.yijiawen.userSystem.model.dto.request.TeamQuitRequest;
import com.yijiawen.userSystem.model.dto.request.TeamUpdateRequest;
import com.yijiawen.userSystem.model.entity.Team;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.model.vo.TeamListVO;
import com.yijiawen.userSystem.service.TeamService;
import com.yijiawen.userSystem.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    TeamService teamService;

    @PostMapping("/createTeam")
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

    /***
     * 前端根据dto按需传入要修改的内容
     * @param teamUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/updateTeam")
    public BaseResponse<String> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        String resId = teamService.updateTeam(teamUpdateRequest, request);


        return ResultUtil.success(resId);
    }

    @PostMapping("/joinTeam")
    private BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        boolean res = teamService.joinTeam(teamJoinRequest, request);

        return ResultUtil.success(res);


    }

    @PostMapping("/quitTeam")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        return ResultUtil.success(teamService.quitTeam(teamQuitRequest, request));
    }
    @PostMapping("/deleteTeam")
    public BaseResponse<Boolean> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        if(teamDeleteRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }
        boolean deleteTeam = teamService.deleteTeam(teamDeleteRequest, request);
        return ResultUtil.success(deleteTeam);

    }

}
