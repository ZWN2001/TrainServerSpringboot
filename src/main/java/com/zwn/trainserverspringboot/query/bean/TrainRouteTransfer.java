package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

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
//    private int durationAll;
    private int durationTransfer;
    private Map<Integer, Integer> ticketsFirst;
    private Map<Integer, Integer> ticketsNext;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainRouteTransfer that = (TrainRouteTransfer) o;
        return  Objects.equals(trainRouteId1, that.trainRouteId1) && Objects.equals(trainRouteId2, that.trainRouteId2);
    }


    public void setNextRouteInfo(TrainRoute route){
        this.trainRouteId2 = route.getTrainRouteId();
        this.transStationId = route.getFromStationId();
        this.toStationId = route.getToStationId();
        this.startTimeTrans = route.getStartTime();
        this.arriveTimeTo = route.getArriveTime();
    }

    public void caculateDuration(){
//        durationAll = caculateTimeMinute(startTimeFrom, arriveTimeTo);
        durationTransfer = caculateTimeMinute(arriveTimeTrans, startTimeTrans);
    }

    private int caculateTimeMinute(String fromTime, String toTime){
        return 60 * ((24 - Integer.parseInt(fromTime.substring(0,2)) + Integer.parseInt(toTime.substring(0,2)))%24)
                + (60 - Integer.parseInt(toTime.substring(3,5)) + Integer.parseInt(fromTime.substring(3,5))) % 60;
    }
}
