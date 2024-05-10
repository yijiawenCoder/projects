package com.yijiawen.userSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController

public class lession {

    @Autowired
    private DataSource dataSource;
    @RequestMapping("lession")
    public String lession(){
        return dataSource.getClass().getName();
    }



}
