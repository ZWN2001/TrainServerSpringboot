package com.zwn.trainserverspringboot.query.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class TrainRouteAtom implements Serializable {
    String station_id;
    String station_name;
    int station_no;
    String arrive_time;
    String start_time;
    int  stopover_time;

    @Builder
    public TrainRouteAtom() {}
}
