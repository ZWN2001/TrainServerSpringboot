package com.zwn.trainserverspringboot.command.bean;

import lombok.Data;

import java.util.List;

@Data
public class OrderPrimaryKey {
    String departureDate;
    String trainRouteId;
    List<String> passengerIdList;
}
