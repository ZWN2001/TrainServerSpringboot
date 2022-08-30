package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderGeneral;
import com.zwn.trainserverspringboot.query.bean.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketQueryMapper {
    List<TicketsRemain> getTicketsRemain(String train_route_id, String ticket_date, String from_station_id, String to_station_id);
    List<TicketPrice> getTicketPrices(String train_route_id, String from_station_id, String to_station_id);
    double getTicketPrice(String train_route_id, String from_station_id, String to_station_id, int seat_type_id);
    List<OrderGeneral> getSelfTicket(long userId);
    List<OrderGeneral> getSelfOrder(long userId);
    List<OrderGeneral> getSelfPaiedOrder(long userId);
    List<Order> getSoldOrderInfo(String orderId);
    SeatInfo getTicketSeatInfo(String ticketId);
    List<Order> getTicketToPayDetail(long userId);
    int getTicketToPayNum(long userId);
    List<String> getOrderStatus(String orderId);
    List<String> getOrderPassengers(String orderId);
    List<SeatBookingInfo> getPreferSeatBookingInfo(String orderId);
    List<SeatSoldInfo> getSoldSeatInfo(String orderId);
}
