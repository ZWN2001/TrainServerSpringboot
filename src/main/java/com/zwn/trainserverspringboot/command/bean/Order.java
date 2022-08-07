package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.IdCardUtil;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@Builder
public class Order implements Serializable {
    String orderId;
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


    public ResultCodeEnum isRequestLegal(){
        if(orderTime == null || passengerId == null){
            return ResultCodeEnum.BAD_REQUEST;
        }
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
