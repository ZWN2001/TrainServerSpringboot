package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.PassengerQueryService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/passenger")
public class PassengerQueryController {

    @Resource
    private PassengerQueryService passengerQueryService;

    @GetMapping("/all")
    String queryAllPassengers(long userId){
        return JSON.toJSONString(passengerQueryService.queryAllPassengers(userId));
    }

    @GetMapping("/single")
    String querySinglePassenger(long userId, String passengerId){
        return JSON.toJSONString(passengerQueryService.querySinglePassenger(userId, passengerId));
    }

}
