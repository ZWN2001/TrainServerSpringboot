package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.IdCardUtil;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Order {
    long orderId;
    long userId;
    String passengerId;
    String departureDate;
    String trainRouteId;
    String fromStationId;
    String toStationId;
    int seatTypeId;
    String orderStatus;
    String orderTime;
    double price;

    @Builder
    public Order() {}

    public ResultCodeEnum isRequestLegal(){
        SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            timeFrtmat.parse(orderTime);
        } catch (ParseException e) {
            return ResultCodeEnum.ORDER_TIME_FORMAT_ERROR;
        }
        if(!IdCardUtil.isValidCard(passengerId)){
            return ResultCodeEnum.ORDER_PASSENGER_ID_ILLEGAL;
        }
        return ResultCodeEnum.SUCCESS;
    }
}
