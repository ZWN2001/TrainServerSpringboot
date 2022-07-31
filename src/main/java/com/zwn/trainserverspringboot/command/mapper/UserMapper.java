package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.UserDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void register(long user_id, String user_name, boolean gender, String login_key, String email);

    int login(long user_id, String login_key);

    List<UserDetail> queryUserById(long user_id);

}
