package com.zwn.trainserverspringboot.query.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class SeatTypeQueryControllerTest {

    @Resource
    private SeatQueryController seatTypeQueryController;

    @Test
    void getAllSeatType() {
        System.out.println(seatTypeQueryController.getAllSeatType());
    }
}