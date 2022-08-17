package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.PassengerCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/passenger/command")
public class PassengerCommandController {

    @Resource
    private PassengerCommandService passengerCommandService;

    @PostMapping("/add")
    String addPassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return JSON.toJSONString(passengerCommandService.addPassenger(passenger));
            }else {
                return JSON.toJSONString(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
    }

    @PostMapping("/modify")
    String modifyPassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return JSON.toJSONString(passengerCommandService.modifyPassenger(passenger));
            }else {
                return JSON.toJSONString(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
    }

    @PostMapping("/delete")
    String deletePassenger(String passengerJSON){
        try {
            JSONObject jsonObject = JSONObject.parseObject(passengerJSON);
            Passenger passenger = JSONObject.toJavaObject(jsonObject, Passenger.class);
            Result result = UserCheck.checkWithUserId(passenger.getUserId());
            if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
                return JSON.toJSONString(passengerCommandService.deletePassenger(passenger));
            }else {
                return JSON.toJSONString(result);
            }
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
    }
}
