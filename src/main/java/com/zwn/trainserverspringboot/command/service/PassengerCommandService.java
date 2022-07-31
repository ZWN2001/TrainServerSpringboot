package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.mapper.PassengerCommandMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class PassengerCommandService {
    @Resource
    private PassengerCommandMapper passengerCommandMapper;

    public Result addPassenger(Passenger passenger) {
        if (passenger.isLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()) {
            try {
                    passengerCommandMapper.addPassenger(passenger.getUserId(), passenger.getPassengerId(),
                            passenger.getPassengerName(), passenger.getPhoneNum());
                } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    return Result.getResult(ResultCodeEnum.PASSENGER_USER_NOT_EXIST);
                } else if (cause instanceof DuplicateKeyException) {
                    return Result.getResult(ResultCodeEnum.PASSENGER_EXIST);
                } else {
                    return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
            return Result.getResult(ResultCodeEnum.SUCCESS);
        } else {
            return Result.getResult(passenger.isLegal());
        }
    }


}
