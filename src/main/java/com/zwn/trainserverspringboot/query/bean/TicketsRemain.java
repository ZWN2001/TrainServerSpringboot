package com.zwn.trainserverspringboot.query.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class TicketsRemain implements Serializable {
    String train_route_id;
    String seat_type_id;
    int remaining_ticket_num;

}
