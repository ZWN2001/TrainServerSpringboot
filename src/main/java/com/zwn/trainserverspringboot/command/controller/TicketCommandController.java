package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.Passenger;
import com.zwn.trainserverspringboot.command.service.TicketCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
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
    @PostMapping("booking")
    String ticketsBooking(String userId, double price, String departureDate,
                          String trainRouteId, List<String> passengerIds){
        List<Result> results = new ArrayList<>();
        try{
            for (String passengerId : passengerIds) {
                results.add(ticketCommandService.ticketBooking(userId, departureDate, trainRouteId, price, passengerId));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
        }
        return JSON.toJSONString(results);
    }
}
