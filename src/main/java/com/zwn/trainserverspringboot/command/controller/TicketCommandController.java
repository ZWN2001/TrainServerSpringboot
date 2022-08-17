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
    Result ticketsBooking(Order order , List<String> passengerIds){
        Result result = UserCheck.checkWithUserId(order.getUserId());
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBooking(order, passengerIds);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
            return results;
        }else {
            return result;
        }
    }

    @PostMapping("/bookingCancel")
    Result ticketBookingCancel(OrderPrimaryKey key){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBookingCancel(key);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
            return results;
        }else {
            return result;
        }
    }

    @PostMapping("/refund")
    Result ticketRefund(String orderId, String passengerId){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return ticketCommandService.ticketRefund(orderId,passengerId);
        }else {
            return result;
        }
    }

    @PostMapping("/rebook")
    Result ticketRebook(String orderId, String passengerId, String departureDate, String trainRouteId, String carriage, String seat){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return ticketCommandService.ticketRebook(orderId, passengerId, UserUtil.getCurrentUserId(), departureDate,
                    trainRouteId, carriage, seat);
        }else {
            return result;
        }
    }

    @PostMapping("/get")
    Result ticketGet(Order order, String passengerId, int carriage_id, int seat){
        Result result = UserCheck.checkWithUserId(order.getUserId());
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return ticketCommandService.ticketGet(order.getOrderId(), passengerId, carriage_id, seat);
        }else {
            return result;
        }
    }
}
