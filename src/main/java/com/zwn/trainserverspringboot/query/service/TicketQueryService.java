package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderGeneral;
import com.zwn.trainserverspringboot.query.bean.SeatInfo;
import com.zwn.trainserverspringboot.query.bean.TicketPrice;
import com.zwn.trainserverspringboot.query.bean.TicketsRemain;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
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

    public Result getTicketPrices(String train_route_id, String from_station_id, String to_station_id){
        List<TicketPrice> ticketPrices;
        try {
            ticketPrices = ticketQueryMapper.getTicketPrices(train_route_id, from_station_id, to_station_id);
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

    public Result getTicketPrice(String train_route_id, String from_station_id, String to_station_id,int seat_type_id){
        double ticketPrices;
        try {
            ticketPrices = ticketQueryMapper.getTicketPrice(train_route_id, from_station_id, to_station_id,seat_type_id);
        }catch (Exception e){
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return Result.getResult(ResultCodeEnum.ORDER_REQUEST_ILLEGAL);
            } else {
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
        return Result.getResult(ResultCodeEnum.SUCCESS,ticketPrices);
    }

    ///获取自己的票（已出票的订单）
    public Result getSelfTicket(long userId){
        List<OrderGeneral> result;
        try {
            result = ticketQueryMapper.getSelfTicket(userId);
            return Result.getResult(ResultCodeEnum.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    ///获取自己的所有订单
    public Result getSelfOrder(long userId){
        List<OrderGeneral> result;
        try {
            result = ticketQueryMapper.getSelfOrder(userId);
            return Result.getResult(ResultCodeEnum.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    public Result getTicketInfo(String ticketId){
        Order result;
        try {
            result = ticketQueryMapper.getTicketInfo(ticketId);
            return Result.getResult(ResultCodeEnum.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    public Result getTicketSeatInfo(String ticketId){
        try {
            SeatInfo result = ticketQueryMapper.getTicketSeatInfo(ticketId);
            return Result.getResult(ResultCodeEnum.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }
}
