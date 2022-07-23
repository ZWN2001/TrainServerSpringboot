package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.mapper.UserMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public Result register(long user_id, String user_name, boolean gender, String login_key, String email){
        try{
            userMapper.register(user_id, user_name, gender, login_key, email);
        }catch (Exception exception){
            if (exception.getClass().getName().equals("org.springframework.dao.DuplicateKeyException")){
                return  Result.getResult(ResultCodeEnum.REGISTER_EXIST);
            }else {
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
        return Result.getResult(ResultCodeEnum.SUCCESS);
    }

    public Result login(long user_id, String login_key){
        int result;
        try {
            result = userMapper.login(user_id, login_key);
            return result == 1 ? Result.getResult(ResultCodeEnum.SUCCESS) : Result.getResult(ResultCodeEnum.LOGIN_ERROR);
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
        }
    }
}
