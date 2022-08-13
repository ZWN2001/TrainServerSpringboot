package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderPrimaryKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketCommandMapper {
    void ticketBooking(Order order, String time);

    void ticketPay(String orderId, String tradeNo);

    void ticketRefund(String orderId, String passengerId);

    void ticketRebook(String orderId, String passengerId, String departureDate, String trainRouteId);

    void updateTicketSold(String orderId, String passengerId, String carriage, String seat);

    void ticketGet(String orderId, String passengerId, int carriage_id, int seat);

    void updateTicketRemain(String trainRouteId, int seatTypeId, String ticketDate, String fromStationId,
                            String toStationId, int remain);

    void ticketBookingCancel(OrderPrimaryKey key);
}
