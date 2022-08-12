package com.zwn.trainserverspringboot.query.bean;

import lombok.Data;

@Data
public class SeatInfo {
    String ticketId;
    String passengerId;
    int carriageId;
    int seat;
}
