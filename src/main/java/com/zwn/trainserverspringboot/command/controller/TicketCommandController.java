package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderPrimaryKey;
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
@RequestMapping("/ticket/command")
public class TicketCommandController {

    @Resource
    private TicketCommandService ticketCommandService;

    //默认是一张票的预定，可以预定多个乘客
    @PostMapping("/booking")
    String ticketsBooking(Order order , List<String> passengerIds){
        Result result = UserCheck.checkWithUserId(order.getUserId());
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBooking(order, passengerIds);
            }catch (Exception e){
                e.printStackTrace();
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
            }
            return JSON.toJSONString(results);
        }else {
            return JSON.toJSONString(result);
        }
    }

    @PostMapping("/bookingCancel")
    String ticketBookingCancel(OrderPrimaryKey key){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBookingCancel(key);
            }catch (Exception e){
                e.printStackTrace();
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
            }
            return JSON.toJSONString(results);
        }else {
            return JSON.toJSONString(result);
        }
    }

    @PostMapping("/refund")
    String ticketRefund(String orderId, String passengerId){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketRefund(orderId,passengerId));
        }else {
            return JSON.toJSONString(result);
        }
    }

    @PostMapping("/rebook")
    String ticketRebook(String orderId, String passengerId, String departureDate, String trainRouteId, String carriage, String seat){
        if (UserCheck.check().getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketRebook(orderId, passengerId, UserUtil.getCurrentUserId(), departureDate,
                    trainRouteId, carriage, seat));
        }else {
            return JSON.toJSONString(UserCheck.check());
        }
    }

    @PostMapping("/get")
    String ticketGet(Order order, String passengerId, int carriage_id, int seat){
        if (UserCheck.checkWithUserId(order.getUserId()).getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketCommandService.ticketGet(order.getOrderId(), passengerId, carriage_id, seat));
        }else {
            return JSON.toJSONString(UserCheck.check());
        }
    }
}
