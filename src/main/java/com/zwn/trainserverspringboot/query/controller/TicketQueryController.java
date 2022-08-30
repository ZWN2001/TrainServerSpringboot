package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
import com.zwn.trainserverspringboot.util.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ticket/query")
public class TicketQueryController {

    @Resource
    private TicketQueryService ticketQueryService;

    //查从出发地到目的地的某车次余票
    @GetMapping("/ticketRemain")
    Result getTicketsRemain(String trainRouteId, String ticketDate,String fromStationId,String toStationId){
        try {
            return ticketQueryService.getTicketsRemain(trainRouteId, ticketDate,fromStationId,toStationId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    //查票价
    @GetMapping("/ticketPrices")
    Result getTicketPrice(String trainRouteId,String fromStationId,String toStationId){
        try {
            return ticketQueryService.getTicketPrices(trainRouteId, fromStationId, toStationId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @GetMapping("/selfTicket")
    Result getSelfTicket(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getSelfTicket(userId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/selfOrder")
    Result getSelfOrder(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getSelfOrder(userId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/selfPaiedOrder")
    Result getSelfPaiedOrder(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getSelfPaiedOrder(userId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/orderInfo")
    Result getOrderInfo(long userId, String orderId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getOrderInfo(orderId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/ticketSeatInfo")
    Result getTicketSeatInfo(String ticketId){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getTicketSeatInfo(ticketId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/ticketToPayDetail")
    Result getTicketToPayDetail(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try {
                return ticketQueryService.getTicketToPayDetail(userId);
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    @GetMapping("/orderPayStatus")
    Result getOrderPayStatus(String orderId){
        try {
            return ticketQueryService.getOrderPayStatus(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    @GetMapping("/orderRebookStatus")
    Result getOrderRebookStatus(String orderId){
        try {
            return ticketQueryService.getOrderRebookStatus(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    @GetMapping("/orderById")
    Result getOrderById(String orderId){
        try {
            return ticketQueryService.getOrderById(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    @GetMapping("/orderToRebook")
    Result getOrderToRebook(){
        try {
            return ticketQueryService.getOrderToRebook(UserUtil.getCurrentUserId());
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }


}
