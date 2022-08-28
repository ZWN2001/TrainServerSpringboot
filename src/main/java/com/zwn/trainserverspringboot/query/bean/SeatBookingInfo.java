package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatBookingInfo {
    private String passengerId;
    private String departureDate;
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    int seatType;
    int seatBooking;//这其实是location
}
