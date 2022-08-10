package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.Passenger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PassengerCommandMapper {
    void addPassenger(Passenger passenger);

    void modifyPassenger(Passenger passenger);

    void deletePassenger(Passenger passenger);

}
