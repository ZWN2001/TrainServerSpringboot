package com.zwn.dbtext.query.mapper;

import java.util.List;

public interface StationQueryMapper {
    List<String> getSameCityStationId(String stationName);
}
