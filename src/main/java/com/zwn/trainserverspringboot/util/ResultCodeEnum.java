package com.zwn.trainserverspringboot.util;

import java.io.Serializable;

public enum ResultCodeEnum implements Serializable {

//    common code
    SUCCESS(0,"success"),
    UNKNOWN_REASON(-1,"unknown error"),
    FETCH_TOKEN_FAILED(401,"can not fetch token"),

//    login/register/resetPw/getSelfInfo/getTechnicalName code
    LOGIN_ERROR(11,"incorrect account or password"),
    LOGIN_LACK(12,"blank account or password"),
    REGISTER_EXIST(13,"account already exists"),
    REGISTER_LACK(15,"information is incomplete"),

//    demand code
    DEMAND_LACK(21,"information is incomplete");

    final int code;
    final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
