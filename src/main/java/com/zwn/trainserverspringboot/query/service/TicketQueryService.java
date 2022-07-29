package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.TicketPrice;
import com.zwn.trainserverspringboot.query.bean.TicketsRemain;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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

    public Result getTicketPrice(String train_route_id, String from_station_id, String to_station_id){
        List<TicketPrice> ticketPrices;
        try {
            ticketPrices = ticketQueryMapper.getTicketPrice(train_route_id, from_station_id, to_station_id);
        }catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return Result.getResult(ResultCodeEnum.ORDER_REQUEST_ILLEGAL);
            } else {
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
        return Result.getResult(ResultCodeEnum.SUCCESS,ticketPrices);
    }
}
