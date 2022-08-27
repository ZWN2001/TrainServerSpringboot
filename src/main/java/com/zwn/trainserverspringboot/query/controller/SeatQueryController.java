package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.SeatQueryService;
import com.zwn.trainserverspringboot.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/seatType/query")
public class SeatQueryController {
    @Resource
    private SeatQueryService seatTypeQueryService;

    @GetMapping("/allSeatType")
    Result getAllSeatType(){
        return seatTypeQueryService.getAllSeatType();
    }
}
