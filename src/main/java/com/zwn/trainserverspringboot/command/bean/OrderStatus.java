package com.zwn.trainserverspringboot.command.bean;

public class OrderStatus {
    public static final String UN_PAY = "未支付";
    public static final String CANCEL = "已取消";
    public static final String TIMEOUT = "支付超时";
    public static final String PAIED = "已支付";
    public static final String REFUNDED = "已退票";
    public static final String TO_REBOOK = "待改签";
    public static final String REBOOK = "已改签";
}
