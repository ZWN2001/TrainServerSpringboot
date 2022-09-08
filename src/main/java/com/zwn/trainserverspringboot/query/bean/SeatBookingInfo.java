package com.zwn.trainserverspringboot.query.bean;

import com.zwn.trainserverspringboot.command.bean.RebookOrder;
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

    public static SeatBookingInfo getFromRebookOrder(RebookOrder order){
        SeatBookingInfo info = new SeatBookingInfo();
        info.setPassengerId(order.getPassengerId());
        info.setDepartureDate(order.getDepartureDate());
        info.setTrainRouteId(order.getTrainRouteId());
        info.setFromStationId(order.getFromStationId());
        info.setToStationId(order.getToStationId());
        info.setSeatType(order.getSeatTypeId());
        info.setSeatBooking(order.getSeatBooking());
        return info;
    }
}
