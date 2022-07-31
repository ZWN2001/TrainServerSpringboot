package com.zwn.trainserverspringboot.command.service;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TicketCommandService {

    Date date = new Date();
    SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private TicketCommandMapper ticketCommandMapper;

    public Result ticketBooking(Order order, String passengerId){
        order.setPassengerId(passengerId);
        if (order.isRequestLegal().getCode() == ResultCodeEnum.SUCCESS.getCode()){
            //先验:订单是否存在
            try{
                String time = timeFrtmat.format(date.getTime());
                order.setOrderTime(time);
                //TODO:结算票数

//            ticketCommandMapper.ticketBooking(
//                    userId, departureDate, trainRouteId, passengerId, OrderStatus.UN_PAY, time, price);

                return Result.getResult(ResultCodeEnum.SUCCESS,passengerId);
            }catch (Exception e){
                Throwable cause = e.getCause();
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    return Result.getResult(ResultCodeEnum.ORDER_REQUEST_ILLEGAL);
                } else if (cause instanceof DuplicateKeyException) {
                    return Result.getResult(ResultCodeEnum.ORDER_EXIST);
                } else {
                    return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR);
                }
            }
        }else {
            return Result.getResult(order.isRequestLegal(),passengerId);
        }
    }

    private void lockTicket(String trainRouteId, String departureDate, int seatTypeId,
                            String fromStationId, String toStationId){

    }
}
