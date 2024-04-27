package com.yijiawen.userSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yijiawen.userSystem.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


/**
 * @author 26510
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-04-22 17:15:01
 */
public interface UserService extends IService<User> {
    String register(String userAccount, String userPassword, String checkPassword, String userName);

    String login(String userAccount, String userPassword, HttpServletRequest request);

    User getCurrentUser(HttpServletRequest request);

    boolean deleteById(String userId, HttpServletRequest request);

    boolean update(User user, HttpServletRequest request);

    User getLoginUser (HttpServletRequest request);
    Page<User> getRecommendUsers(long pageSize, long pageNum, HttpServletRequest request);


}
