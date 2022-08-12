package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.UserCheck;
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
    String getTicketsRemain(String train_route_id, String ticket_date,String from_station_id,String to_station_id){
        return JSON.toJSONString(ticketQueryService.getTicketsRemain(train_route_id, ticket_date,from_station_id,to_station_id));
    }

    //查票价
    @GetMapping("/ticketPrice")
    String getTicketPrice(String train_route_id,String from_station_id,String to_station_id){
        return JSON.toJSONString(ticketQueryService.getTicketPrices(train_route_id, from_station_id, to_station_id));
    }

//    @GetMapping("/ticketHistory")
//    String getTicketHistory(){
//        String userId ="";
//        return JSON.toJSONString(ticketQueryService.getTicketHistory(userId));
//    }
    @GetMapping("/selfTicket")
    String getSelfTicket(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketQueryService.getSelfTicket(userId));
        }else {
            return JSON.toJSONString(result);
        }
    }

    @GetMapping("/selfOrder")
    String getSelfOrder(long userId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketQueryService.getSelfOrder(userId));
        }else {
            return JSON.toJSONString(result);
        }
    }

    @GetMapping("/ticketInfo")
    String getTicketInfo(long userId, String ticketId){
        Result result = UserCheck.checkWithUserId(userId);
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            return JSON.toJSONString(ticketQueryService.getTicketInfo(ticketId));
        }else {
            return JSON.toJSONString(result);
        }
    }

}
