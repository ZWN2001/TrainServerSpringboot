package com.zwn.trainserverspringboot.command.service;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import com.zwn.trainserverspringboot.query.bean.AtomStationKey;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderStatus;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
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
    private TicketQueryMapper ticketQueryMapper;
    @Resource
    private OrderQueryMapper orderQueryMapper;
    @Resource
    private TrainRouteQueryMapper trainRouteQueryMapper;
    @Resource
    private MQProducer producer;

    public Result ticketBooking(Order order, List<String> passengerIds){
        if (ticketQueryMapper.getTicketToPayNum(order.getUserId()) != 0){
            ///存在未支付订单，不允许订票
            return Result.getResult(ResultCodeEnum.ORDER_HAVE_UN_PAIED);
        }
        if (!isEnough(order,passengerIds.size())){//不足
            return Result.getResult(ResultCodeEnum.TICKET_SURPLUS_NOT_ENOUGH);
        }else {
            List<Result> results = new ArrayList<>();
            String orderNumber = GenerateNum.generateOrder();
            order.setOrderId(orderNumber);
            for(String passengerId : passengerIds){
                order.setPassengerId(passengerId);
                if (order.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
                    try{
                        order.setOrderStatus(OrderStatus.UN_PAY);
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
                            //扣库存
                            redisDecr(order);
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

    public Result ticketBookingCancel(String departureDate, String trainRouteId, List<String> passengerIds){
        try {
            for (String pid:passengerIds) {
                System.out.println(departureDate);
                System.out.println(trainRouteId);
                System.out.println(pid);
                ticketCommandMapper.ticketBookingCancel(departureDate,trainRouteId,pid);
            }
            return Result.getResult(ResultCodeEnum.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    public Result ticketRefund(String orderId, String passengerId){
        Order order = orderQueryMapper.getOrderById(orderId, passengerId);
        if (order == null){
            return Result.getResult(ResultCodeEnum.ORDER_NOT_EXIST);
        }else if ( !Objects.equals(order.getOrderStatus(), OrderStatus.PAIED)){
            return Result.getResult(ResultCodeEnum.ORDER_STATUS_ERROR);
        }else {
            try {
                ticketCommandMapper.ticketRefund(orderId, passengerId);
                return Result.getResult(ResultCodeEnum.SUCCESS, order);
            }catch (Exception e){
                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR, e);
            }
        }
    }

    public Result ticketRebook(String orderId, String passengerId, long userId, String departureDate, String trainRouteId, String carriage,
                               String seat){
        Order order = orderQueryMapper.getOrderById(orderId, passengerId);
        if (userId != order.getUserId()){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,"user ID error");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PAIED)){
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
                    ticketCommandMapper.ticketRebook(orderId, passengerId, departureDate, trainRouteId);
                    if (order.getOrderStatus().equals(OrderStatus.PAIED) && carriage != null && seat != null){
                        ticketCommandMapper.updateTicketSold(orderId, passengerId, carriage, seat);
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
//    public Result ticketGet(String orderId, String passengerId, int carriage_id, int seat){
//        try{
//            //todo:判断是否有座
//            ticketCommandMapper.ticketGet(orderId, passengerId, carriage_id, seat);
//            return Result.getResult(ResultCodeEnum.SUCCESS);
//        }catch (Exception e){
//            e.printStackTrace();
//            Throwable cause = e.getCause();
//            if (cause instanceof SQLIntegrityConstraintViolationException) {
//                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
//            }  else if (cause instanceof DuplicateKeyException) {
//                return Result.getResult(ResultCodeEnum.PASSENGER_EXIST);
//            } else {
//                return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
//            }
//        }
//    }

    private boolean isEnough(Order order, int ticketNum){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        boolean enough = true;
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = JSON.toJSONString(atomStationKey);
            long surplusNumber = redisUtil.decr(key, ticketNum);
            redisUtil.incr(key, ticketNum);
            if (surplusNumber < 0) {
                enough = false;
                break;
            }
        }
        return enough;
    }


    ///扣库存
    private void redisDecr(Order order){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = JSON.toJSONString(atomStationKey);
            redisUtil.decr(key, 1);
        }
    }


}
