package com.zwn.trainserverspringboot.util;

import java.io.Serializable;

public enum ResultCodeEnum implements Serializable {

//    common code
    SUCCESS(ResultCode.SUCCESS,"success"),
    UNKNOWN_ERROR(ResultCode.UNKNOWN_ERROR,"unknown error"),
    FETCH_TOKEN_FAILED(ResultCode.FETCH_TOKEN_FAILED,"can not fetch token"),


    LOGIN_ERROR(ResultCode.LOGIN_ERROR,"incorrect account or password"),
    LOGIN_LACK(ResultCode.LOGIN_LACK,"blank account or password"),
    REGISTER_EXIST(ResultCode.REGISTER_EXIST,"account already exists"),
    REGISTER_LACK(ResultCode.REGISTER_LACK,"information is incomplete"),

    //
    PASSENGER_USER_NOT_EXIST(ResultCode.PASSENGER_USER_NOT_EXIST,"passenger: user not exist"),
    PASSENGER_ID_ILLEGAL(ResultCode.PASSENGER_ID_ILLEGAL,"passenger: id illegal"),
    PASSENGER_NAME_ILLEGAL(ResultCode.PASSENGER_NAME_ILLEGAL,"passenger: name illegal"),
    PASSENGER_PHONE_NUMBER_ILLEGAL(ResultCode.PASSENGER_PHONE_NUMBER_ILLEGAL,"passenger: phone number is illegal"),
    PASSENGER_EXIST(ResultCode.PASSENGER_EXIST,"passenger exist"),

    ORDER_REQUEST_ILLEGAL(ResultCode.ORDER_REQUEST_ILLEGAL,"order: request illegal"),
    ORDER_EXIST(ResultCode.ORDER_EXIST,"order exist"),
    ORDER_TIME_FORMAT_ERROR(ResultCode.ORDER_TIME_FORMAT_ERROR,"order: tome format error"),
    ORDER_PASSENGER_ID_ILLEGAL(ResultCode.ORDER_PASSENGER_ID_ILLEGAL,"order: passenger id illegal");

    final int code;
    final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
}

