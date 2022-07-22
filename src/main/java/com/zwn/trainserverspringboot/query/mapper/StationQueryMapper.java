package com.zwn.trainserverspringboot.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface StationQueryMapper {
    List<String> getSameCityStationId(String stationName);
}
