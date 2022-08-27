package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.query.bean.SeatType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeatQueryMapper {
    List<SeatType> getAllSeatType();

    String getSeatRemain(String trainRouteId, String departDate, int carriageId, int location);
}
