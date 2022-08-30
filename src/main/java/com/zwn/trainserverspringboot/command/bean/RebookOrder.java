package com.zwn.trainserverspringboot.command.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class RebookOrder  implements Serializable {
    private String orderId;
    private long userId;
    private String passengerId;
    private String departureDate;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    private int seatTypeId;
    private int seatBooking;
    private double originalPrice;
    private double price;
    private String createTime;
}
