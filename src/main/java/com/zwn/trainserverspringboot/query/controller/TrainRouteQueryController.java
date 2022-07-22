package com.zwn.dbtext.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.dbtext.query.service.TrainRouteQueryService;
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

    @PostMapping("/train_route")
    String queryTrainRoute(String from, String to) {
        return JSON.toJSONString(trainRouteQueryService.queryTrainRoute(from, to));
    }

    @GetMapping("/train_route_detail")
    String queryTrainRouteDetail(String train_route_id){
        return JSON.toJSONString(trainRouteQueryService.queryTrainRouteDetail(train_route_id));
    }
}

