package com.example.dataconverter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    String trainRouteId;
    String fromStationId;
    String toStationId;
}
