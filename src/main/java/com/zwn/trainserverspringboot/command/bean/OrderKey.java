package com.zwn.trainserverspringboot.command.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderKey {
    String departureDate;
    String trainRouteId;
    String fromStationId;
    String toStationId;
    int seatTypeId;

    public static OrderKey getFromOrder(Order order){
        return new OrderKey(order.departureDate,order.trainRouteId,order.fromStationId,order.toStationId,order.seatTypeId);
    }
}
