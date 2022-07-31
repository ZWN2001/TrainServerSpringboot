package com.zwn.trainserverspringboot.util;

import java.io.Serializable;

public enum ResultCodeEnum implements Serializable {

//    common code
    SUCCESS(200,"success"),
    UNKNOWN_ERROR(-1,"unknown error"),
    BAD_REQUEST(400, "参数或者语法不对"),
    UNAUTHORIZED(401,"can not fetch token"),
    FORBIDDEN(403, "禁止访问"),


    LOGIN_ERROR(11,"incorrect account or password"),
    LOGIN_LACK(12,"blank account or password"),
    REGISTER_EXIST(13,"account already exists"),
    REGISTER_ILLEGAL_PHONE(14,"手机号非法"),
    REGISTER_ILLEGAL_EMAIL(14,"邮箱非法"),
    REGISTER_ILLEGAL_PASSWORD(14,"密码长度非法"),

    //
    PASSENGER_USER_NOT_EXIST(21,"passenger: user not exist"),
    PASSENGER_ID_ILLEGAL(22,"passenger: id illegal"),
    PASSENGER_NAME_ILLEGAL(23,"passenger: name illegal"),
    PASSENGER_PHONE_NUMBER_ILLEGAL(24,"passenger: phone number is illegal"),
    PASSENGER_EXIST(25,"passenger exist"),

    ORDER_REQUEST_ILLEGAL(31,"order: request illegal"),
    ORDER_EXIST(32,"order exist"),
    ORDER_TIME_FORMAT_ERROR(33,"order: tome format error"),
    ORDER_PASSENGER_ID_ILLEGAL(34,"order: passenger id illegal");

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


