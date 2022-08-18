package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.mapper.PassengerCommandMapper;
import com.zwn.trainserverspringboot.query.mapper.PassengerQueryMapper;
import com.zwn.trainserverspringboot.util.Generate.PassengerIdGenerateUtil;
import com.zwn.trainserverspringboot.util.Generate.PassengerNameGenerateUtil;
import com.zwn.trainserverspringboot.util.Generate.PassengerPhoneNumGenerateUtil;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

@Service
public class PassengerCommandService {
    @Resource
    private PassengerCommandMapper passengerCommandMapper;
    @Resource
    private PassengerQueryMapper passengerQueryMapper;

    public Result addPassenger(Passenger passenger) {
        if (passenger.isLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()) {
            try {
                    passengerCommandMapper.addPassenger(passenger);
                } catch (Exception e) {
                e.printStackTrace();
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

    public Result modifyPassenger(Passenger newPassenger){
        Passenger oldPassenger = passengerQueryMapper.querySinglePassenger(newPassenger.getUserId(),
                newPassenger.getPassengerId());
        if (oldPassenger == null) {
            return Result.getResult(ResultCodeEnum.PASSENGER_NOT_EXIST);
        }else if (Objects.equals(oldPassenger.getPassengerName(), newPassenger.getPassengerName())
                && Objects.equals(oldPassenger.getPhoneNum(), newPassenger.getPhoneNum())) {
            return Result.getResult(ResultCodeEnum.PASSENGER_NO_NEED_MODIFY);
        }

        try{
            passengerCommandMapper.modifyPassenger(newPassenger);
            return Result.getResult(ResultCodeEnum.SUCCESS);
        }catch (Exception e){
//            Throwable cause = e.getCause();
            e.printStackTrace();
//            if (cause instanceof SQLIntegrityConstraintViolationException) {
//                return Result.getResult(ResultCodeEnum.PASSENGER_USER_NOT_EXIST);
//            } else if (cause instanceof DuplicateKeyException) {
//                return Result.getResult(ResultCodeEnum.PASSENGER_EXIST);
//            } else {
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
//            }
        }
    }

    public Result deletePassenger(Passenger passenger){
        try{
            passengerCommandMapper.deletePassenger(passenger);
            return Result.getResult(ResultCodeEnum.SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
        }
    }

    public Result randomPassenger(){
        Passenger passenger = new Passenger();
        passenger.setPassengerId(PassengerIdGenerateUtil.generate());
        passenger.setPassengerName(PassengerNameGenerateUtil.generate());
        passenger.setPhoneNum(PassengerPhoneNumGenerateUtil.generate());
        String s = passenger.getPhoneNum();
        if (Integer.parseInt(s.substring(s.length()-3,s.length()-1)) % 4 == 1){
            passenger.setRole("student");
        }else {
            passenger.setRole("common");
        }
        return Result.getResult(ResultCodeEnum.SUCCESS,passenger);
    }
}
