package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.PassengerQueryService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
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
        if (UserCheck.checkWithUserId(userId).getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(passengerQueryService.queryAllPassengers(userId));
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }

    @GetMapping("/single")
    String querySinglePassenger(long userId, String passengerId){
        if (UserCheck.checkWithUserId(userId).getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(passengerQueryService.querySinglePassenger(userId, passengerId));
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }

}
