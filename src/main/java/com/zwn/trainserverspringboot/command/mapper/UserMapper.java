package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.User;
import com.zwn.trainserverspringboot.command.bean.UserDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void register(User user);

    int login(long user_id, String login_key);

    List<UserDetail> queryUserById(long user_id);

}
