package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRouteTransfer {
    private String trainRouteId1;
    private String trainRouteId2;
    private String fromStationId;
    private String transStationId;
    private String toStationId;
    //始发站发车时间
    private String startTimeFrom;
    //换乘站到达时间
    private String arriveTimeTrans;
    //换乘站发车时间
    private String startTimeTrans;
    //目的站到达时间
    private String arriveTimeTo;
    private Map<Integer, Integer> ticketsFirst;
    private Map<Integer, Integer> ticketsNext;

    public void setNextRouteInfo(TrainRoute route){
        this.trainRouteId2 = route.getTrainRouteId();
        this.transStationId = route.getFromStationId();
        this.toStationId = route.getToStationId();
        this.startTimeTrans = route.getStartTime();
        this.arriveTimeTo = route.getArriveTime();
    }
}
