package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRouteTimeInfo {
    String startTime;
    String arriveTime;
    String durationInfo;

}
