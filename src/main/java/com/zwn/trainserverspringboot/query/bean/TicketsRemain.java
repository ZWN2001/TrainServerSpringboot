package com.zwn.trainserverspringboot.query.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class TicketsRemain implements Serializable {
    String trainRouteId;
    int seatTypeId;
    int remainingTicketNum;

    @Builder
    public TicketsRemain(){}
}
