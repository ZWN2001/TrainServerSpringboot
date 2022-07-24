package com.zwn.trainserverspringboot.command.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketCommandMapper {
    void ticketBooking(String userId, String departureDate, String trainRouteId, String passengerId,
                       String orderStatus, String orderTime, double price);
}
