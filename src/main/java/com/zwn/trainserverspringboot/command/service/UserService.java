package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import com.zwn.trainserverspringboot.command.mapper.UserMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "CustomUserDetailsService")
@Lazy
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {this.userMapper = userMapper;}

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDetail userDetail = (UserDetail) userMapper.queryUserById(Long.parseLong(userId)).get(0);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", userId));
        }
        return userDetail;
    }

//    public Result register(User user){
//        try{
//            userMapper.register(user.getUserId(), user.getUserName(), user.isGender(), user.getLoginKey(), user.getEmail());
//        }catch (Exception exception){
//            if (exception.getClass().getName().equals("org.springframework.dao.DuplicateKeyException")){
//                return  Result.getResult(ResultCodeEnum.REGISTER_EXIST);
//            }else {
//                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
//            }
//        }
//        return Result.getResult(ResultCodeEnum.SUCCESS);
//    }
//
//    public Result login(long user_id, String login_key){
//        int result;
//        try {
//            result = userMapper.login(user_id, login_key);
//            return result == 1 ? Result.getResult(ResultCodeEnum.SUCCESS) : Result.getResult(ResultCodeEnum.LOGIN_ERROR);
//        }catch (Exception e){
//            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
//        }
//    }
}
