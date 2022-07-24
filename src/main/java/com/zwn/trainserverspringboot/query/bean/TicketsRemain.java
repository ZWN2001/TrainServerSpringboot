package com.zwn.trainserverspringboot.query.bean;

import lombok.Data;

@Data
public class TicketsRemain {
    String train_route_id;
    String seat_type_id;
    int remaining_ticket_num;

}
