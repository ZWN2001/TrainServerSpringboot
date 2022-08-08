package com.zwn.trainserverspringboot.query.mapper;

import com.zwn.trainserverspringboot.command.bean.AtomStationKey;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.query.bean.TrainRoute;
import com.zwn.trainserverspringboot.query.bean.TrainRouteAtom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainRouteQueryMapper {
    List<TrainRoute> getTrainRoutesByFromAndTo(String fromStationId, String toStationId);
    List<TrainRouteAtom> queryTrainRouteDetail(String train_route_id);
    List<AtomStationKey> getAtonStationKeys(Order order);
}
