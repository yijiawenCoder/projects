package com.yijiawen.userSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.yijiawen.userSystem.mapper")
@EnableRedisHttpSession
public class UserSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSystemApplication.class, args);
    }

}
