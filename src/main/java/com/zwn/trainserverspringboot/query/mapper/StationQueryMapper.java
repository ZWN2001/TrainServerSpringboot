package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.query.bean.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface StationQueryMapper {
    List<String> getSameCityStationId(String stationName);

    List<Station> getAllStationInfo();
}
