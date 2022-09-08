package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.RebookOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketCommandMapper {
    void ticketBooking(Order order, String time, int seatBooking);

    void ticketPay(String orderId, String tradeNo);

    void ticketSoldInit(String orderId, String passengerId, int carriageId, int seat);

    void ticketSoldUpdate(String orderId, String passengerId, int carriageId, int seat);

    void ticketRefund(String orderId);

    void ticketRebook(RebookOrder rebookOrder);

    void updateTicketSold(String orderId, String passengerId, String carriage, String seat);

    void ticketGet(String orderId, String passengerId, int carriage_id, int seat);

    void updateTicketRemain(String trainRouteId, int seatTypeId, String ticketDate, String fromStationId,
                            String toStationId, int remain);

    void ticketBookingCancel(String departureDate, String trainRouteId, String passengerId);

    int getTicketNum(String passengerId, String departureDate, String trainRouteId);

    void ticketRebookCancel(String orderId);

    void ticketRebookDone(String orderId);

    void updateRebookInfo(String orderId, String passengerId, double price, int seatTypeId);
}
