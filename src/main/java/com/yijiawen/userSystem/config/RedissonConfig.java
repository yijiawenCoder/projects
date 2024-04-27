package com.yijiawen.userSystem.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@ConfigurationProperties(prefix = "spring.datasource.redis")
@Data
public class RedissonConfig {
    private String host;
    private String port;


    public RedissonClient RedissonClient() {
        // 1. Create config object
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(0);

        // 2. Create Redisson instance创建实例


        RedissonClient redisson = Redisson.create(config);

        return redisson;


    }
}
