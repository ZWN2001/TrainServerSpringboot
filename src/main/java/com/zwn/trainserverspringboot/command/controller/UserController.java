package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.User;
import com.zwn.trainserverspringboot.command.service.AuthServiceImpl;
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
    String register(User user){
        return JSON.toJSONString(authService.register(user));
    }

    @PostMapping("/login")
    String login(long userId, String loginKey){
        return JSON.toJSONString(authService.login(String.valueOf(userId), loginKey));
    }
}
