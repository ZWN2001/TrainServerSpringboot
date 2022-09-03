package com.zwn.trainserverspringboot.query.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRemainKey {
    int fromStationNo;
    int toStationNo;
    int seat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatRemainKey that = (SeatRemainKey) o;
        return fromStationNo == that.fromStationNo && toStationNo == that.toStationNo;
    }

    public static SeatRemainKey fromString(String str){
        String[] s = str.split("_");
        if(s.length != 3){
            return null;
        }else {
            return new SeatRemainKey(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));
        }
    }

    @Override
    public String toString() {
        return  fromStationNo + "_" + toStationNo + "_" + seat ;
    }
}