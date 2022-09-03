package com.zwn.trainserverspringboot.command.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGeneral {
    private String orderId;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    private String departureDate;
    private String orderStatus;
    private String passengerId;
}
