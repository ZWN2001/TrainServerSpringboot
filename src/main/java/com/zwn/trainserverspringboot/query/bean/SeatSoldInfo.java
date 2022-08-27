package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatSoldInfo {
    private String passengerId;
    private String departureDate;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    int carriageId;
    int seatType;
    int seat;
}
