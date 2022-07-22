package com.zwn.dbtext.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.dbtext.command.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    String register(long user_id, String user_name, boolean gender, String login_key, String email){
        return JSON.toJSONString(userService.register(user_id, user_name, gender, login_key, email));
    }

    @PostMapping("/login")
    String login(long user_id, String login_key){
        return JSON.toJSONString(userService.login(user_id, login_key));
    }
}
