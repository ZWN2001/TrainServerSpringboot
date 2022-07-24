package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.StringUtil;
import lombok.Builder;
import lombok.Data;

@Data
public class Passenger {
    long userId;
    String passengerId;
    String passengerName;
    String phoneNum;

    @Builder
    public Passenger(){}

    public ResultCodeEnum isLegal(){
        if (passengerId == null ||passengerId.length() != 14){
            return ResultCodeEnum.PASSENGER_ID_ILLEGAL;
        }else if (passengerName == null || passengerName.length()<2 ||
                passengerName.length()>8 || !StringUtil.isChineseStr(passengerName)){
            return ResultCodeEnum.PASSENGER_NAME_ILLEGAL;
        }else if (phoneNum == null || StringUtil.isMobileNum(phoneNum)){
            return ResultCodeEnum.PASSENGER_PHONE_NUMBER_ILLEGAL;
        }else {
            return ResultCodeEnum.SUCCESS;
        }
    }
}