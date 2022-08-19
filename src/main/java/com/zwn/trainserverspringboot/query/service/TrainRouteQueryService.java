package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.TrainRoute;
import com.zwn.trainserverspringboot.query.bean.TrainRouteAtom;
import com.zwn.trainserverspringboot.query.mapper.StationQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainRouteQueryService {

    @Resource
    private StationQueryMapper stationQueryMapper;

    @Resource
    TrainRouteQueryMapper trainRouteQueryMapper;

    public Result querytrainRoute(String from, String to){
        List<TrainRoute> trainRoutes = new ArrayList<>();
        //两个城市的所有车站
        List<String> allFromStations = stationQueryMapper.getSameCityStationId(from);
        List<String> allToStations = stationQueryMapper.getSameCityStationId(to);
        for (String fromStation:allFromStations) {
            for (String toStation:allToStations){
                List<TrainRoute> trainRoute = trainRouteQueryMapper.getTrainRoutesByFromAndTo(fromStation, toStation);
                trainRoutes.addAll(trainRoute);
            }
        }
        return Result.getResult(ResultCodeEnum.SUCCESS,trainRoutes);
    }

    public Result queryTrainRouteDetail(String train_route_id){
        List<TrainRouteAtom> trainRouteAtoms = trainRouteQueryMapper.queryTrainRouteDetail(train_route_id);
        return Result.getResult(ResultCodeEnum.SUCCESS,trainRouteAtoms);
    }
}
