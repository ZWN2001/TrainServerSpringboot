package com.zwn.dbtext.query.mapper;

import com.zwn.dbtext.query.bean.TicketsRemain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketQueryMapper {
    List<TicketsRemain> getTicketsRemain(String train_route_id, String ticket_date, String from_station_id, String to_station_id);
}
