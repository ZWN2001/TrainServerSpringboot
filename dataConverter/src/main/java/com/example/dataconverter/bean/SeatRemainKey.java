package com.example.dataconverter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRemainKey {
    int fromStationNo;
    int toStationNo;
    int seat;

    @Override
    public String toString() {
        return fromStationNo + "_" + toStationNo + "_" + seat;
    }
}