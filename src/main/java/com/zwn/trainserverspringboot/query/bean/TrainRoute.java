package com.zwn.trainserverspringboot.query.bean;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainRoute implements Serializable {

    String trainRouteId;
    String fromStationId;
    String toStationId;
//    String startTime;
//    String arriveTime;
    boolean formIsStart;
    boolean toIsEnd;

}
