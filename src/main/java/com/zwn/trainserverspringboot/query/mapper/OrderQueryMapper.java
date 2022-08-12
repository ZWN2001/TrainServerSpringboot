package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderQueryMapper {
//    int queryOrderWithInfo(long userId, long passengerId, String departureDate, String trainRouteId,int seatTypeId);

    Order getOrderById(String orderId, String passengerId);
}
