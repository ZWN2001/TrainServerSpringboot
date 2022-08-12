package com.zwn.trainserverspringboot.command.bean;

import lombok.Data;

@Data
public class OrderGeneral {
    private String orderId;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    private String departureDate;
    private String orderStatus;
}
