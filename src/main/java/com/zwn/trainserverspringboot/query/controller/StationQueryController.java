package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.StationQueryService;
import com.zwn.trainserverspringboot.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/stations")
public class StationQueryController {
    @Resource
    private StationQueryService stationQueryService;

    @GetMapping("/all")
    Result allStationDetail(){
        return stationQueryService.getAllStationInfo();
    }
}
