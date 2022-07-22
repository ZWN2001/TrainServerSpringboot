package com.zwn.dbtext.query.mapper;

import com.zwn.dbtext.query.bean.TrainRoute;
import com.zwn.dbtext.query.bean.TrainRouteAtom;

import java.util.List;

public interface TrainRouteQueryMapper {
    List<TrainRoute> getTrainRoutesByFromAndTo(String fromStationId, String toStationId);
    List<TrainRouteAtom> queryTrainRouteDetail(String train_route_id);

}
