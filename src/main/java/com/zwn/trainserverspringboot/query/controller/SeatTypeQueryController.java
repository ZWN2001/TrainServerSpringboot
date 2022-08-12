package com.zwn.trainserverspringboot.query.controller;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.query.service.SeatTypeQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/seatType/query")
public class SeatTypeQueryController {
    @Resource
    private SeatTypeQueryService seatTypeQueryService;

    @GetMapping("allSeatType")
    String getAllSeatType(){
        return JSON.toJSONString(seatTypeQueryService.getAllSeatType());
    }
}