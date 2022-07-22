package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.TicketsRemain;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
import com.zwn.dbtext.util.Result;
import com.zwn.dbtext.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TicketQueryService {

    @Resource
    private TicketQueryMapper ticketQueryMapper;

//    @Resource
//    private StationQueryMapper stationQueryMapper;
//
//    @Resource
//    TrainRouteQueryMapper trainRouteQueryMapper;

    public Result getTicketsRemain(String train_route_id, String ticket_date,String from_station_id,String to_station_id){
        List<TicketsRemain> ticketsRemain = ticketQueryMapper.getTicketsRemain(train_route_id, ticket_date, from_station_id, to_station_id);
        return Result.getResult(ResultCodeEnum.SUCCESS,ticketsRemain);
    }

}
