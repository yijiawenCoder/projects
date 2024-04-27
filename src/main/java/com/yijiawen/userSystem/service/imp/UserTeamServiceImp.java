package com.yijiawen.userSystem.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.entity.User;
import com.yijiawen.userSystem.entity.UserTeam;
import com.yijiawen.userSystem.mapper.UserMapper;
import com.yijiawen.userSystem.mapper.UserTeamMapper;
import com.yijiawen.userSystem.service.UserService;
import com.yijiawen.userSystem.service.UserTeamService;
import org.springframework.stereotype.Service;

@Service
public class UserTeamServiceImp extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {
}
