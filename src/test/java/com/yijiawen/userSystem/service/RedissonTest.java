/*package com.yijiawen.userSystem.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {
    @Resource
    RedissonClient redissonClient;
    @Test
     void test(){
        RList<String> list = redissonClient.getList("key");
        list.add("yijiawenCoder");
        System.out.println(list.get(0));

    }
}*/
