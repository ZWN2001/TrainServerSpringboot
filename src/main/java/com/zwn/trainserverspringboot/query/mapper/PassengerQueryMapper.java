package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.command.bean.Passenger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PassengerQueryMapper {
    List<Passenger> queryAllPassengers(long userId);
    Passenger querySinglePassenger(long userId, String passengerId);
}
