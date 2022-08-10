package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.service.TicketCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("command/ticket")
public class TicketCommandController {

    @Resource
    private TicketCommandService ticketCommandService;

    //默认是一张票的预定，可以预定多个乘客
    @PostMapping("/booking")
    String ticketsBooking(Order order , List<String> passengerIds){
        if (UserCheck.checkWithUserId(order.getUserId()).getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBooking(order, passengerIds);
            }catch (Exception e){
                e.printStackTrace();
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
            }
            return JSON.toJSONString(results);
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }

    @PostMapping("/refund")
    String ticketRefund(String orderId){
        if (UserCheck.check().getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketRefund(orderId));
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }

    @PostMapping("/rebook")
    String ticketRebook(String orderId, String departureDate, String trainRouteId, String carriage, String seat){
        if (UserCheck.check().getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketRebook(orderId, UserUtil.getCurrentUserId(), departureDate,
                    trainRouteId, carriage, seat));
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }

    @PostMapping("/get")
    String ticketGet(Order order, int carriage_id, int seat){
        if (UserCheck.checkWithUserId(order.getUserId()).getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketGet(order.getOrderId(),carriage_id,seat));
        }else {
            return com.alibaba.fastjson2.JSON.toJSONString(UserCheck.check());
        }
    }
}
