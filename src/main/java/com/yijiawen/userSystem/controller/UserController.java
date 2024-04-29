package com.yijiawen.userSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yijiawen.userSystem.common.BaseResponse;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.model.entity.User;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.model.dto.request.UserLoginRequest;
import com.yijiawen.userSystem.model.dto.request.UserRegisterRequest;
import com.yijiawen.userSystem.service.UserService;
import com.yijiawen.userSystem.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    /***
     * @author  <a href = "https://github.com/yijiawenCoder">yijiawenCoder </>
     *
     */
    @Resource
    private UserService userService;

    /***
     * 注册接口
     * @param userRegisterRequest  用户注册请求体
     * @return 被注册后的用户的唯一Id
     */
    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userName = userRegisterRequest.getUserName();
        String userId = userService.register(userAccount, userPassword, checkPassword, userName);
        return ResultUtil.success(userId, "注册成功");
    }

    /***
     * 登录接口
     * @param userLoginRequest 用户登录请求体
     * @param request  存放session到redis
     * @return 用户登录后的唯一Id

     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String userId = userService.login(userAccount, userPassword, request);
        return ResultUtil.success(userId, "登录成功");
    }

    /***
     *  获取当前登录的用户
     * @param request 取出存放在redis里面的session
     * @return 用户所有信息
     */
    @GetMapping("/getCurrentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        return ResultUtil.success(userService.getCurrentUser(request));
    }

    /***
     *  根据用户id逻辑删除
     * @param userId 用户id
     * @param request 只能自己或管理员注销
     * @return 是否成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(String userId, HttpServletRequest request) {
        // 检查用户ID是否为空
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "用户ID不能为空");
        }

        return ResultUtil.success(userService.deleteById(userId, request));
    }

    /***
     *
     * @param user 更改的信息
     * @param request  鉴权
     * @return 是否成功
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserMessage(User user, HttpServletRequest request) {

        //1.校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }

        //3.触发业务
        boolean isSuccess = userService.update(user, request);
        return ResultUtil.success(isSuccess);

    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize,long pageNum, HttpServletRequest request) {

        Page<User> recommendUsers = userService.getRecommendUsers(pageSize, pageNum, request);
        return ResultUtil.success(recommendUsers);
    }
    //TODO  根据前端页面查询所有用户


}
