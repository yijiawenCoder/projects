package com.yijiawen.userSystem.controller;

import com.yijiawen.userSystem.common.BaseResponse;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.entity.Team;
import com.yijiawen.userSystem.entity.User;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.service.TeamService;
import com.yijiawen.userSystem.service.UserService;
import com.yijiawen.userSystem.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    TeamService teamService;

    @PostMapping("/addTeam")
    public BaseResponse<String> addTeam(@RequestBody Team team, HttpServletRequest request){
        if(team==null){
            throw new BusinessException(ErrorCode.PARAMS_ERRORS);
        }

        String resId = teamService.addTeam(team, request);
        return ResultUtil.success(resId);
    }
  //  @GetMapping("/search")
}
