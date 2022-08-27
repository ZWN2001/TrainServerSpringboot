package com.zwn.trainserverspringboot.command.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeatCommandMapper {
    void updateSeatRemain(String trainRouteId, String date, int carriageId,int locate, String seatRemain);
}
