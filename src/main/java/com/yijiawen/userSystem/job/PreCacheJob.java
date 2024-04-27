package com.yijiawen.userSystem.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yijiawen.userSystem.entity.User;
import com.yijiawen.userSystem.mapper.UserMapper;
import com.yijiawen.userSystem.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;



@Component
@Slf4j
public class PreCacheJob {
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource
    UserService userService;





    @Scheduled(cron = "0 59 23 * * *")
    public void doPreCacheJob() {
        QueryWrapper  queryWrapper = new QueryWrapper();
        String userRedisKey = String.format("userSystem_user_recommend_%s", "9e3eaf16580b4161c82ada44307e6fc6");
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Page<User> userPage =userService.page(new Page<>(1, 10), queryWrapper);
        try {
            valueOperations.set(userRedisKey, userPage,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e){
            log.error("redis insert error");
        }

    }
}
