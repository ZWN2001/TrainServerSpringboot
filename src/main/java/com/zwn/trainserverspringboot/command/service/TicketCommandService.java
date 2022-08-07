package com.zwn.trainserverspringboot.command.service;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderKey;
import com.zwn.trainserverspringboot.command.bean.OrderStatus;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.util.GenerateNum;
import com.zwn.trainserverspringboot.util.RedisUtil;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TicketCommandService {

    Date date = new Date();
    SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TicketCommandMapper ticketCommandMapper;

    @Resource
    private TicketQueryService ticketQueryService;

    @Resource
    private OrderQueryMapper orderQueryMapper;

    public Result ticketBooking(Order order, List<String> passengerIds){
        OrderKey orderKey = OrderKey.getFromOrder(order);
        String key = JSON.toJSONString(orderKey);
        long surplusNumber = redisUtil.decr(key,passengerIds.size());
        if(surplusNumber < 0){
            redisUtil.incr(key,passengerIds.size());
            return Result.getResult(ResultCodeEnum.TICKET_SURPLUS_NOT_ENOUGH);
        }else {
            List<Result> results = new ArrayList<>();
            for(String passengerId : passengerIds){
                order.setPassengerId(passengerId);
                if (order.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
                    try{
                        String time = timeFrtmat.format(date.getTime());
                        order.setOrderTime(time);
                        order.setOrderStatus(OrderStatus.UN_PAY);
                        String orderNumber = GenerateNum.generateOrder();
                        order.setOrderId(orderNumber);
                        //获取价格
                        Result priceResult = ticketQueryService.getTicketPrice(order.getTrainRouteId(),
                                order.getFromStationId(),order.getToStationId(),order.getSeatTypeId());
                        if (priceResult.getCode() != ResultCodeEnum.SUCCESS.getCode()){
                            results.add(Result.getResult(ResultCodeEnum.TICKET_PRICE_ERROR));
                        }else {
                            order.setPrice((double) priceResult.getData());
                            ticketCommandMapper.ticketBooking(order);
                            results.add(Result.getResult(ResultCodeEnum.SUCCESS,order));
                        }
                    }catch (Exception e){
                        Throwable cause = e.getCause();
                        if (cause instanceof SQLIntegrityConstraintViolationException) {
                            results.add(Result.getResult(ResultCodeEnum.ORDER_REQUEST_ILLEGAL,passengerId));
                        } else if (cause instanceof DuplicateKeyException) {
                            results.add(Result.getResult(ResultCodeEnum.ORDER_EXIST,passengerId));
                        } else {
                            results.add(Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,passengerId)) ;
                        }
                    }
                }else {
                    results.add(Result.getResult(order.isRequestLegal(),passengerId));
                }
            }
            return Result.getResult(ResultCodeEnum.SUCCESS,results);
        }
    }

    public Result ticketRefund(String orderId, long userId){
        Order order = orderQueryMapper.getOrderById(orderId);
        if (order == null){
            return Result.getResult(ResultCodeEnum.ORDER_NOT_EXIST);
        }else if (order.getUserId() != userId || !Objects.equals(order.getOrderStatus(), "已支付")){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }else {
            try {
                ticketCommandMapper.ticketRefund(orderId);
                return Result.getResult(ResultCodeEnum.SUCCESS, order);
            }catch (Exception e){
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR, e);
            }
        }
    }
}
