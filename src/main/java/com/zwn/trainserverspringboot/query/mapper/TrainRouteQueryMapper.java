package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.query.bean.TrainRoute;
import com.zwn.trainserverspringboot.query.bean.TrainRouteAtom;

import java.util.List;

public interface TrainRouteQueryMapper {
    List<TrainRoute> getTrainRoutesByFromAndTo(String fromStationId, String toStationId);
    List<TrainRouteAtom> queryTrainRouteDetail(String train_route_id);

}
