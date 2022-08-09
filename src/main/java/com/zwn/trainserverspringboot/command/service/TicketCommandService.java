package com.zwn.trainserverspringboot.command.service;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import com.zwn.trainserverspringboot.query.bean.AtomStationKey;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderStatus;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.rabbitmq.MQProducer;
import com.zwn.trainserverspringboot.util.GenerateNum;
import com.zwn.trainserverspringboot.util.RedisUtil;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TicketCommandService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TicketCommandMapper ticketCommandMapper;
    @Resource
    private TicketQueryService ticketQueryService;
    @Resource
    private OrderQueryMapper orderQueryMapper;
    @Resource
    private TrainRouteQueryMapper trainRouteQueryMapper;
    MQProducer producer;

    public Result ticketBooking(Order order, List<String> passengerIds){
        if (!isEnough(order,passengerIds.size())){//不足
            return Result.getResult(ResultCodeEnum.TICKET_SURPLUS_NOT_ENOUGH);
        }else {
            List<Result> results = new ArrayList<>();
            for(String passengerId : passengerIds){
                order.setPassengerId(passengerId);
                if (order.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
                    try{
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
                            OrderMessage message = OrderMessage.builder().order(order).num(passengerIds.size()).build();
                            producer.sendTicketBooking(message);
                            results.add(Result.getResult(ResultCodeEnum.SUCCESS,order));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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

    public Result ticketRefund(String orderId){
        Order order = orderQueryMapper.getOrderById(orderId);
        if (order == null){
            return Result.getResult(ResultCodeEnum.ORDER_NOT_EXIST);
        }else if ( !Objects.equals(order.getOrderStatus(), "已支付")){
            return Result.getResult(ResultCodeEnum.ORDER_STATUS_ERROR);
        }else {
            try {
                ticketCommandMapper.ticketRefund(orderId);
                return Result.getResult(ResultCodeEnum.SUCCESS, order);
            }catch (Exception e){
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR, e);
            }
        }
    }

    public Result ticketRebook(String orderId, long userId, String departureDate, String trainRouteId, String carriage,
                               String seat){
        Order order = orderQueryMapper.getOrderById(orderId);
        if (userId != order.getUserId()){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,"user ID error");
        }
        if (!order.getOrderStatus().equals("已支付")  || !order.getOrderStatus().equals("已出票")){
            return Result.getResult(ResultCodeEnum.TICKET_UNABLE_REBOOK);
        }

        if (departureDate != null){
            order.setDepartureDate(departureDate);
        } else if (trainRouteId != null) {
            order.setTrainRouteId(trainRouteId);
        }

        if (order.isRequestLegal().getCode() != ResultCodeEnum.SUCCESS.getCode()) {
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        } else {
            if (isEnough(order, 1)) {
                if (carriage != null && seat != null){
                    //TODO:检查座位是否可分配
                }
                try {
                    ticketCommandMapper.ticketRebook(orderId, departureDate, trainRouteId);
                    if (order.getOrderStatus().equals("已出票") && carriage != null && seat != null){
                        ticketCommandMapper.updateTicketSold(orderId, carriage, seat);
                    }
                    return Result.getResult(ResultCodeEnum.SUCCESS);
                }catch (Exception e){
                    Throwable cause = e.getCause();
                    if (cause instanceof SQLIntegrityConstraintViolationException) {
                        return Result.getResult(ResultCodeEnum.BAD_REQUEST);
                    }  else {
                        return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
                    }
                }
            } else {
                return Result.getResult(ResultCodeEnum.TICKET_SURPLUS_NOT_ENOUGH);
            }
        }
    }

    //取票
    public Result ticketGet(String orderId, int carriage_id, int seat){
        try{
            //todo:判断是否有座
            ticketCommandMapper.ticketGet(orderId, carriage_id, seat);
            return Result.getResult(ResultCodeEnum.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }  else if (cause instanceof DuplicateKeyException) {
                return Result.getResult(ResultCodeEnum.PASSENGER_EXIST);
            } else {
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
            }
        }
    }

    private boolean isEnough(Order order, int ticketNum){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        boolean enough = true;
        int index = 0;//用于标识从哪个下标以前的余票需要加回
        for (int i = 0; i< atomStationKeys.size();i++){
            atomStationKeys.get(i).setDepartureDate(order.getDepartureDate());
            atomStationKeys.get(i).setSeatTypeId(order.getSeatTypeId());
            String key = JSON.toJSONString(atomStationKeys.get(i));
            long surplusNumber = redisUtil.decr(key,ticketNum);
            if(surplusNumber < 0) {
                enough = false;
                index = i;
                break;
            }
        }
        if (!enough){//不足
            for (int i = 0; i <= index; i++) {
                String key = JSON.toJSONString(atomStationKeys.get(i));
                redisUtil.incr(key, ticketNum);
            }
        }
        return enough;
    }


}
