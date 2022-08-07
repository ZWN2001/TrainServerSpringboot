package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.query.bean.TicketPrice;
import com.zwn.trainserverspringboot.query.bean.TicketsRemain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketQueryMapper {
    List<TicketsRemain> getTicketsRemain(String train_route_id, String ticket_date, String from_station_id, String to_station_id);
    List<TicketPrice> getTicketPrices(String train_route_id, String from_station_id, String to_station_id);

    double getTicketPrice(String train_route_id, String from_station_id, String to_station_id, int seat_type_id);
}
