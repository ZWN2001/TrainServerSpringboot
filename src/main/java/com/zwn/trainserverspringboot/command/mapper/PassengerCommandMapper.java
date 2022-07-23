package com.zwn.trainserverspringboot.command.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PassengerCommandMapper {
    void addPassenger(long user_id,String passenger_id,String passenger_name,String phone_num);
}
