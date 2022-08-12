package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.StationQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/station/query")
public class StationQueryController {
    @Resource
    private StationQueryService stationQueryService;

    @GetMapping("/allDetail")
    String allStationDetail(){
        return JSON.toJSONString(stationQueryService.getAllStationInfo());
    }
}
