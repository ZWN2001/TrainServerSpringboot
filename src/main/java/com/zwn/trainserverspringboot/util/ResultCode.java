package com.zwn.trainserverspringboot.util;

public class ResultCode {
    public static final int SUCCESS = 0;
    public static final int UNKNOWN_ERROR = -1;
    public static final int FETCH_TOKEN_FAILED = 401;

    public static final int LOGIN_ERROR = 11;
    public static final int LOGIN_LACK = 12;
    public static final int REGISTER_EXIST = 13;
    public static final int REGISTER_LACK = 14;

    public static final int PASSENGER_USER_NOT_EXIST = 21;
    public static final int PASSENGER_ID_ILLEGAL = 22;
    public static final int PASSENGER_NAME_ILLEGAL = 23;
    public static final int PASSENGER_PHONE_NUMBER_ILLEGAL = 24;

}
