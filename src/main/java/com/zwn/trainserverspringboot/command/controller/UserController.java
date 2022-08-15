package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.User;
import com.zwn.trainserverspringboot.command.service.AuthServiceImpl;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserUtil;
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
    String register(long userId,String password){
        User user = new User();
        user.setUserId(userId);
        user.setLoginKey(password);
        try{
            return JSON.toJSONString(authService.register(user));
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString()));
        }
    }

    @PostMapping("/login")
    String login(long userId, String loginKey){
        try{
            return JSON.toJSONString(authService.login(String.valueOf(userId), loginKey));
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString()));
        }
    }

    @PostMapping("/logout")
    String logout(String token){
        try{
            return JSON.toJSONString(authService.logout(token));
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString()));
        }
    }

    @PostMapping("/refresh")
    String refresh(String token){
        try{
            return JSON.toJSONString(authService.refresh(token));
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString()));
        }
    }

    @GetMapping("/getUserInfo")
    String getUserInfo(){
        try {
            if (UserUtil.getCurrentUserId() == 0 ){
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
            }else {
                return JSON.toJSONString(authService.getUserInfo(UserUtil.getCurrentUserId()));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
    }
}
