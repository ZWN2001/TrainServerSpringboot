package com.zwn.trainserverspringboot.command.controller;

import com.alibaba.fastjson.JSONObject;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.service.TicketCommandService;
import com.zwn.trainserverspringboot.util.*;
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
    Result ticketsBooking(String orderString , String passengerIdsString, String seatLocationListString){
        JSONObject jsonObject = JSONObject.parseObject(orderString);
        Order order = JSONObject.toJavaObject(jsonObject, Order.class);
        List<String> passengerIds = StringUtil.getListFromString(passengerIdsString);
        List<String> locations = StringUtil.getListFromString(seatLocationListString);
        if(locations.size() != passengerIds.size()){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        Result result = UserCheck.checkWithUserId(order.getUserId());
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBooking(order, passengerIds, locations);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
            return results;
        }else {
            return result;
        }
    }

    @PostMapping("/bookingCancel")
    Result ticketBookingCancel(String departureDate, String trainRouteId, String passengetIdString){
        List<String> passengerIds = StringUtil.getListFromString(passengetIdString);
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketBookingCancel(departureDate,trainRouteId,passengerIds);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
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
            try{
                return ticketCommandService.ticketRefund(orderId,passengerId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }

        }else {
            return result;
        }
    }

    @PostMapping("/rebook")
    Result ticketRebook(String orderId, String passengerId, String departureDate, String trainRouteId, String carriage, String seat){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try{
                return ticketCommandService.ticketRebook(orderId, passengerId, UserUtil.getCurrentUserId(), departureDate,
                        trainRouteId, carriage, seat);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }

        }else {
            return result;
        }
    }

//    @PostMapping("/get")
//    Result ticketGet(Order order, String passengerId, int carriageId, int seat){
//        Result result = UserCheck.checkWithUserId(order.getUserId());
//        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
//            try{
//                return ticketCommandService.ticketGet(order.getOrderId(), passengerId, carriageId, seat);
//            }catch (Exception e){
//                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
//            }
//
//        }else {
//            return result;
//        }
//    }
}
