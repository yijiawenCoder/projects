package com.yijiawen.userSystem.service;

import com.yijiawen.userSystem.mapper.UserTeamMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

public class avatarTest {
    @Resource
    UserTeamMapper userTeamMapper;
    @Test
    void testAvatar() {
        List<String> ebb97ccb462a6dbc69254fc3380a5fae = userTeamMapper.joinUserAvatarUrl("ebb97ccb462a6dbc69254fc3380a5fae");
        System.out.println(ebb97ccb462a6dbc69254fc3380a5fae.toString());


    }
}
