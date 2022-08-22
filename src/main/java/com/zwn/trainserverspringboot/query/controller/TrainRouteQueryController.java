package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.TrainRouteQueryService;
import com.zwn.trainserverspringboot.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/trainRoute/query")
public class TrainRouteQueryController {

    @Resource
    private TrainRouteQueryService trainRouteQueryService;

    //以出发站与终点站查可用车次
    @GetMapping("/trainRoute")
    Result querytrainRoute(String from, String to, String date) {
        return trainRouteQueryService.querytrainRoute(from, to, date);
    }

    //查询车次详情（从出发地到最终目的地）
    @GetMapping("/trainRouteDetail")
    Result queryTrainRouteDetail(String trainRouteId){
        return trainRouteQueryService.queryTrainRouteDetail(trainRouteId);
    }

    @GetMapping("/ticketRouteTimeInfo")
    Result queryTicketRouteTimeInfo(String trainRouteId, String fromStationId, String toStationId){
        return trainRouteQueryService.queryTicketRouteTimeInfo(trainRouteId,fromStationId,toStationId);
    }

}

