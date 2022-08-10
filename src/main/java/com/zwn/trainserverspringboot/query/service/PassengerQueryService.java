package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.query.mapper.PassengerQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PassengerQueryService {

    @Resource
    private PassengerQueryMapper passengerQueryMapper;

    public Result queryAllPassengers(long userId){
        List<Passenger> passengerDetails = passengerQueryMapper.queryAllPassengers(userId);
        return Result.getResult(ResultCodeEnum.SUCCESS,passengerDetails);
    }

    public Result querySinglePassenger(long userId, String passengerId){
        Passenger passenger = passengerQueryMapper.querySinglePassenger(userId, passengerId);
        return Result.getResult(ResultCodeEnum.SUCCESS,passenger);
    }

}
