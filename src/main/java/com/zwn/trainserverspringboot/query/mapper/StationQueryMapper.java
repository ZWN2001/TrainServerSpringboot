package com.zwn.trainserverspringboot.query.mapper;

import java.util.List;

public interface StationQueryMapper {
    List<String> getSameCityStationId(String stationName);
}
