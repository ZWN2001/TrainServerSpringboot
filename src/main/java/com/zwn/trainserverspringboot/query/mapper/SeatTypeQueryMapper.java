package com.zwn.trainserverspringboot.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface SeatTypeQueryMapper {
    Map<Integer, String> getAllSeatType();
}
