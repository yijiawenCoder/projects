package com.yijiawen.userSystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yijiawen.userSystem.model.dto.TeamQuery;
import com.yijiawen.userSystem.model.entity.Team;
import com.yijiawen.userSystem.model.vo.TeamListVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TeamService extends IService<Team> {
    String addTeam(Team team, HttpServletRequest request);

    /***
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<TeamListVO> listTeams(TeamQuery teamQuery);


}
