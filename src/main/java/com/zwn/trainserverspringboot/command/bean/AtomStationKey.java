package com.zwn.trainserverspringboot.command.bean;

import lombok.Data;

@Data
public class AtomStationKey {
    String departureDate;
    String trainRouteId;
    int seatTypeId;
    String stationId;
}
