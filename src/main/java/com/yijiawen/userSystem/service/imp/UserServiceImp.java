package com.yijiawen.userSystem.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yijiawen.userSystem.common.ErrorCode;
import com.yijiawen.userSystem.entity.User;
import com.yijiawen.userSystem.exception.BusinessException;
import com.yijiawen.userSystem.mapper.UserMapper;
import com.yijiawen.userSystem.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yijiawen.userSystem.constant.UserConstant.Admin_Role;
import static com.yijiawen.userSystem.constant.UserConstant.USER_LOGIN_STATE;

/***
 * 业务实现
 * @author  <a href = "https://github.com/yijiawenCoder">yijiawenCoder </>
 *
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /***
     * 加密器
     * @param passwordEncoder 加密工具
     */
    public UserServiceImp(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /***
     * 用户注册业务
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 验证密码
     * @param userName 用户昵称
     * @return 用户的Id
     * @author  <a href = "https://github.com/yijiawenCoder">yijiawenCoder </>
     */


    @Override
    public String register(String userAccount, String userPassword, String checkPassword, String userName) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "用户密码过短");
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "账号不能包含特殊字符");

        }
        //账号具有唯一性
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "账号已存在");

        }

        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "两次输入的密码不一致");
        }
        // 2. 加密
        String password = passwordEncoder.encode(userPassword);
        User newUser = new User();
        newUser.setUserAccount(userAccount);
        newUser.setUserPassword(password);
        newUser.setUserName(userName);
        newUser.setCreateTime(new Timestamp(new java.util.Date().getTime()));
        boolean result = this.save(newUser);
        if (result == false) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，请重试");
        }
        return newUser.getUserId();

    }

    /***
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request   把session存放在redis里面
     * @return 用户id
     */
    public String login(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "密码错误");
        }


        //账户不能包含特殊字符
        String validPatter = "^[a-zA-Z0-9]{6,20}$";
        Matcher matcher = Pattern.compile(validPatter).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "账号有特殊字符");

        }
        //查询账户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        User loginUser = userMapper.selectOne(queryWrapper);
        if (loginUser.equals(null)) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "用户不存在");
        }
        if (!passwordEncoder.matches(userPassword, loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "账号密码不匹配");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, loginUser);
        return loginUser.getUserId();


    }

    /***
     * 获取当前登录用户的session
     * @param request
     * @return
     */

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "未登录");
        }
        User CurrentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return CurrentUser;

    }

    /***
     *
     * @param userId  根据用户唯一Id，做逻辑删除
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String userId, HttpServletRequest request) {
        //鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "无权限");
        }
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userId != loginUser.getUserId()) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "无权限");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERRORS, "用户不存在");

        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("userId", userId);

        updateWrapper.set("userState", 1);
        int update = userMapper.update(null, updateWrapper);
        if (update == 0) {
            return false;
        }

        return true;
    }

    /***
     *
     * @param user  已经更改的用户信息
     * @param request  获得登录的用户
     * @return 是否成功
     */
    @Override
    public boolean update(User user, HttpServletRequest request) {

        //鉴权
        if (getLoginUser(request).getUserId() != user.getUserId() && !isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION, "无权限");
        }

        int row = userMapper.updateById(user);
        //TODO 返回失败修改


        return true;

    }

    /***
     * 获得当前登录的用户信息
     * @param request 通过session获得当前登录的用户用户
     * @return 返回已经登录的用户信息
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "用户未登录");
        }

        return (User) request.getSession().getAttribute(USER_LOGIN_STATE);
    }

    /***
     *
     * @param request 根据登录用户session统一鉴权
     * @return true管理员 false 普通用户
     */
    public boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if (user == null || user.getUserRole() != Admin_Role) {
            return false;
        }
        return true;
    }

    /***
     *
     * @param loginUser    根据用户鉴权
     * @return 管理员 false 普通用户
     */
    public boolean isAdmin(User loginUser) {
        if (loginUser == null || loginUser.getUserRole() != Admin_Role) {
            return false;
        }
        return true;
    }
}
