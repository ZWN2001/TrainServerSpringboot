package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainRouteAtom implements Serializable {
    String stationId;
    String stationName;
    int stationNo;
    String arriveTime;
    String startTime;
    int  stopoverTime;
}
