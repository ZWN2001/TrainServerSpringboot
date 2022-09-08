package com.example.dataconverter.Mapper;

import com.example.dataconverter.bean.Route;
import com.example.dataconverter.bean.RouteAtom;
import com.example.dataconverter.bean.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyMapper {
    void initSeat();
    List<Station> getAllStationInfo();

    List<Route> getAllTrainRoutes();

    void insertCarriageInfo(String trainRouteId, int seatTypeId);

    void insertSeatRemain(String trainRouteId, String date, int carriageId,int locate, String seatRemain);

    List<RouteAtom> getRouteAtomByRouteId(String trainRouteId);

    void insertTicketPriceAtom(String trainRouteId,String stationId,int station_no,int seatTypeId,double price);

    void insertTicketRemain(String trainRouteId,int seatTypeId,String stationId,int stationNo,String date,int num);

    int getStationNo(String trainRouteId, String stationId);

    int atomNum(String trainRouteId);
}
