package com.yijiawen.userSystem.service;

import com.yijiawen.userSystem.model.entity.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {
@Resource
    RedisTemplate redisTemplate = new StringRedisTemplate();
    @Test
    void    test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("String","yijiawen");
        valueOperations.set("int",1);
        valueOperations.set("Entity",new User());
       /* valueOperations.get("String");*/



    }
}
