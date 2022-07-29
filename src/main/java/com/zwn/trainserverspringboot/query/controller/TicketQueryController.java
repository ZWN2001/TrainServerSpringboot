package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/ticket")
public class TicketQueryController {

    @Resource
    private TicketQueryService ticketQueryService;

    //查从出发地到目的地的某车次余票
    @GetMapping("/ticketsRemain")
    String getTicketsRemain(String train_route_id, String ticket_date,String from_station_id,String to_station_id){
        return JSON.toJSONString(ticketQueryService.getTicketsRemain(train_route_id, ticket_date,from_station_id,to_station_id));
    }

    //查票价
    @GetMapping("/ticketPrice")
    String getTicketPrice(String train_route_id,String from_station_id,String to_station_id){
        return JSON.toJSONString(ticketQueryService.getTicketPrice(train_route_id, from_station_id, to_station_id));
    }

}
