package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import com.zwn.trainserverspringboot.command.mapper.UserMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component(value = "CustomUserDetailsService")
@Lazy
public class UserService implements UserDetailsService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {this.userMapper = userMapper;}

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDetail userDetail = userMapper.queryUserById(Long.parseLong(userId)).get(0);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", userId));
        }
        return userDetail;
    }
}
