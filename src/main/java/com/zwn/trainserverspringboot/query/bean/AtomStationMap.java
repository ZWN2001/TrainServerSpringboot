package com.zwn.trainserverspringboot.query.bean;

public class AtomStationMap {
    String trainRouteId;
    int seatTypeId;
    String stationId;
    String departureDate;
    int remainingNum;

    public AtomStationKey getKey(){
        return AtomStationKey.builder()
                .departureDate(departureDate)
                .trainRouteId(trainRouteId)
                .seatTypeId(seatTypeId)
                .stationId(stationId)
                .build();
    }
    public int getValue(){
        return remainingNum;
    }
}
