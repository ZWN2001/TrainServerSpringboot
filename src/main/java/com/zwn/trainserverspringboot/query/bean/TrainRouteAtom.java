package com.zwn.trainserverspringboot.query.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
public class TrainRouteAtom {
    String station_id;
    String station_name;
    int station_no;
    String arrive_time;
    String start_time;
    int  stopover_time;

    @Builder
    public TrainRouteAtom() {}
}
