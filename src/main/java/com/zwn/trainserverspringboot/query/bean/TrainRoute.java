package com.zwn.trainserverspringboot.query.bean;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRoute implements Serializable {
    private String trainRouteId;
    private String fromStationId;
    private String toStationId;
    private boolean formIsStart;
    private boolean toIsEnd;
    private String startTime;
    private String arriveTime;
    private String durationInfo;
    private Map<Integer, Integer> tickets;
}
