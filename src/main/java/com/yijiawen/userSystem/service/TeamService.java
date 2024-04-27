package com.yijiawen.userSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yijiawen.userSystem.entity.Team;
import com.yijiawen.userSystem.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface TeamService extends IService<Team> {
    String addTeam(Team team, HttpServletRequest request);
}
