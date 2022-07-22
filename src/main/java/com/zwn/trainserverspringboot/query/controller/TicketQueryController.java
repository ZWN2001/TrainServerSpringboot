package com.zwn.dbtext.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.dbtext.query.service.TicketQueryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query")
public class TicketQueryController {

    @Resource
    private TicketQueryService ticketQueryService;

    String getTicketsRemain(String train_route_id, String ticket_date,String from_station_id,String to_station_id){
        return JSON.toJSONString(ticketQueryService.getTicketsRemain(train_route_id, ticket_date,from_station_id,to_station_id));
    }

}
