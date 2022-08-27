package com.zwn.trainserverspringboot.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarriageQueryMapper {
    List<Integer> getCarriageIds(String trainRouteId, int seatTypeId);
}
