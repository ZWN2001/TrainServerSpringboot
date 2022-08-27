package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.query.bean.*;
import com.zwn.trainserverspringboot.command.bean.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainRouteQueryMapper {
    List<TrainRoute> getTrainRoutesByFromAndTo(String fromStationId, String toStationId);
    RunPlan getRunPlan(String routeIds);
    List<TrainRouteAtom> queryTrainRouteDetail(String train_route_id);
    List<AtomStationKey> getAtomStationKeys(Order order);
    List<AtomStationMap> getAllAtomStationKeys();
    List<TrainRouteAtom> queryTrainRouteInfo(String trainRouteId, String fromStationId, String toStationId);
    SeatRemainKey getFromToNo(String trainRouteId, String fromStationId, String toStationId);
}
