package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.RebookOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderQueryMapper {
//    int queryOrderWithInfo(long userId, long passengerId, String departureDate, String trainRouteId,int seatTypeId);

    List<Order> getOrderByIdAndPid(String orderId, String passengerId);

    List<Order> getOrderById(String orderId);

    List<RebookOrder> getRebookOrder(long userId);

    List<RebookOrder> getRebookOrderByOrderId(String orderId);
}
