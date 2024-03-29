package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.TrainRouteQueryService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/trainRoutes")
public class TrainRouteQueryController {

    @Resource
    private TrainRouteQueryService trainRouteQueryService;

    //以出发站与终点站查可用车次
    @GetMapping("/direct")
    Result queryTrainRoute(String from, String to, String date) {
        try {
            return trainRouteQueryService.querytrainRoute(from, to, date);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @GetMapping("/transfer")
    Result queryTrainRouteTransfer(String from, String to, String date) {
        try {
            return trainRouteQueryService.querytrainRouteTransfer(from, to, date);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    //查询车次详情（从出发地到最终目的地）
    @GetMapping("/detail")
    Result queryTrainRouteDetail(String trainRouteId){
        try {
            return trainRouteQueryService.queryTrainRouteDetail(trainRouteId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

    @GetMapping("/timeInfo")
    Result queryTicketRouteTimeInfo(String trainRouteId, String fromStationId, String toStationId){
        try {
            return trainRouteQueryService.queryTicketRouteTimeInfo(trainRouteId,fromStationId,toStationId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
    }

}

