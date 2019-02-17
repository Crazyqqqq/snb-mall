package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description:前台用户的管理类
 * @author: snb
 * @create: 2019-01-23 11:00
 */
@Controller
@RequestMapping("/user/")
public class UserController{
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response=iUserService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出成功");
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验email和用户名是否存在，防止恶意用户通过接口调用注册接口
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取登陆用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    /**
     * 忘记密码
     * @param username
     * @return 密码提示问题
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验问题答案是否正确
     * 使用本地缓存检查问题答案
     * @param username
     * @param question
     * @param answer
     * @return forgetToken 几乎不可重复的字符串
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码中的重置密码。重置密码的时候，需要携带校验问题答案正确后产生的forgetToken
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登陆状态的重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 登录状态更新个人信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);//防止越权
        if(currentUser==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 获取用户的详细信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody//返回值自动序列化成json
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);//防止越权
        if(currentUser==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登陆status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }





}
