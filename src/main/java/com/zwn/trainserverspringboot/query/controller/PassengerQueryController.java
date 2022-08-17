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
@RequestMapping("/passenger/query")
public class PassengerQueryController {

    @Resource
    private PassengerQueryService passengerQueryService;

    @GetMapping("/all")
    Result queryAllPassengers(){
        try{
            return passengerQueryService.queryAllPassengers(UserUtil.getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @GetMapping("/single")
    Result querySinglePassenger(long userId, String passengerId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return passengerQueryService.querySinglePassenger(userId, passengerId);
        }else {
            return result;
        }
    }

}
