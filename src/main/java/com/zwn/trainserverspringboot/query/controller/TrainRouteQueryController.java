package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.TrainRouteQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query")
public class TrainRouteQueryController {

    @Resource
    private TrainRouteQueryService trainRouteQueryService;

    //以出发站与终点站查可用车次
    @PostMapping("/trainRoute")
    String queryTrainRoute(String from, String to) {
        return JSON.toJSONString(trainRouteQueryService.queryTrainRoute(from, to));
    }

    //查询车次详情（从出发地到最终目的地）
    @GetMapping("/trainRouteDetail")
    String queryTrainRouteDetail(String train_route_id){
        return JSON.toJSONString(trainRouteQueryService.queryTrainRouteDetail(train_route_id));
    }
}

