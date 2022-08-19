package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TrainRouteAtom implements Serializable {
    String stationId;
    String stationName;
    int stationNo;
    String arriveTime;
    String startTime;
    int  stopoverTime;
}
