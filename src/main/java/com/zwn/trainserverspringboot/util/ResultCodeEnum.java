package com.zwn.trainserverspringboot.util;

import java.io.Serializable;

public enum ResultCodeEnum implements Serializable {

//    common code
    SUCCESS(200,"success"),
    UNKNOWN_ERROR(-1,"unknown error"),
    BAD_REQUEST(400, "参数或者语法不对"),
    UNAUTHORIZED(401,"can not fetch token"),
    FORBIDDEN(403, "禁止访问"),


    LOGIN_ERROR(11,"login:账号或密码错误"),
    LOGIN_LACK(12,"login:账号或密码不能为空"),
    REGISTER_EXIST(13,"register:账号已存在"),
    REGISTER_ILLEGAL_PHONE(14,"register:手机号非法"),
    REGISTER_ILLEGAL_EMAIL(14,"register:邮箱非法"),
    REGISTER_ILLEGAL_PASSWORD(14,"register:密码长度非法"),

    //
    PASSENGER_USER_NOT_EXIST(21,"passenger:用户不存在"),
    PASSENGER_ID_ILLEGAL(22,"passenger: 身份证号非法"),
    PASSENGER_NAME_ILLEGAL(23,"passenger: 姓名非法"),
    PASSENGER_PHONE_NUMBER_ILLEGAL(24,"passenger: 手机号非法"),
    PASSENGER_EXIST(25,"passenger:乘员已存在"),

    ORDER_REQUEST_ILLEGAL(31,"order: 请求错误"),
    ORDER_EXIST(32,"order:订单已存在"),
    ORDER_TIME_FORMAT_ERROR(33,"order: 日期格式错误"),
    ORDER_PASSENGER_ID_ILLEGAL(34,"order: 身份证号非法"),
    ORDER_NOT_EXIST(35,"order:订单不存在"),

    TICKET_SURPLUS_NOT_ENOUGH(41,"ticket: 余票不足"),
    TICKET_PRICE_ERROR(42,"ticket: 票价获取错误");

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


