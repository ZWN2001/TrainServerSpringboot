package com.zwn.trainserverspringboot.query.controller;

import com.zwn.trainserverspringboot.query.service.SeatQueryService;
import com.zwn.trainserverspringboot.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/seatTypes")
public class SeatQueryController {
    @Resource
    private SeatQueryService seatTypeQueryService;

    @GetMapping("/all")
    Result getAllSeatType(){
        return seatTypeQueryService.getAllSeatType();
    }
}
