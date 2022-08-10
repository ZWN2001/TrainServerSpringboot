package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.service.TicketCommandService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
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
        try {
            if (UserUtil.getCurrentUserId() == 0 || order.getUserId() != UserUtil.getCurrentUserId()){
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
        Result results;
        try{
            results = ticketCommandService.ticketBooking(order, passengerIds);
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
        }
        return JSON.toJSONString(results);
    }

    @PostMapping("/refund")
    String ticketRefund(String orderId){
        try{
           if (UserUtil.getCurrentUserId() == 0){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
            return JSON.toJSONString(ticketCommandService.ticketRefund(orderId));
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR));
        }
    }

    @PostMapping("/rebook")
    String ticketRebook(String orderId, String departureDate, String trainRouteId, String carriage, String seat){
        try {
            if (UserUtil.getCurrentUserId() == 0 ){
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
        return JSON.toJSONString(ticketCommandService.ticketRebook(orderId, UserUtil.getCurrentUserId(), departureDate,
                trainRouteId, carriage, seat));
    }

    @PostMapping("/get")
    String ticketGet(Order order, int carriage_id, int seat){
        try {
            if (UserUtil.getCurrentUserId() == 0 || order.getUserId() != UserUtil.getCurrentUserId()){
                return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.getResult(ResultCodeEnum.BAD_REQUEST));
        }
      return JSON.toJSONString(ticketCommandService.ticketGet(order.getOrderId(),carriage_id,seat));
    }
}
