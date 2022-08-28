package com.zwn.trainserverspringboot.command.service;

import com.alibaba.fastjson2.JSON;
import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import com.zwn.trainserverspringboot.command.mapper.SeatCommandMapper;
import com.zwn.trainserverspringboot.query.bean.AtomStationKey;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderStatus;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.query.bean.SeatRemainKey;
import com.zwn.trainserverspringboot.query.bean.SeatSoldInfo;
import com.zwn.trainserverspringboot.query.mapper.*;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.rabbitmq.MQProducer;
import com.zwn.trainserverspringboot.util.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

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
    private SeatQueryMapper seatQueryMapper;
    @Resource
    private SeatCommandMapper seatCommandMapper;
    @Resource
    private MQProducer producer;

    public Result ticketBooking(Order order, List<String> passengerIds, List<String> locations){
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
            for(int i = 0; i < passengerIds.size(); i++){
                order.setPassengerId(passengerIds.get(i));
                //检查乘员是否买过这班车的票
                if (ticketCommandMapper.getTicketNum(passengerIds.get(i),order.getDepartureDate(),order.getTrainRouteId()) != 0){
                    return Result.getResult(ResultCodeEnum.TICKET_BOUGHT);
                }
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
                            OrderMessage message = OrderMessage.builder().order(order).seatLocation(Integer.parseInt(locations.get(i))).num(1).build();
                            producer.sendTicketBooking(message);
                            results.add(Result.getResult(ResultCodeEnum.SUCCESS,order));
                            //扣库存
                            redisDecr(order);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Throwable cause = e.getCause();
                        if (cause instanceof SQLIntegrityConstraintViolationException) {
                            results.add(Result.getResult(ResultCodeEnum.ORDER_REQUEST_ILLEGAL,passengerIds.get(i)));
                        } else if (cause instanceof DuplicateKeyException) {
                            results.add(Result.getResult(ResultCodeEnum.ORDER_EXIST,passengerIds.get(i)));
                        } else {
                            results.add(Result.getResult(ResultCodeEnum.BAD_REQUEST,passengerIds.get(i))) ;
                        }
                    }
                }else {
                    results.add(Result.getResult(order.isRequestLegal(),passengerIds.get(i)));
                }
            }
            return Result.getResult(ResultCodeEnum.SUCCESS,results);
        }
    }

    public Result ticketsBookingTansfer(Order order1, Order order2, List<String> locations1,
                                        List<String> locations2, List<String> passengerIds){
        if (ticketQueryMapper.getTicketToPayNum(order1.getUserId()) != 0){
            ///存在未支付订单，不允许订票
            return Result.getResult(ResultCodeEnum.ORDER_HAVE_UN_PAIED);
        }
        if (!isEnough(order1,passengerIds.size()) || !isEnough(order2,passengerIds.size())){//不足
            return Result.getResult(ResultCodeEnum.TICKET_SURPLUS_NOT_ENOUGH);
        }else {
            List<Result> results = new ArrayList<>();
            String orderNumber = GenerateNum.generateOrder();
            order1.setOrderId(orderNumber);
            order2.setOrderId(orderNumber);
            for(int i = 0; i < passengerIds.size(); i++){
                order1.setPassengerId(passengerIds.get(i));
                order2.setPassengerId(passengerIds.get(i));
                //检查乘员是否买过这班车的票
                if (ticketCommandMapper.getTicketNum(passengerIds.get(i),order1.getDepartureDate(),order1.getTrainRouteId()) != 0
                ||ticketCommandMapper.getTicketNum(passengerIds.get(i),order2.getDepartureDate(),order2.getTrainRouteId()) != 0){
                    return Result.getResult(ResultCodeEnum.TICKET_BOUGHT);
                }
                if (order1.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()
                        && order2.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
                    try {
                        order1.setOrderStatus(OrderStatus.UN_PAY);
                        //获取价格
                        Result priceResult1 = ticketQueryService.getTicketPrice(order1.getTrainRouteId(),
                                order1.getFromStationId(), order1.getToStationId(), order1.getSeatTypeId());
                        if (priceResult1.getCode() != ResultCodeEnum.SUCCESS.getCode()) {
                            results.add(Result.getResult(ResultCodeEnum.TICKET_PRICE_ERROR));
                        } else {
                            order1.setPrice((double) priceResult1.getData());
                            OrderMessage message1 = OrderMessage.builder().order(order1).seatLocation(Integer.parseInt(locations1.get(i))).num(1).build();
                            producer.sendTicketBooking(message1);
                            results.add(Result.getResult(ResultCodeEnum.SUCCESS, order1));
                            redisDecr(order1);
                        }

                        order2.setOrderStatus(OrderStatus.UN_PAY);
                        //获取价格
                        Result priceResult2 = ticketQueryService.getTicketPrice(order2.getTrainRouteId(),
                                order2.getFromStationId(), order2.getToStationId(), order2.getSeatTypeId());
                        if (priceResult2.getCode() != ResultCodeEnum.SUCCESS.getCode()) {
                            results.add(Result.getResult(ResultCodeEnum.TICKET_PRICE_ERROR));
                        } else {
                            order2.setPrice((double) priceResult2.getData());
                            OrderMessage message2 = OrderMessage.builder().order(order2).seatLocation(Integer.parseInt(locations2.get(i))).num(1).build();
                            producer.sendTicketBooking(message2);
                            results.add(Result.getResult(ResultCodeEnum.SUCCESS, order2));
                            //扣库存
                            redisDecr(order2);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        Throwable cause = e.getCause();
                         if (cause instanceof DuplicateKeyException) {
                            results.add(Result.getResult(ResultCodeEnum.ORDER_EXIST,passengerIds.get(i)));
                        } else {
                            results.add(Result.getResult(ResultCodeEnum.BAD_REQUEST,passengerIds.get(i))) ;
                        }
                    }
                }else {
                    results.add(Result.getResult(ResultCodeEnum.BAD_REQUEST));
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

    public Result ticketRefund(String orderId) {
        List<Order> orders = orderQueryMapper.getOrderById(orderId);
        if (orders == null) {
            return Result.getResult(ResultCodeEnum.ORDER_NOT_EXIST);
        }

        for (Order order: orders) {
            if(order.getUserId() != UserUtil.getCurrentUserId()){
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
            if ( !Objects.equals(order.getOrderStatus(), OrderStatus.PAIED)){
                return Result.getResult(ResultCodeEnum.ORDER_STATUS_ERROR);
            }
        }

        try {
            List<SeatSoldInfo> seatSoldInfos = ticketQueryMapper.getSoldSeatInfo(orderId);
            addBack(seatSoldInfos);
            ticketCommandMapper.ticketRefund(orderId);
            return Result.getResult(ResultCodeEnum.SUCCESS, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR, e);
        }

    }

    public Result ticketRebook(String orderId, String passengerId, long userId, String departureDate, String trainRouteId, String carriage,
                               String seat){
        Order order = orderQueryMapper.getOrderByIdAndPid(orderId, passengerId);
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

    private void addBack(List<SeatSoldInfo> seatSoldInfos){
        SeatSoldInfo info = seatSoldInfos.get(0);
        //加库存，加座位，加余票
        Order order = new Order();
        order.setTrainRouteId(info.getTrainRouteId());
        order.setFromStationId(info.getFromStationId());
        order.setToStationId(info.getToStationId());
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = JSON.toJSONString(atomStationKey);
            redisUtil.incr(key, seatSoldInfos.size());
        }

        ticketCommandMapper.updateTicketRemain(info.getTrainRouteId(),info.getSeatType(),info.getDepartureDate(),
                info.getFromStationId(),info.getToStationId(), seatSoldInfos.size());

        String remainString = seatQueryMapper.getSeatRemain(info.getTrainRouteId(),
                info.getDepartureDate(), info.getCarriageId(), info.getSeat() % 4);
        SeatRemainKey passengerKey = trainRouteQueryMapper.getFromToNo(info.getTrainRouteId(),
                info.getFromStationId(), info.getToStationId());

        Map map = JSON.parseObject(remainString, Map.class);
        Map<String, Integer> remainMap = new HashMap<>();

        int value;
        for (Object obj : map.keySet()) {
            value = (int) map.get(obj);
            remainMap.put(obj.toString(), value);
        }
        Set<String> s = new HashSet<>(remainMap.keySet());
        for (String keyString : s ){
            SeatRemainKey key = SeatRemainKey.fromString(keyString);
            value = remainMap.get(keyString);
            assert key != null;
            if(passengerKey.getFromStationNo() == key.getToStationNo()){
                SeatRemainKey newKey = new SeatRemainKey();
                newKey.setFromStationNo(key.getFromStationNo());
                newKey.setToStationNo(passengerKey.getToStationNo());
                if(value == seatSoldInfos.size()){
                    remainMap.put(newKey.toString(),value);
                    remainMap.remove(keyString);
                }else if (value < seatSoldInfos.size()){
                    remainMap.remove(keyString);
                    remainMap.put(newKey.toString(),value);
                    int remainToAdd = seatSoldInfos.size() - value;
                    remainMap.put(passengerKey.toString(),remainToAdd);
                }else {
                    remainMap.remove(keyString);
                    remainMap.put(newKey.toString(),seatSoldInfos.size());
                    int remainToAdd = value - seatSoldInfos.size();
                    remainMap.put(keyString, remainToAdd);
                }
            }else {
                remainMap.put(passengerKey.toString(),seatSoldInfos.size());
            }
        }

        int value1,value2;
        s.clear();
        s.addAll(remainMap.keySet());
        for (String keyString1 : s) {
            for (String keyString2 : s) {//处理可能存在的 1_2,2_4的情况
                if(!keyString1.equals(keyString2)){
                    SeatRemainKey key1 = SeatRemainKey.fromString(keyString1);
                    try {
                        value1 = remainMap.get(keyString1);
                    }catch (Exception e){
                        continue;
                    }
                    SeatRemainKey key2 = SeatRemainKey.fromString(keyString2);
                    try {
                        value2 = remainMap.get(keyString1);
                    }catch (Exception e){
                        continue;
                    }
                    assert key1 != null && key2 != null;
                    if(key1.getToStationNo() == key2.getFromStationNo()){
                        SeatRemainKey newKey = new SeatRemainKey();
                        newKey.setFromStationNo(key1.getFromStationNo());
                        newKey.setToStationNo(key2.getToStationNo());
                        int v = 0;
                        if (remainMap.containsKey(newKey.toString())){
                            v = remainMap.get(newKey.toString());
                        }
                        if(value1 == value2){
                            remainMap.put(newKey.toString(),value1 + v);
                            remainMap.remove(keyString1);
                            remainMap.remove(keyString2);
                        }else if (value1 < value2){
                            remainMap.remove(keyString1);
                            remainMap.put(newKey.toString(),value1 + v);
                            int remainToAdd = value2 - value1;
                            remainMap.put(keyString1, remainToAdd);
                        }else {
                            remainMap.remove(keyString2);
                            remainMap.put(newKey.toString(),value2 + v);
                            int remainToAdd = value1 - value2;
                            remainMap.put(keyString2, remainToAdd);
                        }
                    }
                }
            }
        }

        String seatRemainString = JSON.toJSONString(remainMap);
        System.out.println("更新："+seatRemainString);
        seatCommandMapper.updateSeatRemain(seatSoldInfos.get(0).getTrainRouteId(),
                seatSoldInfos.get(0).getDepartureDate(),
                seatSoldInfos.get(0).getCarriageId(), seatSoldInfos.get(0).getSeat() % 4, seatRemainString);
    }

}
