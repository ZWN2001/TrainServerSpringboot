package com.zwn.trainserverspringboot.query.bean;
import lombok.*;

import java.io.Serializable;

@Data
public class TrainRoute implements Serializable {

    String train_route_id;
    String from_station_id;
    String to_station_id;
    String from_time;
    String to_time;
    boolean form_is_start;
    boolean to_is_end;

    @Builder
    public TrainRoute() {}
}
