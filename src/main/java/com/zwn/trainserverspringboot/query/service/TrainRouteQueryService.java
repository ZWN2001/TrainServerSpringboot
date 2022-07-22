package com.zwn.dbtext.query.service;

import com.zwn.dbtext.query.bean.TrainRoute;
import com.zwn.dbtext.query.bean.TrainRouteAtom;
import com.zwn.dbtext.query.mapper.StationQueryMapper;
import com.zwn.dbtext.query.mapper.TicketQueryMapper;
import com.zwn.dbtext.query.mapper.TrainRouteQueryMapper;
import com.zwn.dbtext.util.Result;
import com.zwn.dbtext.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainRouteQueryService {

    @Resource
    private TicketQueryMapper ticketQueryMapper;

    @Resource
    private StationQueryMapper stationQueryMapper;

    @Resource
    TrainRouteQueryMapper trainRouteQueryMapper;

    public Result queryTrainRoute(String from, String to){
        List<TrainRoute> trainRoutes = new ArrayList<>();
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
