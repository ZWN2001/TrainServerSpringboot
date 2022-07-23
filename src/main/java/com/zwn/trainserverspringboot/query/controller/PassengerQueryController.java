package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.PassengerQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/passenger")
public class PassengerQueryController {

    @Resource
    private PassengerQueryService passengerQueryService;

    @GetMapping("/passengerDetails")
    String queryPassengerDetails(String userId){
        return JSON.toJSONString(passengerQueryService.queryPassengerDetails(userId));
    }
}
