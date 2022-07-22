package com.zwn.dbtext.command.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void register(long user_id, String user_name, boolean gender, String login_key, String email);

    int login(long user_id, String login_key);

}
