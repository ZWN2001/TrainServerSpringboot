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
        Order order;
        List<String> passengerIds;
        List<String> locations;
        try {
            JSONObject jsonObject = JSONObject.parseObject(orderString);
            order = JSONObject.toJavaObject(jsonObject, Order.class);
            passengerIds = StringUtil.getListFromString(passengerIdsString);
            locations = StringUtil.getListFromString(seatLocationListString);
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }

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

    @PostMapping("/bookingTansfer")
    Result ticketsBookingTansfer(String orderString1, String seatLocationListString1,
                                 String orderString2, String seatLocationListString2, String passengerIdsString){
        Order order1;
        Order order2;
        List<String> locations1;
        List<String> locations2;
        List<String> passengerIds;
        try {
            JSONObject jsonObject1 = JSONObject.parseObject(orderString1);
            order1 = JSONObject.toJavaObject(jsonObject1, Order.class);
            //第一程的location
            locations1 = StringUtil.getListFromString(seatLocationListString1);

            JSONObject jsonObject2 = JSONObject.parseObject(orderString2);
            order2 = JSONObject.toJavaObject(jsonObject2, Order.class);
            //第二程的location
            locations2 = StringUtil.getListFromString(seatLocationListString2);

            passengerIds = StringUtil.getListFromString(passengerIdsString);
        }catch (Exception e){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        if(locations1.size() != passengerIds.size() || locations2.size() != passengerIds.size()){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }

        Result result1 = UserCheck.checkWithUserId(order1.getUserId());
        Result result2 = UserCheck.checkWithUserId(order2.getUserId());
        if (result1.getCode() == ResultCodeEnum.SUCCESS.getCode() && result2.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            Result results;
            try{
                results = ticketCommandService.ticketsBookingTansfer(order1,order2,locations1,locations2,passengerIds);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
            return results;
        }else {
            if (result1.getCode() != ResultCodeEnum.SUCCESS.getCode()){
                return result1;
            }
            return result2;
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
    Result ticketRefund(String orderId){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try{
                return ticketCommandService.ticketRefund(orderId);
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
