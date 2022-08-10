package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.PassengerCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/command/passenger")
public class PassengerCommandController {

    @Resource
    private PassengerCommandService passengerCommandService;

    @PostMapping("/add")
    String addPassenger(Passenger passenger){
        try {
            if (UserUtil.getCurrentUserId() == 0){
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
        return JSON.toJSONString(passengerCommandService.addPassenger(passenger));
    }

    @PostMapping("/modify")
    String modifyPassenger(Passenger passenger){
        return JSON.toJSONString(passengerCommandService.modifyPassenger(passenger));
    }

    @PostMapping("/delete")
    String deletePassenger(Passenger passenger){
        return JSON.toJSONString(passengerCommandService.deletePassenger(passenger));
    }

}
