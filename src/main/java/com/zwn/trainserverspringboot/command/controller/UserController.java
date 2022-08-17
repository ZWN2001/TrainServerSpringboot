package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.User;
import com.zwn.trainserverspringboot.command.service.AuthServiceImpl;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private AuthServiceImpl authService;

    @PostMapping("/register")
    Result register(long userId,String password){
        User user = new User();
        user.setUserId(userId);
        user.setLoginKey(password);
        try{
            return authService.register(user);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    @PostMapping("/login")
    Result login(long userId, String loginKey){
        try{
            return authService.login(String.valueOf(userId), loginKey);
        }catch (Exception e){
            e.printStackTrace();
            Throwable cause = e.getCause();
            if(cause instanceof InternalAuthenticationServiceException){
                return Result.getResult(ResultCodeEnum.LOGIN_ERROR);
            }
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    @PostMapping("/logout")
    Result logout(String token){
        try{
            return authService.logout(token);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    @PostMapping("/refresh")
    Result refresh(String token){
        try{
            return authService.refresh(token);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    @GetMapping("/getUserInfo")
    Result getUserInfo(){
        try {
            if (UserUtil.getCurrentUserId() == 0 ){
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }else {
                return authService.getUserInfo(UserUtil.getCurrentUserId());
            }
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }
}
