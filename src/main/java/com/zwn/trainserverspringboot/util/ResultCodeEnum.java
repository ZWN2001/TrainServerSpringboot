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
    PASSENGER_USER_NOT_EXIST(ResultCode.PASSENGER_USER_NOT_EXIST,"passenger user not exist"),
    PASSENGER_ID_ILLEGAL(ResultCode.PASSENGER_ID_ILLEGAL,"passenger id illegal"),
    PASSENGER_NAME_ILLEGAL(ResultCode.PASSENGER_NAME_ILLEGAL,"passenger name illegal"),
    PASSENGER_PHONE_NUMBER_ILLEGAL(ResultCode.PASSENGER_PHONE_NUMBER_ILLEGAL,"passenger's phone number is illegal"),
    PASSENGER_EXIST(ResultCode.PASSENGER_EXIST,"passenger exist");

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

