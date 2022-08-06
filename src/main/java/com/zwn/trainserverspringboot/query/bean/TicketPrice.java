package com.zwn.trainserverspringboot.query.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class TicketPrice implements Serializable {
    String trainRouteId;
    int deatTypeId;
    double price;

    @Builder
    public TicketPrice(){}
}