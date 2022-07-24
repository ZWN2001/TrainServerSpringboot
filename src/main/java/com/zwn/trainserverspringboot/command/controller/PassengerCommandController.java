package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.PassengerCommandService;
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
        return JSON.toJSONString(passengerCommandService.addPassenger(passenger));
    }

}
