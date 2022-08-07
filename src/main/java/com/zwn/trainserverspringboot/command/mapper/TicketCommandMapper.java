package com.zwn.trainserverspringboot.command.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketCommandMapper {
    void ticketBooking(Order order);

    void ticketPay(String orderId, String tradeNo);

    void ticketRefund(String orderId);
}
