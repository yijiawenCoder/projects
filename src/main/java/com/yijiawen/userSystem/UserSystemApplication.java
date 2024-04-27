package com.yijiawen.userSystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.yijiawen.userSystem.mapper")
@EnableScheduling
public class UserSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSystemApplication.class, args);
    }

}
