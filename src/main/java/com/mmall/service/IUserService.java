package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @program: mmall
 * @description:处理用户接口
 * @author: snb
 * @create: 2019-01-23 11:25
 */
public interface IUserService {

    //用户登陆
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse<String> register(User user);

    //校验email和用户名是否存在，防止恶意用户通过接口调用注册接口
    ServerResponse<String> checkValid(String str, String type);

    //忘记密码，返回密码提示问题
    ServerResponse selectQuestion(String username);

    //校验密码是否正确
    ServerResponse<String> checkAnswer(String username,String question,String answer);

    //修改密码
    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    //登陆状态下，重置密码
    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    //登录状态更新个人信息
    ServerResponse<User> updateInformation(User user);

    //获取用户的详细信息
    ServerResponse<User> getInformation(Integer userId);

    //校验是否是管理员
    ServerResponse checkAdminRole(User user);
}
