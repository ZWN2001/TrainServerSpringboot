package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.TicketRouteTimeInfo;
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

    public Result queryTicketRouteTimeInfo(String trainRouteId, String fromStationId, String toStationId){
        List<TrainRouteAtom> atomList = trainRouteQueryMapper.queryTrainRouteInfo(trainRouteId,fromStationId,toStationId);
        System.out.println(atomList);
        if (atomList.size() == 0){
            return Result.getResult(ResultCodeEnum.TRAIN_ROUTE_NOT_EXIST);
        }else {
            TicketRouteTimeInfo ticketRouteTimeInfo = new TicketRouteTimeInfo();
            ticketRouteTimeInfo.setStartTime(atomList.get(0).getStartTime());
            ticketRouteTimeInfo.setArriveTime(atomList.get(atomList.size() - 1).getArriveTime());
            int day = 0;
            String hourAndMinute;
            for (int i = 1; i < atomList.size(); i++) {
                if (isTimeReduce(atomList.get(i - 1).getStartTime(),atomList.get(i).getArriveTime())){
                    day++;
                }
            }
            //出发时间的小时比到达的小时要大（到达减出发为负数）
            if (isTimeReduce(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime())){
                day--;//需要借一天进行计算
                hourAndMinute = getHourAndMinute2(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime());
            }else {
                hourAndMinute = getHourAndMinute1(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime());
            }
            System.out.println(day+":"+hourAndMinute);
            String duration = day > 0 ? day + ":" + hourAndMinute : hourAndMinute;
            ticketRouteTimeInfo.setDurationInfo(duration);
            return Result.getResult(ResultCodeEnum.SUCCESS,ticketRouteTimeInfo);
        }
    }

    private boolean isTimeReduce(String first, String next){
        return Integer.parseInt(first.substring(0, 2)) > Integer.parseInt(next.substring(0, 2));
    }

    //first < next, 即first早于next
    private String getHourAndMinute1(String first, String next){
        int allMinutes = 60 * (Integer.parseInt(first.substring(0, 2)) - Integer.parseInt(next.substring(0, 2)))
                + (Integer.parseInt(first.substring(3, 5)) - Integer.parseInt(next.substring(3, 5)));
        return allMinutes/60 + ":" + allMinutes % 60;
    }

    private String getHourAndMinute2(String first, String next){
        int allMinutes = 60 * (Integer.parseInt(first.substring(0, 2)) - Integer.parseInt(next.substring(0, 2)))
                + (Integer.parseInt(first.substring(3, 5)) - Integer.parseInt(next.substring(3, 5)));
        allMinutes = 24 * 60 - allMinutes;
        return allMinutes/60 + ":" + allMinutes % 60;
    }
}
