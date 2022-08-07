package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.TicketCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("command/ticket")
public class TicketCommandController {

    @Resource
    private TicketCommandService ticketCommandService;

    //默认是一张票的预定，可以预定多个乘客
    @PostMapping("/booking")
    String ticketsBooking(Order order , List<String> passengerIds){
        if (UserUtil.getCurrentUserId() == 0 || UserUtil.getCurrentUserId() != order.getUserId()){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST,"user ID 非法"));
        }
        Result results;
        try{
            results = ticketCommandService.ticketBooking(order, passengerIds);
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
        }
        return JSON.toJSONString(results);
    }

    @PostMapping("/refund")
    String ticketRefund(String orderId){
        if (UserUtil.getCurrentUserId() == 0){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
        return JSON.toJSONString(ticketCommandService.ticketRefund(orderId,UserUtil.getCurrentUserId()));
    }
}
