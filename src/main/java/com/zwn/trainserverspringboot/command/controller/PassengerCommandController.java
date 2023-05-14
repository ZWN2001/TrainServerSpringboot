package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSONObject;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.PassengerCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/command/passengers")
public class PassengerCommandController {

    @Resource
    private PassengerCommandService passengerCommandService;

    @PostMapping("/add")
    Result addPassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return passengerCommandService.addPassenger(passenger);
            }else {
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @PutMapping("/modify")
    Result modifyPassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return passengerCommandService.modifyPassenger(passenger);
            }else {
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    Result deletePassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return passengerCommandService.deletePassenger(passenger);
            }else {
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @GetMapping("/random")
    Result randomPassenger(){
        return passengerCommandService.randomPassenger();
    }
}
