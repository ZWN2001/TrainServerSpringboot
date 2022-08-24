package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.IdCardUtil;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable {
    private String orderId;
    private long userId;
    private String passengerId;
    private String departureDate;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    private int seatTypeId;
    private String orderStatus;
    private  String orderTime;
    private double price;
    private String tradeNo;


    public ResultCodeEnum isRequestLegal(){
        if(departureDate == null || passengerId == null){
            return ResultCodeEnum.BAD_REQUEST;
        }
        SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            timeFrtmat.parse(departureDate);
        } catch (ParseException e) {
            return ResultCodeEnum.ORDER_TIME_FORMAT_ERROR;
        }
//        if(!IdCardUtil.isValidCard(passengerId)){
//            return ResultCodeEnum.ORDER_PASSENGER_ID_ILLEGAL;
//        }
        return ResultCodeEnum.SUCCESS;
    }
}
